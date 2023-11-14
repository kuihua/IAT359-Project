package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    int affection=0;
    int currency=100;

    //buttons
    Button shop, home, feed, play;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Nav Buttons
        shop = (Button)findViewById(R.id.button1);
        home = (Button)findViewById(R.id.button2);
        feed = (Button)findViewById(R.id.button3);
        play = (Button)findViewById(R.id.button4);
    }

    public void gotoShop(View view) {
        Intent i = new Intent(this, Shop.class);
        startActivity(i);
    }
}