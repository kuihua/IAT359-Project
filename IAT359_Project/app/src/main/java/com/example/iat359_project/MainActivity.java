package com.example.iat359_project;

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

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnTouchListener {

    ImageView food, plate, night, snow, rain, bodyItem, hatItem, neckItem, creature;
    TextView petNameView, currencyTextView, affectionTextView;
    private String result, url, lat, lng;
    private double latitude, longitude;
    boolean feeding = false;

    //light sensor values
    private SensorManager mySensorManager = null;
    private Sensor lightSensor = null;
    float[] light_vals = new float[1];
    public static final String DEFAULT = "no name";

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
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String petName = sharedPref.getString("petName", DEFAULT);
        checkConnection();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        //Images
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

        //using the player database to ensure they are wearing clothes from last session
        db = new MyDatabase(this);
        helper = new MyHelper(this);

        cursor = db.getPlayerData();

        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.TYPE);
        int index3 = cursor.getColumnIndex(Constants.WEARING);
//        int index4 = cursor.getColumnIndex(Constants.IMAGE);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String itemName = cursor.getString(index1);
            String itemType = cursor.getString(index2);
            String itemWear = cursor.getString(index3);
//            String itemImage = cursor.getString(index4);

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
            }
            cursor.moveToNext();
        } // end of cursor

        //Pet name displayed in main
        petNameView = (TextView) findViewById(R.id.namingTextView);
        petNameView.setText(petName);

        //display currency and affection values
        currencyTextView = (TextView) findViewById(R.id.currency);
        affectionTextView = (TextView) findViewById(R.id.affection);
        int coin = sharedPref.getInt("coin", 100);
        int love = sharedPref.getInt("affection", 0);
        currencyTextView.setText("Coins: " + coin);
        affectionTextView.setText("Affection: " + love);

        //Sensor
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //listeners for touch framework
        //set for all creature items to ensure layered images don't affect the touch
        bodyItem.setOnTouchListener(this);
        hatItem.setOnTouchListener(this);
        neckItem.setOnTouchListener(this);
        creature.setOnTouchListener(this);

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
        Intent i = new Intent(this, Customization.class);
        startActivity(i);
    }

    //for quest
    public void questButton(View view) {
        Intent i = new Intent(this, Quest.class);
        startActivity(i);
    }

    //go to matching mini game
    public void play(View v) {
        Intent i = new Intent(this, MatchingGame.class);
        startActivity(i);
    } // end of play

    //feeding the pet
    public void feed(View v) {
        if (!feeding) {
            //if the pet is not eating, start feeding
            plate.setVisibility(VISIBLE);
            food.setVisibility(VISIBLE);
            feeding = true;
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
        } else {
            //if the pet is being fed and the button is clicked, stop feeding
            plate.setVisibility(v.GONE);
            food.setVisibility(v.GONE);
            feeding = false;
        }
    } // end of feed

    //touch method
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
    }

    //for light sensor
    @Override
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
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

    // light sensor
    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_LIGHT) {
            light_vals = event.values;

            //if there's no light, change window to night time
            if (Sensor.TYPE_LIGHT >= light_vals[0]) {
//              Toast.makeText(this, "night time", Toast.LENGTH_LONG).show();
                night.setVisibility(VISIBLE);
            } else {
//              Toast.makeText(this, "good morning", Toast.LENGTH_LONG).show();
                //if there's light, change the window to day time
                night.setVisibility(View.GONE);
            }

        }
    } // end of sensor changed

    //GPS + weather
    public void getWeather(View v) {
        if (currentLocation != null) {
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
            lat = Double.toString(latitude);
            lng = Double.toString(longitude);

            url = "http://api.geonames.org/findNearByWeatherJSON?lat=" +
                    lat + "&lng=" +
                    lng + "&username=jvillaso";
//        url = "http://api.geonames.org/findNearByWeatherJSON?lat=43&lng=-2&username=jvillaso";
            int temperature = 10;

            Thread myThread = new Thread(new GetWeatherThread());
            myThread.start();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject weatherObservationItems =
                        new JSONObject(jsonObject.getString("weatherObservation"));
                //as the service does not have weather conditions, we opted to use temperature instead
                //get temperature from weather service
                String temp = "" + weatherObservationItems.getString("temperature");
                temperature = Integer.parseInt(temp);
                //if temperature is below zero
                if (temperature <= 0) {
                    snow.setVisibility(VISIBLE); //change to snow
                } else if(temperature >= 15){
                    // if temperature is above 15, hide snow, rain. shows default which is sun
                    snow.setVisibility(View.INVISIBLE);
                    rain.setVisibility(View.INVISIBLE);
                } else {
                    rain.setVisibility(VISIBLE); //change to rain
                }
            } catch (Exception e) {
                Log.d("ReadWeatherJSONDataTask", e.getLocalizedMessage());
            }
        }else{
            //if no location, show sun
            snow.setVisibility(View.INVISIBLE);
            rain.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No location", Toast.LENGTH_SHORT).show();
        }
    } // end of getWeather

    public void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSON_CODE);
            return;
        }
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
    }

    //check if connection is online
    public void checkConnection() {
        ConnectivityManager connectMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //fetch data

            String networkType = networkInfo.getTypeName().toString();
//            Toast.makeText(this, "connected to " + networkType, Toast.LENGTH_LONG).show();
        } else {
            //display error
            Toast.makeText(this, "No network connection", Toast.LENGTH_LONG).show();
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

    //   to share to social media (screenshot)
//    public void share(View v) {
//        saveImage();
//    }

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
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.putExtra(Intent.EXTRA_TEXT, "Play Yumi today!");
            startActivity(Intent.createChooser(intent, "Share using: "));
        } catch (ActivityNotFoundException e) {
        }
    } // end of share

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveImage();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }

