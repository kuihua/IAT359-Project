package com.example.iat359_project;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

// main activity the user will interact with
public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnTouchListener {

    ImageView food, plate, night, snow, rain, bodyItem, hatItem, neckItem, creature;
    TextView petNameView, currencyTextView, affectionTextView;
    private String result, url, lat, lng;
    private double latitude, longitude;
    boolean feeding = false;
    MediaPlayer mp;

    //light sensor values
    private SensorManager mySensorManager = null;
    private Sensor lightSensor = null;
    float[] light_vals = new float[1];
    public static final String DEFAULT = "Yumi";

    private MyDatabase db;
    private MyHelper helper;
    Cursor cursor;

    private final int FINE_PERMISSON_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    //enable permission to access external storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] permissionStorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getting pet's name from shared preferences
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String petName = sharedPref.getString("petName", DEFAULT);

        //checking network connection
        checkConnection();

        // getting the user's location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // window images
        food = (ImageView) findViewById(R.id.foodView);
        plate = (ImageView) findViewById(R.id.plateView);
        night = (ImageView) findViewById(R.id.nightWindow);
        snow = (ImageView) findViewById(R.id.snowWindow);
        rain = (ImageView) findViewById(R.id.rainWindow);

        //creature images
        bodyItem = (ImageView) findViewById(R.id.creatureBodyImageView);
        hatItem = (ImageView) findViewById(R.id.creatureHatImageView);
        neckItem = (ImageView) findViewById(R.id.creatureNeckImageView);
        creature = (ImageView) findViewById(R.id.creatureImageView);

        db = new MyDatabase(this);
        helper = new MyHelper(this);

        // using the player database to ensure they are wearing clothes from last session
        cursor = db.getPlayerData();

        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.TYPE);
        int index3 = cursor.getColumnIndex(Constants.WEARING);
