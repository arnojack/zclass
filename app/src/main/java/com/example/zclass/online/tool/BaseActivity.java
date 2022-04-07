package com.example.zclass.online.tool;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zclass.online.Dao.Course;
import com.example.zclass.online.Dao.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseActivity {
    public static void setbackground(View view,double num) {
        Drawable drawable = view.getBackground();
        view.setBackground(null);

        view.post(() -> {
            int width = view.getWidth();
            int height = view.getHeight();

            view.setBackground(drawable);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = (int) (num*width);
            layoutParams.height =(int) (num*height);
        });
    }
    public static ArrayList jtol_cou(String data) throws JSONException {

        Map<String,String> map=new HashMap<>();
        ArrayList<Map> mlists = new ArrayList<Map>();
        JSONArray array = new JSONArray(new String(data));
        for (int i = 0; i < array.length(); i++) {
            Map<String,String> m=new HashMap<>();
            JSONObject item = array.getJSONObject(i);

            if(item.has(Course.COUONID)) m.put(Course.COUONID,item.getString(Course.COUONID)) ;
            if(item.has(Course.COUONNAME)) m.put(Course.COUONNAME,item.getString(Course.COUONNAME)) ;
            if(item.has(Course.COUGRADE)) m.put(Course.COUGRADE,item.getString(Course.COUGRADE)) ;
            if(item.has(Course.COUCLASS)) m.put(Course.COUCLASS,item.getString(Course.COUCLASS)) ;
            if(item.has(Course.TEAID)) m.put(Course.TEAID,item.getString(Course.TEAID)) ;
            if(item.has(Course.TEANAME)) m.put(Course.TEANAME,item.getString(Course.TEANAME)) ;

            mlists.add(m);
        }

        return mlists;
    }
    public static ArrayList jtol_user(String data) throws JSONException {

        Map<String,String> map=new HashMap<>();
        ArrayList<Map> mlists = new ArrayList<Map>();
        JSONArray array = new JSONArray(new String(data));
        for (int i = 0; i < array.length(); i++) {
            Map<String,String> m=new HashMap<>();
            JSONObject item = array.getJSONObject(i);
            String temp=null;

            if(item.has(User.USERID)) m.put(User.USERID,item.getString(User.USERID)) ;
            if(item.has(User.USERNAME)) m.put(User.USERNAME,item.getString(User.USERNAME)) ;
            if(item.has(User.SEX)) m.put(User.SEX,item.getString(User.SEX)) ;
            if(item.has(User.CODE)) m.put(User.CODE,item.getString(User.CODE)) ;
            if(item.has(User.USERID)) m.put(User.USERID,item.getString(User.USERID)) ;
            if(item.has(User.PASSWORD)) m.put(User.PASSWORD,item.getString(User.PASSWORD)) ;
            if(item.has(User.PHONENUMBER)) m.put(User.PHONENUMBER,item.getString(User.PHONENUMBER)) ;
            if(item.has(User.PROFESS)) m.put(User.PROFESS,item.getString(User.PROFESS)) ;
            if(item.has(User.SCHOOL)) m.put(User.SCHOOL,item.getString(User.SCHOOL)) ;

            mlists.add(m);
        }

        return mlists;
    }
}
