package com.apartment.swamedia.sewaapartemenbandung.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;


import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.service.AlarmInboxReceiver;
import com.apartment.swamedia.sewaapartemenbandung.welcomescreen.WelcomeScreen;


/**
 * Created by Nurul Akbar on 16/10/2015.
 */
public class SplashAct extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    PackageInfo pInfo;

    TextView versi;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

//        gifView = (GifView) findViewById(R.id.gif_view);


//        String stringInfo = "";
//        stringInfo += "Duration: " + gifView.getMovieDuration() + "\n";
//        stringInfo += "W x H: " + gifView.getMovieWidth() + " x " + gifView.getMovieHeight() + "\n";


        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }



        String version = pInfo.versionName;
        versi = (TextView)findViewById(R.id.versi);
        versi.setText("Version "+version);

        Intent alarm_inbox = new Intent(this, AlarmInboxReceiver.class);
//        boolean alarmInboxRunning = (PendingIntent.getBroadcast(this, 0, alarm_inbox, PendingIntent.FLAG_NO_CREATE) != null);
//        if(alarmInboxRunning == false) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm_inbox, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashAct.this, WelcomeScreen.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
