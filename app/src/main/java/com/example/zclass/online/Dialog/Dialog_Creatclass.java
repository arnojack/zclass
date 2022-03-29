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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.zclass.R;

public class Dialog_Creatclass extends Dialog implements View.OnClickListener{
    private String title;
    private String couname;
    private String grade;
    private String classname;
    private String submit;
    private TextView msubmit;
    private TextView mgrade;
    private TextView mclassname;
    private TextView mcouname;
    private IonsubmitListener submitListener;


    public String getSubmit() {
        TextView textView=findViewById(R.id.creat_submit);
        return textView.getText().toString();
    }

    public String getTitle() {
        return title;
    }

    public Dialog_Creatclass setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCouname() {
        TextView textView=findViewById(R.id.creat_couname);
        return textView.getText().toString();
    }



    public void setMcouname(TextView mcouname) {
        this.mcouname = mcouname;
    }



    public Dialog_Creatclass(@NonNull Context context, int themeResId) {
        super(context,themeResId);
    }

    public String getGrade() {
        TextView textView=findViewById(R.id.creat_grade);
        return textView.getText().toString();
    }


    public String getClassname() {
        TextView textView=findViewById(R.id.creat_class);
        return textView.getText().toString();
    }


    public Dialog_Creatclass setsubmit(String submit, Dialog_Creatclass.IonsubmitListener Listener) {
        this.submit = submit;
        this.submitListener=Listener;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_creatclass);
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

        TextView mTvtitle = findViewById(R.id.creat_title);
        msubmit =findViewById(R.id.creat_submit);
        mgrade =findViewById(R.id.creat_grade);

        Drawable leftDrawable2 = mgrade.getCompoundDrawables()[0];
        if(leftDrawable2!=null){
            leftDrawable2.setBounds(0, 0, 80, 80);
            mgrade.setCompoundDrawables(leftDrawable2, mgrade.getCompoundDrawables()[1]
                    , mgrade.getCompoundDrawables()[2], mgrade.getCompoundDrawables()[3]);
        }
        mclassname=findViewById(R.id.creat_class);
        Drawable leftDrawable3 = mclassname.getCompoundDrawables()[0];
        if(leftDrawable3!=null){
            leftDrawable3.setBounds(0, 0, 80, 80);
            mclassname.setCompoundDrawables(leftDrawable3, mclassname.getCompoundDrawables()[1]
                    , mclassname.getCompoundDrawables()[2], mclassname.getCompoundDrawables()[3]);
        }
        mcouname =findViewById(R.id.creat_couname);
        Drawable leftDrawable4 = mcouname.getCompoundDrawables()[0];
        if(leftDrawable4!=null){
            leftDrawable4.setBounds(0, 0, 80, 80);
            mcouname.setCompoundDrawables(leftDrawable4, mcouname.getCompoundDrawables()[1]
                    , mcouname.getCompoundDrawables()[2], mcouname.getCompoundDrawables()[3]);
        }

        if(!TextUtils.isEmpty(title)){
            mTvtitle.setText(title);
        }
        if(!TextUtils.isEmpty(submit)){
            msubmit.setText(submit);
        }

        msubmit.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.creat_submit:
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
