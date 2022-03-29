package com.example.zclass.online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zclass.MainActivity;
import com.example.zclass.R;
import com.example.zclass.online.Dao.Cou_Stu;
import com.example.zclass.online.Dao.Course;
import com.example.zclass.online.Dao.User;
import com.example.zclass.online.Dialog.Dialog_Creatclass;
import com.example.zclass.online.Dialog.Dialog_Joinclass;
import com.example.zclass.online.Dialog.LoadingDialog;
import com.example.zclass.online.fragment.ListviewAdapter;
import com.example.zclass.online.service.HttpClientUtils;
import com.example.zclass.online.tool.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class Class_OnlineActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBt_createdclass,mBt_joinedclass,mBt_pop;
    private PopupWindow mpop;
    public ListView lv;
    private Dialog dialog_lod;

    private String BaseUrl="http://192.168.0.106:8080/demo_war/";
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
        BottomNavigationView mNaviView=findViewById(R.id.bottom_navigation);
        mNaviView.setSelectedItemId(R.id.page_2);

        mNaviView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        Intent intent1=new Intent(Class_OnlineActivity.this,MainActivity.class);
                        intent1.putExtra("user",MainActivity.user_info);
                        startActivity(intent1);
                        Class_OnlineActivity.this.finish();
                        return true;
                    case R.id.page_2:
                        return true;
                    case R.id.page_3:
                        Intent intent=new Intent(Class_OnlineActivity.this,MyInfoActivity.class);
                        intent.putExtra("user",MainActivity.user_info);
                        startActivity(intent);
                        Class_OnlineActivity.this.finish();
                        return true;
                }
                return false;
            }
        });

        lv = (ListView) findViewById(R.id.listView1);
        dialog_lod = LoadingDialog.createLoadingDialog(Class_OnlineActivity.this);
        Mylisten_class();
        //test();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(getApplicationContext(), "你点击了第" + arg2 + "行",
                        Toast.LENGTH_SHORT).show();
            }
        });
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
                        Dialog_Joinclass joinclass = new Dialog_Joinclass(Class_OnlineActivity.this, R.style.MyDialog);
                        joinclass.setTitle("加入课程").setText("请输入课程编码").setsubmit("提交", new Dialog_Joinclass.IonsubmitListener() {
                            @Override
                            public void onsubmit(Dialog dialog) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Dialog dialog1 =LoadingDialog.createLoadingDialog(Class_OnlineActivity.this);
                                        dialog1.show();

                                        if("".equals(joinclass.getText())){
                                            dialog1.hide();
                                            Toast.makeText(getApplicationContext(), "请不要输入空消息!",
                                                    Toast.LENGTH_SHORT).show();
                                        }else {
                                            Myjoin_class(joinclass.getText());
                                            dialog1.hide();
                                            joinclass.cancel();
                                        }
                                    }
                                });
                            }
                        }).show();
                    }
                });
                TextView mTV_create=popview.findViewById(R.id.mtv_create);
                mTV_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mpop.dismiss();
                        //创建班级
                        Dialog_Creatclass creatclass = new Dialog_Creatclass(Class_OnlineActivity.this,R.style.MyDialog);
                        creatclass.setTitle("创建班级").setsubmit("提交", new Dialog_Creatclass.IonsubmitListener() {
                            @Override
                            public void onsubmit(Dialog dialog) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String cou_on_name=creatclass.getCouname();
                                        String cou_class =creatclass.getClassname();
                                        String cou_grade =creatclass.getGrade();
                                        //正在加载 图片
                                        Dialog dialog1 =LoadingDialog.createLoadingDialog(Class_OnlineActivity.this);
                                        dialog1.show();

                                        if("".equals(cou_on_name)||"".equals(cou_class)||"".equals(cou_grade)){
                                            dialog1.hide();
                                            Toast.makeText(getApplicationContext(), "请不要输入空消息!",
                                                    Toast.LENGTH_SHORT).show();
                                        }else {
                                            Course course =new Course();
                                            course.setCou_on_name(cou_on_name);
                                            course.setCou_class(cou_class);
                                            course.setCou_grade(cou_grade);

                                            course.setTea_userid(MainActivity.user_info.getUserid());
                                            course.setTea_name(MainActivity.user_info.getUsername());
                                            Mycreat_class(course);
                                            dialog1.hide();
                                            creatclass.cancel();
                                        }
                                    }
                                });
                            }
                        }).show();
                    }
                });
                break;
        }
    }
    public void Myjoin_class(String cou_on_id){
        /*定义一个以HashMap为内容的动态数组*/
        //User user_info =(User) getIntent().getSerializableExtra("user");
        HashMap<String, String> stringHashMap=new HashMap<String,String>();
        stringHashMap.put(Cou_Stu.STUID,MainActivity.user_info.getUserid());
        stringHashMap.put(Cou_Stu.COUONID, cou_on_id);
        stringHashMap.put(Course.METHOD,"Update");
        stringHashMap.put(User.WAY,"join");
        String url=BaseUrl+"CourseServlet";
        dialog_lod.show();

        HttpClientUtils.post(url, HttpClientUtils.maptostr(stringHashMap), new HttpClientUtils.OnRequestCallBack() {
            @Override
            public void onSuccess(String json) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog_lod.hide();
                        if("Ok".equals(json)){
                            Toast.makeText(getApplicationContext(), "加入成功!",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "班级不存在\n或已加入该班级!" + json,
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
    public void Mycreat_class(Course course){
        /*定义一个以HashMap为内容的动态数组*/
        //User user_info =(User) getIntent().getSerializableExtra("user");
        HashMap<String, String> stringHashMap=new HashMap<String,String>();
        stringHashMap.put(Course.TEAID, course.getTea_userid());
        if(course.getTea_name()!=null)
        stringHashMap.put(Course.TEANAME, course.getTea_name());
        else
            stringHashMap.put(Course.TEANAME, "");
        stringHashMap.put(Course.COUONNAME,course.getCou_on_name());
        stringHashMap.put(Course.COUGRADE,course.getCou_grade());
        stringHashMap.put(Course.COUCLASS,course.getCou_class());
        stringHashMap.put(Course.METHOD,"Update");
        stringHashMap.put(User.WAY,"create");
        String url=BaseUrl+"CourseServlet";
        dialog_lod.show();

        HttpClientUtils.post(url, HttpClientUtils.maptostr(stringHashMap), new HttpClientUtils.OnRequestCallBack() {
            @Override
            public void onSuccess(String json) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog_lod.hide();
                        if("Ok".equals(json)){
                            Toast.makeText(getApplicationContext(), "创建成功!",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "错误!" + json,
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
    public void test(){
        ArrayList<HashMap<String,Object>> temp=new ArrayList<>();
        for(int i=0;i<100;i++){
            HashMap<String, Object> stringHashMap=new HashMap<String,Object>();
            stringHashMap.put("第", i+"条消息");
            temp.add(stringHashMap);
        }
        lv.setAdapter(new ListviewAdapter(Class_OnlineActivity.this, temp));//为ListView绑定适配器
    }
    public void Mylisten_class(){
        /*定义一个以HashMap为内容的动态数组*/
        //User user_info =(User) getIntent().getSerializableExtra("user");
        HashMap<String, String> stringHashMap=new HashMap<String,String>();
        stringHashMap.put(Cou_Stu.STUID, MainActivity.user_info.getUserid());
        stringHashMap.put(Course.METHOD,"Queue");
        stringHashMap.put(User.WAY,"stuget");
        String url=BaseUrl+"CourseServlet";
        dialog_lod.show();

        HttpClientUtils.post(url, HttpClientUtils.maptostr(stringHashMap), new HttpClientUtils.OnRequestCallBack() {
            @Override
            public void onSuccess(String json) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog_lod.hide();
                        try {
                            ArrayList temp= BaseActivity.jtol_cou(json);
                            lv.setAdapter(new ListviewAdapter(Class_OnlineActivity.this, temp));//为ListView绑定适配器
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                        Toast.makeText(getApplicationContext(), "错误" + errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void Myteach_class(){
        /*定义一个以HashMap为内容的动态数组*/
        //User user_info =(User) getIntent().getSerializableExtra("user");
        HashMap<String, String> stringHashMap=new HashMap<String,String>();
        stringHashMap.put(Course.TEAID, MainActivity.user_info.getUserid());
        stringHashMap.put(Course.METHOD,"Queue");
        stringHashMap.put(User.WAY,"teaget");
        String url=BaseUrl+"CourseServlet";
        dialog_lod.show();

        HttpClientUtils.post(url, HttpClientUtils.maptostr(stringHashMap), new HttpClientUtils.OnRequestCallBack() {
            @Override
            public void onSuccess(String json) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog_lod.hide();
                        try {
                            ArrayList temp=BaseActivity.jtol_cou(json);
                            lv.setAdapter(new ListviewAdapter(Class_OnlineActivity.this,temp ));//为ListView绑定适配器
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                        Toast.makeText(getApplicationContext(), "错误" + errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}