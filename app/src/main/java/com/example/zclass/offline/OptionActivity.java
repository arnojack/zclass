package com.example.zclass.offline;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zclass.MainActivity;
import com.example.zclass.R;
import com.example.zclass.offline.dao.CourseDao;
import com.example.zclass.offline.pojo.Course;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OptionActivity extends AppCompatActivity {

    private CourseDao courseDao = new CourseDao(OptionActivity.this);
    private TextView tv_1;
    private ListView lvContent;
    private List<Course> courseList;
    private Button btn_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        tv_1 = findViewById(R.id.tv_1);
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                textone();

            }
        });
        lvContent = findViewById(R.id.lvContent);

        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = courseList.get(position);
                Intent intent = new Intent(OptionActivity.this, UpdateCourseActivity.class);
                intent.putExtra("course", course);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        show();
    }

    /**
     * 所有课程
     */
    private void show() {
        courseList = courseDao.listAll();
        int listSize = courseList.size();
        if (listSize > 0) {
            String[] courseNames = new String[listSize];
            for (int i = 0; i < listSize; i++) courseNames[i] = courseList.get(i).getCourseName();
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseNames);
            lvContent.setAdapter(arrayAdapter);
        } else {
            Toast.makeText(this, "暂无数据，请添加课程！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        this.finish();
    }

    /**
     * 添加课程
     *
     * @param view
     */
    public void addCourse(View view) {
        final View inflate = getLayoutInflater().inflate(R.layout.add_course_item, null);
        new AlertDialog.Builder(this)
                .setView(inflate)
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String courseName = ((EditText) inflate.findViewById(R.id.etCourseName)).getText().toString();
                        String teacherName = ((EditText) inflate.findViewById(R.id.etTeacherName)).getText().toString();
                        String startWeek = ((EditText) inflate.findViewById(R.id.etStartWeek)).getText().toString();
                        String endWeek = ((EditText) inflate.findViewById(R.id.etEndWeek)).getText().toString();
                        if ("".equals(courseName) || "".equals(startWeek) || "".equals(endWeek)) {
                            Toast.makeText(OptionActivity.this, "课程名和周次信息不可为空！", Toast.LENGTH_SHORT).show();
                        } else {
                            Course course = new Course();
                            course.setCourseName(courseName);
                            course.setTeacherName(teacherName);
                            course.setStartWeek(Integer.parseInt(startWeek));
                            course.setEndWeek(Integer.parseInt(endWeek));
                            long insert = courseDao.insert(course);
                            if (insert != -1) {
                                Toast.makeText(OptionActivity.this, "添加课程成功！", Toast.LENGTH_SHORT).show();
                                show();
                            }else{
                                Toast.makeText(OptionActivity.this, "添加失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    /**
     * 修改开学时间
     * @param view
     */
    public void alterDate(View view) {
        final SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
        final DatePicker datePicker = new DatePicker(this);
        long date = config.getLong("date", 0);
        if(date != 0){
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(date));
            datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), null);
        }
        new DatePickerDialog.Builder(this)
                .setTitle("选择开学日期")
                .setView(datePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int dayOfMonth = datePicker.getDayOfMonth();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth, 0, 0, 0);
                        Date time = calendar.getTime();
                        config.edit().putLong("date", time.getTime()).apply();
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }
    public  void textone(){
        int today = 1;
        List<Course> cs = courseDao.query2(today);
        String result = "";
        for(Course course1 : cs)
        {
            result += "课程"+course1.getCourseName()+"星期"+course1.getDay()+"节次"+course1.getSection()+"单双周"+
                    course1.getWeekType()+"\n";
        }

        tv_1.setText(result);
    }

    public void qd(){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                // TODO Auto-generated method stu
                Toast.makeText(OptionActivity.this,"\n"+"通过SimpleDateFormat获取24小时制时间：\n"+"sdf.format(new Date())",Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 5000);// 50是延时时长
            }
        };
        handler.postDelayed(runnable, 5000);// 打开定时器，执行操作
    }
}
