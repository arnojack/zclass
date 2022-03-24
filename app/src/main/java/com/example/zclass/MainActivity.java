package com.example.zclass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zclass.online.Dialog.Dialog_Signin;
import com.example.zclass.online.Dialog.Dialog_Signup;
import com.example.zclass.online.Class_OnlineActivity;
import com.example.zclass.online.Dao.Info_User;
import com.example.zclass.online.Dialog.LoadingDialog;
import com.example.zclass.online.service.HttpClientUtils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mBtn_offline,mBtn_online;
    private String BaseUrl="http://192.168.0.106:80/";
    public static Info_User user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn_offline=findViewById(R.id.btn_offline);
        mBtn_online=findViewById(R.id.btn_online);
        mBtn_online.setOnClickListener(this);
        mBtn_offline.setOnClickListener(this);
        user_info =new Info_User();
    }

    @Override
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()){
            case R.id.btn_offline:
                break;
            case R.id.btn_online:
                //跳转到登录页面
                if(user_info.getFlag_login()==1){
                    //跳转到线上课堂
                    intent=new Intent(MainActivity.this, Class_OnlineActivity.class);
                    intent.putExtra("user",user_info);
                    startActivity(intent);
                }else{
                    /*ProgressDialog pd =new ProgressDialog(this);
                    //pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setIcon(R.drawable.icon_progress);
                    pd.setCancelable(true);
*/
                    Dialog_Signin sign_Dialog =new Dialog_Signin(MainActivity.this,R.style.MyDialog);
                    sign_Dialog.setTitle("登录").setUsername("userid").setPassword("password")
                            .setsignin("登录", new Dialog_Signin.IonsigninListener() {
                                @Override
                                public void onsignin(Dialog dialog) {

                                    //正在加载 图片
                                    sign_Dialog.hide();

                                    Dialog dialog_lod =LoadingDialog.createLoadingDialog(MainActivity.this,"正在加载");
                                    dialog_lod.show();



                                    String url_login=BaseUrl+"LoginServlet";
                                    String user_id =sign_Dialog.getUsername();
                                    String user_password =sign_Dialog.getPassword();


                                    if(user_id==null||user_password==null){
                                        Toast.makeText(getApplicationContext(), "用户名或密码为空!",
                                                Toast.LENGTH_SHORT).show();
                                        sign_Dialog.show();
                                        //pd.cancel();
                                        dialog_lod.cancel();
                                    }else {

                                        if(user_info.getFlag_login()==1){

                                            //pd.cancel();
                                            dialog_lod.cancel();

                                            user_info.setUser_id(user_id);
                                            user_info.setPassword(user_password);
                                            sign_Dialog.hide();
                                            //跳转到线上课堂
                                            user_info.setFlag_login(1);
                                            Intent intent=new Intent(MainActivity.this, Class_OnlineActivity.class);
                                            intent.putExtra("user",user_info);
                                            startActivity(intent);
                                        }else{
                                            HashMap<String, String> stringHashMap=new HashMap<String,String>();
                                            stringHashMap.put(Info_User.USERID, user_id);
                                            stringHashMap.put(Info_User.PASSWORD, user_password);
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
                            signup_Dialog.setTitle("注册").setUsername("userid").setPassword("password")
                                    .setsubmit("提交", new Dialog_Signup.IonsubmitListener() {
                                        @Override
                                        public void onsubmit(Dialog dialog) {
                                            user_info.setUser_id(sign_Dialog.getUsername());
                                            user_info.setPassword(sign_Dialog.getPassword());

                                            signup_Dialog.hide();
                                            //跳转到线上课堂
                                            user_info.setFlag_login(1);
                                            Intent intent=new Intent(MainActivity.this, Class_OnlineActivity.class);
                                            intent.putExtra("user",user_info);
                                            startActivity(intent);
                                        }
                                    }).show();
                        }
                    }).show();
                }
                break;
        }
    }
}