package com.ariellevit.welldone;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MyService extends Service {

    ArrayList<Timer> timers;
    public Runnable mRunnable = null;


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Handler mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                TimerDbAdapter db = new TimerDbAdapter(getBaseContext());
                db.open();
                timers = db.getAllTimers();
                db.close();

                for (Timer timer : timers) {
                    long now = Calendar.getInstance().getTimeInMillis();
                    long foodTime = timer.getTime()*1000;
                    long result1 = (timer.getStart() + foodTime) - now ;
                    long result = result1 / 1000;
                    if (result == 0){

                        NotificationBuilder();

                    }
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 1000);

        return super.onStartCommand(intent, flags, startId);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void NotificationBuilder() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_stat_noti_icon)
                        .setContentTitle("Check your meal at WD")
                        .setContentText("This is a test notification");
        Intent notificationIntent = new Intent(this, MyMeal.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        builder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
        builder.setVibrate(pattern);
        builder.setStyle(new NotificationCompat.InboxStyle());
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }




}
