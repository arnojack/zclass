package com.example.zclass.Dialog;

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
import android.widget.TextView;

import com.example.zclass.R;

public class Dialog_sign extends Dialog implements View.OnClickListener{
    private String title,message,student,teacher;
    private IonstudentListener studentListener;
    private IonteacherListener teacherListener;
    public Dialog_sign(@NonNull Context context) {
        super(context);
    }

    public Dialog_sign setMessage(String message) {
        this.message = message;
        return this;
    }

    public Dialog_sign setTitle(String title) {
        this.title = title;
        return this;
    }

    public Dialog_sign setStudent(String student, IonstudentListener Listener) {
        this.student = student;
        this.studentListener=Listener;
        return this;
    }

    public Dialog_sign setTeacher(String teacher, IonteacherListener Listener) {
        this.teacher = teacher;
        this.teacherListener=Listener;
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

        TextView mTvstudent = findViewById(R.id.student);
        TextView mTvteacher = findViewById(R.id.teacher);
        TextView mTvtitle = findViewById(R.id.title_dialog);
        TextView mTvmessage = findViewById(R.id.message_dialog);
        if(!TextUtils.isEmpty(title)){
            mTvtitle.setText(title);
        }
        if(!TextUtils.isEmpty(message)){
            mTvmessage.setText(message);
        }
        if(!TextUtils.isEmpty(student)){
            mTvstudent.setText(student);
        }
        if(!TextUtils.isEmpty(teacher)){
            mTvteacher.setText(teacher);
        }
        mTvteacher.setOnClickListener(this);
        mTvstudent.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.student:
                if(studentListener!=null){
                    studentListener.onstudent(this);
                }
                break;
            case R.id.teacher:
                if(teacherListener!=null){
                    teacherListener.onteacher(this);
                }
                break;
        }
    }

    public interface IonstudentListener{
        void onstudent(Dialog dialog);
    }
    public interface IonteacherListener{
        void onteacher(Dialog dialog);
    }
}