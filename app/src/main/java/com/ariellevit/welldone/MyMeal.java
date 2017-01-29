package com.ariellevit.welldone;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MyMeal extends ListActivity {

    private ArrayList<Timer> timers;
    private TimerAdapter timerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meal);
        // Start Service
        startService(new Intent(this, MyService.class));

        TimerDbAdapter db = new TimerDbAdapter(this.getBaseContext());
        db.open();
        timers = db.getAllTimers();
        db.close();

        timerAdapter = new TimerAdapter(this, timers);
        setListAdapter(timerAdapter);

        registerForContextMenu(getListView());

        Button cleanButton = (Button) findViewById(R.id.buttonCleanMeal);
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerDbAdapter db = new TimerDbAdapter(getBaseContext());
                db.open();
                db.deleteMeal();
                db.close();
                //Refresh
                Intent intent = getIntent();
                finish();
                startActivity(intent);

                stopService(new Intent(getBaseContext(), MyService.class));
            }
        });

        Button addFoodButton = (Button) findViewById(R.id.buttonAddFood);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), FoodListActivity.class);
                startActivity(intent);
            }
        });
    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}