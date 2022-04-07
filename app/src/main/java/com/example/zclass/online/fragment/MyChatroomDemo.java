package com.example.zclass.online.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zclass.R;
import com.example.zclass.online.Dao.Msg;

import java.util.ArrayList;
import java.util.List;

public class MyChatroomDemo extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<>();
    private EditText editText;
    private Button sendButton;
    private RecyclerView recyclerView;
    private MsgAdapter msgAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        initMsgs();
        editText = (EditText)findViewById(R.id.enter);
        sendButton = (Button)findViewById(R.id.send);
        recyclerView =(RecyclerView)findViewById(R.id.chatroomRecyclerView);
        //布局排列方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(msgAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //得到输入框中的内容
                String content = editText.getText().toString();
                //判断内容不是空的
                if(!"".equals(content)){
                    //将内容添加到单例中
                    Msg msg = new Msg(content,Msg.TYPE_SENT);
                    msgList.add(msg);
                    //要求适配器重新刷新
                    msgAdapter.notifyItemInserted(msgList.size()-1);
                    //要求recyclerView布局将消息刷新
                    recyclerView.scrollToPosition(msgList.size()-1);
                    editText.setText("");
                }
            }
        });
    }
    public void  initMsgs(){
        Msg msg1 = new Msg("你好！",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("谢谢！你好。",Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("加班么？",Msg.TYPE_RECEIVED);
        msgList.add(msg3);


    }

}
