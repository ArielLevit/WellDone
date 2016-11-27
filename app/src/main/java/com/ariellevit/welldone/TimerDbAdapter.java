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
    private static final int DATABASE_VERSION = 1;

    public static final String FOOD_TABLE = "food";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    //time for food to get ready:
    public static final String COLUMN_TIME = "time";
    //current time of action:
    public static final String COLUMN_DATE = "date";

    private String[] allColumns = { COLUMN_ID, COLUMN_NAME, COLUMN_TIME, COLUMN_DATE};

    public static final String CREATE_TABLE_FOOD = "create table " + FOOD_TABLE + " ( "
            + COLUMN_ID + " integer primary  key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_TIME + " long not null, "
            + COLUMN_DATE + ");";

    private SQLiteDatabase sqlDB;
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

    public Food createFood(String name, long time){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        long insertId = sqlDB.insert(FOOD_TABLE, null, values);

        Cursor cursor = sqlDB.query(FOOD_TABLE, allColumns, COLUMN_ID + " = " + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Food newFood = cursorToFood(cursor);
        cursor.close();
        return newFood;
    }

    public boolean deleteFood(long id) {
        return sqlDB.delete(FOOD_TABLE, COLUMN_ID + "=" + id, null) > 0;
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


    private Food cursorToFood (Cursor cursor) {
        Food newFood = new Food ( cursor.getString(1), cursor.getLong(2), cursor.getLong(0),
                cursor.getLong(3) );
        return newFood;
    }


    private static class TimerDbHelper extends SQLiteOpenHelper {



        TimerDbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create note table
            db.execSQL(CREATE_TABLE_FOOD);
//            getTableAsString(db, FOOD_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TimerDbHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            //Destroy data
            db.execSQL("DROP TABLE IF EXIST " + FOOD_TABLE);
            onCreate(db);

        }

        String TAG = "DbHelper";
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
            Log.d(TAG, "getTableAsString called");
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


    }

}















