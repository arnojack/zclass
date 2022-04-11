package com.example.zclass.offline.dao;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.zclass.offline.pojo.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseDao extends SQLiteOpenHelper {

    private String tableName = "t_course";
    public CourseDao(Context context){
        super(context, "timetable.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table t_course(" +
                "_id integer primary key autoincrement," +
                "courseName varchar(50)," +
                "teacherName varchar(50)," +
                "classroom varchar(50), " +
                "weekType varchar(50), " +
                "day integer," +
                "section integer," +
                "startWeek integer," +
                "endWeek integer," +
                "courseTime varchar(255)" +
                ")";
        db.execSQL(sql);
    }

    public long insert(String nullColumnHack, ContentValues values){
        SQLiteDatabase database = getWritableDatabase();
        long count = database.insert(tableName, nullColumnHack, values);
        database.close();
        return count;
    }

    public long insert(@NonNull Course course){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        if(!TextUtils.isEmpty(course.getCourseName())){
            values.put("courseName", course.getCourseName());
        }
        if(!TextUtils.isEmpty(course.getTeacherName())){
            values.put("teacherName", course.getTeacherName());
        }
        if(!TextUtils.isEmpty(course.getCourseTime())) {
            values.put("courseTime", course.getCourseTime());
        }
        if(course.getStartWeek() != 0) {
            values.put("startWeek", course.getStartWeek());
        }
        if(course.getEndWeek() != 0) {
            values.put("endWeek", course.getEndWeek());
        }
        long count = database.insert(tableName, null, values);
        database.close();
        return count;
    }

    public int delete(String whereClause, String[] whereArgs){
        SQLiteDatabase database = getWritableDatabase();
        int count = database.delete(tableName, whereClause, whereArgs);
        database.close();
        return count;
    }

    public int delete(int id){
        SQLiteDatabase database = getWritableDatabase();
        int count = database.delete(tableName, "_id=?", new String[]{String.valueOf(id)});
        database.close();
        return count;
    }

    public int update(ContentValues values, String whereClause, String[] whereArgs){
        SQLiteDatabase database = getWritableDatabase();
        int count = database.update(tableName, values, whereClause, whereArgs);
        database.close();
        return count;
    }

    public int update(Course course){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        if(!TextUtils.isEmpty(course.getCourseName())){
            values.put("courseName", course.getCourseName());
        }
        if(!TextUtils.isEmpty(course.getTeacherName())){
            values.put("teacherName", course.getTeacherName());
        }
        if(null != course.getCourseTime()) {
            values.put("courseTime", course.getCourseTime());
        }
        if(course.getStartWeek() != 0) {
            values.put("startWeek", course.getStartWeek());
        }
        if(course.getEndWeek() != 0) {
            values.put("endWeek", course.getEndWeek());
        }
        int count = database.update(tableName, values, "_id=?", new String[]{String.valueOf(course.getId())});
        database.close();
        return count;
    }

    public List<Course> listAll(){
        List<Course> list = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        Cursor data = database.query(tableName, null, null, null, null, null, null);
        if(data.getCount() > 0){
            while(data.moveToNext()) {
                Course course = new Course();
                course.setId(data.getInt(0));
                course.setCourseName(data.getString(1));
                course.setTeacherName(data.getString(2));
                course.setClassroom(data.getString(3));
                course.setWeekType(data.getString(4));
                course.setDay(data.getInt(5));
                course.setSection(data.getInt(6));
                course.setStartWeek(data.getInt(7));
                course.setEndWeek(data.getInt(8));
                course.setCourseTime(data.getString(9));
                list.add(course);
            }
        }
        database.close();
        return list;
    }
    @SuppressLint("Range")
    public List<Course> query1(){
        SQLiteDatabase db1 = getWritableDatabase();
        List<Course> cs = new ArrayList<>();
        int a= 1;

        Cursor cursor = db1.query("t_course",null,null,null,null,null,null);
        if(cursor!=null) {
            while (cursor.moveToNext()) {

                String courseName1=  cursor.getString(cursor.getColumnIndex("courseName"));
                String teacherName1=  cursor.getString(cursor.getColumnIndex("teacherName"));
                String weekType1=  cursor.getString(cursor.getColumnIndex("weekType"));
                int day1 = cursor.getInt(cursor.getColumnIndex("day"));
                Course c = new Course();
                c.setDay(day1);
                c.setCourseName(courseName1);
                c.setWeekType(weekType1);
                c.setTeacherName(teacherName1);
                cs.add(c);
            }
        }
        cursor.close();
        return cs;
    }
    public List<Course> query2(int today1){
        int today = today1;
        SQLiteDatabase db = getWritableDatabase();
        String sql_search_day = "SELECT * FROM t_course WHERE day=today";
        Cursor result = db.rawQuery("SELECT * FROM t_course", null);
        List<Course> cs = new ArrayList<>();
        for (result.moveToFirst(); !result.isAfterLast();

             result.moveToNext()) {
            String courseName1=  result.getString(1);
            String teacherName1=  result.getString(2);
            String weekType1=  result.getString(7);
            int startwWeek1=  result.getInt(4);
            int endWeek1=  result.getInt(5);
            int day1 = result.getInt(8);
            int section1 = result.getInt(9);
            if(day1!=today)
                continue;
            Course c = new Course();
            c.setDay(day1);
            c.setCourseName(courseName1);
            c.setWeekType(weekType1);
            c.setTeacherName(teacherName1);
            c.setEndWeek(endWeek1);
            c.setStartWeek(startwWeek1);
            c.setSection(section1);
            cs.add(c);


        }

        db.close() ;
        result.close();


        return cs;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
