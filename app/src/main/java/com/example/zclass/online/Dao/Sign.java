package com.example.zclass.online.Dao;

import java.io.Serializable;

public class Sign implements Serializable {
    private int    cou_stu_id;
    private String cou_sign_id;//签到编号
    private String cou_leave;
    private String sign;
    private String way;


    //用于URL传参和取参时的key
    public static String WAY = "way";
    public static String COUONID = "cou_on_id";
    public static String COUSIGNID = "cou_sign_id";
    public static String STUD = "stu_userid";
    
    public String getSign() {
        return sign;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getCou_stu_id() {
        return cou_stu_id;
    }

    public void setCou_stu_id(int cou_stu_id) {
        this.cou_stu_id = cou_stu_id;
    }

    public String getCou_sign_id() {
        return cou_sign_id;
    }

    public void setCou_sign_id(String cou_sign_id) {
        this.cou_sign_id = cou_sign_id;
    }

    public String getCou_leave() {
        return cou_leave;
    }

    public void setCou_leave(String cou_leave) {
        this.cou_leave = cou_leave;
    }
}
