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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mBtn_offline,mBtn_online;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn_offline=findViewById(R.id.btn_offline);
        mBtn_online=findViewById(R.id.btn_online);
        mBtn_online.setOnClickListener(this);
        mBtn_offline.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_offline:
                break;
            case R.id.btn_online:
                //跳转到登录页面
               Dialog_Signin sign_Dialog =new Dialog_Signin(MainActivity.this,R.style.MyDialog);
               sign_Dialog.setTitle("登录").setUsername("username").setPassword("password")
               .setsignin("signin", new Dialog_Signin.IonsigninListener() {
                   @Override
                   public void onsignin(Dialog dialog) {
                       String username=sign_Dialog.getUsername();
                       String password=sign_Dialog.getPassword();

                       sign_Dialog.hide();
                       //跳转到线上课堂
                       Intent intent=new Intent(MainActivity.this, Class_OnlineActivity.class);
                       startActivity(intent);
                   }
               }).setsignup("signup", new Dialog_Signin.IonsignupListener(){
                   @Override
                   public void onsignup(Dialog dialog) {
                       sign_Dialog.hide();
                       //跳转到注册页面
                       Dialog_Signup signup_Dialog =new Dialog_Signup(MainActivity.this,R.style.MyDialog);
                       signup_Dialog.setTitle("注册").setUsername("username").setPassword("password")
                               .setsubmit("submit", new Dialog_Signup.IonsubmitListener() {
                                   @Override
                                   public void onsubmit(Dialog dialog) {
                                       String username=signup_Dialog.getUsername();
                                       String password=signup_Dialog.getPassword();

                                       signup_Dialog.hide();
                                       Intent intent=new Intent(MainActivity.this, Class_OnlineActivity.class);
                                       startActivity(intent);
                                   }
                               }).show();
                   }
               }).show();

                break;
        }
    }
}