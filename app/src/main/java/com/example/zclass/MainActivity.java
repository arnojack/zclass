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

import com.example.zclass.offline.dao.CourseDao;
import com.example.zclass.offline.OptionActivity;
import com.example.zclass.online.Dao.User;
import com.example.zclass.online.Class_OnlineActivity;
import com.example.zclass.online.Dialog.Dialog_Signin;
import com.example.zclass.online.Dialog.Dialog_Signup;
import com.example.zclass.online.Dialog.LoadingDialog;
import com.example.zclass.online.MyInfoActivity;
import com.example.zclass.online.service.HttpClientUtils;
import com.example.zclass.online.service.UpdateUser;
import com.example.zclass.online.tool.BaseActivity;
import com.example.zclass.offline.pojo.Course;
import com.example.zclass.offline.view.TimeTableView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String BaseUrl="http://192.168.0.106:8080/demo_war/";
    public static User user_info;
    public static Boolean result=false;
    private CourseDao courseDao = new CourseDao(this);
    private TimeTableView timeTable;
    private SharedPreferences sp;
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
                    }
            }
            return result;
        }
    }
    public void login(Class cl){
        String url_login=BaseUrl+"LoginServlet";

        Dialog_Signin sign_Dialog =new Dialog_Signin(MainActivity.this,R.style.MyDialog);
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
                                update_onl();
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
                                                    user_info.setFlag_login(1);
                                                    Intent intent=new Intent(MainActivity.this, cl);
                                                    intent.putExtra("user",user_info);
                                                    startActivity(intent);
                                                    MainActivity.this.finish();
                                                    result =true;
                                                }
                                            });
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


                Dialog_Signup signup_Dialog =new Dialog_Signup(MainActivity.this,R.style.MyDialog);
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
                                    update_onl();
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
                                                        //跳转到cl
                                                        signup_Dialog.hide();
                                                        user_info.setFlag_login(1);
                                                        Intent intent=new Intent(MainActivity.this, cl);
                                                        intent.putExtra("user",user_info);
                                                        startActivity(intent);
                                                        MainActivity.this.finish();
                                                        result =true;
                                                    }
                                                });
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
}