package com.example.zclass.online.Dialog;

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

import androidx.annotation.NonNull;

import com.example.zclass.R;
import com.example.zclass.online.service.HttpClientUtils;
import com.example.zclass.online.tool.BaseActivity;

import java.util.HashMap;

public class Dialog_upUser extends Dialog implements View.OnClickListener{
    private String title;
    private String text;
    private String submit;
    private TextView msubmit;
    private String KEY;
    private TextView mTvtext;
    private IonsaveListener saveListener;
    private HashMap<String, String> stringHashMap;

    String url;

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
        mTvtext=findViewById(R.id.upuser_info);
        return mTvtext.getText().toString();
    }
    public String getKEY() {
        return KEY;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }

    public Dialog_upUser setText(String text) {
        this.text = text;
        return this;
    }

    public TextView getMsubmit() {
        return msubmit;
    }


    public Dialog_upUser(@NonNull Context context, int themeResId) {
        super(context,themeResId);
    }

    public Dialog_upUser setTitle(String title) {
        this.title = title;
        return this;
    }

    public Dialog_upUser setsubmit(String url,HashMap<String, String> stringHashMap, Dialog_upUser.IonsaveListener Listener) {
        this.saveListener=Listener;
        this.stringHashMap =stringHashMap;
        this.url=url;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upuser);
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

        mTvtext =findViewById(R.id.upuser_info);

        msubmit =findViewById(R.id.upuser_save);


        if(!TextUtils.isEmpty(text)){
            mTvtext.setText(text);
        }

        msubmit.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.upuser_save:
                stringHashMap.put(KEY,getText());
                saveListener.submit();
                HttpClientUtils.post(BaseActivity.BaseUrl+url, HttpClientUtils.maptostr(stringHashMap), new HttpClientUtils.OnRequestCallBack() {
                    @Override
                    public void onSuccess(String json) {
                        saveListener.success(json);
                    }
                    @Override
                    public void onError(String errorMsg) {
                        saveListener.error(errorMsg);
                    }
                });
                break;
        }
    }

    public interface IonsaveListener{
        void submit();
        void success(String json);
        void error(String error);
    }
}