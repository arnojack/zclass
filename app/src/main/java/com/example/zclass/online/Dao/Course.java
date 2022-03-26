package com.example.zclass.online.Dao;

import java.io.Serializable;

public class Course implements Serializable {
    private String cou_on_id;
    private String cou_on_name;
    private String tea_userid;
    private String cou_grade;
    private String cou_class;
    private String way;

    //用于URL传参和取参时的key
    public static String WAY = "way";
    public static String COUONNAME = "cou_on_name";
    public static String COUONID = "cou_on_id";
    public static String TEAID = "tea_userid";

    public String getCou_on_id() {
        return cou_on_id;
    }

    public void setCou_on_id(String cou_on_id) {
        this.cou_on_id = cou_on_id;
    }

    public String getCou_on_name() {
        return cou_on_name;
    }

    public void setCou_on_name(String cou_on_name) {
        this.cou_on_name = cou_on_name;
    }

    public String getTea_id() {
        return tea_userid;
    }

    public void setTea_id(String tea_id) {
        this.tea_userid = tea_id;
    }
    public String getWay() {
        return way;
    }

    public String getTea_userid() {
        return tea_userid;
    }

    public void setTea_userid(String tea_userid) {
        this.tea_userid = tea_userid;
    }

    public String getCou_grade() {
        return cou_grade;
    }

    public void setCou_grade(String cou_grade) {
        this.cou_grade = cou_grade;
    }

    public String getCou_class() {
        return cou_class;
    }

    public void setCou_class(String cou_class) {
        this.cou_class = cou_class;
    }

    public void setWay(String way) {
        this.way = way;
    }
}
