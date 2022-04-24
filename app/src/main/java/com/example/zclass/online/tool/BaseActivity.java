package com.example.zclass.online.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zclass.MainActivity;
import com.example.zclass.online.Dao.Cou_Stu;
import com.example.zclass.online.Dao.Course;
import com.example.zclass.online.Dao.Msg;
import com.example.zclass.online.Dao.User;
import com.example.zclass.online.fragment.ClassAdapter;
import com.example.zclass.online.service.HttpClientUtils;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseActivity {
    public static final String base="zclass.free.idcfengye.com/";
    public static String ws = "ws://"+base+"websocket/";
    public static String BaseUrl="http://"+base;

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
    public static void setbackground(View view, double num) {
        Drawable drawable = view.getBackground();
        view.setBackground(null);

        view.post(() -> {
            int width = view.getWidth();
            int height = view.getHeight();

            view.setBackground(drawable);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = (int) (num*width);
            layoutParams.height =(int) (num*height);
        });
    }
    public static void setlTV(TextView textView){
        Drawable leftDrawable=textView.getCompoundDrawables()[0];
        TextView mET=textView;
        if(leftDrawable!=null){
            leftDrawable.setBounds(0, 0, 80, 80);
            mET.setCompoundDrawables(leftDrawable, mET.getCompoundDrawables()[1]
                    , mET.getCompoundDrawables()[2], mET.getCompoundDrawables()[3]);
        }
    }
    public static void setrTV(TextView mETpassword){
        Drawable rightDrawable = mETpassword.getCompoundDrawables()[2];
        if(rightDrawable!=null){
            rightDrawable.setBounds(0, 0,50 , 50);
            mETpassword.setCompoundDrawables(mETpassword.getCompoundDrawables()[0], mETpassword.getCompoundDrawables()[1]
                    ,rightDrawable ,mETpassword.getCompoundDrawables()[3]);
        }
    }
    public static ArrayList jtol_cou(String data) throws JSONException {

        Map<String,String> map=new HashMap<>();
        ArrayList<Map> mlists = new ArrayList<Map>();
        JSONArray array = new JSONArray(new String(data));
        for (int i = 0; i < array.length(); i++) {
            Map<String,String> m=new HashMap<>();
            JSONObject item = array.getJSONObject(i);

            if(item.has(Course.COUONID)) m.put(Course.COUONID,item.getString(Course.COUONID)) ;
            if(item.has(Course.COUONNAME)) m.put(Course.COUONNAME,item.getString(Course.COUONNAME)) ;
            if(item.has(Course.COUGRADE)) m.put(Course.COUGRADE,item.getString(Course.COUGRADE)) ;
            if(item.has(Course.COUCLASS)) m.put(Course.COUCLASS,item.getString(Course.COUCLASS)) ;
            if(item.has(Course.TEAID)) m.put(Course.TEAID,item.getString(Course.TEAID)) ;
            if(item.has(Course.TEANAME)) m.put(Course.TEANAME,item.getString(Course.TEANAME)) ;

            mlists.add(m);
        }

        return mlists;
    }
    public static ArrayList jtol_user(String data) throws JSONException {

        Map<String,String> map=new HashMap<>();
        ArrayList<Map> mlists = new ArrayList<Map>();
        JSONArray array = new JSONArray(new String(data));
        for (int i = 0; i < array.length(); i++) {
            Map<String,String> m=new HashMap<>();
            JSONObject item = array.getJSONObject(i);
            String temp=null;

            if(item.has(User.USERID)) m.put(User.USERID,item.getString(User.USERID)) ;
            if(item.has(User.USERNAME)) m.put(User.USERNAME,item.getString(User.USERNAME)) ;
            if(item.has(User.SEX)) m.put(User.SEX,item.getString(User.SEX)) ;
            if(item.has(User.CODE)) m.put(User.CODE,item.getString(User.CODE)) ;
            if(item.has(User.USERID)) m.put(User.USERID,item.getString(User.USERID)) ;
            if(item.has(User.PASSWORD)) m.put(User.PASSWORD,item.getString(User.PASSWORD)) ;
            if(item.has(User.PHONENUMBER)) m.put(User.PHONENUMBER,item.getString(User.PHONENUMBER)) ;
            if(item.has(User.PROFESS)) m.put(User.PROFESS,item.getString(User.PROFESS)) ;
            if(item.has(User.SCHOOL)) m.put(User.SCHOOL,item.getString(User.SCHOOL)) ;

            mlists.add(m);
        }

        return mlists;
    }
    //这个方法返回了指定索引对应的数据项的视图
    public static void iconDO(ImageView imageView, String userid){
        String saveDir= Environment.getExternalStorageDirectory().getAbsolutePath();
        File file=new File(saveDir,userid+".jpg");
        Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }else {
            HttpClientUtils.download("icon",userid, null, new HttpClientUtils.OnDownloadListener() {
                @Override
                public void onDownloadSuccess() {
                    File file=new File(saveDir,userid+".jpg");
                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap!=null)
                                imageView.setImageBitmap(bitmap);
                        }
                    });
                }
                @Override
                public void onDownloading(int progress) {
                }

                @Override
                public void onDownloadFailed(String msg) {
                }
            });
        }

    }
    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c >= 0 && c <= 255) {
                sb.append(c);

            } else {
                byte[] b;

                try {
                    b = String.valueOf(c).getBytes("utf-8");

                } catch (Exception ex) {
                    System.out.println(ex);

                    b = new byte[0];

                }

                for (int j = 0; j < b.length; j++) {
                    int k = b[j];

                    if (k < 0)

                        k += 256;

                    sb.append("%" + Integer.toHexString(k).toUpperCase());

                }

            }

        }

        return sb.toString();

    }

