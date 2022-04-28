package com.example.zclass.offline;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;

import java.util.Calendar;

import aidl.IStockQuoteService;

public class MyService extends Service
{
	 private Vibrator vibrator;

		private static final String tag="tag";
		private ConnectivityManager connectivityManager;
	    private NetworkInfo info;
//----------------------时间变量------------------
	    private int mHour1;
	    private int mMinute1;

	    private int mHour2;
	    private int mMinute2;

	    private int nowHour;
	    private int nowMinute;

	    private int countTime1;
	    private int countTime2;

	    private SharedPreferences sharedPreferences;

	    //------------------------------
	static final String TAG="MyService";
	//注册广播接收者进行网络状态的监听
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d(tag, "网络状态已经改变");
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if(info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    Log.d(tag, "当前网络名称：" + name);
                 //获取时间
                    final Calendar c = Calendar.getInstance();
                    nowHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
                    nowMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
                    System.out.println("获取得来的当前时间：") ;
     			  System.out.println(nowHour) ;System.out.println(nowMinute) ;
     			       if(rightTime(nowHour,nowMinute)){
     			    	  //alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
     			  // new AlertDialog.Builder(context).setMessage("没有可以使用的网络").setPositiveButton("Ok", null).show();
//     			    	  Intent it =new Intent(MyService.this, DialogActivity04.class);
//     			    	  it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//     			    	 startActivity(it);
     			      //  long [] pattern = {100,400,100,400};
	  	    	        //  停止 开启 停止 开启   27

	  	    	    //  vibrator.vibrate(pattern,2);
                       vibrator.vibrate(new long[] { 100,1000,100,1000 }, 0);

     			        }
                } else {
                    Log.d(tag, "没有可用网络");
                   // vibrator.cancel();
                    if(null!=vibrator){   vibrator.cancel();   }

                  //doSomething()
                }
            }
        }
    };
	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);

		//获取震动服务
		  vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

		  //获取时间
		//用于保持上一次数据
	       sharedPreferences = this.getSharedPreferences("laohufu",MODE_PRIVATE);

	       //获取上一次设置的数据
	       mHour1 = sharedPreferences.getInt("mHour1", 8);
	       mMinute1 = sharedPreferences.getInt("mMinute1", 0);
	       mHour2 = sharedPreferences.getInt("mHour2", 23);
	       mMinute2 = sharedPreferences.getInt("mMinute2", 0);

	}

	//定义内部类MyServiceImpl继承我们的AIDL文件自动生成的内部类，
	//并且实现我们AIDL文件定义的接口方法
	private class MyServiceImpl extends  IStockQuoteService.Stub
	{
		@Override
		public  double getMyTime(int myHour1,int myMinute1,int myHour2,int myMinute2) throws RemoteException
		{
			Log.i(TAG, "getTime");
			//------设置时间
			 mHour1=myHour1;
			     mMinute1=myMinute1;


			      //第二个时间
			    mHour2=myHour2;
			     mMinute2=myMinute2;


			     System.out.println("传进来 的时间：") ;

			   System.out.println(mHour1) ;System.out.println(mMinute1) ;


			//
			return 10.5;
			}
		}
	//时间半段函数
	private boolean rightTime(int nowHour,int nowMinute){

		   countTime1=mHour1*100+mMinute1;
		   countTime2=mHour2*100+mMinute2;
		   System.out.println(countTime1) ;
		   System.out.println(countTime2) ;
		int nowCountTime=nowHour*100+nowMinute;
		System.out.println("现在时间：证明这个汗被调用了！！！") ;
		   System.out.println(nowCountTime) ;
		//
		   if(countTime1<countTime2){
			if(nowCountTime>=countTime1&&nowCountTime<=countTime2){
				System.out.println("对着呢！！！！！！！！") ;
				return true;
			}

		   }else{
			   if(nowCountTime>=countTime1&&nowCountTime<=2400||nowCountTime>=0&&nowCountTime<=countTime1)
			   {
				   System.out.println("对着呢！！！！！！！！") ;
				   return true;
			   }
		   }

		return false;
	}
	@Override
	public IBinder onBind(Intent arg0)
	{
		//返回AIDL实现
		return new MyServiceImpl();
		}
	@Override
	public void onDestroy()
	{
		Log.e(TAG, "Release MyService");
		unregisterReceiver(mReceiver);
		//vibrator.cancel();
		if(null!=vibrator){   vibrator.cancel();   }
		super.onDestroy();
		}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	}

