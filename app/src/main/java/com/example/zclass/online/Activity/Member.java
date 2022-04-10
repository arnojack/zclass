package com.example.zclass.online.Activity;

import static com.example.zclass.online.tool.BaseActivity.BaseUrl;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zclass.R;
import com.example.zclass.online.Dao.Cou_Stu;
import com.example.zclass.online.Dao.Course;
import com.example.zclass.online.Dao.User;
import com.example.zclass.online.Dialog.LoadingDialog;
import com.example.zclass.online.fragment.MemAdapter;
import com.example.zclass.online.service.HttpClientUtils;
import com.example.zclass.online.tool.BaseActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Member extends AppCompatActivity {

    public ListView lv;
    private Dialog dialog_lod;

    private TextView mem_num;
    private String cou_on_id;
    private String teaid;
    private String teaname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member);
        mem_num=findViewById(R.id.mem_num);
        cou_on_id=getIntent().getStringExtra(Cou_Stu.COUONID);
        teaid=getIntent().getStringExtra(Course.TEAID);
        teaname=getIntent().getStringExtra(Course.TEANAME);

        lv = (ListView) findViewById(R.id.mem_lv);
        dialog_lod = LoadingDialog.createLoadingDialog(Member.this);
        getmem();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }
        });
    }
    private void getmem(){
        HashMap<String, String> stringHashMap=new HashMap<String,String>();
        stringHashMap.put(Cou_Stu.COUONID, cou_on_id);
        stringHashMap.put(Course.METHOD,"Queue");
        stringHashMap.put(Cou_Stu.WAY,"memget");
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
                            ArrayList tt=new ArrayList();
                            ArrayList temp= BaseActivity.jtol_user(json);
                            Map<String,String>map=new HashMap<>();
                            map.put(User.USERID,teaid);
                            map.put(User.USERNAME,teaname);
                            map.put("title","教师");
                            tt.add(map);
                            tt.addAll(temp);
                            mem_num.setText("成员: "+tt.size()+"人");
                            lv.setAdapter(new MemAdapter(Member.this, tt));//为ListView绑定适配器
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