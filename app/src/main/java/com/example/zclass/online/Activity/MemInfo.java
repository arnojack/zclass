package com.example.zclass.online.Activity;

import static com.example.zclass.online.tool.BaseActivity.BaseUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.AudioFocusRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zclass.MainActivity;
import com.example.zclass.R;
import com.example.zclass.online.Dao.Cou_Stu;
import com.example.zclass.online.Dao.Course;
import com.example.zclass.online.Dao.User;
import com.example.zclass.online.Dialog.Dialog_Confim;
import com.example.zclass.online.Dialog.LoadingDialog;
import com.example.zclass.online.service.HttpClientUtils;
import com.example.zclass.online.tool.BaseActivity;

import java.util.HashMap;

public class MemInfo extends AppCompatActivity implements View.OnClickListener {
    private ImageView icon;
    private TextView name;
    private TextView sex;
    private TextView profess;
    private TextView school;
    private ImageView delete;

    private String stu_id;
    private String roomid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_info);
        findview();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.meminfo_delete:
                Dialog_Confim confim1 = new Dialog_Confim(MemInfo.this,R.style.upuser);
                confim1.setsubmit(new Dialog_Confim.IonsaveListener() {
                    @Override//点击取消按钮
                    public void submit() {
                        confim1.cancel();
                    }
                }, new Dialog_Confim.IonsaveListener() {
                    @Override//点击确认按钮
                    public void submit() {
                        HashMap<String, String> stringHashMap=new HashMap<String,String>();
                        stringHashMap.put(Cou_Stu.STUID, stu_id);
                        stringHashMap.put(Cou_Stu.COUONID, roomid);
                        stringHashMap.put(Course.METHOD,"Update");
                        stringHashMap.put(Cou_Stu.WAY,"delstu");
                        delete(stringHashMap);
                        MemInfo.this.finish();
                    }
                }).show();
                break;
        }
    }
    private void findview(){
        roomid=getIntent().getStringExtra(Course.COUONID);
        stu_id=getIntent().getStringExtra(User.USERID);

        icon=findViewById(R.id.meminfo_icon);
        BaseActivity.iconDO(icon,stu_id);
        name=findViewById(R.id.meminfo_username);
        name.setText(getIntent().getStringExtra(User.USERNAME));

        sex=findViewById(R.id.meminfo_sex);
        sex.setText(getIntent().getStringExtra(User.SEX));

        profess=findViewById(R.id.meminfo_profess);
        profess.setText(getIntent().getStringExtra(User.PROFESS));

        school=findViewById(R.id.meminfo_school);
        school.setText(getIntent().getStringExtra(User.SCHOOL));

        delete=findViewById(R.id.meminfo_delete);


        String tea_id=getIntent().getStringExtra(Course.TEAID);
        if(!MainActivity.user_info.getUserid().equals(tea_id)
            || stu_id.equals(tea_id)){
            delete.setVisibility(View.GONE);
        }else {
            delete.setOnClickListener(this);
        }
    }
    public void delete(HashMap stringHashMap){
        String url=BaseUrl+"CourseServlet";

        Dialog dialog_lod = LoadingDialog.createLoadingDialog(MemInfo.this);
        dialog_lod.show();

        HttpClientUtils.post(url, HttpClientUtils.maptostr(stringHashMap), new HttpClientUtils.OnRequestCallBack() {
            @Override
            public void onSuccess(String json) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog_lod.hide();
                        if("Ok".equals(json)){
                            Toast.makeText(getApplicationContext(), "操作成功!",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "操作失败!" + json,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onError(String errorMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog_lod.hide();
                        Toast.makeText(getApplicationContext(), "错误!" + errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}