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

import java.util.ArrayList;

public class Customization extends AppCompatActivity{

        private RecyclerView myRecycler;
        private MyPlayerDatabase db;

        private MyAdapter adapter;
        Button prevButton, nextButton;
        Paginator paginator = new Paginator();
        private long totalPages = Paginator.totalNumItems / Paginator.ITEMS_PER_PAGE;
        private int currentPage = 0;

        private MyPlayerHelper helper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_customization);
            myRecycler = (RecyclerView) findViewById(R.id.recyclerView);
            prevButton = (Button) findViewById(R.id.backButton);
            nextButton = (Button) findViewById(R.id.nextButton);
            //previous button won't work if its on first page
            prevButton.setEnabled(false);

            myRecycler.setLayoutManager(new LinearLayoutManager(this));
            myRecycler.setAdapter(new MyAdapter(Customization.this, paginator.generatePage(currentPage)));

            db = new MyPlayerDatabase(this);
            helper = new MyPlayerHelper(this);

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPage += 1;
                    myRecycler.setAdapter(new MyAdapter(Customization.this, paginator.generatePage(currentPage)));
                    toggleButtons();
                }
            });

            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPage -= 1;
                    myRecycler.setAdapter(new MyAdapter(Customization.this, paginator.generatePage(currentPage)));
                    toggleButtons();

                }
            });

;
        }

        //buttons toggled based on the page you are on
        private void toggleButtons(){
            if(currentPage == totalPages){
                nextButton.setEnabled(false);
                prevButton.setEnabled(true);
            }else if(currentPage == 0){
                nextButton.setEnabled(true);
                prevButton.setEnabled(false);
            }else if(currentPage >=1 && currentPage <= totalPages){
                nextButton.setEnabled(true);
                prevButton.setEnabled(true);
            }
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