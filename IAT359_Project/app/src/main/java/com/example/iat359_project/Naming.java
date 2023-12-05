package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Naming extends AppCompatActivity {

    EditText petNameEdit;
    boolean rename;
    boolean firstTime, isPlaying;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);

        //checking if the user if a first time user or not, if they are continue on this page
        //else will get brought to the main activity
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstTime = sharedPrefs.getBoolean("firstTime", true);
        rename = sharedPrefs.getBoolean("rename", false);
        isPlaying = sharedPrefs.getBoolean("mainBGM", false);

        //starts playing the main bgm
        if(!isPlaying){
            startService(new Intent(this, MainMusicService.class));
            isPlaying = true;
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putBoolean("firstTime", isPlaying);
            editor.commit();
        }

        if (!firstTime && !rename) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        //when user is a first time user, add all data to the db shop table
        if(firstTime){
            db = new MyDatabase(this);
            db.insertShopData("Bowtie", "Neck", "35", "bowtie_icon.png");
            db.insertShopData("Crown", "Head", "50", "crown_icon.png");
            db.insertShopData("Tie","Neck","15","tie_icon.png");
            db.insertShopData("Cloak", "Body", "25", "cloak_icon.png");
            db.insertShopData("Tophat","Head","15","tophat_icon.png");
            db.insertShopData("Tuxedo", "Body", "30", "tuxedo_icon.png");

            //giving the user base affection, starter money
            SharedPreferences.Editor editor = sharedPrefs.edit();
            // currency, affection
            editor.putInt("coin", 50);
            editor.putInt("affection", 0);
            //for quest completion, not completed
            editor.putBoolean("feed", false);
            editor.putBoolean("play", false);
            editor.putBoolean("pet", false);
            //for if the main bgm is playing
            editor.putBoolean("mainBGM", true);
            editor.commit();
        }

        petNameEdit = (EditText)findViewById(R.id.petNameEdit);

    }//end of onCreate

    public void name (View view){
        //save pet's name to shared preferences, player is not a first time user anymore
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        //remembering pet name
        editor.putString("petName", petNameEdit.getText().toString());
        //set rename false
        editor.putBoolean("rename", false);
        //remembering if its the first time accessing this app
        editor.putBoolean("firstTime", false);
        //save data to shared prefs
        editor.commit();

        //go to main activity
        Toast.makeText(this, "Welcome home "+petNameEdit.getText().toString()+"!", Toast.LENGTH_LONG).show();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }//end of naming the pet

    //if app is closed, stop service
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("mainBGM", false);
        editor.commit();
        stopService(new Intent(this, MainMusicService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        isPlaying = sharedPrefs.getBoolean("mainBGM", false);

        //starts playing the main bgm
        if (!isPlaying) {
            startService(new Intent(this, MainMusicService.class));
            isPlaying = true;
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putBoolean("mainBGM", isPlaying);
            editor.commit();
        }
    } // end of onResume



}//end of activity