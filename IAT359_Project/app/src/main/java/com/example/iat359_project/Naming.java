package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Naming extends AppCompatActivity {

    EditText petNameEdit;
    boolean rename=false;
    boolean firstTime;
    private MyDatabase db;
//    public static final boolean DEFAULT = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);

        //checking if the user is using the app for the first time
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstTime = sharedPrefs.getBoolean("firstTime", true);
        //if the user is not a first time user or in the midst of renaming their pet, send them to main activity
        if (!firstTime && !rename) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        //initializing editText
        petNameEdit = (EditText)findViewById(R.id.petNameEdit);

        //if it's a first time user, add all items to the database (for the shop items)
        if(firstTime){
            db = new MyDatabase(this);
            db.insertShopData("Red Bow Tie", "Neck", "10", "R.drawable.bowtie_red");
            db.insertShopData("Crown Red Jewels", "Head", "10", "R.drawable.crown_red_jewels");
            db.insertShopData("Loose Tie","Neck","10","R.drawable.loose_tie_red");
            db.insertShopData("Red Cloak", "Body", "10", "R.drawable.red_cloak");
            db.insertShopData("Top Hat","Head","10","R.drawable.tophat");
            db.insertShopData("Tuxedo", "Body", "10", "R.drawable.tuxedo");
        }

    }//end of onCreate

    //name button to name the pet
    public void name (View view){
        //once you name your pet, it is no longer the user's first time accessing this app
        rename=false;
        firstTime=false;

        //placing the pet's name into shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("petName", petNameEdit.getText().toString());
        editor.putBoolean("rename", rename);
        editor.putBoolean("firstTime", firstTime);
//        Toast.makeText(this, "Welcome!", Toast.LENGTH_LONG).show();
        editor.commit();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }//end of naming the pet

}//end of activity