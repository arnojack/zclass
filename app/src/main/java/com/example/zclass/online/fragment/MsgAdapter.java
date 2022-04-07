package com.example.zclass.online.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.zclass.R;
import com.example.zclass.online.Dao.Msg;
import com.example.zclass.online.tool.BaseActivity;

import java.util.List;

/*
适配器类，注意适配器类中的泛型不是List集合而是Viewholder缓存内部类
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    //  写一个从外部得到的List的全局变量。
    private List<Msg> msgList;
    /*
    缓存子布局的内部类
     */
    static  class ViewHolder extends RecyclerView.ViewHolder{
        View myView;
        LinearLayout left_layout;
        LinearLayout right_layout;
        TextView left_msg;
        TextView right_msg;
        TextView left_time;
        TextView right_time;

        TextView left_name;
        TextView right_name;

        ImageView left_ic;
        ImageView right_ic;

        public ViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            left_layout = (LinearLayout)itemView.findViewById(R.id.left_layout);
            right_layout = (LinearLayout)itemView.findViewById(R.id.right_layout);
            left_msg = (TextView)itemView.findViewById(R.id.left_msg);
            BaseActivity.setbackground(left_msg,1.5);
            right_msg = (TextView)itemView.findViewById(R.id.right_msg);
            BaseActivity.setbackground(right_msg,1.5);

            left_ic=itemView.findViewById(R.id.left_ic);
            right_ic=itemView.findViewById(R.id.right_ic);

            left_time=itemView.findViewById(R.id.left_time);
            right_time=itemView.findViewById(R.id.right_time);

            left_name=itemView.findViewById(R.id.left_name);
            right_name=itemView.findViewById(R.id.right_name);
        }
    }
    /*
    传入外部list的构造方法
     */
    public MsgAdapter(List<Msg> msgList){
        this.msgList = msgList;
    }
    /*
    必须要重写的方法
    将子布局填充到父类布局里，在将父类布局添加到缓存布局的内部类中，并且返回缓存布局内部类。
    此处写RecyclerView布局的点击事件
     */
    @Override
    public MsgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();//得到当前点击的位置
                Msg msg = msgList.get(position);//从点击位置里得到List中的单例
                //从单例中得到时间
                Toast.makeText(v.getContext(), "消息时间："+msg.getTime(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    /*
    将布局数据导入到布局中的一个必须重写的方法
     */
    @Override
    public void onBindViewHolder(MsgAdapter.ViewHolder holder, int position) {
        Msg msg = msgList.get(position);
        //判断是信息是接收还是发送的，并且分别判断需要隐藏的布局和显示的布局
        if (msg.getType() == Msg.TYPE_RECEIVED){
            //判断到信息是接收的，将左边的布局显示，右边的布局隐藏
            holder.left_layout.setVisibility(View.VISIBLE);
            holder.right_layout.setVisibility(View.GONE);
            holder.left_msg.setText(msg.getContent());
            holder.left_name.setText(msg.getName());
            holder.left_time.setText(msg.getTime());
            //holder.left_ic.setImageDrawable();
        } else if (msg.getType() == Msg.TYPE_SENT){
            holder.right_layout.setVisibility(View.VISIBLE);
            holder.left_layout.setVisibility(View.GONE);
            holder.right_msg.setText(msg.getContent());
            holder.right_name.setText(msg.getName());
            holder.right_time.setText(msg.getTime());
            //holder.right_ic.setImageDrawable();
        }
    }
    //  必须要重写的方法，返回list的长度
    @Override
    public int getItemCount() {
        return msgList.size();
    }
}
