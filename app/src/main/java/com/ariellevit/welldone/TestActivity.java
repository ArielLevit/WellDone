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
    private int timeSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timer);

        Food food1 = new Food("passta", 22);

        timeSeconds = (int) food1.getTime();

        tvTime = (TextView) findViewById(R.id.timer);
        reverseTimer(timeSeconds, tvTime);

        TimerDbAdapter dbAdapter = new TimerDbAdapter(getBaseContext());
        dbAdapter.open();
        dbAdapter.createFood(food1.getName() + "", food1.getTime());

        dbAdapter.close();

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
