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
import com.example.zclass.online.Dao.User;

import java.util.ArrayList;
import java.util.HashMap;

public class MemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    public ArrayList<HashMap<String, Object>> listItem;
    //public int[] color={R.color.dark_blue,R.color.dark_green,R.color.dark_purple,R.color.light_blue,R.color.light_gray,R.color.light_green,R.color.light_purple,R.color.light_yellow};

    public MemAdapter(Context context, ArrayList<HashMap<String, Object>> listItem) {
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
    static class ViewHolder
    {
        public ImageView img;
        public TextView title;
        public TextView name;
    }//声明一个外部静态类
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mem_item, null);

            holder.img=convertView.findViewById((R.id.mem_item_icon));
            holder.name=convertView.findViewById(R.id.mem_item_name);
            holder.title = (TextView)convertView.findViewById(R.id.mem_item_title);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();

        }
        //holder.img.setImageResource((Integer) listItem.get(position).get("ItemImage"));
        holder.title.setText((String) listItem.get(position).get("title"));

        holder.name.setText(listItem.get(position).get(User.USERNAME).toString());
        //holder.item.setBackgroundColor(color[position]);

        return convertView;
    }//这个方法返回了指定索引对应的数据项的视图
}