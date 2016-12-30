package com.ariellevit.welldone;

/**
 * Created by אריאל on 05/11/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class TestActivity extends Activity {

    private TextView tvTime;
    private int timeSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timer);

        tvTime = (TextView) findViewById(R.id.timer);



        TimerDbAdapter dbAdapter = new TimerDbAdapter(getBaseContext());
        dbAdapter.open();

        Food ariel = new Food("passta", 22, "33:33");

//        dbAdapter.createFood(ariel);

//        timeSeconds = (int) food1.getTime();
//



        List<Food> foods = dbAdapter.getAllFoods();

        for (Food food : foods) {
            String log = "Id: " + food.getNameId() + " ,Name: " + food.getName() + " ,Time: " + food.getTime();
// Writing foods to log
            Log.d("Food: ", log);
        }



        dbAdapter.close();

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