//        location permissions
        if(requestCode == FINE_PERMISSON_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                Toast.makeText(this, "Location permissions denied", Toast.LENGTH_SHORT).show();
            }
        } // end of checking location permissions
    } // end of onRequestPermissionsResult

    private void saveImage() {

        if(!checkPermission())
            return;

        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/AppName";
            File fileDir = new File(path);
            if (fileDir.exists())
                fileDir.mkdir();

            String mPath = path + "/ScreenShot" + new Date().getTime() + ".png";

            Bitmap bitmap = screenShot();
            File file = new File(mPath);
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            Toast.makeText(this, "Image Saved", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end of saveImage

    private Bitmap screenShot() {
        View v = findViewById(R.id.mainView);
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private boolean checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    } // end of checkPermission

 //   save and share screenshot
//    private File saveImage(){
//        Exception exception = null;
//        try{
//            String path = Environment.getExternalStorageDirectory().toString() + "/com.example.iat359_project";
//            File filedir = new File(path);
//            if(!filedir.exists()){
//                filedir.mkdir();
//            }
//
//            String mPath = path + "/Screenshot_" + new Date().getTime() + ".png";
//            Bitmap bitmap = screenshot();
//            File file = new File(mPath);
//            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
//
//            Log.d("testingInputStream", file.toString());
//            FileOutputStream fOut = new FileOutputStream(file);
//            Toast.makeText(this, "bbbbb", Toast.LENGTH_SHORT).show();
//
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//
//            fOut.flush();
//            fOut.close();
//
//            Toast.makeText(this, "image saved", Toast.LENGTH_SHORT).show();
//
//            return file;
//        }catch(IOException e){
//            exception = e;
//        }
//        return null;
//    }
//
//    private void shareImage(File file){
//        Uri uri;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            uri = FileProvider.getUriForFile(this, getPackageManager()+".provider", file);
//        }else{
//            uri = Uri.fromFile(file);
//        }
//
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Screenshot");
//        intent.putExtra(Intent.EXTRA_TEXT, "This is my Yumi!");
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//
//        try{
//            startActivity(Intent.createChooser(intent, "Share using"));
//        }catch (ActivityNotFoundException e){
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//
//    }//end of shareImage

} // end of class