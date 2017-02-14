package com.ariellevit.welldone;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;
import java.util.ArrayList;

public class FoodListActivity extends ListActivity {

    private ArrayList<Food> foods;
    private FoodAdapter foodAdapter;
    private AlertDialog confirmDialogObject;
    private AlertDialog confirmDialogObject_Edit;
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

        //grab the note information associated with whatever note item we clicked on
        Food food = (Food) getListAdapter().getItem(position);
        long fTime = food.getTime();

        //Crate a new intent that launches our
        Intent intent = new Intent(this, MyMeal.class);

        //Pass the information of the note we clicked on to the NoteDetailActivity
        intent.putExtra(MainActivity.FOOD_ID_EXTRA, food.getFoodId());
        intent.putExtra(MainActivity.FOOD_NAME_EXTRA, food.getName());
        intent.putExtra(MainActivity.FOOD_TIME_EXTRA, fTime);
        intent.putExtra(MainActivity.FOOD_DATE_EXTRA, food.getDateCreatedMilli());
        startActivity(intent);

        String foodName = intent.getExtras().getString(MainActivity.FOOD_NAME_EXTRA);
        TimerDbAdapter db = new TimerDbAdapter(getBaseContext());
        db.open();
        db.createTimer(foodName, fTime);
        db.close();

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
                //Name:
                String Name;
                if (foodName.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Give your food a Name", Toast.LENGTH_LONG).show();
                    return;
                } Name = foodName.getText().toString();
                //Hour:
                int hour;
                if (foodTimeH.getText().toString().equals("")) {
                    hour = 0;
                } else hour = Integer.parseInt(foodTimeH.getText().toString());
                //min:
                int min;
                if (foodTimeM.getText().toString().equals("")) {
                    min = 0;
                } else min = Integer.parseInt(foodTimeM.getText().toString());
                //sec:
                int sec;
                if (foodTimeS.getText().toString().equals("")) {
                    sec = 0;
                } else sec = Integer.parseInt(foodTimeS.getText().toString());

                long seconds = toSeconds(sec, min, hour);
                String printedTime = secToMin(seconds);

                Food newFood = new Food(Name, seconds, printedTime);

                TimerDbAdapter db = new TimerDbAdapter(getBaseContext());
                db.open();
                db.createFood(Name, seconds, printedTime);
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

    @Override
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.long_press_menu, menu);
    }


        @Override
    public boolean onContextItemSelected (MenuItem item){

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int rowPosition = info.position;
        Food food = (Food) getListAdapter().getItem(rowPosition);
            TimerDbAdapter dbAdapter = new TimerDbAdapter(this.getBaseContext());
        switch (item.getItemId()){
            case R.id.edit:

                buildEditFoodDialog(food);

                return true;

            case R.id.delete:

                dbAdapter.open();

                long id = food.getFoodId();
                dbAdapter.toDeleted(id);
                foodAdapter.notifyDataSetChanged();
                dbAdapter.close();
                //Refresh
                Intent intent = getIntent();
                finish();
                startActivity(intent);

                Toast.makeText(this, "Food Deleted", Toast.LENGTH_LONG).show();
                return true;
        }

        return onContextItemSelected(item);
    }



    private void buildEditFoodDialog(Food foodToUpdate) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        final View popupView = inflater.inflate(R.layout.popup_add_food, null);
        alertDialog.setView(popupView);

        final long idToUpdate = foodToUpdate.getFoodId();

        final EditText foodName = (EditText) popupView.findViewById(R.id.editName);
        final EditText foodTimeH = (EditText) popupView.findViewById(R.id.editHour);
        final EditText foodTimeM = (EditText) popupView.findViewById(R.id.editMin);
        final EditText foodTimeS = (EditText) popupView.findViewById(R.id.editSec);

        //TimePicker:
//        showPicker(popupView);

        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Name:
                String Name;
                if (foodName.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Give your food a Name", Toast.LENGTH_LONG).show();
                    return;
                } Name = foodName.getText().toString();
                //Hour:
                int hour;
                if (foodTimeH.getText().toString().equals("")) {
                    hour = 0;
                } else hour = Integer.parseInt(foodTimeH.getText().toString());
                //min:
                int min;
                if (foodTimeM.getText().toString().equals("")) {
                    min = 0;
                } else min = Integer.parseInt(foodTimeM.getText().toString());
                //sec:
                int sec;
                if (foodTimeS.getText().toString().equals("")) {
                    sec = 0;
                } else sec = Integer.parseInt(foodTimeS.getText().toString());

                long seconds = toSeconds(sec, min, hour);
                String printedTime = secToMin(seconds);

                TimerDbAdapter db = new TimerDbAdapter(getBaseContext());
                db.open();
                db.updateFood(idToUpdate, Name, seconds, printedTime);
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

        confirmDialogObject_Edit = alertDialog.show();

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


    public long toSeconds(int sec, int min, int hour){
        int minToSec = min * 60;
        int hourToSec = hour * 3600;

        long result = (long) minToSec + hourToSec + sec;

        return result;
    }

    public String secToMin (long sec){
        long minutes = sec / 60 ;
        long seconds =  (sec % 60);
        String result = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        return result;
    }







}
