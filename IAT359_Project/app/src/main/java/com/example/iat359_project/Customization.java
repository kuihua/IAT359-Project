package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Customization extends AppCompatActivity{

        private RecyclerView myRecycler;
        private MyDatabase db;
        private TextView itemNameText, itemTypeText, itemWearingText;
        private ImageView itemImageView;
        Button filterHead, filterNeck, filterBody, allButton;
        private CustomAdapter customAdapter;

        private MyHelper helper;
        private LinearLayoutManager mLayoutManager;
        Cursor cursor;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_customization);
            myRecycler = (RecyclerView) findViewById(R.id.recyclerView);

            myRecycler.setLayoutManager(new LinearLayoutManager(this));
            db = new MyDatabase(this);
            helper = new MyHelper(this);

            //initializing buttons
            allButton = (Button) findViewById(R.id.allButton);
            filterHead = (Button) findViewById(R.id.filterHeadButton);
            filterNeck = (Button) findViewById(R.id.filterNeckButton);
            filterBody = (Button) findViewById(R.id.filterBodyButton);

            //filtering data
            Intent intent = getIntent();
            if(intent.hasExtra("Item")){
                //filtering the data by the filter
                String item = intent.getExtras().getString("Item");
                cursor = db.getPlayerQueryData(item);
            }else if(intent.hasExtra("Item")){
                String item = intent.getExtras().getString("Item");
                //if the player chooses to filter by all, display everything
                if(item.equals("All")){
                    cursor = db.getPlayerData();
                }
            }else{
                //else display everything
                cursor = db.getPlayerData();
            }

            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.WEARING);
            int index4 = cursor.getColumnIndex(Constants.IMAGE);

            ArrayList<String> mArrayList = new ArrayList<String>();

            //get item information to the cursor
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String itemName = cursor.getString(index1);
                String itemType = cursor.getString(index2);
                String itemWearing= cursor.getString(index3);
                String itemImage = cursor.getString(index4);
                String s = itemName +"," + itemType + "," + itemWearing + "," + itemImage;
                mArrayList.add(s);
                cursor.moveToNext();
            }

            customAdapter = new CustomAdapter(mArrayList);
            myRecycler.setAdapter(customAdapter);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            myRecycler.setLayoutManager(mLayoutManager);
            ;
        }//end of onCreate

    //buttons for filtering data, creates a new instance of this activity
    public void filterAll(View view){
        Intent i = new Intent(this, Customization.class);
        i.putExtra("Item", "All");
        startActivity(i);
    }
    public void filterHead(View view){
        Intent i = new Intent(this, Customization.class);
        i.putExtra("Item", "Head");
        startActivity(i);
    }

    public void filterNeck(View view){
        Intent i = new Intent(this, Customization.class);
        i.putExtra("Item", "Neck");
        startActivity(i);
    }

    public void filterBody(View view){
        Intent i = new Intent(this, Customization.class);
        i.putExtra("Item", "Body");
        startActivity(i);
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


}//end of class