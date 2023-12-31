package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Shop extends AppCompatActivity {

    private RecyclerView myRecycler;
    private MyDatabase db;
    private MyHelper helper;
    private CustomAdapter customAdapter;
    private LinearLayoutManager mLayoutManager;
    Cursor cursor;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        myRecycler = (RecyclerView) findViewById(R.id.recyclerView);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));

        //sfx for tap
        mp = MediaPlayer.create(this, R.raw.tap);

        db = new MyDatabase(this);
        helper = new MyHelper(this);

        // for filtering the shop based on item type
        Intent intent = getIntent();
        if(intent.hasExtra("Item")){
            String item = intent.getExtras().getString("Item");
            cursor = db.getShopQueryData(item);
        }else{
            cursor = db.getShopData();
        }

        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.TYPE);
        int index3 = cursor.getColumnIndex(Constants.PRICE);
        int index4 = cursor.getColumnIndex(Constants.IMAGE);

        ArrayList<String> mArrayList = new ArrayList<String>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String itemName = cursor.getString(index1);
            String itemType = cursor.getString(index2);
            String itemPrice= cursor.getString(index3);
            String itemImage = cursor.getString(index4);
            String s = itemName +"," + itemType + "," + itemPrice + "," + itemImage;
            mArrayList.add(s);
            cursor.moveToNext();
        }

        // setting recycler view contents
        customAdapter = new CustomAdapter(mArrayList);
        myRecycler.setAdapter(customAdapter);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(mLayoutManager);

    } // end of onCreate

    //filter by all, show all items
    public void filterAll(View view){
        //sfx for tap
        mp.start();
        //refresh activity, recreate() doesn't work
        Intent i = new Intent(this, Shop.class);
        startActivity(i);
    } // end of filterAll

    // filter by head type items
    public void filterHead(View view){
        //sfx for tap
        mp.start();
        Intent i = new Intent(this, Shop.class);
        i.putExtra("Item", "Head");
        startActivity(i);
    } // end of method

    // filter by neck type item
    public void filterNeck(View view){
        //sfx for tap
        mp.start();
        Intent i = new Intent(this, Shop.class);
        i.putExtra("Item", "Neck");
        startActivity(i);
    } // end of method

    // filter by body type item
    public void filterBody(View view){
        //sfx for tap
        mp.start();
        Intent i = new Intent(this, Shop.class);
        i.putExtra("Item", "Body");
        startActivity(i);
    } // end of filter button

    //for home button
    public void goHome(View view) {
        //sfx for tap
        mp.start();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    } // go to home

    //for custom button
    public void custom(View view) {
        //sfx for tap
        mp.start();
        Intent i = new Intent(this,Customization.class);
        startActivity(i);
    } // go to customization button

    //for quest
    public void questButton(View view) {
        //sfx for tap
        mp.start();
        Intent i = new Intent(this,Quest.class);
        startActivity(i);
    } // go to quest button
} // end of shop activity