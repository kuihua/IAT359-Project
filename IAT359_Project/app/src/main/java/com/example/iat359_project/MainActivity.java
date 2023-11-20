package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    int affection=0;
    int currency=100;

    ImageView toy, food, plate, night;
    boolean feeding = false;
    boolean playing = false;

    //light sensor values
    private SensorManager mySensorManager = null;
    private Sensor lightSensor = null;
    float[] light_vals = new float[1];
    private MyPlayerDatabase playerdb;
    private MyShopDatabase shopdb;
    private MyPlayerHelper playerHelper;
    private MyShopHelper shopHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //using the player database to ensure they are wearing clothes from last session
        playerdb = new MyPlayerDatabase(this);
        playerHelper = new MyPlayerHelper(this);

        //Images
        toy = (ImageView) findViewById(R.id.toyView);
        food = (ImageView) findViewById(R.id.foodView);
        plate = (ImageView) findViewById(R.id.plateView);
        night = (ImageView) findViewById(R.id.nightWindow);

        //Sensor
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    //for shop button
    public void gotoShop(View view) {
        Intent i = new Intent(this, Shop.class);
        startActivity(i);
    }

    //for custom button
    public void custom(View view) {
        Intent i = new Intent(this,Customization.class);
        startActivity(i);
    }

    //for playing using toys
    public void play(View v) {
        if(!playing) {
            //if the pet is not playing, start playing
            toy.setVisibility(v.VISIBLE);
            playing=true;
            //if the pet is playing, set food items to gone
            feeding=false;
            plate.setVisibility(v.GONE);
            food.setVisibility(v.GONE);
        }else{
            //if the pet is playing and the button is clicked, pet stops playing
            toy.setVisibility(v.GONE);
            playing=false;
        }
    }

    //feeding the pet
    public void feed(View v) {
        if(!feeding) {
            //if the pet is not eating, start feeding
            plate.setVisibility(v.VISIBLE);
            food.setVisibility(v.VISIBLE);
            feeding=true;
            //if the pet is being fed, set all toys to gone
            playing=false;
            toy.setVisibility(v.GONE);
        }else{
            //if the pet is being fed and the button is clicked, stop feeding
            plate.setVisibility(v.GONE);
            food.setVisibility(v.GONE);
            feeding=false;
        }
    }

    //for light sensor
    @Override
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(this, lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        //unregister listener - release the sensor
        mySensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_LIGHT) {
            light_vals = event.values;

            //if there's no light, change window to night time
            if(Sensor.TYPE_LIGHT >= light_vals[0]) {
//              Toast.makeText(this, "night time", Toast.LENGTH_LONG).show();
                night.setVisibility(View.VISIBLE);
            } else {
//              Toast.makeText(this, "good morning", Toast.LENGTH_LONG).show();
                night.setVisibility(View.GONE);
            }

        }
    }

}