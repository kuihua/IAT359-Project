package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Quest extends AppCompatActivity {

    Button playQuest, feedQuest, petQuest;
    TextView playReward, feedReward, petReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        checkQuestStatus();

        //setting collect reward buttons
        playQuest = (Button) findViewById(R.id.collectButton1);
        feedQuest = (Button) findViewById(R.id.collectButton2);
        petQuest = (Button) findViewById(R.id.collectButton3);

        //setting reward
        playReward = (TextView) findViewById(R.id.reward1);
        feedReward = (TextView) findViewById(R.id.reward2);
        petReward = (TextView) findViewById(R.id.reward3);

        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        boolean pet = sharedPref.getBoolean("pet", false);
        boolean feed = sharedPref.getBoolean("feed", false);
        boolean play = sharedPref.getBoolean("play", false);

        boolean collected1 = sharedPref.getBoolean("collected1", false);
        boolean collected2 = sharedPref.getBoolean("collected2", false);
        boolean collected3 = sharedPref.getBoolean("collected3", false);

        if(play && !collected1){
            playQuest.setClickable(true);
            playQuest.setText("Collect");
        }else if(play && collected1){
            playQuest.setText("Collected");
            playQuest.setClickable(false);
        }else{
            playQuest.setText("In Progress");
            playQuest.setClickable(false);
        }

        if(feed && !collected2){
            feedQuest.setClickable(true);
            feedQuest.setText("Collect");
        }else if(feed && collected2){
            feedQuest.setText("Collected");
            feedQuest.setClickable(false);
        }else{
            feedQuest.setText("In Progress");
            feedQuest.setClickable(false);
        }

        if(pet && !collected3) {
            petQuest.setClickable(true);
            petQuest.setText("Collect");
        }else if(pet && collected3){
            petQuest.setText("Collected");
            petQuest.setClickable(false);
        }else{
            petQuest.setText("In Progress");
            petQuest.setClickable(false);
        }

    } // end of onCreate

    //reward collection buttons
    public void reward1(View v){
        int rewardAmt = Integer.parseInt(playReward.getText().toString().replace("Reward: ", ""));
        Log.d("rewardAmt", ""+rewardAmt);
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int currentMoney = sharedPref.getInt("coin", 100);
        int newMoney = currentMoney+rewardAmt;
        editor.putInt("coin", newMoney);
        editor.putBoolean("collected1", true);
        editor.commit();
        playQuest.setText("Collected");
        playQuest.setClickable(false);
        Toast.makeText(this, "Gained "+ rewardAmt, Toast.LENGTH_SHORT).show();
    }

    public void reward2(View v){
        int rewardAmt = Integer.parseInt(feedReward.getText().toString().replace("Reward: ", ""));
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int currentMoney = sharedPref.getInt("coin", 100);
        int newMoney = currentMoney+rewardAmt;
        editor.putInt("coin", newMoney);
        editor.putBoolean("collected2", true);
        editor.commit();
        feedQuest.setText("Collected");
        feedQuest.setClickable(false);
        Toast.makeText(this, "Gained "+ rewardAmt, Toast.LENGTH_SHORT).show();
    }

    public void reward3(View v){
        int rewardAmt = Integer.parseInt(petReward.getText().toString().replace("Reward: ", ""));
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int currentMoney = sharedPref.getInt("coin", 100);
        int newMoney = currentMoney+rewardAmt;
        editor.putInt("coin", newMoney);
        editor.putBoolean("collected3", true);
        editor.commit();
        petQuest.setText("Collected");
        petQuest.setClickable(false);
        Toast.makeText(this, "Gained "+ rewardAmt, Toast.LENGTH_SHORT).show();
    }

    //implementing a thread to continuously check quest status
    public void checkQuestStatus() {
        Thread myThread = new Thread(new questThread());
        myThread.start();
    }

    private class questThread implements Runnable {
        @Override
        public void run() {
            //check every 2s
            SystemClock.sleep(2000);
            // if all quests are completed, refresh them
            if (checkQuestRefresh()) {
                SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                //sending values to shared prefs
                editor.putBoolean("play", false);
                editor.putBoolean("pet", false);
                editor.putBoolean("feed", false);
                editor.putBoolean("collected1", false);
                editor.putBoolean("collected2", false);
                editor.putBoolean("collected3", false);
                editor.commit();

                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        //setting button text
                        playQuest.setText("In\nProgress");
                        petQuest.setText("In\nProgress");
                        feedQuest.setText("In\nProgress");
                    }
                }); // end of ui thread
            } // end of if
        } // end of run
    } // end of questThread

    //checking quest status
    public boolean checkQuestRefresh(){
        boolean run=true;
        while(run) {
            String collectedFeed = feedQuest.getText().toString();
            String collectedPlay = playQuest.getText().toString();
            String collectedPet = petQuest.getText().toString();
            if (collectedPet.equals("Collected") && collectedFeed.equals("Collected") && collectedPlay.equals("Collected")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //shop button
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

} // end of class