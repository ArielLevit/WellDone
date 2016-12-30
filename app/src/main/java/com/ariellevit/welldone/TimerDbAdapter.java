package com.ariellevit.welldone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;


public class TimerDbAdapter {


    private static final String DATABASE_NAME = "WellDone.db";
    private static final int DATABASE_VERSION = 9;

    public static final String FOOD_TABLE = "food";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    //time for food to get ready:
    public static final String COLUMN_TIME = "time";
    //current time of action:
    public static final String COLUMN_DATE = "date";
    //time for food list:
    public static final String COLUMN_PRINTED_TIME = "printed_time";

    private String[] allColumns = {COLUMN_ID, COLUMN_NAME, COLUMN_TIME, COLUMN_DATE, COLUMN_PRINTED_TIME};

    public static final String CREATE_TABLE_FOOD = "CREATE TABLE " + FOOD_TABLE + " ( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_TIME + " long not null, "
            + COLUMN_DATE + COLUMN_PRINTED_TIME + ", text DEFAULT '00:00'" + ");";

    public static SQLiteDatabase sqlDB;
    private Context context;
    private TimerDbHelper timerDbHelper;

    public TimerDbAdapter (Context context){
        this.context = context;
    }

    public TimerDbAdapter open() throws android.database.SQLException{
        timerDbHelper = new TimerDbHelper(context);
        sqlDB = timerDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        timerDbHelper.close();
    }

    public void createFood(Food food){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, food.getName());
        values.put(COLUMN_TIME, food.getTime());
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");
        values.put(COLUMN_PRINTED_TIME, food.getPrintedTime() );

        sqlDB.insert(FOOD_TABLE, null, values);


    }


    public boolean deleteFoodById(long id) {
        return sqlDB.delete(FOOD_TABLE, COLUMN_ID + "=" + id, null) > 0;
    }


    public void deleteFoodByName(String name) {
        sqlDB.execSQL("DELETE FROM " + FOOD_TABLE + " WHERE "+COLUMN_NAME+"='"+name+"'");
    }

    public boolean deleteFoodByTime(long time) {
        return sqlDB.delete(FOOD_TABLE, COLUMN_TIME + "=" + time, null) > 0;
    }

    public void getPrintByName(String name) {
        sqlDB.execSQL("SELECT" +COLUMN_PRINTED_TIME+ " FROM " + FOOD_TABLE + " WHERE "+COLUMN_NAME+"='"+name+"'");
    }



    public long updateFood (long idToUpdate, String newName, String newTime){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newName);
        values.put(COLUMN_TIME, newTime);
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        return sqlDB.update(FOOD_TABLE, values, COLUMN_ID + " = " + idToUpdate, null);
    }

    public ArrayList<Food> getAllFoods(){
        ArrayList<Food> foods = new ArrayList<Food>();

        //Grab all the information from our database for the foods in it
        Cursor cursor = sqlDB.query(FOOD_TABLE, allColumns, null, null, null, null, null);

        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){

            Food food = cursorToFood(cursor);
            foods.add(food);
        }

        cursor.close();
        return foods;
    }

    public String secToMin (Long sec){
        int minutes = (int)(sec / 60) ;
        int seconds = (int) (sec - (minutes * 60));
        String result = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        return result;
    }


    private Food cursorToFood (Cursor cursor) {

        Food newFood = new Food ( cursor.getString(1), cursor.getLong(2), cursor.getString(4), cursor.getLong(0),
                cursor.getLong(3) );
        return newFood;
    }


    private static class TimerDbHelper extends SQLiteOpenHelper {



        TimerDbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create table
//            db.execSQL(upgradeQuery);

            db.execSQL(CREATE_TABLE_FOOD);

        }



//

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String upgradeQuery = "ALTER TABLE " + FOOD_TABLE + " ADD COLUMN " + COLUMN_PRINTED_TIME + " text";
            if (oldVersion == 8 && newVersion == 9)
                db.execSQL(upgradeQuery);
        }

//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//            Log.w(TimerDbHelper.class.getName(),
//                    "Upgrading database from version " + oldVersion + " to "
//                            + newVersion + ", which will destroy all old data");
//            //Destroy data
//            db.execSQL("DROP TABLE IF EXISTS " + FOOD_TABLE);
//            onCreate(db);
//
//        }




//        public void addFood(Food food) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            values.put(COLUMN_NAME, food.getName());
//            values.put(COLUMN_TIME, food.getTime());
//            values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");
//            // Inserting Row
//            db.insert(TABLE_SHOPS, null, values);
//            db.close(); // Closing database connection
//        }

    }

}