//将%E4%BD%A0转换为汉字

    public static String unescape(String s) {
        StringBuffer sbuf = new StringBuffer();

        int l = s.length();

        int ch = -1;

        int b, sumb = 0;

        for (int i = 0, more = -1; i < l; i++) {
            /* Get next byte b from URL segment s */

            switch (ch = s.charAt(i)) {
                case '%':

                    ch = s.charAt(++i);

                    int hb = (Character.isDigit((char) ch) ? ch - '0'

                            : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;

                    ch = s.charAt(++i);

                    int lb = (Character.isDigit((char) ch) ? ch - '0'

                            : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;

                    b = (hb << 4) | lb;

                    break;

                case '+':

                    b = ' ';

                    break;

                default:

                    b = ch;

            }

            /* Decode byte b as UTF-8, sumb collects incomplete chars */

            if ((b & 0xc0) == 0x80) { // 10xxxxxx (continuation byte)

                sumb = (sumb << 6) | (b & 0x3f); // Add 6 bits to sumb

                if (--more == 0)

                    sbuf.append((char) sumb); // Add char to sbuf

            } else if ((b & 0x80) == 0x00) { // 0xxxxxxx (yields 7 bits)

                sbuf.append((char) b); // Store in sbuf

            } else if ((b & 0xe0) == 0xc0) { // 110xxxxx (yields 5 bits)

                sumb = b & 0x1f;

                more = 1; // Expect 1 more byte

            } else if ((b & 0xf0) == 0xe0) { // 1110xxxx (yields 4 bits)

                sumb = b & 0x0f;

                more = 2; // Expect 2 more bytes

            } else if ((b & 0xf8) == 0xf0) { // 11110xxx (yields 3 bits)

                sumb = b & 0x07;

                more = 3; // Expect 3 more bytes

            } else if ((b & 0xfc) == 0xf8) { // 111110xx (yields 2 bits)

                sumb = b & 0x03;

                more = 4; // Expect 4 more bytes

            } else /*if ((b & 0xfe) == 0xfc)*/{ // 1111110x (yields 1 bit)

                sumb = b & 0x01;

                more = 5; // Expect 5 more bytes

            }

            /* We don't test if the UTF-8 encoding is well-formed */

        }

        return sbuf.toString();

    }
    public static String getWs() {
        return ws;
    }

    public static void setWs(String roomname,String username) {
        BaseActivity.ws = ws+roomname+"/"+username;
    }

}
