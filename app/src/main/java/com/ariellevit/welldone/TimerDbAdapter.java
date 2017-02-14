package com.ariellevit.welldone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;


public class TimerDbAdapter {


    private static final String DATABASE_NAME = "WellDone.db";
    private static final int DATABASE_VERSION = 33;

    public static final String FOOD_TABLE = "food";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    //time for food to get ready:
    public static final String COLUMN_TIME = "time";
    //current time of action:
    public static final String COLUMN_DATE = "date";
    //time for food list:
    public static final String COLUMN_PRINTED_TIME = "printed_time";
    public static final String COLUMN_DELETED = "deleted";

    //TABLE 2
    public static final String TIMER_TABLE = "timer";
    public static final String C_ID = "_id";
    public static final String C_START = "start";
    public static final String C_NAME = "name";
    public static final String C_TIME = "time";

    private String[] allColumns = {COLUMN_ID, COLUMN_NAME, COLUMN_TIME, COLUMN_DATE, COLUMN_PRINTED_TIME, COLUMN_DELETED};
    private String[] allC = {C_ID, C_START, C_NAME, C_TIME};

    public static final String CREATE_TABLE_FOOD = "CREATE TABLE IF NOT EXISTS " + FOOD_TABLE + " ( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_TIME + " long not null, "
            + COLUMN_DATE + COLUMN_PRINTED_TIME + ", text DEFAULT '00:00', "
            + COLUMN_DELETED + " INTEGER DEFAULT 0"
            + ");";

    public static final String CREATE_TABLE_TIMER = "CREATE TABLE IF NOT EXISTS " + TIMER_TABLE + " ( "
            + C_ID + " integer primary key autoincrement, "
            + C_START + " long, " + C_NAME + " text, " + C_TIME + " long" + ");";



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



public Food createFood(String name, Long time, String pTime){
    ContentValues values = new ContentValues();
    values.put(COLUMN_NAME, name);
    values.put(COLUMN_TIME, time);
    values.put(COLUMN_PRINTED_TIME, pTime);

    values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

    long insertId = sqlDB.insert(FOOD_TABLE, null, values);

    Cursor cursor = sqlDB.query(FOOD_TABLE, allColumns, COLUMN_ID + " = " + insertId,
            null, null, null, null);

    cursor.moveToFirst();
    Food newFood = cursorToFood(cursor);
    cursor.close();

    return newFood;
}

    public Timer createTimer(String name, long time){
        ContentValues values = new ContentValues();

        values.put(C_START, -1);
        values.put(C_NAME, name);
        values.put(C_TIME, time);

        long insertId = sqlDB.insert(TIMER_TABLE, null, values);

        Cursor cursor = sqlDB.query(TIMER_TABLE, allC, C_ID + " = " + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Timer newTimer = cursorToTimer(cursor);
        cursor.close();

        return newTimer;
    }


    public boolean deleteFoodById(long id) {
        return sqlDB.delete(FOOD_TABLE, COLUMN_ID + "=" + id, null) > 0;
    }

    public int toDeleted (long idToUpdate){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DELETED, 1);

        String[] whereArgs =  new String[] {String.valueOf(idToUpdate)};
        int s = sqlDB.update(FOOD_TABLE, values, COLUMN_ID + " = ?",whereArgs);
        return s;
    }


    public void deleteFoodById(String id) {
        sqlDB.execSQL("DELETE FROM " + FOOD_TABLE + " WHERE "+COLUMN_ID+"='"+id+"'");
    }



    public void getPrintByName(String name) {
        sqlDB.execSQL("SELECT" +COLUMN_DELETED+ " FROM " + FOOD_TABLE + " WHERE "+COLUMN_NAME+"='"+name+"'");

    }

    public void deleteMeal() {

        //Grab all the information from our database for the timers in it
        Cursor cursor = sqlDB.query(TIMER_TABLE, allC, null, null, null, null, null);

        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){

            Timer timer = cursorToTimer(cursor);
            int i = cursor.getColumnIndex("_id");
            long idToDeleted = cursor.getLong(i);
            sqlDB.delete(TIMER_TABLE, C_ID + "=" + idToDeleted, null);
        }
        cursor.close();
    }



    public long updateFood (long idToUpdate, String newName, long newTime, String newPtime){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newName);
        values.put(COLUMN_TIME, newTime);
        values.put(COLUMN_PRINTED_TIME, newPtime);
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        return sqlDB.update(FOOD_TABLE, values, COLUMN_ID + " = " + idToUpdate, null);
    }

    public long updateStartTimer (long idToUpdate){
        ContentValues values = new ContentValues();

        values.put(C_START, Calendar.getInstance().getTimeInMillis() + "");

        return sqlDB.update(TIMER_TABLE, values, C_ID + " = " + idToUpdate, null);
    }

    public ArrayList<Food> getAllFoods(){
        ArrayList<Food> foods = new ArrayList<Food>();
        //Grab all the information from our database for the foods in it
        Cursor cursor = sqlDB.query(FOOD_TABLE, allColumns, null, null, null, null, null);

        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){

            Food food = cursorToFood(cursor);
            int c = cursor.getColumnIndex("deleted");
            int deleted = cursor.getInt(c);
            if(deleted == 1 ){

            }else {
                foods.add(food);
            }
        }

        cursor.close();
        return foods;
    }

    public ArrayList<Timer> getAllTimers(){
        ArrayList<Timer> timers = new ArrayList<Timer>();
        //Grab all the information from our database for the timers in it
        Cursor cursor = sqlDB.query(TIMER_TABLE, allC, null, null, null, null, null);

        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){

            Timer timer = cursorToTimer(cursor);
            timers.add(timer);
        }

        cursor.close();
        return timers;
    }


    private Food cursorToFood (Cursor cursor) {

        Food newFood = new Food ( cursor.getString(1), cursor.getLong(2), cursor.getString(4), cursor.getLong(3),
                cursor.getLong(0) );
        return newFood;
    }

    private Timer cursorToTimer (Cursor cursor) {

        Timer newTimer = new Timer ( cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getLong(3));
        return newTimer;
    }


    private static class TimerDbHelper extends SQLiteOpenHelper {



        TimerDbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create table
            db.execSQL(CREATE_TABLE_FOOD);
            db.execSQL(CREATE_TABLE_TIMER);

        }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                        Log.w(TimerDbHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", COLUMN");
            String upgradeQuery = "ALTER TABLE " + TIMER_TABLE + " ADD COLUMN " + C_TIME + " long";
            if (oldVersion == 32 && newVersion == 33)
                db.execSQL(upgradeQuery);
        }

//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//            Log.w(TimerDbHelper.class.getName(),
//                    "Upgrading database from version " + oldVersion + " to "
//                            + newVersion + ", which will destroy all old data");
//            //Destroy data
//            db.execSQL("DROP TABLE IF EXISTS " + TIMER_TABLE);
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















