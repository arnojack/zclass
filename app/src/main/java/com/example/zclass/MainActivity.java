package com.example.zclass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zclass.online.Dialog.Dialog_Signin;
import com.example.zclass.online.Dialog.Dialog_Signup;
import com.example.zclass.online.Class_OnlineActivity;
import com.example.zclass.online.Info_User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mBtn_offline,mBtn_online;
    public Info_User user_info;
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
                    Dialog_Signin sign_Dialog =new Dialog_Signin(MainActivity.this,R.style.MyDialog);
                    sign_Dialog.setTitle("登录").setUsername("userid").setPassword("password")
                            .setsignin("登录", new Dialog_Signin.IonsigninListener() {
                                @Override
                                public void onsignin(Dialog dialog) {
                                    user_info.setUser_id(sign_Dialog.getUsername());
                                    user_info.setPassword(sign_Dialog.getPassword());

                                    sign_Dialog.hide();
                                    //跳转到线上课堂
                                    user_info.setFlag_login(1);
                                    Intent intent=new Intent(MainActivity.this, Class_OnlineActivity.class);
                                    intent.putExtra("user",user_info);
                                    startActivity(intent);
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