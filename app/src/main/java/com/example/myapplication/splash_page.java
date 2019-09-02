package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class splash_page extends AppCompatActivity {

    ImageView imageView=null;
    TextView textView1=null;
    TextView textView2=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        imageView = findViewById(R.id.iv);
        textView1 = findViewById(R.id.tv1);
        textView2 = findViewById(R.id.tv2);

        imageView.setAnimation(myanim);
        textView1.setAnimation(myanim);
        textView2.setAnimation(myanim);
        final Intent intent = new Intent(this, login_activity.class);

        new Thread() {

            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
    }
}
