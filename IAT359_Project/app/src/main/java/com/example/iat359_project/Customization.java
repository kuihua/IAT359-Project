package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Customization extends AppCompatActivity {

    Button shop, home, feed, play, quest, custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);

        //Buttons
        shop = (Button)findViewById(R.id.button1);
        home = (Button)findViewById(R.id.button2);
        feed = (Button)findViewById(R.id.button3);
        play = (Button)findViewById(R.id.button4);
        quest = (Button)findViewById(R.id.button5);
        custom = (Button)findViewById(R.id.button6);
    }

    //for shop button
    public void gotoShop(View view) {
        Intent i = new Intent(this, Shop.class);
        startActivity(i);
    }

    //for home button
    public void goHome(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}