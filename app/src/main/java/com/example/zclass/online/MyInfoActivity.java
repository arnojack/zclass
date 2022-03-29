package com.example.zclass.online;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zclass.MainActivity;
import com.example.zclass.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mTVuic;
    private TextView mTVuichang;
    private TextView mTVuid;
    private TextView mTVusex;
    private TextView mTVupassw;
    private TextView mTVuname;
    private TextView mTVuprofess;
    private TextView mTVuschool;
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
        setpic(mTVuichang);
        mTVuid=findViewById(R.id.info_userid);
        setpic(mTVuid);
        mTVuid.setText(MainActivity.user_info.getUserid());
        mTVusex=findViewById(R.id.info_sex);
        setpic(mTVusex);
        mTVusex.setText(MainActivity.user_info.getSex());
        mTVupassw=findViewById(R.id.info_passw);
        setpic(mTVupassw);
        mTVupassw.setText(MainActivity.user_info.getPassword());
        mTVuname=findViewById(R.id.info_username);
        setpic(mTVuname);
        mTVuname.setText(MainActivity.user_info.getUsername());
        mTVuprofess=findViewById(R.id.info_profess);
        setpic(mTVuprofess);
        mTVuprofess.setText(MainActivity.user_info.getProfess());
        mTVuschool=findViewById(R.id.info_school);
        mTVuschool.setText(MainActivity.user_info.getSchool());
        setpic(mTVuschool);
        super.onCreate(savedInstanceState);
    }
    public void setpic(TextView mETpassword){
        Drawable rightDrawable = mETpassword.getCompoundDrawables()[2];
        if(rightDrawable!=null){
            rightDrawable.setBounds(0, 0,50 , 50);
            mETpassword.setCompoundDrawables(mETpassword.getCompoundDrawables()[0], mETpassword.getCompoundDrawables()[1]
                    ,rightDrawable ,mETpassword.getCompoundDrawables()[3]);
        }
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
                break;
            case R.id.info_passw:
                break;
            case R.id.info_username:
                break;
            case R.id.info_school:
                break;

        }
    }
}
