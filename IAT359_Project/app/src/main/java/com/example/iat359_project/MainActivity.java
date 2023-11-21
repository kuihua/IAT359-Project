package com.example.iat359_project;

import static com.example.iat359_project.Naming.DEFAULT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    int affection=0;
    int currency=100;

    ImageView toy, food, plate, night;
    TextView petNameView;
    String lat, lng, result, url;
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
    public static final String DEFAULT = "no name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String petName = sharedPref.getString("petName", DEFAULT);
        checkConnection();

        //using the player database to ensure they are wearing clothes from last session
        playerdb = new MyPlayerDatabase(this);
        playerHelper = new MyPlayerHelper(this);

        //Images
        toy = (ImageView) findViewById(R.id.toyView);
        food = (ImageView) findViewById(R.id.foodView);
        plate = (ImageView) findViewById(R.id.plateView);
        night = (ImageView) findViewById(R.id.nightWindow);

        //Pet name displayed in main
        petNameView = (TextView) findViewById(R.id.namingTextView);
        petNameView.setText(petName);

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

    //GPS + weather
    public void getWeather(View v) {
        url = "http://api.geonames.org/findNearByWeatherJSON?lat=";

        Thread myThread = new Thread(new GetWeatherThread());
        myThread.start();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherObservationItems =
                    new JSONObject(jsonObject.getString("weatherObservation"));


        } catch (Exception e) {
            Log.d("ReadWeatherJSONDataTask", e.getLocalizedMessage());
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

}