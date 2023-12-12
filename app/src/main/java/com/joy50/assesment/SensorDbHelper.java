package com.joy50.assesment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SensorDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sensor_data.db";
    private static final int DATABASE_VERSION = 1;

    // Define the table and column names
    private static final String TABLE_SENSOR_DATA = "sensor_data_table";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LIGHT_VALUE = "light_value";
    private static final String COLUMN_PROXIMITY_VALUE = "proximity_value";
    private static final String COLUMN_ACCELEROMETER_X = "accelerometer_x";
    private static final String COLUMN_ACCELEROMETER_Y = "accelerometer_y";
    private static final String COLUMN_ACCELEROMETER_Z = "accelerometer_z";
    private static final String COLUMN_GYROSCOPE_X = "gyroscope_x";
    private static final String COLUMN_GYROSCOPE_Y = "gyroscope_y";
    private static final String COLUMN_GYROSCOPE_Z = "gyroscope_z";

    // Create table statement
    private static final String CREATE_TABLE_SENSOR_DATA =
            "CREATE TABLE " + TABLE_SENSOR_DATA + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LIGHT_VALUE + " REAL, " +
                    COLUMN_PROXIMITY_VALUE + " REAL, " +
                    COLUMN_ACCELEROMETER_X + " REAL, " +
                    COLUMN_ACCELEROMETER_Y + " REAL, " +
                    COLUMN_ACCELEROMETER_Z + " REAL, " +
                    COLUMN_GYROSCOPE_X + " REAL, " +
                    COLUMN_GYROSCOPE_Y + " REAL, " +
                    COLUMN_GYROSCOPE_Z + " REAL);";

    public SensorDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SENSOR_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //
    }
}
