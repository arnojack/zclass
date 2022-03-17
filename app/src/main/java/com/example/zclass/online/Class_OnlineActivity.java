package com.example.zclass.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.zclass.R;

public class Class_OnlineActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBt_createdclass,mBt_joinedclass,mBt_pop;
    private PopupWindow mpop;
    private My_CreatedClassActivity teachFrament;
    private My_JoinedClassActivity listenFrament;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_online);
        mBt_createdclass=findViewById(R.id.btn_CreatedClass);
        mBt_joinedclass=findViewById(R.id.btn_JoinedClass);
        mBt_pop=findViewById(R.id.btn_pop);
        mBt_createdclass.setOnClickListener(this);
        mBt_joinedclass.setOnClickListener(this);
        mBt_pop.setOnClickListener(this);

        teachFrament =new My_CreatedClassActivity();
        listenFrament =new My_JoinedClassActivity();
        getFragmentManager().beginTransaction().replace(R.id.mFL_container, listenFrament).commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_CreatedClass:
                //跳转到我教的课
                getFragmentManager().beginTransaction().replace(R.id.mFL_container, teachFrament).commitAllowingStateLoss();
                break;
            case R.id.btn_JoinedClass:
                //跳转到我听的课
                getFragmentManager().beginTransaction().replace(R.id.mFL_container, listenFrament).commitAllowingStateLoss();
                break;
            case R.id.btn_pop:
                //下拉框
                View popview =getLayoutInflater().inflate(R.layout.activity_pop_window,null);
                mpop =new PopupWindow(popview,mBt_pop.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                mpop.setOutsideTouchable(true);
                mpop.setFocusable(true);
                mpop.showAsDropDown(mBt_pop);
                TextView mTV_join=popview.findViewById(R.id.mtv_jion);
                mTV_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mpop.dismiss();
                        //加入班级
                    }
                });
                TextView mTV_create=popview.findViewById(R.id.mtv_create);
                mTV_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mpop.dismiss();
                        //创建班级
                    }
                });
                break;
        }
    }
}