package com.example.zclass.online.Activity;

import static com.example.zclass.online.tool.BaseActivity.BaseUrl;
import static com.example.zclass.online.tool.BaseActivity.setlTV;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.zclass.MainActivity;
import com.example.zclass.R;
import com.example.zclass.online.Dao.Cou_Stu;
import com.example.zclass.online.Dao.Course;
import com.example.zclass.online.Dao.Msg;
import com.example.zclass.online.Dao.User;
import com.example.zclass.online.Dialog.Dialog_Confim;
import com.example.zclass.online.Dialog.Dialog_upUser;
import com.example.zclass.online.Dialog.LoadingDialog;
import com.example.zclass.online.fragment.MsgAdapter;
import com.example.zclass.online.service.HttpClientUtils;
import com.example.zclass.online.service.JWebSocketClient;
import com.example.zclass.online.service.JWebSocketClientService;
import com.example.zclass.online.tool.BaseActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Chatroom extends AppCompatActivity implements View.OnClickListener {
    private List<Msg> msgList = new ArrayList<>();
    private EditText editText;
    private Button sendButton;
    private Button chatPop;
    private RecyclerView recyclerView;
    private MsgAdapter msgAdapter;
    private Context mContext;
    private PopupWindow mpop;

    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private ChatMessageReceiver chatMessageReceiver;

    private TextView memTV;
    private TextView workTV;
    private TextView roomNa;

    private String roomname;
    private String roomid;
    private String roomgrade;
    private String roomclass;
    private String teaid;
    private String teaname;
    private String teasex;
    private Boolean mana =false;

    TextView ropop_name;
    TextView ropop_class;
    TextView ropop_grade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        mContext= Chatroom.this;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.chat_pop:
                findpop();
                break;
            case R.id.ropop_name:
                HashMap<String, String> stringHashMap2=new HashMap<String,String>();
                stringHashMap2.put(Course.WAY,"upclname");
                toast(Course.COUONNAME,ropop_name.getText().toString(),stringHashMap2);
                break;
            case R.id.ropop_grade:
                HashMap<String, String> stringHashMap3=new HashMap<String,String>();
                stringHashMap3.put(Course.WAY,"upclgrade");
                toast(Course.COUGRADE,ropop_grade.getText().toString(),stringHashMap3);
                break;
            case R.id.ropop_class:
                HashMap<String, String> stringHashMap4=new HashMap<String,String>();
                stringHashMap4.put(Course.WAY,"upclc");
                toast(Course.COUCLASS,ropop_class.getText().toString(),stringHashMap4);
                break;
            case R.id.ropop_out:
                Dialog_Confim confim1 = new Dialog_Confim(Chatroom.this,R.style.MyDialog);
                confim1.setsubmit(new Dialog_Confim.IonsaveListener() {
                    @Override//点击取消按钮
                    public void submit() {
                        confim1.cancel();
                    }
                }, new Dialog_Confim.IonsaveListener() {
                    @Override//点击确认按钮
                    public void submit() {
                        HashMap<String, String> stringHashMap=new HashMap<String,String>();
                        stringHashMap.put(Cou_Stu.STUID, MainActivity.user_info.getUserid());
                        stringHashMap.put(Cou_Stu.COUONID, roomid);
                        stringHashMap.put(Course.METHOD,"Update");
                        stringHashMap.put(Cou_Stu.WAY,"delstu");
                        delete(stringHashMap);
                        Intent intent =new Intent(Chatroom.this,Class_OnlineActivity.class);
                        startActivity(intent);
                        Chatroom.this.finish();
                    }
                }).show();
                break;
            case R.id.ropop_delete:
                Dialog_Confim confim = new Dialog_Confim(Chatroom.this,R.style.MyDialog);
                confim.setsubmit(new Dialog_Confim.IonsaveListener() {
                    @Override//点击取消按钮
                    public void submit() {
                        confim.cancel();
                    }
                }, new Dialog_Confim.IonsaveListener() {
                    @Override//点击确认按钮
                    public void submit() {
                        HashMap<String, String> stringHashMap=new HashMap<String,String>();
                        stringHashMap.put(Course.COUONID, roomid);
                        stringHashMap.put(Course.METHOD,"Update");
                        stringHashMap.put(Course.WAY,"delall");
                        delete(stringHashMap);
                        Intent intent =new Intent(Chatroom.this,Class_OnlineActivity.class);
                        startActivity(intent);
                        Chatroom.this.finish();
                    }
                }).show();
                break;
            case R.id.room_mem:
                intent=new Intent(Chatroom.this, Member.class);
                intent.putExtra(Cou_Stu.COUONID,roomid);
                intent.putExtra(Course.TEAID,teaid);
                intent.putExtra(Course.TEANAME,teaname);
                intent.putExtra(User.SEX,teasex);
                startActivity(intent);
                break;
            case R.id.room_work:
                break;
            case R.id.room_send:
                //得到输入框中的内容
                String content = editText.getText().toString();
                if (content.length() <= 0) {
                    BaseActivity.showToast(mContext, "消息不能为空哟");
                    return;
                }

                if (client != null && client.isOpen()) {
                    //暂时将发送的消息加入消息列表，实际以发送成功为准（也就是服务器返回你发的消息时）
                    Date date=new Date(System.currentTimeMillis());
                    Msg chatMessage=new Msg(MainActivity.user_info.getUserid(),MainActivity.user_info.getUsername(),
                            MainActivity.user_info.getType(),content,Msg.TYPE_SENT,date);

                    jWebSClientService.sendMsg(JSON.toJSONString(chatMessage));

                    msgList.add(chatMessage);
                    initChatMsgListView();
                    editText.setText("");
                } else {
                    try {
                        client.connectBlocking();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    BaseActivity.showToast(mContext, "连接已断开，请稍等或重启App哟");
                }
                break;
        }

    }
    public void delete(HashMap stringHashMap){
        String url=BaseUrl+"CourseServlet";

        Dialog dialog_lod = LoadingDialog.createLoadingDialog(Chatroom.this);
        dialog_lod.show();

        HttpClientUtils.post(url, HttpClientUtils.maptostr(stringHashMap), new HttpClientUtils.OnRequestCallBack() {
            @Override
            public void onSuccess(String json) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog_lod.hide();
                        if("Ok".equals(json)){
                            Toast.makeText(getApplicationContext(), "操作成功!",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "操作失败!" + json,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onError(String errorMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog_lod.hide();
                        Toast.makeText(getApplicationContext(), "错误!" + errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void toast(String KEY, String text, HashMap<String,String> stringHashMap){
        HashMap<String, String> stringHashMap2=new HashMap<String,String>();
        stringHashMap2.put(Course.COUONID, roomid);
        stringHashMap2.put(Course.METHOD,"Update");
        stringHashMap2.putAll(stringHashMap);
        String url ="courseServlet";
        Dialog dialog = LoadingDialog.createLoadingDialog(Chatroom.this);

        Dialog_upUser Dialod_upsex = new Dialog_upUser(Chatroom.this,R.style.MyDialog);
        Dialod_upsex.setKEY(KEY);
        Dialod_upsex.setText(text).setsubmit(url,stringHashMap2, new Dialog_upUser.IonsaveListener() {
            @Override
            public void submit() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }
            @Override
            public void success(String json) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                        if("Ok".equals(json)){
                            Toast.makeText(getApplicationContext(), "更新成功!",
                                    Toast.LENGTH_SHORT).show();
                            switch (stringHashMap.get("way")){
                                case "upclname":
                                    ropop_name.setText(Dialod_upsex.getText());
                                    break;
                                case "upclgrade":
                                    ropop_grade.setText(Dialod_upsex.getText());
                                    break;
                                case "upclc":
                                    ropop_class.setText(Dialod_upsex.getText());
                                    break;
                            }
                            Dialod_upsex.cancel();
                        }else {
                            Toast.makeText(getApplicationContext(), "更新失败!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void error(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                        Toast.makeText(getApplicationContext(), "网络崩溃!/n"+error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).show();
    }
    public void findpop(){
        View popview =getLayoutInflater().inflate(R.layout.chatroom_pop,null);
        mpop =new PopupWindow(popview,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mpop.setOutsideTouchable(true);
        mpop.setFocusable(true);
        mpop.showAsDropDown(chatPop);
        ropop_name=popview.findViewById(R.id.ropop_name);
        ropop_name.setText(roomname);
        TextView ropop_id=popview.findViewById(R.id.ropop_id);
        ropop_id.setText(roomid);
        ropop_class=popview.findViewById(R.id.ropop_class);
        ropop_class.setText(roomclass);
        ropop_grade=popview.findViewById(R.id.ropop_grade);
        ropop_grade.setText(roomgrade);
        TextView delete =popview.findViewById(R.id.ropop_delete);
        setlTV(delete);
        Button out=popview.findViewById(R.id.ropop_out);
        ImageView imageView1 =popview.findViewById(R.id.rightp1);
        ImageView imageView2 =popview.findViewById(R.id.rightp2);
        ImageView imageView3 =popview.findViewById(R.id.rightp3);
        if(mana){
            ropop_name.setOnClickListener(this);
            ropop_grade.setOnClickListener(this);
            ropop_class.setOnClickListener(this);
            delete.setOnClickListener(this);
            out.setVisibility(View.GONE);
        }else {
            out.setOnClickListener(this);
            delete.setVisibility(View.GONE);
            imageView1.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onStart() {
        findViewById();
        //启动服务
        startJWebSClientService();
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
        //检测通知是否开启
        checkNotification(mContext);
        initView();
        super.onStart();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MyChatroomDemo", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MyChatroomDemo", "服务与活动成功断开");
        }
    };
    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message=intent.getStringExtra("message");

            Msg msg =JSON.parseObject(message,Msg.class);
            msg.setType(Msg.TYPE_RECEIVED);
            if(!MainActivity.user_info.getUserid().equals(msg.getUserid())){
                msgList.add(msg);
                initChatMsgListView();
            }

        }
    }
    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(mContext, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }
    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(mContext, JWebSocketClientService.class);
        startService(intent);
    }
    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
        registerReceiver(chatMessageReceiver, filter);
    }


    private void findViewById() {
        //initMsgs();
        editText = (EditText)findViewById(R.id.room_text);
        sendButton = (Button)findViewById(R.id.room_send);
        //BaseActivity.setbackground(sendButton,0.75);
        memTV=findViewById(R.id.room_mem);
        setlTV(memTV);
        workTV=findViewById(R.id.room_work);
        setlTV(workTV);
        chatPop=findViewById(R.id.chat_pop);
        //BaseActivity.setbackground(chatPop,0.35);
        roomNa=findViewById(R.id.room_name);

        sendButton.setOnClickListener(this);
        memTV.setOnClickListener(this);
        workTV.setOnClickListener(this);
        chatPop.setOnClickListener(this);

        roomname=getIntent().getStringExtra(Course.COUONNAME);
        roomid=getIntent().getStringExtra(Course.COUONID);
        teaid=getIntent().getStringExtra(Course.TEAID);
        teaname=getIntent().getStringExtra(Course.TEANAME);
        teasex=getIntent().getStringExtra(User.SEX);
        roomgrade=getIntent().getStringExtra(Course.COUGRADE);
        roomclass=getIntent().getStringExtra(Course.COUCLASS);

        if(teaid.equals(MainActivity.user_info.getUserid()))
            mana=true;

        BaseActivity.setWs(roomname,MainActivity.user_info.getUsername());

        roomNa.setText(roomname);

        recyclerView =(RecyclerView)findViewById(R.id.chatroomRecyclerView);
        //布局排列方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }
    private void initView() {
        //监听输入框的变化
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText.getText().toString().length() > 0) {
                    sendButton.setVisibility(View.VISIBLE);
                } else {
                    sendButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void initChatMsgListView(){
        msgAdapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(msgAdapter);
        //要求适配器重新刷新
        msgAdapter.notifyItemInserted(msgList.size()-1);
        //要求recyclerView布局将消息刷新
        recyclerView.scrollToPosition(msgList.size()-1);
    }


    /**
     * 检测是否开启通知
     *
     * @param context
     */
    private void checkNotification(final Context context) {
        if (!isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("温馨提示")
                    .setMessage("你还未开启系统通知，将影响消息的接收，要去开启吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(context);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }
    /**
     * 如果没有开启通知，跳转至设置界面
     *
     * @param context
     */
    private void setNotification(Context context) {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(localIntent);
    }

    /**
     * 获取通知权限,监测是否开启了系统通知
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
