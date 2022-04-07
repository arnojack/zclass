package com.example.zclass.online.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zclass.R;
import com.example.zclass.online.Dao.Msg;
import com.example.zclass.online.tool.BaseActivity;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MyChatroomDemo extends AppCompatActivity implements View.OnClickListener {
    private List<Msg> msgList = new ArrayList<>();
    private EditText editText;
    private Button sendButton;
    private Button chatPop;
    private RecyclerView recyclerView;
    private MsgAdapter msgAdapter;

    private TextView memTV;
    private TextView workTV;
    private TextView roomNa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        initMsgs();
        editText = (EditText)findViewById(R.id.room_text);
        sendButton = (Button)findViewById(R.id.room_send);
        BaseActivity.setbackground(sendButton,0.75);

        memTV=findViewById(R.id.room_mem);
        setlTV(memTV);
        workTV=findViewById(R.id.room_work);
        setlTV(workTV);
        chatPop=findViewById(R.id.chat_pop);
        BaseActivity.setbackground(chatPop,0.35);
        roomNa=findViewById(R.id.room_name);

        sendButton.setOnClickListener(this);
        memTV.setOnClickListener(this);
        workTV.setOnClickListener(this);

        roomNa.setText(getIntent().getStringExtra("roomNa"));

        recyclerView =(RecyclerView)findViewById(R.id.chatroomRecyclerView);
        //布局排列方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(msgAdapter);
    }
    public void  initMsgs(){
        Msg msg1 = new Msg("你好！",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("谢谢！你好。",Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("加班么？",Msg.TYPE_RECEIVED);
        msgList.add(msg3);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.room_send:
                //得到输入框中的内容
                String content = editText.getText().toString();
                sendMsg(content);
                editText.setText("");
                break;
        }

    }
    public void sendMsg(String msg){
        //判断内容不是空的
        if(!"".equals(msg)){
            //将内容添加到单例中
            StringBuilder stringBuilder=new StringBuilder(msg);
            int mod= (int) Math.sqrt(stringBuilder.length())+2;
            for (int i=0;i<stringBuilder.length();i++){
                if(i%mod==0)stringBuilder.insert(i,"\n");
            }
            Msg mssage = new Msg(stringBuilder.toString(),Msg.TYPE_SENT);
            msgList.add(mssage);
            //要求适配器重新刷新
            msgAdapter.notifyItemInserted(msgList.size()-1);
            //要求recyclerView布局将消息刷新
            recyclerView.scrollToPosition(msgList.size()-1);
        }
    }
    public void setlTV(TextView textView){
        Drawable leftDrawable=textView.getCompoundDrawables()[0];
        TextView mET=textView;
        if(leftDrawable!=null){
            leftDrawable.setBounds(0, 0, 80, 80);
            mET.setCompoundDrawables(leftDrawable, mET.getCompoundDrawables()[1]
                    , mET.getCompoundDrawables()[2], mET.getCompoundDrawables()[3]);
        }
    }
}
