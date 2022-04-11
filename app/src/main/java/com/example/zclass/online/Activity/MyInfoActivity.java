package com.example.zclass.online.Activity;

import static com.example.zclass.online.tool.BaseActivity.setrTV;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zclass.MainActivity;
import com.example.zclass.R;
import com.example.zclass.online.Dao.User;
import com.example.zclass.online.Dialog.Dialog_upUser;
import com.example.zclass.online.Dialog.LoadingDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mTVuic;
    private TextView mTVuichang;
    private TextView mTVuid;
    private TextView mTVusex;
    private TextView mTVupassw;
    private TextView mTVuname;
    private TextView mTVuprofess;
    private TextView mTVuschool;
    private TextView mTVuphone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.myinfo);
        BottomNavigationView mNaviView=findViewById(R.id.bottom_navigation);
        mNaviView.setSelectedItemId(R.id.page_3);

        mNaviView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        Intent intent1=new Intent(MyInfoActivity.this, MainActivity.class);
                        intent1.putExtra("user",MainActivity.user_info);
                        startActivity(intent1);
                        MyInfoActivity.this.finish();
                        return true;
                    case R.id.page_2:
                        Intent intent=new Intent(MyInfoActivity.this, Class_OnlineActivity.class);
                        intent.putExtra("user",MainActivity.user_info);
                        startActivity(intent);
                        MyInfoActivity.this.finish();
                        return true;
                    case R.id.page_3:
                        return true;
                }
                return false;
            }
        });
        mTVuic=findViewById(R.id.info_icon);
        mTVuichang=findViewById(R.id.info_iconchange);
        setrTV(mTVuichang);
        mTVuid=findViewById(R.id.info_userid);
        setrTV(mTVuid);
        mTVuid.setText(MainActivity.user_info.getUserid());
        mTVusex=findViewById(R.id.info_sex);
        setrTV(mTVusex);
        mTVusex.setOnClickListener(this);
        mTVusex.setText(MainActivity.user_info.getSex());
        mTVupassw=findViewById(R.id.info_passw);
        setrTV(mTVupassw);
        mTVupassw.setOnClickListener(this);
        mTVupassw.setText(MainActivity.user_info.getPassword());
        mTVuname=findViewById(R.id.info_username);
        setrTV(mTVuname);
        mTVuname.setOnClickListener(this);
        mTVuname.setText(MainActivity.user_info.getUsername());
        mTVuprofess=findViewById(R.id.info_profess);
        setrTV(mTVuprofess);
        mTVuprofess.setOnClickListener(this);
        mTVuprofess.setText(MainActivity.user_info.getProfess());
        mTVuschool=findViewById(R.id.info_school);
        mTVuschool.setText(MainActivity.user_info.getSchool());
        setrTV(mTVuschool);
        mTVuschool.setOnClickListener(this);
        mTVuphone=findViewById(R.id.info_phone);
        mTVuphone.setText(MainActivity.user_info.getPhonenumber());
        setrTV(mTVuphone);
        mTVuphone.setOnClickListener(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.info_icon:
                break;
            case R.id.info_iconchange:
                break;
            case R.id.info_userid:
                break;
            case R.id.info_sex:
                HashMap<String, String> stringHashMap2=new HashMap<String,String>();
                stringHashMap2.put(User.WAY,"upsex");
                toast(User.SEX,mTVusex.getText().toString(),stringHashMap2);
                break;
            case R.id.info_passw:
                HashMap<String, String> stringHashMap3=new HashMap<String,String>();
                stringHashMap3.put(User.WAY,"uppass");
                toast(User.PASSWORD,mTVupassw.getText().toString(),stringHashMap3);
                break;
            case R.id.info_username:
                HashMap<String, String> stringHashMap4=new HashMap<String,String>();
                stringHashMap4.put(User.WAY,"upname");
                toast(User.USERNAME,mTVuname.getText().toString(),stringHashMap4);
                break;
            case R.id.info_school:
                HashMap<String, String> stringHashMap5=new HashMap<String,String>();
                stringHashMap5.put(User.WAY,"upschool");
                toast(User.SCHOOL,mTVuschool.getText().toString(),stringHashMap5);
                break;
            case R.id.info_phone:
                HashMap<String, String> stringHashMap6=new HashMap<String,String>();
                stringHashMap6.put(User.WAY,"upphone");
                toast(User.PHONENUMBER,mTVuphone.getText().toString(),stringHashMap6);
                break;
            case R.id.info_profess:
                HashMap<String, String> stringHashMap7=new HashMap<String,String>();
                stringHashMap7.put(User.WAY,"upprof");
                toast(User.PROFESS,mTVuprofess.getText().toString(),stringHashMap7);
                break;

        }
    }
    public void toast(String KEY, String text, HashMap<String,String> stringHashMap){
        HashMap<String, String> stringHashMap2=new HashMap<String,String>();
        stringHashMap2.put(User.USERID, MainActivity.user_info.getUserid());
        stringHashMap2.put(User.METHOD,"login");
        stringHashMap2.putAll(stringHashMap);

        Dialog dialog = LoadingDialog.createLoadingDialog(MyInfoActivity.this);

        Dialog_upUser Dialod_upsex = new Dialog_upUser(MyInfoActivity.this,R.style.MyDialog);
        Dialod_upsex.setKEY(KEY);
        Dialod_upsex.setText(text).setsubmit("LoginServlet",stringHashMap2, new Dialog_upUser.IonsaveListener() {
            @Override
            public void submit() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }
            @Override
            public void success(String json) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                        if("Ok".equals(json)){
                            Toast.makeText(getApplicationContext(), "更新成功!",
                                    Toast.LENGTH_SHORT).show();
                            switch (stringHashMap.get("way")){
                                case "upsex":
                                    MainActivity.user_info.setSex(Dialod_upsex.getText());
                                    mTVusex.setText(Dialod_upsex.getText());
                                    break;
                                case "uppass":
                                    MainActivity.user_info.setPassword(Dialod_upsex.getText());
                                    mTVupassw.setText(Dialod_upsex.getText());
                                    break;
                                case "upname":
                                    MainActivity.user_info.setUsername(Dialod_upsex.getText());
                                    mTVuname.setText(Dialod_upsex.getText());
                                    break;
                                case "upschool":
                                    MainActivity.user_info.setSchool(Dialod_upsex.getText());
                                    mTVuschool.setText(Dialod_upsex.getText());
                                    break;
                                case "upphone":
                                    MainActivity.user_info.setPhonenumber(Dialod_upsex.getText());
                                    mTVuphone.setText(Dialod_upsex.getText());
                                    break;
                                case "upprof":
                                    MainActivity.user_info.setProfess(Dialod_upsex.getText());
                                    mTVuprofess.setText(Dialod_upsex.getText());
                                    break;
                            }
                            Dialod_upsex.cancel();
                        }else {
                            Toast.makeText(getApplicationContext(), "更新失败!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void error(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                        Toast.makeText(getApplicationContext(), "网络崩溃!/n"+error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).show();
    }
}
