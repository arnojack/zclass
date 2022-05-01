package com.example.zclass.online.Dialog;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.zclass.R;

public class Dialog_Joinclass extends Dialog implements View.OnClickListener{
    private String title;
    private String text;
    private String submit;
    private ImageView msubmit;
    private TextView mTvtext;
    private IonsubmitListener submitListener;

    public String getTitle() {
        return title;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public String getText() {
        mTvtext=findViewById(R.id.join_class);
        return mTvtext.getText().toString();
    }

    public Dialog_Joinclass setText(String text) {
        this.text = text;
        return this;
    }

    public Dialog_Joinclass(@NonNull Context context, int themeResId) {
        super(context,themeResId);
    }

    public Dialog_Joinclass setTitle(String title) {
        this.title = title;
        return this;
    }

    public Dialog_Joinclass setsubmit(String submit, Dialog_Joinclass.IonsubmitListener Listener) {
        this.submit = submit;
        this.submitListener=Listener;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_joinclass);
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

        TextView mTvtitle = findViewById(R.id.join_title);
        mTvtext =findViewById(R.id.join_class);
        Drawable leftDrawable2 = mTvtext.getCompoundDrawables()[0];
        if(leftDrawable2!=null){
            leftDrawable2.setBounds(0, 0, 80, 80);
            mTvtext.setCompoundDrawables(leftDrawable2, mTvtext.getCompoundDrawables()[1]
                    , mTvtext.getCompoundDrawables()[2], mTvtext.getCompoundDrawables()[3]);
        }
        msubmit =findViewById(R.id.join_submit);

        msubmit.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.join_submit:
                if(submitListener!=null){
                    submitListener.onsubmit(this);
                }
                break;
        }
    }

    public interface IonsubmitListener{
        void onsubmit(Dialog dialog);
    }
}