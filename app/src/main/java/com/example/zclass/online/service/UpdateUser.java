package com.example.zclass.online.service;

import android.widget.Toast;

import com.example.zclass.MainActivity;
import com.example.zclass.online.Dao.User;
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
}
