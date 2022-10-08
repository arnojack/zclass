package com.example.zclass.online.Entity;

import java.io.Serializable;

public class Cou_Stu implements Serializable {
    private int    cou_stu_id;
    private String cou_on_id;
    private String stu_userid;
    private String way;

    public static String WAY = "way";
    public static String COUONID = "cou_on_id";
    public static String STUID = "stu_userid";


    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getCou_on_id() {
        return cou_on_id;
    }

    public void setCou_on_id(String cou_on_id) {
        this.cou_on_id = cou_on_id;
    }

    public String getStu_userid() {
        return stu_userid;
    }

    public void setStu_userid(String stu_userid) {
        this.stu_userid = stu_userid;
    }
}
