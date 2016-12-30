package com.ariellevit.welldone;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;

import java.util.ArrayList;

public class FoodListActivity extends ListActivity {

    private ArrayList<Food> foods;
    private FoodAdapter foodAdapter;
    private AlertDialog confirmDialogObject;
    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.foods_list);

        TimerDbAdapter dbAdapter = new TimerDbAdapter(this.getBaseContext());
        dbAdapter.open();
        foods = dbAdapter.getAllFoods();
        dbAdapter.close();

        foodAdapter = new FoodAdapter(this, foods);
        setListAdapter(foodAdapter);

        registerForContextMenu(getListView());

        //Add new food
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.addFoodList);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAddFoodDialog();
                confirmDialogObject.show();
            }
        });


    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


    }


    private void buildAddFoodDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        final View popupView = inflater.inflate(R.layout.popup_add_food, null);
        alertDialog.setView(popupView);



        final EditText foodName = (EditText) popupView.findViewById(R.id.editName);
        final EditText foodTimeH = (EditText) popupView.findViewById(R.id.editHour);
        final EditText foodTimeM = (EditText) popupView.findViewById(R.id.editMin);
        final EditText foodTimeS = (EditText) popupView.findViewById(R.id.editSec);

        //TimePicker:
//        showPicker(popupView);

        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String Name = foodName.getText().toString();
                int hour = Integer.parseInt(foodTimeH.getText().toString());
                int min = Integer.parseInt(foodTimeM.getText().toString());
                int sec = Integer.parseInt(foodTimeS.getText().toString());
                int seconds = toSeconds(sec, min, hour);
                String printedTime = secToMin(seconds);

                Food newFood = new Food(Name, seconds, printedTime);

                TimerDbAdapter db = new TimerDbAdapter(getBaseContext());
                db.open();
                db.createFood(newFood);
                db.close();
                //Refresh
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        confirmDialogObject = alertDialog.show();

    }



    //Time Picker:
    public void showPicker(View v) {
//        Calendar now = Calendar.getInstance();
        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                // TODO Auto-generated method stub
                time.setText(getString(R.string.time) + String.format("%02d", hourOfDay) +
                        ":" + String.format("%02d", minute) +
                        ":" + String.format("%02d", seconds));
            }

        },00,00,00,true);
//                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);

        mTimePicker.show();
    }


    public int toSeconds(int sec, int min, int hour){
        int minToSec = min * 60;
        int hourToSec = hour * 3600;

        int result = minToSec + hourToSec + sec;

        return result;
    }

    public String secToMin (int sec){
        int minutes = sec / 60 ;
        int seconds =  (sec % 60);
        String result = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        return result;
    }


}
