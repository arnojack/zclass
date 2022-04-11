package com.example.zclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.zclass.offline.OptionActivity;
import com.example.zclass.offline.dao.CourseDao;
import com.example.zclass.offline.pojo.Course;
import com.example.zclass.offline.view.TimeTableView;
import com.example.zclass.online.Dao.User;
import com.example.zclass.online.Activity.Class_OnlineActivity;
import com.example.zclass.online.Dialog.Dialog_Signin;
import com.example.zclass.online.Dialog.Dialog_Signup;
import com.example.zclass.online.Dialog.LoadingDialog;
import com.example.zclass.online.Activity.MyInfoActivity;
import com.example.zclass.online.service.HttpClientUtils;
import com.example.zclass.online.service.UpdateUser;
import com.example.zclass.online.tool.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.zclass.offline.dao.CourseDao;
import com.example.zclass.offline.OptionActivity;
import com.example.zclass.online.Dao.User;

import com.example.zclass.online.Dialog.Dialog_Signin;
import com.example.zclass.online.Dialog.Dialog_Signup;
import com.example.zclass.online.Dialog.LoadingDialog;
import com.example.zclass.online.service.HttpClientUtils;
import com.example.zclass.online.service.UpdateUser;
import com.example.zclass.online.tool.BaseActivity;
import com.example.zclass.offline.pojo.Course;
import com.example.zclass.offline.view.TimeTableView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static User user_info;
    public static Boolean result=false;
    private CourseDao courseDao = new CourseDao(this);
    private TimeTableView timeTable;
    private SharedPreferences sp;

    private MediaPlayer mMediaPlayer;
    private MediaPlayer myMediaPlayer;
    private SharedPreferences saved_prefs;
    public static AssetFileDescriptor afd = null;
    private boolean SoundEnabled = true;
    Context context= null;
    private AudioManager amanager = null;

    Dialog_Signin sign_Dialog;
    Dialog_Signup signup_Dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        timeTable = findViewById(R.id.timeTable);
        timeTable.addListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListener();
            }
        });
        user_info =new User();
        update_dl();
        BottomNavigationView mNaviView=findViewById(R.id.bottom_navigation);
        mNaviView.setOnItemSelectedListener(new NavigationViewlistener());
