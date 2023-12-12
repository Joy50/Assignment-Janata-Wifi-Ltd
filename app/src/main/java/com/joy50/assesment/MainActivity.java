package com.joy50.assesment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private CardView lightCardView, proximityCardView, accelerometerCardView, gyroscopeCardView;
    // Variables to store the current sensor values
    private float currentLightValue = 0.0f;
    private float currentProximityValue = 0.0f;
    private float[] currentAccelerometerValues = {0.0f, 0.0f, 0.0f};
    private float[] currentGyroscopeValues = {0.0f, 0.0f, 0.0f};
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor proximitySensor;
    private Sensor accelerometerSensor;
    private Sensor gyroscopeSensor;
    private Handler handler;
    private SensorDbHelper sensorDbHelper;
    private SQLiteDatabase database;
    private TextView lightValueText;
    private TextView proximityValueText;
    private TextView accelerometerValueText;
    private TextView gyroscopeValueText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TextView Initialization
        lightValueText = findViewById(R.id.light_sensor_value);
        proximityValueText = findViewById(R.id.proximity_sensor_value);
        accelerometerValueText = findViewById(R.id.accelerometer_sensor_value);
        gyroscopeValueText = findViewById(R.id.gyroscope_sensor_value);
        //Card initialization
        lightCardView = findViewById(R.id.light_card);
        proximityCardView = findViewById(R.id.proximity_card);
        accelerometerCardView = findViewById(R.id.accelerometer_card);
        gyroscopeCardView = findViewById(R.id.gyroscope_card);
        //Sensor Initialization
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        handler = new Handler();
        //Database Initialization
        sensorDbHelper = new SensorDbHelper(this);
        database = sensorDbHelper.getWritableDatabase();

        startUpdatingSensorData(database);
        Intent serviceIntent = new Intent(this, SensorService.class);
        startService(serviceIntent);
    }

    private void startUpdatingSensorData(final SQLiteDatabase database) {
        // Schedule the task to run every 5 minutes (300,000 milliseconds)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Store sensor data in the SQLite database
                storeSensorData(database, currentLightValue,currentProximityValue,currentAccelerometerValues,currentGyroscopeValues);

                // Schedule the next update after 5 minutes
                handler.postDelayed(this, 300000);
            }
        }, 300000);
    }

    private void storeSensorData(SQLiteDatabase database, float lightValue, float proximityValue,
                                 float[] accelerometerValues, float[] gyroscopeValues) {
        // Insert sensor data into the SQLite database
        ContentValues values = new ContentValues();
        values.put("light_value", lightValue);
        values.put("proximity_value", proximityValue);
        values.put("accelerometer_x", accelerometerValues[0]);
        values.put("accelerometer_y", accelerometerValues[1]);
        values.put("accelerometer_z", accelerometerValues[2]);
        values.put("gyroscope_x", gyroscopeValues[0]);
        values.put("gyroscope_y", gyroscopeValues[1]);
        values.put("gyroscope_z", gyroscopeValues[2]);
        // Insert data into the database
        database.insert("sensor_data_table", null, values);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightValueText.setText("Light: " + sensorEvent.values[0]);
            // Update the current light value
            currentLightValue = sensorEvent.values[0];
            storeSensorData(database,currentLightValue,currentProximityValue,currentAccelerometerValues,currentGyroscopeValues);
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximityValueText.setText("Proximity: " + sensorEvent.values[0]);
            // Update the current proximity value
            currentProximityValue = sensorEvent.values[0];
            storeSensorData(database,currentLightValue,currentProximityValue,currentAccelerometerValues,currentGyroscopeValues);
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValueText.setText("Accelerometer: X=" + sensorEvent.values[0] + ", Y=" + sensorEvent.values[1] + ", Z=" + sensorEvent.values[2]);
            // Update the current accelerometer values
            currentAccelerometerValues[0] = sensorEvent.values[0];
            currentAccelerometerValues[1] = sensorEvent.values[1];
            currentAccelerometerValues[2] = sensorEvent.values[2];
            storeSensorData(database,currentLightValue,currentProximityValue,currentAccelerometerValues,currentGyroscopeValues);
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroscopeValueText.setText("Gyroscope: X=" + sensorEvent.values[0] + ", Y=" + sensorEvent.values[1] + ", Z=" + sensorEvent.values[2]);
            // Update the current gyroscope values
            currentGyroscopeValues[0] = sensorEvent.values[0];
            currentGyroscopeValues[1] = sensorEvent.values[1];
            currentGyroscopeValues[2] = sensorEvent.values[2];
            storeSensorData(database,currentLightValue,currentProximityValue,currentAccelerometerValues,currentGyroscopeValues);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,proximitySensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,gyroscopeSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database!=null){
            database.close();
        }
    }
}