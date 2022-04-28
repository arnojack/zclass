package com.example.zclass.offline.aidltest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.example.zclass.R;


public class DialogActivity03 extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog01);
        ImageButton button = (ImageButton) findViewById(R.id.dialog_button_cancel);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               finish();
            }
        });

    }
}