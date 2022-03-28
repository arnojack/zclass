package com.example.zclass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zclass.online.Dao.User;
import com.example.zclass.online.Dialog.Dialog_Signin;
import com.example.zclass.online.Dialog.Dialog_Signup;
import com.example.zclass.online.Class_OnlineActivity;
import com.example.zclass.online.Dialog.LoadingDialog;
import com.example.zclass.online.service.HttpClientUtils;
import com.example.zclass.online.service.UpdateUser;
import com.example.zclass.online.tool.BaseActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mBtn_offline,mBtn_online;
    private String BaseUrl="http://192.168.0.106:8080/demo_war/";
    public static User user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn_offline=findViewById(R.id.btn_offline);
        mBtn_online=findViewById(R.id.btn_online);
        mBtn_online.setOnClickListener(this);
        mBtn_offline.setOnClickListener(this);
        user_info =new User();
    }

    @Override
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()){
            case R.id.btn_offline:
                break;
            case R.id.btn_online:
                //跳转到登录页面

                    //跳转到线上课堂
                    user_info.setUserid("1");
                    intent=new Intent(MainActivity.this, Class_OnlineActivity.class);
                    intent.putExtra("user",user_info);
                    startActivity(intent);/*if(user_info.getFlag_login()==1){
                }else{
                    String url_login=BaseUrl+"LoginServlet";

                    Dialog_Signin sign_Dialog =new Dialog_Signin(MainActivity.this,R.style.MyDialog);
                    sign_Dialog.setTitle("登录").setUsername("userid").setPassword("password")
                            .setsignin("登录", new Dialog_Signin.IonsigninListener() {
                                @Override
                                public void onsignin(Dialog dialog) {

                                    //正在加载 图片
                                    //sign_Dialog.hide();
                                    Dialog dialog_lod =LoadingDialog.createLoadingDialog(MainActivity.this);
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
                                            //跳转到线上课堂
                                            user_info.setFlag_login(1);
                                            Intent intent=new Intent(MainActivity.this, Class_OnlineActivity.class);
                                            intent.putExtra("user",user_info);
                                            startActivity(intent);
                                        }else{
                                            update();
                                            HashMap<String, String> stringHashMap=new HashMap<String,String>();
                                            stringHashMap.put(User.USERID, user_id);
                                            stringHashMap.put(User.PASSWORD, user_password);
                                            stringHashMap.put(User.METHOD,"login");
                                            stringHashMap.put(User.WAY,"signin");

                                            HttpClientUtils.post(url_login, HttpClientUtils.maptostr(stringHashMap), new HttpClientUtils.OnRequestCallBack() {
                                                @Override
                                                public void onSuccess(String json) {
                                                    //跳转到线上课堂
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
                                                                Intent intent=new Intent(MainActivity.this, Class_OnlineActivity.class);
                                                                intent.putExtra("user",user_info);
                                                                startActivity(intent);
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
                                                update();
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
                                                                    //跳转到线上课堂
                                                                    signup_Dialog.hide();
                                                                    user_info.setFlag_login(1);
                                                                    Intent intent=new Intent(MainActivity.this, Class_OnlineActivity.class);
                                                                    intent.putExtra("user",user_info);
                                                                    startActivity(intent);
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
                }*/
                break;
        }
    }
    public void update(){
        UpdateUser.Update(new UpdateUser.Updatelistener() {
            @Override
            public void onSuccess(String json) {
                ArrayList<HashMap<String,String>> temp=null;
                try {
                    temp= BaseActivity.jtol_user(json);
                    HashMap t=temp.get(0);
                    user_info.setUserid((String) t.get(User.USERID));
                    user_info.setUsername((String) t.get(User.USERNAME));
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
}