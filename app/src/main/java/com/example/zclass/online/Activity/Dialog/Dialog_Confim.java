package com.example.zclass.online.Activity.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.zclass.R;


public class Dialog_Confim extends Dialog implements View.OnClickListener{

    private Button mTVconcel;
    private Button mTVconfim;

    private Dialog_Confim.IonsaveListener saveListener1;
    private Dialog_Confim.IonsaveListener saveListener2;


    public Dialog_Confim(@NonNull Context context, int themeResId) {
        super(context,themeResId);
    }

    public Dialog_Confim setsubmit( Dialog_Confim.IonsaveListener Listener1,Dialog_Confim.IonsaveListener Listener2) {
        this.saveListener1=Listener1;
        this.saveListener2=Listener2;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confim);
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

        mTVconcel =findViewById(R.id.delet_cancel);
        mTVconcel.setOnClickListener(this);
        mTVconfim =findViewById(R.id.delet_confim);
        mTVconfim.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delet_cancel:
                saveListener1.submit();
                break;
            case R.id.delet_confim:
                saveListener2.submit();
                break;
        }
    }

    public interface IonsaveListener{
        void submit();
    }
}
