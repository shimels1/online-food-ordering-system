package com.example.shimeb.orderfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {


    boolean schedul=false;
    Timer splashTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.splash_ll);
        ScaleAnimation scal=new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float)0.5,Animation.RELATIVE_TO_SELF, (float)0.5);
        scal.setDuration(500);
        scal.setFillAfter(true);
        linearLayout.setAnimation(scal);

       /* Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);
        linearLayout.setAnimation(fadeIn);
*/
        splashTimer=new Timer();
        splashTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String id = sp.getString("id", "");

                if(id==""){
                startActivity(new Intent(getApplicationContext(),login.class));
                }else{
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        },3000);
        schedul=true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (schedul){
            splashTimer.purge();
        }
    }
}
