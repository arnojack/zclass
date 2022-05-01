package com.example.zclass.online.Dialog;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.zclass.R;

public class Dialog_Signin extends Dialog implements View.OnClickListener{
    private String title;
    private String username;
    private String password;
    private String signup;
    private String signin;
    EditText mETusername ;
    EditText mETpassword ;
    private IonsigninListener signinListener;
    private IonsignupListener signupListener;

    public String getPassword() {
        mETpassword = findViewById(R.id.password);
        return mETpassword.getText().toString();
    }

    public Dialog_Signin setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUsername() {
        mETusername = findViewById(R.id.username);
        return mETusername.getText().toString();
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

        Button mTvsignin = findViewById(R.id.signin);
        Button mTvsignup = findViewById(R.id.signup);

        mETusername=findViewById(R.id.username);

        Drawable leftDrawable1 = mETusername.getCompoundDrawables()[0];
        if(leftDrawable1!=null){
            leftDrawable1.setBounds(0, 0, 80, 80);
            mETusername.setCompoundDrawables(leftDrawable1, mETusername.getCompoundDrawables()[1]
                    , mETusername.getCompoundDrawables()[2], mETusername.getCompoundDrawables()[3]);
        }
        mETpassword = findViewById(R.id.password);

        Drawable leftDrawable2 = mETpassword.getCompoundDrawables()[0];
        if(leftDrawable2!=null){
            leftDrawable2.setBounds(0, 0, 80, 80);
            mETpassword.setCompoundDrawables(leftDrawable2, mETpassword.getCompoundDrawables()[1]
                    , mETpassword.getCompoundDrawables()[2], mETpassword.getCompoundDrawables()[3]);
        }

        if(!TextUtils.isEmpty(username)){
            mETusername.setText(username);
        }
        if(!TextUtils.isEmpty(password)){
            mETpassword.setText(password);
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