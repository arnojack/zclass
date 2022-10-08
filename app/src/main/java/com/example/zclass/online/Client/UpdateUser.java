package com.example.zclass.online.Client;


import android.content.Intent;
import android.util.Log;

import com.example.zclass.MainActivity;
import com.example.zclass.online.Entity.User;
import com.example.zclass.online.tool.BaseActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateUser {
private Updatelistener updatelistener;
    public static void Update(Updatelistener updatelistener){
        String url_login= BaseActivity.BaseUrl+"LoginServlet";
        HashMap<String, String> stringHashMap=new HashMap<String,String>();
        stringHashMap.put(User.USERID, MainActivity.user_info.getUserid());
        stringHashMap.put(User.WAY,"getuser");
        stringHashMap.put(User.METHOD,"update");
        HttpClientUtils.post(url_login, HttpClientUtils.maptostr(stringHashMap), new HttpClientUtils.OnRequestCallBack() {
            @Override
            public void onSuccess(String json) {
                updatelistener.onSuccess(json);
            }
            @Override
            public void onError(String errorMsg) {
                updatelistener.onError(errorMsg);
            }
        });
    }
    public interface Updatelistener {
        void onSuccess(String json);
        void onError(String errorMsg);
    }
    public static void update_onl(){
        UpdateUser.Update(new UpdateUser.Updatelistener() {
            @Override
            public void onSuccess(String json) {
                ArrayList<HashMap<String,String>> temp=null;
                try {
                    temp= BaseActivity.jtol_user(json);
                    HashMap t=temp.get(0);
                    MainActivity.user_info.setUserid((String) t.get(User.USERID));
                    MainActivity.user_info.setUsername((String) t.get(User.USERNAME));
                    MainActivity.user_info.setSex((String) t.get(User.SEX));
                    MainActivity.user_info.setCode((String) t.get(User.CODE));
                    MainActivity.user_info.setPhonenumber((String) t.get(User.PHONENUMBER));
                    MainActivity.user_info.setProfess((String) t.get(User.PROFESS));
                    MainActivity.user_info.setSchool((String) t.get(User.SCHOOL));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String errorMsg) {
                Log.e("UpdateUser","网络崩溃!"+errorMsg);
            }
        });
    }
    public static void update_dl(Intent intent){
        User m=(User) intent.getSerializableExtra("user");
        if(m!=null)
            MainActivity.user_info= m;
    }
}
