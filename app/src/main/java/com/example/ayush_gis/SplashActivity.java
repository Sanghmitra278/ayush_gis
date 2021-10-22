package com.example.ayush_gis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView ivlogo;
    TextView rsac,up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ivlogo=(ImageView)findViewById( R.id.imageView );
        rsac=(TextView)findViewById( R.id.textView );
        up=(TextView)findViewById( R.id.textView2 );

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
//Set animation to elements
        ivlogo.setAnimation(topAnim);
        rsac.setAnimation(bottomAnim);
        up.setAnimation(bottomAnim);
        //creating thread that will sleep for 10 seconds
        Thread t=new Thread() {
            public void run() {

                try {
                    //sleep thread for 10 seconds, time in milliseconds
                    sleep(4000);

                    //start new activity
                    Intent i=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(i);

                    //destroying Splash activity
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        //start thread
        t.start();
    }

}