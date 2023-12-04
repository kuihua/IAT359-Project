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
    boolean rename;
    boolean firstTime;
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

        if (!firstTime && !rename) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        //when user is a first time user, add all data to the db shop table
        if(firstTime){
            db = new MyDatabase(this);
            db.insertShopData("Bowtie", "Neck", "10", "bowtie_icon.png");
            db.insertShopData("Crown", "Head", "10", "crown_icon.png");
            db.insertShopData("Tie","Neck","10","tie_icon.png");
            db.insertShopData("Cloak", "Body", "10", "cloak_icon.png");
            db.insertShopData("Tophat","Head","10","tophat_icon.png");
            db.insertShopData("Tuxedo", "Body", "10", "tuxedo_icon.png");

            SharedPreferences.Editor editor = sharedPrefs.edit();
            // currency, affection
            editor.putInt("coin", 100);
            editor.putInt("affection", 0);
            //for quest completion
            editor.putBoolean("feed", false);
            editor.putBoolean("play", false);
            editor.putBoolean("pet", false);
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

        editor.commit();

        Toast.makeText(this, "Welcome home "+petNameEdit.getText().toString()+"!", Toast.LENGTH_LONG).show();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }//end of naming the pet

}//end of activity