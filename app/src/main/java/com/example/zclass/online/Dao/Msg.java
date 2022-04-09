package com.example.zclass.online.Dao;
import java.io.Serializable;
import java.util.Date;

/*
 我们需要单例保存的数据为：
 1.消息的内容；
 2.消息的类型：发送还是接收；
 3.消息创建时间
 */
public class Msg implements Serializable {
    private String userid;
    private String name;
    private String content;
    private String user_ty;
    private int type;
    private Date time;
    public final static int TYPE_RECEIVED=0;
    public final static int TYPE_SENT=1;

    public static String STU="student";
    public static String TEA="teacher";
    public static String USERTY="user_ty";
    public static String USERID="userid";
    public static String NAME="name";
    public static String CONTENT = "content";
    public static String TYPE = "type";
    public static String TIME = "time";

    public Msg(String userid,String name,String user_ty,String content,int type,Date time){
        this.userid=userid;
        this.name=name;
        this.user_ty=user_ty;
        this.content =content;
        this.type = type;
        this.time = time;
    }

    public String getUser_ty() {
        return user_ty;
    }

    public void setUser_ty(String user_ty) {
        this.user_ty = user_ty;
    }

    public Msg() {

    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public Date getTime() {
        return time;
    }

}