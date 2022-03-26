package com.example.zclass.online;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zclass.R;
import com.example.zclass.online.fragment.ListviewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class Class_OnlineActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBt_createdclass,mBt_joinedclass,mBt_pop;
    private PopupWindow mpop;
    public ListView lv;
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

        lv = (ListView) findViewById(R.id.listView1);
        Mylisten_class();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(getApplicationContext(), "你点击了第" + arg2 + "行",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Mylisten_class(){
        /*定义一个以HashMap为内容的动态数组*/
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();/*在数组中存放数据*/
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", R.drawable.icon_password);//加入图片
            map.put("ItemTitle", "第" + i + "行");
            map.put("ItemText", "这是第" + i + "行");
            listItem.add(map);
        }
        lv.setAdapter(new ListviewAdapter(this, listItem));//为ListView绑定适配器
    }
    public void Myteach_class(){
        /*定义一个以HashMap为内容的动态数组*/
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();/*在数组中存放数据*/
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", R.drawable.icon_user);//加入图片
            map.put("ItemTitle", "第" + i + "行");
            map.put("ItemText", "这是第" + i + "行");
            listItem.add(map);
        }
        lv.setAdapter(new ListviewAdapter(this, listItem));//为ListView绑定适配器
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_CreatedClass:
                //跳转到我教的课
                Myteach_class();
                break;
            case R.id.btn_JoinedClass:
                //跳转到我听的课
                Mylisten_class();
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