package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    int affection=0;
    int currency=100;

    ImageView toy, food, plate;
    boolean feeding = false;
    boolean playing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Images
        toy = (ImageView) findViewById(R.id.toyView);
        food = (ImageView) findViewById(R.id.foodView);
        plate = (ImageView) findViewById(R.id.plateView);
    }

    //for shop button
    public void gotoShop(View view) {
        Intent i = new Intent(this, Shop.class);
        startActivity(i);
    }

    //for custom button
    public void custom(View view) {
        Intent i = new Intent(this,Customization.class);
        startActivity(i);
    }

    //for playing using toys
    public void play(View v) {
        if(!playing) {
            toy.setVisibility(v.VISIBLE);
            playing=true;
            //if the pet is playing, set food items to gone
            feeding=false;
            plate.setVisibility(v.GONE);
            food.setVisibility(v.GONE);
        }else{
            toy.setVisibility(v.GONE);
            playing=false;
        }
    }

    //feeding the pet
    public void feed(View v) {
        if(!feeding) {
            plate.setVisibility(v.VISIBLE);
            food.setVisibility(v.VISIBLE);
            feeding=true;
            //if the pet is being fed, set all toys to gone
            playing=false;
            toy.setVisibility(v.GONE);
        }else{
            plate.setVisibility(v.GONE);
            food.setVisibility(v.GONE);
            feeding=false;
        }
    }
}