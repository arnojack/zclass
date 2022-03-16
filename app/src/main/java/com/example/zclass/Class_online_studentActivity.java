package com.example.zclass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Class_online_studentActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBt_signin,mBt_submit,mBt_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_online);
        mBt_signin=findViewById(R.id.btn_signin);
        mBt_register=findViewById(R.id.btn_register);
        mBt_submit=findViewById(R.id.btn_submit_job);
        mBt_submit.setOnClickListener(this);
        mBt_register.setOnClickListener(this);
        mBt_signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signin:
                break;
            case R.id.btn_register:
                break;
            case R.id.btn_submit_job:
                break;
        }
    }
}