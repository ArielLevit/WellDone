package com.ariellevit.welldone;

/**
 * Created by אריאל on 05/11/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class TestActivity extends Activity {

    private TextView tvTime;
    private int timeSeconds = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timer);

        tvTime = (TextView) findViewById(R.id.timer);
        reverseTimer(timeSeconds, tvTime);



    }

    // //////////////COUNT DOWN START/////////////////////////
    public void reverseTimer(int Seconds, final TextView tv){

        new CountDownTimer(Seconds* 1000+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tv.setText("TIME : " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                tv.setText("Completed");
            }
        }.start();
    }
    // //////////////COUNT DOWN END/////////////////////////







//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra(MainActivity.TIME_EXTRA, timeSeconds);
//        startActivity(intent);
//    }

}
