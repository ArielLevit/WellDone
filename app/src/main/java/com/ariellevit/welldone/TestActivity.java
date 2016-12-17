package com.ariellevit.welldone;

/**
 * Created by אריאל on 05/11/2016.
 */

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

//        Food food1 = new Food("passta", 22);

//        timeSeconds = (int) food1.getTime();

        tvTime = (TextView) findViewById(R.id.timer);



        String TAG = "All foods:";

        TimerDbAdapter dbAdapter = new TimerDbAdapter(getBaseContext());
        dbAdapter.open();
//        Food food1  = dbAdapter.addFood("passta", 22);
//        dbAdapter.createFood(new Food("passta", 22));
        dbAdapter.createFood(new Food("pizza", 358));
//        timeSeconds = (int) food1.getTime();




        List<Food> foods = dbAdapter.getAllFoods();

        for (Food food : foods) {
            String log = "Id: " + food.getNameId() + " ,Name: " + food.getName() + " ,Time: " + food.getTime();
// Writing foods to log
            Log.d("Food: ", log);
        }


        dbAdapter.close();


        reverseTimer(timeSeconds, tvTime);



//        getTableAsString(TimerDbAdapter.sqlDB, TimerDbAdapter.FOOD_TABLE);

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



    String TAG = "Ariel";
    // functions omitted


    /**
     * Helper function that parses a given table into a string
     * and returns it for easy printing. The string consists of
     * the table name and then each row is iterated through with
     * column_name: value pairs printed out.
     *
     * @param db the database to get the table from
     * @param tableName the the name of the table to parse
     * @return the table tableName as a string
     */
    public String getTableAsString(SQLiteDatabase db, String tableName) {
        Log.d(TAG, "Ariel");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }



//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra(MainActivity.TIME_EXTRA, timeSeconds);
//        startActivity(intent);
//    }

}
