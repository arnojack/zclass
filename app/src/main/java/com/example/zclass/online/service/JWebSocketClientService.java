package com.example.zclass.online.service;



import static com.example.zclass.APP.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.alibaba.fastjson.JSON;
import com.example.zclass.R;
import com.example.zclass.online.Activity.Chatroom;
import com.example.zclass.online.Dao.Msg;
import com.example.zclass.online.tool.BaseActivity;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class JWebSocketClientService extends Service {
    public static JWebSocketClient client;
    private JWebSocketClientBinder mBinder = new JWebSocketClientBinder();
    private final static int GRAY_SERVICE_ID = 1001;
    //灰色保活
    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID,
                    new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
    PowerManager.WakeLock wakeLock;//锁屏唤醒
    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock()
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock)
            {
                wakeLock.acquire();
            }
        }
    }

    //用于Activity和service通讯
    public class JWebSocketClientBinder extends Binder {
        public JWebSocketClientService getService() {
            return JWebSocketClientService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        initSocketClient();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //初始化websocket
        //设置service为前台服务，提高优先级
        if (Build.VERSION.SDK_INT < 18) {
            //Android4.3以下 ，隐藏Notification上的图标
            startForeground(GRAY_SERVICE_ID, new Notification());
        } else if(Build.VERSION.SDK_INT>18 && Build.VERSION.SDK_INT<25){
            //Android4.3 - Android7.0，隐藏Notification上的图标
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }else{
            //Android7.0以上app启动后通知栏会出现一条"正在运行"的通知
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

        acquireWakeLock();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        closeConnect();
        super.onDestroy();
    }

    public JWebSocketClientService() {
    }


    /**
     * 初始化websocket连接
     */
    public void initSocketClient() {
        URI uri = URI.create(BaseActivity.toUtf8String(BaseActivity.getWs()));
        client = new JWebSocketClient(uri) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMessage(String message) {
                Log.e("JWebSocketClientService", "收到的消息：" + message);


                Intent intent = new Intent();
                intent.setAction("com.xch.servicecallback.content");
                intent.putExtra("message", new String(message));
                sendBroadcast(intent);

                Msg msg = JSON.parseObject(message,Msg.class);
                String Nmsg=String.format("%s : %s",msg.getName(),msg.getContent());
                checkLockAndShowNotification(msg.getRoom(),Nmsg);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                Log.e("JWebSocketClientService", "websocket连接成功");
            }
        };
        connect();
    }

    /**
     * 连接websocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        if (null != client) {
            Log.e("JWebSocketClientService", "发送的消息：" + msg);
            client.send(msg);
        }
    }

    /**
     * 断开连接
     */
    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }


//    -----------------------------------消息通知--------------------------------------------------------

    /**
     * 检查锁屏状态，如果锁屏先点亮屏幕
     * @param title
     * @param content
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkLockAndShowNotification(String title,String content) {
        //管理锁屏的一个服务
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {//锁屏
            //获取电源管理器对象
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "bright");
                wl.acquire();  //点亮屏幕
                wl.release();  //任务结束后释放
            }
            sendNotification(title,content);
        } else {
            sendNotification(title,content);
        }
    }

    /**
     * 发送通知
     *
     * @param title
     * @param content
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String title,@NonNull String content) {
        Intent notificationIntent =  new Intent(this, JWebSocketClientService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.classid)
                .setContentIntent(pendingIntent)
                .build();


        startForeground(1,notification);
    }

    //    -------------------------------------websocket心跳检测------------------------------------------------
    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("JWebSocketClientService", "心跳保活");
            if(client==null){
                initSocketClient();
            }
            if(!client.isOpen()){
                client.reconnect();
            }
            //每隔一定的时间，对长连接进行一次心跳检测
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };
}