//        int index4 = cursor.getColumnIndex(Constants.IMAGE);

        cursor.moveToFirst();
        // we only need to access these 3 columns
        while (!cursor.isAfterLast()) {
            String itemName = cursor.getString(index1);
            String itemType = cursor.getString(index2);
            String itemWear = cursor.getString(index3);

            //display clothes
            if (itemWear.equals("True")) {
                int id = getImage(itemName);
                if (itemType.contains("Head")) {
                    hatItem.setImageResource(id);
                    hatItem.setVisibility(VISIBLE);
                }
                if (itemType.equals("Neck")) {
                    neckItem.setImageResource(id);
                    neckItem.setVisibility(VISIBLE);
                }
                if (itemType.equals("Body")) {
                    bodyItem.setImageResource(id);
                    bodyItem.setVisibility(VISIBLE);
                }
            } // end of if statements
            cursor.moveToNext();
        } // end of cursor

        // pet name displayed in main, taken from shared preferences
        petNameView = (TextView) findViewById(R.id.namingTextView);
        petNameView.setText(petName);

        // display currency and affection values, taken from shared preferences
        currencyTextView = (TextView) findViewById(R.id.currency);
        affectionTextView = (TextView) findViewById(R.id.affection);
        int coin = sharedPref.getInt("coin", 100);
        int love = sharedPref.getInt("affection", 0);
        currencyTextView.setText("Coins: " + coin);
        affectionTextView.setText("Affection: " + love);

        // Sensor, getting the light sensor
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //listeners for touch framework
        //set for all creature items to ensure layered images don't affect the touch
        bodyItem.setOnTouchListener(this);
        hatItem.setOnTouchListener(this);
        neckItem.setOnTouchListener(this);
        creature.setOnTouchListener(this);

    } // end of onCreate

    //method for getting image
    public int getImage(String name) {
        String file = name.toLowerCase();
        // we want to access the clothing images,
        file = file.replace("_icon", "");
        int id = getResources().getIdentifier(file, "drawable", getPackageName());
        return id;
    } // end of getImage

    // rename button
    public void renameButton(View view){
        // sfx for tap
        mp = MediaPlayer.create(this, R.raw.tap);
        mp.start();
        // set rename to true so the naming activity won't bring the user back to main
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("rename", true);
        editor.commit();
        //create intent to naming activity
        Intent i = new Intent(this, Naming.class);
        startActivity(i);
    } // end of rename button

    // go to shop button
    public void gotoShop(View view) {
        // sfx for tap
        mp = MediaPlayer.create(this, R.raw.tap);
        mp.start();
        // explicit intent to go to the shop activity
        Intent i = new Intent(this, Shop.class);
        startActivity(i);
    } // end of shop button

    // go to customization button
    public void custom(View view) {
        // sfx for tap
        mp = MediaPlayer.create(this, R.raw.tap);
        mp.start();
        // intent to go to customization activity
        Intent i = new Intent(this, Customization.class);
        startActivity(i);
    } // end of customization button

    // go to quest button
    public void questButton(View view) {
        //sfx for tap
        mp = MediaPlayer.create(this, R.raw.tap);
        mp.start();
        // intent to go to quest activity
        Intent i = new Intent(this, Quest.class);
        startActivity(i);
    } // end of quest button

    // go to matching mini game play button
    public void play(View v) {
        //go the mini game activity
        //sfx for tap
        mp = MediaPlayer.create(this, R.raw.tap);
        mp.start();
        // intent to go to the matching game
        Intent i = new Intent(this, MatchingGame.class);
        startActivity(i);
    } // end of play button

    // button for feeding the pet
    public void feed(View v) {
        //sfx for tap
        mp = MediaPlayer.create(this, R.raw.tap);
        mp.start();
        // checks if the pet is in the middle of eating
        // only allow if the pet is not eating or has already finished eating
        if (!feeding) {
            // if the pet is not eating, start feeding
            // show the plate with the food and set feeding to true
            plate.setVisibility(VISIBLE);
            food.setVisibility(VISIBLE);
            feeding = true;

            //sfx for eating
            mp = MediaPlayer.create(this, R.raw.eating);
            mp.start();

            //after 2 seconds execute code in run()
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    //remove food from plate
                    food.setVisibility(v.GONE);

                    //increase affection
                    SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                    int currentAffection = sharedPref.getInt("affection", 0);
                    int newAffection = currentAffection + 20;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("affection", newAffection);
                    //saving feed data for feed quest completion
                    editor.putBoolean("feed", true);
                    editor.commit();
                    affectionTextView.setText("Affection: " + newAffection);
                }
            }, 2000);
            // end of handler

            // second handler to execute code after 3 seconds
            // makes the plate and food (in case) disappear
            // sets feeding to false to let the user feed it again
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    plate.setVisibility(v.GONE);
                    food.setVisibility(v.GONE);
                    feeding = false;
                }
            }, 3000);
            // end of handler
        } // end of if(!feeding)
    } // end of feed button

    // touch method for petting
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //sfx for happy yumi
            mp = MediaPlayer.create(this, R.raw.pet_happy);
            mp.start();
            // increase affection
            SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            int currentAffection = sharedPref.getInt("affection", 0);
            int newAffection = currentAffection + 5;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("affection", newAffection);
            editor.putBoolean("pet", true);
            editor.commit();
            affectionTextView.setText("Affection: " + newAffection);
            return true;
        } else {
            return false;
        }
    } // end of onTouch

    //for light sensor
    @Override
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    } // end of onResume

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

    // light sensor to change window
    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_LIGHT) {
            light_vals = event.values;
            //if there's no light, change window to night time
            if (Sensor.TYPE_LIGHT >= light_vals[0]) {
                night.setVisibility(VISIBLE);
            } else {
                //if there's light, change the window to day time
                night.setVisibility(View.GONE);
            }
        }
    } // end of sensor changed

    //GPS + weather
    public void getWeather(View v) {
        //sfx for tap
        mp = MediaPlayer.create(this, R.raw.tap);
        mp.start();
        getLastLocation();
        if (currentLocation != null) {
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
            lat = Double.toString(latitude);
            lng = Double.toString(longitude);

            url = "http://api.geonames.org/findNearByWeatherJSON?lat=" +
                    lat + "&lng=" +
                    lng + "&username=jvillaso";
            float temperature = 10;

            // start weather thread
            Thread myThread = new Thread(new GetWeatherThread());
            myThread.start();

            try {
                Toast.makeText(this, "Your location: "+lat+" "+lng, Toast.LENGTH_SHORT).show();

                JSONObject jsonObject = new JSONObject(result);
                JSONObject weatherObservationItems =
                        new JSONObject(jsonObject.getString("weatherObservation"));

                // as the service does not have weather conditions, we opted to use temperature instead
                // get temperature from weather service
                String temp = weatherObservationItems.getString("temperature");
                Log.d("location testing testing", ""+temp);
                // ensuring it is numeric only
                temp = temp.replaceAll("[^\\d.]", "");
                temp.trim();
                temperature = Float.parseFloat(temp);
                // change window based on temperature
                if (temperature <= 0) {
                    snow.setVisibility(VISIBLE); //change to snow
                } else if(temperature >= 15){
                    // if temperature is above 15, hide snow, rain. shows default which is sun
                    snow.setVisibility(INVISIBLE);
                    rain.setVisibility(INVISIBLE);
                } else {
                    rain.setVisibility(VISIBLE); //change to rain
                }
            } catch (Exception e) {
                Log.d("ReadWeatherJSONDataTask", e.getLocalizedMessage());
            }
        }else{
            // if no location, show sun
            snow.setVisibility(INVISIBLE);
            rain.setVisibility(INVISIBLE);
            Toast.makeText(this, "No location.", Toast.LENGTH_SHORT).show();
        }
    } // end of getWeather

    // getting the user's location
    public void getLastLocation() {
        // checking permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSON_CODE);
            return;
        }
        // getting user location
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation = location;
                }
            }
        });
    } // end of getLastLocation

    // getting json data from the web service
    private class GetWeatherThread implements Runnable {
        @Override
        public void run() {
            Exception exception = null;
            try {
                result = readJSONData(url);
            } catch (IOException e) {
                exception = e;
            }
        }
    } // end of GetWeatherThread

    //check if connection is online
    public void checkConnection() {
        ConnectivityManager connectMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //nothing
        } else {
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
        }
    } // end of checkConnection

    // reading json data
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

            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // close input stream
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
    } // end of readJSONData

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    } // end of readIt

    //share button for sharing the yumi ad
    public void share(View v) {
        try {
            Uri imageUri = null;
            try {
                //store the png as a bitmap, regular png does not work: sends empty image
                imageUri = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), BitmapFactory.decodeResource(getResources(), R.drawable.yumi_ad), null, null));
            }
            catch (NullPointerException e) {
            }
            //creating implicit intent to share the img and text to a platform of user's choosing
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            //putting the imageUri and text into the intent
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.putExtra(Intent.EXTRA_TEXT, "Play Yumi today!");
            // let's the user share where they want to
            startActivity(Intent.createChooser(intent, "Share using: "));
        } catch (ActivityNotFoundException e) {
        }
    } // end of share

    // checking permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        location permissions
        if(requestCode == FINE_PERMISSON_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
                Toast.makeText(this, "Location permissions allowed.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Location permissions denied.", Toast.LENGTH_SHORT).show();
            }
        } // end of checking location permissions
    } // end of onRequestPermissionsResult

} // end of main activity class