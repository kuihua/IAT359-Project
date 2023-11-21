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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstTime = sharedPrefs.getBoolean("firstTime", true);
        if (!firstTime && !rename) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        //add all data to the db
        if(firstTime){
            db = new MyDatabase(this);
            db.insertShopData("Red Bow Tie", "Neck", "10", "bowtie_red.png");
            db.insertShopData("Crown Red Jewels", "Head", "10", "crown_red_jewels.png");
            db.insertShopData("Loose Tie","Neck","10","loose_tie_red.png");
            db.insertShopData("Red Cloak", "Body", "10", "red_cloak.png");
            db.insertShopData("Top Hat","Head","10","tophat.png");
            db.insertShopData("Tuxedo", "Body", "10", "tuxedo.png");
        }

        petNameEdit = (EditText)findViewById(R.id.petNameEdit);

    }//end of onCreate

    public void name (View view){
        rename=false;
        firstTime=false;
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("petName", petNameEdit.getText().toString());
        editor.putBoolean("rename", rename);
        editor.putBoolean("firstTime", firstTime);
        Toast.makeText(this, "Welcome home!", Toast.LENGTH_LONG).show();
        editor.commit();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }//end of naming the pet

}//end of activity