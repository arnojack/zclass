package com.example.zclass.online.Dialog;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zclass.R;

public class Dialog_Signin extends Dialog implements View.OnClickListener{
    private String title;
    private String username;
    private String password;
    private String signup;
    private String signin;
    private IonsigninListener signinListener;
    private IonsignupListener signupListener;

    public String getPassword() {
        return password;
    }

    public Dialog_Signin setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Dialog_Signin setUsername(String username) {
        this.username = username;
        return this;
    }

    public Dialog_Signin(@NonNull Context context, int themeResId) {
        super(context,themeResId);
    }

    public Dialog_Signin setTitle(String title) {
        this.title = title;
        return this;
    }

    public Dialog_Signin setsignin(String signin, IonsigninListener Listener) {
        this.signin = signin;
        this.signinListener=Listener;
        return this;
    }

    public Dialog_Signin setsignup(String signup, IonsignupListener Listener) {
        this.signup = signup;
        this.signupListener=Listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign);
        /*
         * 将对话框的大小按屏幕大小的百分比设置
         */
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        Point size =new Point();
        d.getSize(size);
        p.width =(int)(size.x *0.8);//设置dialog的宽度为当前手机屏幕的宽度*0.8
        getWindow().setAttributes(p);

        TextView mTvsignin = findViewById(R.id.signin);
        TextView mTvsignup = findViewById(R.id.signup);
        TextView mTvtitle = findViewById(R.id.title_dialog);
        EditText mETusername = findViewById(R.id.username);
        EditText mETpassword = findViewById(R.id.password);
        if(!TextUtils.isEmpty(title)){
            mTvtitle.setText(title);
        }

        if(!TextUtils.isEmpty(signin)){
            mTvsignin.setText(signin);
        }
        if(!TextUtils.isEmpty(signup)){
            mTvsignup.setText(signup);
        }
        mTvsignup.setOnClickListener(this);
        mTvsignin.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signin:
                if(signinListener!=null){
                    signinListener.onsignin(this);
                }
                break;
            case R.id.signup:
                if(signupListener!=null){
                    signupListener.onsignup(this);
                }
                break;
        }
    }

    public interface IonsigninListener{
        void onsignin(Dialog dialog);
    }
    public interface IonsignupListener{
        void onsignup(Dialog dialog);
    }
}