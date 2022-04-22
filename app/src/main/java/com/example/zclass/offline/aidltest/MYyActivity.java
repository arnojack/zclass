package com.example.zclass.offline.aidltest;




import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.example.zclass.R;

import aidl.IStockQuoteService;

public class MYyActivity extends Activity
{     
	static final String TAG="MYyActivity";    
	private TimePicker mTimePicker;
	private TimePicker mTimePicker2;
	 private int mHour1; 
	    private int mMinute1; 
	   
	    private int mHour2; 
	    private int mMinute2; 
	    //用于保持上一次数据
	    private SharedPreferences sharedPreferences;  
	    private SharedPreferences.Editor editor;  
        private IBinder myService;
	
	
	@Override      
	public void onCreate(Bundle savedInstanceState)
	{         
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_myy);
		//待会儿再第二个数据设置好之后再绑定服务-----------------------------------------------------
		bindMyService();  //绑定服务
		Button btnCall=(Button)findViewById(R.id.button);    
		Button userButton=(Button)findViewById(R.id.userButton);  
		//加入时间设置
		mTimePicker = (TimePicker)findViewById(R.id.mTimPicker);
		mTimePicker2  = (TimePicker)findViewById(R.id.mTimPicker2);
		         
		//用于保持上一次数据
		       sharedPreferences = this.getSharedPreferences("laohufu",MODE_PRIVATE);
		       editor = sharedPreferences.edit(); 
		       //获取上一次设置的数据
		       mHour1 = sharedPreferences.getInt("mHour1", 0);
		       mMinute1 = sharedPreferences.getInt("mMinute1", 0);
		       mHour2 = sharedPreferences.getInt("mHour2", 0); 
		       mMinute2 = sharedPreferences.getInt("mMinute2", 0); 
		
		       
		mTimePicker.setIs24HourView(true);//是否显示24小时制？默认false
		mTimePicker.setCurrentHour(mHour1);     //设置当前小时
		mTimePicker.setCurrentMinute(mMinute1); //设置当前分钟
		
		mTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

				//startTime.setText(new StringBuilder().append(hourOfDay).append(":")
			      //         .append((minute < 10) ? "0" + minute : minute));
				 editor.putInt("mHour1", hourOfDay);  
	                editor.putInt("mMinute1", minute);	 
				      // 一定要提交  
				    editor.commit();  

				 mHour1=hourOfDay; 
			    mMinute1=minute; 
			}
		});
		//设置mTimePicker2为24小时制
		mTimePicker2.setIs24HourView(true);
		//设置mTimePicker2初始值为5
		mTimePicker2.setCurrentHour(mHour2);     //设置当前小时
		mTimePicker2.setCurrentMinute(mMinute2);
		
		
		/**
		 * 设置mTimePicker2时间改变事件处理
		 */
		mTimePicker2.setOnTimeChangedListener(new OnTimeChangedListener() {
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

				//endTime.setText(new StringBuilder().append(hourOfDay).append(":")
			              // .append((minute < 10) ? "0" + minute : minute));
				mHour2=hourOfDay; 
			    mMinute2=minute; 
			    
			    editor.putInt("mHour2", hourOfDay);  
                editor.putInt("mMinute2", minute);	 
			      // 一定要提交  
			    editor.commit();  
			    //绑定服务
			   
			}
		});
		
		if(btnCall!=null)         
			btnCall.setOnClickListener(new OnClickListener()
			{              
				@Override        
				public void onClick(View v) 
				{                   
					//绑定一个服务                 
				//	bindMyService();    
					// new AlertDialog.Builder(MYyActivity.this ).setMessage("设置成功！！！").setPositiveButton("Ok", null).show();
					Intent intent = new Intent();
	            	intent.setClass(MYyActivity.this, DialogActivity03.class);
	            	MYyActivity.this.startActivity(intent);
					iService=IStockQuoteService.Stub.asInterface(myService);       
					//double value=0.0;  
					if(null!=iService){
					try {              
						//value=iService.getMyTime(13,12,17,12);  
						iService.getMyTime(mHour1,mMinute1,mHour2,mMinute2);  
						}           
					catch (RemoteException e)
					{              
						Log.e(TAG,"调用出错！");  
						e.printStackTrace();    
						} 
				      	} 
				   }
				});   
		userButton.setOnClickListener(new OnClickListener()
		{              
			@Override        
			public void onClick(View v) 
			{                   
				//绑定一个      
				 final Intent it = new Intent(MYyActivity.this, user1.class); //你要转向的Activity
			        
			        
		          startActivity(it); //执行
			}
			});     
		}            
	IStockQuoteService iService=null;
	private ServiceConnection  conn=new ServiceConnection()
	{       
		@Override      
		public void onServiceConnected(ComponentName name, IBinder service) {   
			//返回AIDL接口对象，然后可以调用AIDL方法       
			myService=service;
			iService=IStockQuoteService.Stub.asInterface(service);       
			double value=0.0;       
			try {              
				//value=iService.getMyTime(13,12,17,12);  
				value=iService.getMyTime(mHour1,mMinute1,mHour2,mMinute2);  
				}           
			catch (RemoteException e)
			{              
				Log.e(TAG,"调用出错！");  
				e.printStackTrace();    
				}         
			Log.i(TAG, "返回数值为："+value);   
			}     
		@Override     
		public void onServiceDisconnected(ComponentName name) {   
			Log.i(TAG, "释放Service");   
			}    
		};           
		private void bindMyService(){   
			// Intent intent=new Intent("com.dongzi.IStockQuoteService");   
			Intent intent=new Intent(this,MyService.class);      
			startService(intent);      
			bindService(intent, conn, Context.BIND_AUTO_CREATE);  
			}
}
			
	
