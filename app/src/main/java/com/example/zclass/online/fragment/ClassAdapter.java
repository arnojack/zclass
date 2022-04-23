package com.example.zclass.online.fragment;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.zclass.R;
import com.example.zclass.online.Dao.Course;
import com.example.zclass.online.Dao.User;
import com.example.zclass.online.tool.BaseActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassAdapter extends BaseAdapter {
    String TAG="ClassAdapter";

    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    public ArrayList<HashMap<String, Object>> listItem;
    //public int[] color={R.color.dark_blue,R.color.dark_green,R.color.dark_purple,R.color.light_blue,R.color.light_gray,R.color.light_green,R.color.light_purple,R.color.light_yellow};

    public ClassAdapter(Context context, ArrayList<HashMap<String, Object>> listItem) {
        this.mInflater = LayoutInflater.from(context);

        this.listItem = listItem;
    }//声明构造函数

    @Override
    public int getCount() {
        return listItem.size();
    }//这个方法返回了在适配器中所代表的数据集合的条目数

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }//这个方法返回了数据集合中与指定索引position对应的数据项

    @Override
    public long getItemId(int position) {
        return position;
    }//这个方法返回了在列表中与指定索引对应的行id

    //利用convertView+ViewHolder来重写getView()
    public static class ViewHolder
    {
        public ImageView img_icon;
        public TextView title;
        public TextView item_class_id;
        public TextView item_tea_id;
        public TextView item_tea_sex;

        public TextView text_left;
        public TextView text_grade;
        public TextView text_class;
        public ImageView img_up;
        public View  item;
    }//声明一个外部静态类
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.class_item, null);
            holder.item_class_id=convertView.findViewById(R.id.item_class_id);
            holder.item_tea_id=convertView.findViewById(R.id.item_tea_id);
            holder.item_tea_sex=convertView.findViewById(R.id.item_tea_sex);

            holder.img_icon = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.title = (TextView)convertView.findViewById(R.id.item_title);
            holder.text_left = (TextView)convertView.findViewById(R.id.item_bottom_left);
            holder.text_grade = (TextView)convertView.findViewById(R.id.item_clgrade);
            holder.text_class=convertView.findViewById(R.id.item_class);
            holder.img_up = (ImageView)convertView.findViewById(R.id.item_up_right);
            holder.item=convertView.findViewById(R.id.item);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        BaseActivity.iconDO((ImageView) holder.img_icon,(String)listItem.get(position).get(Course.TEAID));
        //holder.img.setImageResource((Integer) listItem.get(position).get("ItemImage"));
        holder.title.setText((String) listItem.get(position).get(Course.COUONNAME));
        holder.text_left.setText((String) listItem.get(position).get(User.USERNAME));
        holder.text_grade.setText((String) listItem.get(position).get(Course.COUGRADE));
        holder.text_class.setText((String)listItem.get(position).get(Course.COUCLASS));
        holder.item_class_id.setText((String)listItem.get(position).get(Course.COUONID));
        holder.item_tea_id.setText((String)listItem.get(position).get(Course.TEAID));
        holder.item_tea_sex.setText((String)listItem.get(position).get(User.SEX));
        //holder.item.setBackgroundColor(color[position]);

        return convertView;
    }
}