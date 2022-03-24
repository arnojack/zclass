package com.example.zclass.online.service;

import com.example.zclass.online.Dao.Info_User;

import java.util.HashMap;

public class Http_Login {
    public String loginGET(String url,String et_data_uid,String et_data_upass) {

        Http_Unit unit =new Http_Unit();
        HashMap<String, String> stringHashMap=new HashMap<String,String>();
        stringHashMap.put(Info_User.USERID, et_data_uid);
        stringHashMap.put(Info_User.PASSWORD, et_data_upass);
        String n=unit.getRun(url,stringHashMap);
        return  n;
    }

    public void loginPOST(String url,String et_data_uid,String et_data_upass) {

        HashMap<String, String> stringHashMap = new HashMap<String, String>();
        stringHashMap.put(Info_User.USERID, et_data_uid);
        stringHashMap.put(Info_User.PASSWORD, et_data_upass);
    }
}
