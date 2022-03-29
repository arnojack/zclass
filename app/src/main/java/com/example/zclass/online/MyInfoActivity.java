package com.example.zclass.online;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zclass.MainActivity;
import com.example.zclass.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MyInfoActivity extends AppCompatActivity {
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
        super.onCreate(savedInstanceState);
    }
}
