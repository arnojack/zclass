package com.example.zclass.offline.aidltest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.zclass.R;


public class user1 extends Activity {

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
    	this.finish();
		super.onPause();
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        Button userButton=(Button)findViewById(R.id.back);
        userButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//绑定一个
				 final Intent it = new Intent(user1.this, MYyActivity.class); //你要转向的Activity


		          startActivity(it); //执行
			}
			});


    }


}
