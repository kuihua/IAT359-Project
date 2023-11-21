package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Shop extends AppCompatActivity {

    private RecyclerView myRecycler;
    private MyDatabase db;
    private MyHelper helper;
    private CustomAdapter customAdapter;
    private ImageView itemImageView;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        myRecycler = (RecyclerView) findViewById(R.id.recyclerView);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));

        db = new MyDatabase(this);
        helper = new MyHelper(this);

        //getting information from the shop table
        Cursor cursor = db.getShopData();

        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.TYPE);
        int index3 = cursor.getColumnIndex(Constants.PRICE);
        int index4 = cursor.getColumnIndex(Constants.IMAGE);

        ArrayList<String> mArrayList = new ArrayList<String>();

        //passing info to the cursor
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

        customAdapter = new CustomAdapter(mArrayList);
        myRecycler.setAdapter(customAdapter);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(mLayoutManager);

    }

    //for home button
    public void goHome(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    //for customization button
    public void custom(View view) {
        Intent i = new Intent(this,Customization.class);
        startActivity(i);
    }
}