qd();
    }
    class NavigationViewlistener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent=null;
            switch (item.getItemId()){
                case R.id.page_1:
                    return true;
                case R.id.page_2:
                    //跳转到线上课堂
                   if(user_info.getFlag_login()==1){
                        intent=new Intent(MainActivity.this, Class_OnlineActivity.class);
                        intent.putExtra("user",user_info);
                        startActivity(intent);
                        MainActivity.this.finish();
                        return true;
                    }else {
                        login(Class_OnlineActivity.class);
                        Log.e("MainActivity","login结束");
                        return false;
                    }
                case R.id.page_3:

                    result=false;
                    if(user_info.getFlag_login()==1){
                        intent=new Intent(MainActivity.this, MyInfoActivity.class);
                        intent.putExtra("user",user_info);
                        startActivity(intent);
                        MainActivity.this.finish();
                        result =true;
                    }else{
                        login(MyInfoActivity.class);
                        Log.e("MainActivity","login结束");
                    }
            }
            return result;
        }
    }

    @Override
    protected void onStop() {
        if (sign_Dialog != null) { sign_Dialog.dismiss();}
        if (signup_Dialog != null) { signup_Dialog.dismiss();}
        super.onStop();
    }

    public void login(Class cl){
        String url_login=BaseActivity.BaseUrl+"LoginServlet";

        sign_Dialog =new Dialog_Signin(MainActivity.this,R.style.MyDialog);
        sign_Dialog.setTitle("登录").setUsername("userid").setPassword("password")
                .setsignin("登录", new Dialog_Signin.IonsigninListener() {
                    @Override
                    public boolean onsignin(Dialog dialog) {

                        //正在加载 图片
                        //sign_Dialog.hide();
                        Dialog dialog_lod = LoadingDialog.createLoadingDialog(MainActivity.this);
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

                            if(user_info.getFlag_login()==1){

                                //pd.cancel();
                                dialog_lod.cancel();

                                sign_Dialog.hide();
                                //跳转到cl
                                user_info.setFlag_login(1);
                                Intent intent=new Intent(MainActivity.this, cl);
                                intent.putExtra("user",user_info);
                                startActivity(intent);
                                MainActivity.this.finish();
                                result=true;
                            }else{
                                //update_onl();
                                HashMap<String, String> stringHashMap=new HashMap<String,String>();
                                stringHashMap.put(User.USERID, user_id);
                                stringHashMap.put(User.PASSWORD, user_password);
                                stringHashMap.put(User.METHOD,"login");
                                stringHashMap.put(User.WAY,"signin");

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
                                            Intent intent=new Intent(MainActivity.this, cl);
                                            intent.putExtra("user",user_info);
                                            startActivity(intent);
                                            MainActivity.this.finish();
                                            result =true;
                                            Log.e("MainActivity","跳转");
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
                        return result;
                    }
                }).setsignup("注册", new Dialog_Signin.IonsignupListener(){
            @Override
            public void onsignup(Dialog dialog) {
                sign_Dialog.hide();
                //跳转到注册页面


                signup_Dialog =new Dialog_Signup(MainActivity.this,R.style.MyDialog);
                signup_Dialog.setTitle("注册").setUserid("userid").setPassword("password")
                        .setsubmit("提交", new Dialog_Signup.IonsubmitListener() {
                            @Override
                            public void onsubmit(Dialog dialog) {

                                //加载
                                //signup_Dialog.hide();
                                Dialog dialog_lod =LoadingDialog.createLoadingDialog(MainActivity.this);
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
                                                Intent intent=new Intent(MainActivity.this, cl);
                                                intent.putExtra("user",user_info);
                                                startActivity(intent);
                                                MainActivity.this.finish();
                                                result =true;
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
    public void update_onl(){
        UpdateUser.Update(new UpdateUser.Updatelistener() {
            @Override
            public void onSuccess(String json) {
                ArrayList<HashMap<String,String>> temp=null;
                try {
                    temp= BaseActivity.jtol_user(json);
                    HashMap t=temp.get(0);
                    user_info.setUserid((String) t.get(User.USERID));
                    user_info.setUsername((String) t.get(User.USERNAME));
                    user_info.setSex((String) t.get(User.SEX));
                    user_info.setCode((String) t.get(User.CODE));
                    user_info.setPhonenumber((String) t.get(User.PHONENUMBER));
                    user_info.setProfess((String) t.get(User.PROFESS));
                    user_info.setSchool((String) t.get(User.SCHOOL));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String errorMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "网络崩溃!"+errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void update_dl(){
        User m=(User) getIntent().getSerializableExtra("user");
        if(m!=null)
        user_info= m;
        //user_info.setUserid((String) t.get(User.USERID));
        //user_info.setUsername((String) t.get(User.USERNAME));
        //user_info.setPhonenumber((String) t.get(User.PHONENUMBER));
        //user_info.setProfess((String) t.get(User.PROFESS));
        //user_info.setSchool((String) t.get(User.SCHOOL));
    }
    protected void onStart() {
        super.onStart();
        //获取开学时间
        long date = sp.getLong("date", new Date().getTime());
        timeTable.loadData(acquireData(), new Date(date));
        Log.i("test", new Date(date).toString());
    }

    private List<Course> acquireData() {
        List<Course> courses = new ArrayList<>();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getBoolean("isFirstUse", true)) {//首次使用
            sp.edit().putBoolean("isFirstUse", false).apply();
        }else {
            courses = courseDao.listAll();
        }
        return courses;
    }

    /**
     * 菜单
     */
    public void categoryListener() {
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }
    //**************************静音响铃函数********************************
    private void startAlarm() {
        mMediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        mMediaPlayer.setLooping(true);
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }

    private void stopAlarm() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMediaPlayer.stop();
            }
        }, 20000);

    }

    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }


    public void stopmusic() {
        this.saved_prefs = getSharedPreferences("RealSilent", 0);// 存储静音前的音量索引
        try {
            afd = getAssets().openFd("test.mp3");
            myMediaPlayer = new MediaPlayer();
            myMediaPlayer.reset();
            myMediaPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            myMediaPlayer.prepare();
            myMediaPlayer.start();
            myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    myMediaPlayer.reset();
                    try {
                        myMediaPlayer.setDataSource(afd.getFileDescriptor(),
                                afd.getStartOffset(), afd.getLength());
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        myMediaPlayer.prepare();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    myMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        amanager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (SoundEnabled) {
            SharedPreferences.Editor localEditor = saved_prefs.edit();

            localEditor.putInt("last_media_volume", amanager.getStreamVolume(AudioManager.STREAM_MUSIC));
            localEditor.commit();
            amanager.getStreamVolume(AudioManager.STREAM_MUSIC);
            amanager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

        } else {
            int i = saved_prefs.getInt("last_media_volume", 0);
            amanager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
        }
        SoundEnabled = !SoundEnabled;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void silentSwitchOff() {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int min = mAudioManager.getStreamMinVolume(mAudioManager.STREAM_SYSTEM);
        int max= mAudioManager.getStreamMaxVolume(mAudioManager.STREAM_SYSTEM);
        int value = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        int predict = max/2;
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);



        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,predict,  0 );  //tempVolume:音量绝对值


        //mAudioManager.setStreamVolume( AudioManager.STREAM_MUSIC,10,0); //音乐音量

    }

    //****************************获取时间函数*************************************
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void gettime() {
        int STU = 0;
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
//年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
//小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //秒
        int second = calendar.get(Calendar.SECOND);

        String mYear, mMonth, mDay, mWay;
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(calendar.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            STU = 7;
        } else if ("2".equals(mWay)) {
            STU = 1;
        } else if ("3".equals(mWay)) {
            STU = 2;
        } else if ("4".equals(mWay)) {
            STU = 3;
        } else if ("5".equals(mWay)) {
            STU = 4;
        } else if ("6".equals(mWay)) {
            STU = 5;
        } else if ("7".equals(mWay)) {
            STU = 6;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"\n"+hour,Toast.LENGTH_SHORT).show();
            }
        });
        //OptionActivity q = new OptionActivity();
        CourseDao c1 = new CourseDao(this);
        List<Course> cs = courseDao.query2(3);
        String result = "";
        for(Course course1 : cs)
        {
            if(course1.getDay()==3)
            {
                if(hour==11||hour==23)
                {
                    //startAlarm();
                    //stopAlarm();
                     //stopmusic();


                }
                switch(course1.getSection()){
                    case 1:
                        if(hour==7&&minute>45)
                        {startAlarm();
                            stopAlarm();
                        }
                        if(hour ==8)
                            stopmusic();
                    case 3:
                        if(hour==9&&minute>55)
                        {startAlarm();
                            stopAlarm();
                        }
                        if(hour ==10)
                            stopmusic();
                    case 5:
                        if(hour==1&&minute>45)
                        {startAlarm();
                            stopAlarm();
                        }
                        if(hour ==2)
                            stopmusic();
                    case 7:
                        if(hour==3&&minute>45)
                        {startAlarm();
                            stopAlarm();
                        }
                        if(hour ==4)
                            stopmusic();


                }
            }else{
                // Toast.makeText(MainActivity.this,"\n"+"fdgfhfyy",Toast.LENGTH_SHORT).show();
            }
        }












        //****************************获取时间函数*************************************

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void alock() {
        AlarmManager alarmMgr = null;
        PendingIntent alarmIntent = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public void qd(){
        final Handler handler = new Handler();
        Thread thread =new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                gettime();
//Toast.makeText(MainActivity.this,"\n"+"通过SimpleDateFormat获取24小时制时间：\n"+"sdf.format(new Date())",Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 5550);// 50是延时时长
            }
        });
        thread.setDaemon(true);
        handler.postDelayed(thread, 5550);// 打开定时器，执行操作
    }
    //程序关闭时
    //获取Do not disturb权限,才可进行音量操作
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onDestroy() {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int min = mAudioManager.getStreamMinVolume(mAudioManager.STREAM_SYSTEM);
        int max= mAudioManager.getStreamMaxVolume(mAudioManager.STREAM_SYSTEM);
        int value = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        int predict = max/2;
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);



        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,predict,  0 );  //tempVolume:音量绝对值

        super.onDestroy();
    }


}
