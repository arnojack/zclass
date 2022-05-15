package com.example.zclass.offline.aidltest;


import static com.example.zclass.MainActivity.user_info;
import static com.example.zclass.online.service.UpdateUser.update_onl;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.zclass.MainActivity;
import com.example.zclass.R;
import com.example.zclass.online.Activity.Class_OnlineActivity;
import com.example.zclass.online.Activity.MyInfoActivity;
import com.example.zclass.online.Dao.User;
import com.example.zclass.online.Dialog.Dialog_Signin;
import com.example.zclass.online.Dialog.Dialog_Signup;
import com.example.zclass.online.Dialog.LoadingDialog;
import com.example.zclass.online.service.HttpClientUtils;
import com.example.zclass.online.tool.BaseActivity;
import com.example.zclass.online.tool.SPUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;

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
	BottomNavigationView mNaviView;
	Dialog_Signin sign_Dialog;
	Dialog_Signup signup_Dialog;
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
		mNaviView=findViewById(R.id.bottom_navigation);
		mNaviView.setOnItemSelectedListener(new NavigationViewlistener());
		}



	class NavigationViewlistener implements NavigationBarView.OnItemSelectedListener {
		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			Intent intent=null;
			switch (item.getItemId()){
				case R.id.page_0:
					return true;
				case R.id.page_1:
					intent=new Intent(MYyActivity.this, MainActivity.class);
					intent.putExtra("user",user_info);
					startActivity(intent);
					MYyActivity.this.finish();
					return true;
				case R.id.page_2:
					//跳转到线上课堂
					if(user_info.getFlag_login()==1){
						intent=new Intent(MYyActivity.this, Class_OnlineActivity.class);
						intent.putExtra("user",user_info);
						startActivity(intent);
						MYyActivity.this.finish();
						return true;
					}else {
						login(MYyActivity.this,Class_OnlineActivity.class);
						Log.e(TAG,"login结束");
						return false;
					}
				case R.id.page_3:
					if(user_info.getFlag_login()==1){
						intent=new Intent(MYyActivity.this, MyInfoActivity.class);
						intent.putExtra("user",user_info);
						startActivity(intent);
						MYyActivity.this.finish();

						return true;
					}else{
						login(MYyActivity.this,MyInfoActivity.class);
						Log.e(TAG,"login结束");

						return false;
					}
			}
			return false;
		}
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
			Intent intent=new Intent(this, MyService.class);
			startService(intent);
			bindService(intent, conn, Context.BIND_AUTO_CREATE);
			}


	public void login(Context context,Class cl){
		String url_login= BaseActivity.BaseUrl+"LoginServlet";

		sign_Dialog =new Dialog_Signin(context,R.style.upuser);
		sign_Dialog.setTitle("登录")
				.setUsername(SPUtils.getString("userid",null,context))
				.setPassword(SPUtils.getString("password",null,context))
				.setsignin("登录", new Dialog_Signin.IonsigninListener() {
					@Override
					public void onsignin(Dialog dialog) {

						//正在加载 图片
						//sign_Dialog.hide();
						Dialog dialog_lod = LoadingDialog.createLoadingDialog(context);
						dialog_lod.show();

						String user_id =sign_Dialog.getUsername();
						String user_password =sign_Dialog.getPassword();

						user_info.setUserid(user_id);
						user_info.setPassword(user_password);

						if( "".equals(user_id) ||"".equals(user_password)){
							Toast.makeText(getApplicationContext(), "用户名或密码为空!",
									Toast.LENGTH_SHORT).show();
							sign_Dialog.show();
							//pd.cancel();
							dialog_lod.cancel();
						}else {
							SPUtils.putString("userid",user_id,context);
							SPUtils.putString("password",user_password,context);
							if(user_info.getFlag_login()==1){

								//pd.cancel();
								dialog_lod.cancel();

								sign_Dialog.hide();
								//跳转到cl
								user_info.setFlag_login(1);
								Intent intent=new Intent(context, cl);
								intent.putExtra("user",user_info);
								startActivity(intent);
								MYyActivity.this.finish();
							}else{
								//update_onl();
								HashMap<String, String> stringHashMap=new HashMap<String,String>();
								stringHashMap.put(User.USERID, user_id);
								stringHashMap.put(User.PASSWORD, user_password);
								stringHashMap.put(User.METHOD,"login");
								stringHashMap.put(User.WAY,"signin");

								Log.e(TAG,"-------------正在登录----------");
								HttpClientUtils.post(url_login, HttpClientUtils.maptostr(stringHashMap), new HttpClientUtils.OnRequestCallBack() {
									@Override
									public void onSuccess(String json) {
										//跳转到cl
										if("Ok".equals(json)){
											runOnUiThread(new Runnable() {
												@Override
												public void run() {
													//pd.cancel();
													dialog_lod.cancel();

													Toast.makeText(getApplicationContext(), "登录成功!",
															Toast.LENGTH_SHORT).show();

													sign_Dialog.hide();
												}
											});
											update_onl();
											user_info.setFlag_login(1);
											Intent intent=new Intent(context, cl);
											intent.putExtra("user",user_info);
											startActivity(intent);
											MYyActivity.this.finish();
											Log.e(TAG,"跳转");
										}else{
											runOnUiThread(new Runnable() {
												@Override
												public void run() {
													//pd.cancel();
													dialog_lod.cancel();

													Toast.makeText(getApplicationContext(), "用户名或密码错误!",
															Toast.LENGTH_SHORT).show();
													sign_Dialog.show();
												}
											});
										}
									}

									@Override
									public void onError(String errorMsg) {
										Log.e(TAG,"-----------"+errorMsg);
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												//pd.cancel();
												dialog_lod.cancel();

												Toast.makeText(getApplicationContext(), "网络崩溃了!",
														Toast.LENGTH_SHORT).show();
												sign_Dialog.show();
											}
										});
									}
								});
							}
						}
					}
				}).setsignup("注册", new Dialog_Signin.IonsignupListener(){
			@Override
			public void onsignup(Dialog dialog) {
				sign_Dialog.hide();
				//跳转到注册页面


				signup_Dialog =new Dialog_Signup(context,R.style.upuser);
				signup_Dialog.setTitle("注册").setUserid("userid").setPassword("password")
						.setsubmit("提交", new Dialog_Signup.IonsubmitListener() {
							@Override
							public void onsubmit(Dialog dialog) {

								//加载
								//signup_Dialog.hide();
								Dialog dialog_lod =LoadingDialog.createLoadingDialog(context);
								dialog_lod.show();

								String user_id =signup_Dialog.getUserid();
								String user_name =signup_Dialog.getUsername();
								String user_password =signup_Dialog.getPassword();

								user_info.setUserid(user_id);
								user_info.setUsername(user_name);
								user_info.setPassword(user_password);

								if( "".equals(user_id) ||"".equals(user_password)||"".equals(user_name)){
									Toast.makeText(getApplicationContext(), "用户名或密码为空!",
											Toast.LENGTH_SHORT).show();
									signup_Dialog.show();
									//pd.cancel();
									dialog_lod.cancel();
								}else {
									//update_onl();
									HashMap<String, String> stringHashMap=new HashMap<String,String>();
									stringHashMap.put(User.USERID, user_info.getUserid());
									stringHashMap.put(User.USERNAME,user_info.getUsername());
									stringHashMap.put(User.PASSWORD, user_info.getPassword());
									stringHashMap.put(User.METHOD,"login");
									stringHashMap.put(User.WAY,"signup");

									HttpClientUtils.post(url_login, HttpClientUtils.maptostr(stringHashMap), new HttpClientUtils.OnRequestCallBack() {
										@Override
										public void onSuccess(String json) {
											if("Ok".equals(json)){
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														dialog_lod.cancel();

														Toast.makeText(getApplicationContext(), "注册成功!",
																Toast.LENGTH_SHORT).show();

														signup_Dialog.hide();
													}
												});//跳转到cl
												update_onl();
												user_info.setFlag_login(1);
												Intent intent=new Intent(context, cl);
												intent.putExtra("user",user_info);
												startActivity(intent);
												MYyActivity.this.finish();
												Log.e("MainActivity","跳转");
											}else {
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														dialog_lod.cancel();

														Toast.makeText(getApplicationContext(), "用户id重复!",
																Toast.LENGTH_SHORT).show();
													}
												});
											}

										}

										@Override
										public void onError(String errorMsg) {
											runOnUiThread(new Runnable() {
												@Override
												public void run() {
													//pd.cancel();
													dialog_lod.cancel();

													Toast.makeText(getApplicationContext(), "网络崩溃了!",
															Toast.LENGTH_SHORT).show();
													signup_Dialog.show();
												}
											});
										}
									});
								}
							}


						}).show();
			}
		}).show();
	}
	@Override
	protected void onStart() {
		super.onStart();
		mNaviView.setSelectedItemId(R.id.page_0);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
		if (sign_Dialog != null) { sign_Dialog.cancel();sign_Dialog=null;}
		if (signup_Dialog != null) { signup_Dialog.cancel();signup_Dialog=null;}
	}
}


