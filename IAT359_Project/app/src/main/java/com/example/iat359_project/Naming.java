package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

// Naming activity, the first activity the user sees as a first time user
// or if the user wants to rename their pet, this is the activity they see
public class Naming extends AppCompatActivity {

    EditText petNameEdit;
    boolean rename;
    boolean firstTime;
    MyDatabase db;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);

        //checking if the user if a first time user or not, if they are continue on this page
        //else will get brought to the main activity
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstTime = sharedPrefs.getBoolean("firstTime", true);
        rename = sharedPrefs.getBoolean("rename", false);

        // if the user is not a first time user or renaming their creature, bring them to the main activity
        if (!firstTime && !rename) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        //when user is a first time user, add all initial data to the db shop table
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
        }

        petNameEdit = (EditText)findViewById(R.id.petNameEdit);

    }//end of onCreate

    // button to confirm pet name and brings user to the main activity
    public void name (View view){
        //sfx for tap
        mp = MediaPlayer.create(this, R.raw.tap);
        mp.start();
        //save pet's name to shared preferences, player is not a first time user anymore
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        // saving the inputted text from the edit text into shared preferences
        editor.putString("petName", petNameEdit.getText().toString());
        // set rename false, the player is not renaming the creature
        editor.putBoolean("rename", false);
        // remembering the user isn't a first time user anymore
        editor.putBoolean("firstTime", false);
        //save data to shared prefs
        editor.commit();

        //go to main activity
        Toast.makeText(this, "Welcome home "+petNameEdit.getText().toString()+"!", Toast.LENGTH_LONG).show();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }//end of naming the pet button
    
}//end of activity