package com.example.zclass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zclass.Dialog.Dialog_sign;

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
                //跳转到线上课堂(学生版
               Dialog_sign sign_Dialog =new Dialog_sign(MainActivity.this);
               sign_Dialog.setTitle("系统提示").setMessage("请问您是学生还是教师?")
               .setStudent("学生", new Dialog_sign.IonstudentListener() {
                   @Override
                   public void onstudent(Dialog dialog) {
                       Intent intent=new Intent(MainActivity.this,Class_online_studentActivity.class);
                       startActivity(intent);
                   }
               }).setTeacher("教师", new Dialog_sign.IonteacherListener() {
                   @Override
                   public void onteacher(Dialog dialog) {

                   }
               }).show();

                break;
        }
    }
}