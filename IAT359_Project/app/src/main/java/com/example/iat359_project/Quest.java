package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Quest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

    }

    public void gotoShop(View view) {
        Intent i = new Intent(this, Shop.class);
        startActivity(i);
    }

    //for customization button
    public void custom(View view) {
        Intent i = new Intent(this,Customization.class);
        startActivity(i);
    }

    //for home
    public void goHome(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}