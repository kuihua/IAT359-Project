package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Quest extends AppCompatActivity {

    public static final String DEFAULT = "no name";
    TextView petNameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        //setting the pet's name based off of shared preferences
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String petName = sharedPref.getString("petName", DEFAULT);

        petNameView = (TextView) findViewById(R.id.namingTextView);
        petNameView.setText(petName);
    }//end of onCreate
}//end of class