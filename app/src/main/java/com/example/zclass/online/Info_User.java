package com.example.zclass.online;

import java.io.Serializable;

public class Info_User implements Serializable {
    private String password;
    private String truename;
    private String Stu_profess;
    private String Stu_school;
    private String Stu_grade;
    private String Stu_class;
    private int Stu_id;
    private int tea_id;
    private String user_id;
    private int flag_login;

    public int getFlag_login() {
        return flag_login;
    }

    public void setFlag_login(int flag_login) {
        this.flag_login = flag_login;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getTea_id() {
        return tea_id;
    }

    public void setTea_id(int tea_id) {
        this.tea_id = tea_id;
    }

    public int getStu_id() {
        return Stu_id;
    }

    public void setStu_id(int stu_id) {
        Stu_id = stu_id;
    }

    public String getStu_class() {
        return Stu_class;
    }

    public void setStu_class(String stu_class) {
        Stu_class = stu_class;
    }

    public String getStu_grade() {
        return Stu_grade;
    }

    public void setStu_grade(String stu_grade) {
        Stu_grade = stu_grade;
    }

    public String getStu_school() {
        return Stu_school;
    }

    public void setStu_school(String stu_school) {
        Stu_school = stu_school;
    }

    public String getStu_profess() {
        return Stu_profess;
    }

    public void setStu_profess(String stu_profess) {
        Stu_profess = stu_profess;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
