package com.example.zclass.online.Dao;

import java.io.Serializable;

public class Work implements Serializable {
    private int    cou_stu_id;
    private String cou_work_id;//作业编号
    private String cou_work;
    private String way;

    //用于URL传参和取参时的key
    public static String WAY = "way";
    public static String COUONID = "cou_on_id";
    public static String WORKID = "cou_work_id";
    public static String USERID = "stu_userid";


    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public int getCou_stu_id() {
        return cou_stu_id;
    }

    public void setCou_stu_id(int cou_stu_id) {
        this.cou_stu_id = cou_stu_id;
    }

    public String getCou_work_id() {
        return cou_work_id;
    }

    public void setCou_work_id(String cou_work_id) {
        this.cou_work_id = cou_work_id;
    }

    public String getCou_work() {
        return cou_work;
    }

    public void setCou_work(String cou_work) {
        this.cou_work = cou_work;
    }
}
