package com.example.zclass;

import static com.example.zclass.online.service.UpdateUser.update_onl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
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
import com.example.zclass.online.tool.SPUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Build;

public class MainActivity extends AppCompatActivity {
    String TAG="MainActivity";
    private final RxPermissions rxPermissions=new RxPermissions(this);
    //是否拥有权限
    private boolean hasPermissions = false;
    //底部弹窗
    public static User user_info;
    public static Boolean result=false;
    private CourseDao courseDao = new CourseDao(this);
    private TimeTableView timeTable;
    private SharedPreferences sp;

    BottomNavigationView mNaviView;
    Dialog_Signin sign_Dialog;
    Dialog_Signup signup_Dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkVersion();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        timeTable = findViewById(R.id.timeTable);
        timeTable.addListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListener();
            }
        });
        user_info =new User();
        mNaviView=findViewById(R.id.bottom_navigation);
        mNaviView.setOnItemSelectedListener(new NavigationViewlistener());
    }
    /**
     * 检查版本
     */
    private void checkVersion() {
        //Android6.0及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果你是在Fragment中，则把this换成getActivity()
            //权限请求
            FragmentManager fragmentManager=this.getFragmentManager();
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {//申请成功
                            //showMsg("已获取权限");
                            hasPermissions=true;
                        } else {//申请失败
                            showMsg("权限未开启");
                            hasPermissions=false;
                        }
                    });

        } else {
            //Android6.0以下
            //showMsg("无需请求动态权限");
        }
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
                        login(MainActivity.this,Class_OnlineActivity.class);
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

                        return true;
                    }else{
                        login(MainActivity.this,MyInfoActivity.class);
                        Log.e("MainActivity","login结束");

                        return false;
                    }
            }
            return result;
        }
    }
    public void login(Context context,Class cl){
        String url_login=BaseActivity.BaseUrl+"LoginServlet";

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
                                MainActivity.this.finish();
                                result=true;
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
    protected void onStart() {
        UpdateUser.update_dl(getIntent());
        mNaviView.setSelectedItemId(R.id.page_1);
        //获取开学时间
        long date = sp.getLong("date", new Date().getTime());
        timeTable.loadData(acquireData(), new Date(date));
        Log.i("test", new Date(date).toString());
        super.onStart();
    }
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
