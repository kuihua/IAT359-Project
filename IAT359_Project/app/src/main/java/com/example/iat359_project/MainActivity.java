package com.example.iat359_project;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class MainActivity extends AppCompatActivity implements SensorEventListener {

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

        cursor = db.getPlayerData();

        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.TYPE);
        int index3 = cursor.getColumnIndex(Constants.WEARING);
//        int index4 = cursor.getColumnIndex(Constants.IMAGE);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String itemName = cursor.getString(index1);
            String itemType = cursor.getString(index2);
            String itemWear= cursor.getString(index3);
//            String itemImage = cursor.getString(index4);

            //display clothes
            if (itemWear.equals("True")) {
                int id = getImage(itemName);
                if(itemType.contains("Head")){
                    hatItem.setImageResource(id);
                    hatItem.setVisibility(VISIBLE);
                }
                if(itemType.equals("Neck")){
                    neckItem.setImageResource(id);
                    neckItem.setVisibility(VISIBLE);
                }
                if(itemType.equals("Body")){
                    bodyItem.setImageResource(id);
                    bodyItem.setVisibility(VISIBLE);
                }
            }
            cursor.moveToNext();
        } // end of cursor


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
//            Toast.makeText(this, "connected to " + networkType, Toast.LENGTH_LONG).show();
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

    //to share to social media
    public void share(View view) {
//        shareScreenshot(screenShot(view));
//        Toast.makeText(this, "clicked",Toast.LENGTH_SHORT).show();
        File file = saveImage();
        if(file != null){
            shareImage(file);
        }
    }

    private File saveImage(){
        Exception exception = null;
        try{
            String path = Environment.getExternalStorageDirectory().toString() + "/com.example.iat359_project";
            File filedir = new File(path);
            if(!filedir.exists()){
                filedir.mkdir();
            }

            String mPath = path + "/Screenshot_" + new Date().getTime() + ".png";
            Bitmap bitmap = screenshot();
            File file = new File(mPath);
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();

            Log.d("testingInputStream", file.toString());
            FileOutputStream fOut = new FileOutputStream(file);
            Toast.makeText(this, "bbbbb", Toast.LENGTH_SHORT).show();

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

            fOut.flush();
            fOut.close();

            Toast.makeText(this, "image saved", Toast.LENGTH_SHORT).show();

            return file;
        }catch(IOException e){
            exception = e;
        }
        return null;
    }

    private void shareImage(File file){
        Uri uri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, getPackageManager()+".provider", file);
        }else{
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Screenshot");
        intent.putExtra(Intent.EXTRA_TEXT, "This is my Yumi!");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try{
            startActivity(Intent.createChooser(intent, "Share using"));
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }//end of shareImage

    private Bitmap screenshot(){
        View v = findViewById(R.id.mainView);
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            saveImage();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }



} // end of class