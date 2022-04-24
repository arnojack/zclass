//package com.example.zclass.offline.aidltest;
//
//import android.app.Activity;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageButton;
//
//import com.example.zclass.R;
//
//
//public class DialogActivity04 extends Activity {
//    /** Called when the activity is first created. */
//	 private MediaPlayer mp;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog02);
//
//      //获取播放器
//		  mp=MediaPlayer.create(DialogActivity04.this,R.raw.jikechufa);
//		 mp.setLooping(true);
//  	 mp.start();
//        ImageButton button = (ImageButton) findViewById(R.id.dialog_button_cancel);
//        button.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//               if(null!=mp) mp.stop();
//               finish();
//            }
//        });
//
//    }
//}