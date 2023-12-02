package com.example.iat359_project;

//import static com.example.iat359_project.Naming.DEFAULT;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    int affection=0;
    int currency=100;

    ImageView toy, food, plate, night, snow, rain, bodyItem, hatItem, neckItem;
    TextView petNameView;
    String lat, lng, result, url, stationName;
    boolean feeding = false;
    boolean playing = false;

    //light sensor values
    private SensorManager mySensorManager = null;
    private Sensor lightSensor = null;
    float[] light_vals = new float[1];
    public static final String DEFAULT = "no name";

    private MyDatabase db;
    private MyHelper helper;
    Cursor cursor;
    int index1=-1;
    int index2, index3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String petName = sharedPref.getString("petName", DEFAULT);
        checkConnection();

        //Images
        toy = (ImageView) findViewById(R.id.toyView);
        food = (ImageView) findViewById(R.id.foodView);
        plate = (ImageView) findViewById(R.id.plateView);
        night = (ImageView) findViewById(R.id.nightWindow);
        snow = (ImageView) findViewById(R.id.snowWindow);
        rain= (ImageView) findViewById(R.id.rainWindow);

        bodyItem = (ImageView) findViewById(R.id.creatureBodyImageView);
        hatItem = (ImageView) findViewById(R.id.creatureHatImageView);
        neckItem = (ImageView) findViewById(R.id.creatureNeckImageView);

        //using the player database to ensure they are wearing clothes from last session
        db = new MyDatabase(this);
        helper = new MyHelper(this);

        db.getPlayerData();

        if(cursor.getColumnIndex(Constants.NAME) != 0){
            
        }
//        index1 = cursor.getColumnIndex(Constants.NAME);

        if(index1 != -1) {
             index1 = cursor.getColumnIndex(Constants.NAME);
             index2 = cursor.getColumnIndex(Constants.TYPE);
             index3 = cursor.getColumnIndex(Constants.WEARING);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String itemName = cursor.getString(index1);
                String itemType = cursor.getString(index2);
                String itemWear= cursor.getString(index3);
//            String itemImage = cursor.getString(index4);

                //display clothes
                if (itemWear == "True") {
                    int id = getImage(itemName);
                    if(itemType=="Head"){
                        hatItem.setImageResource(id);
                        hatItem.setVisibility(VISIBLE);
                    }
                    if(itemType=="Neck"){
                        neckItem.setImageResource(id);
                        neckItem.setVisibility(VISIBLE);
                    }
                    if(itemType=="Body"){
                        bodyItem.setImageResource(id);
                        bodyItem.setVisibility(VISIBLE);
                    }
                }
                cursor.moveToNext();
            }
        }//


        //Pet name displayed in main
        petNameView = (TextView) findViewById(R.id.namingTextView);
        petNameView.setText(petName);

        //Sensor
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

    }

    public int getImage(String name) {
        String file = name.toLowerCase();
        file = file.replace("_icon", "");
        int id = getResources().getIdentifier(file, "drawable", getPackageName());
        return id;
    }

    //for shop button
    public void gotoShop(View view) {
        Intent i = new Intent(this, Shop.class);
        startActivity(i);
    }

    //for customization button
    public void custom(View view) {
        Intent i = new Intent(this,Customization.class);
        startActivity(i);
    }

    //for quest
    public void questButton(View view) {
        Intent i = new Intent(this,Quest.class);
        startActivity(i);
    }

    //for playing using toys
    public void play(View v) {
        if(!playing) {
            //if the pet is not playing, start playing
            toy.setVisibility(VISIBLE);
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
            plate.setVisibility(VISIBLE);
            food.setVisibility(VISIBLE);
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
                night.setVisibility(VISIBLE);
            } else {
//              Toast.makeText(this, "good morning", Toast.LENGTH_LONG).show();
                //if there's light, change the window to day time
                night.setVisibility(View.GONE);
            }

        }
    }

    //GPS + weather
    public void getWeather(View v) {
        url = "http://api.geonames.org/findNearByWeatherJSON?lat=" + lat + "&lng=" + lng + "&username=jvillaso";
        stationName = "http://api.geonames.org/findNearByWeatherJSON?lat=43&lng=-2&username=jvillaso";


        Thread myThread = new Thread(new GetWeatherThread());
        myThread.start();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherObservationItems =
                    new JSONObject(jsonObject.getString("weatherObservation"));

            rain.setVisibility(Integer.parseInt(weatherObservationItems.getString("weatherObservation")));
        } catch (Exception e) {
            Log.d("ReadWeatherJSONDataTask", e.getLocalizedMessage());
            Toast.makeText(this, "weather retrieved", Toast.LENGTH_LONG).show();
                rain.setVisibility(VISIBLE);
            }
        }

    private class GetWeatherThread implements Runnable
    {
        @Override
        public void run() {
            Exception exception = null;
            try{
                result = readJSONData(url);
            }catch(IOException e){
                exception = e;
            }
        }
     }

     //check if connection is online
    public void checkConnection(){
        ConnectivityManager connectMgr =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            //fetch data

            String networkType = networkInfo.getTypeName().toString();
            Toast.makeText(this, "connected to " + networkType, Toast.LENGTH_LONG).show();
        }
        else {
            //display error
            Toast.makeText(this, "no network connection", Toast.LENGTH_LONG).show();
        }
    }

    private String readJSONData(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 2500;

        URL url = new URL(myurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("tag", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

} // end of class