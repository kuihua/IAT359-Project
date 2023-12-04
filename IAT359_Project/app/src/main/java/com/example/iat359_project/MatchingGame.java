package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Collections;

public class MatchingGame extends AppCompatActivity {

    ImageView tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13, tile14, tile15, tile16;
    Integer[] tileArray;
    int firstTile, secondTile, clickedFirst, clickedSecond;
    int cardNumber=1;
    int image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11, image12, image13, image14, image15, image16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_game);

        //getting tile image views
        tile1 = (ImageView) findViewById(R.id.tile1);
        tile2 = (ImageView) findViewById(R.id.tile2);
        tile3 = (ImageView) findViewById(R.id.tile3);
        tile4 = (ImageView) findViewById(R.id.tile4);
        tile5 = (ImageView) findViewById(R.id.tile5);
        tile6 = (ImageView) findViewById(R.id.tile6);
        tile7 = (ImageView) findViewById(R.id.tile7);
        tile8 = (ImageView) findViewById(R.id.tile8);
        tile9 = (ImageView) findViewById(R.id.tile9);
        tile10 = (ImageView) findViewById(R.id.tile10);
        tile11 = (ImageView) findViewById(R.id.tile11);
        tile12 = (ImageView) findViewById(R.id.tile12);
        tile13 = (ImageView) findViewById(R.id.tile13);
        tile14 = (ImageView) findViewById(R.id.tile14);
        tile15 = (ImageView) findViewById(R.id.tile15);
        tile16 = (ImageView) findViewById(R.id.tile16);

        tile1.setTag(0);
        tile2.setTag(1);
        tile3.setTag(2);
        tile4.setTag(3);
        tile5.setTag(4);
        tile6.setTag(5);
        tile7.setTag(6);
        tile8.setTag(7);
        tile9.setTag(8);
        tile10.setTag(9);
        tile11.setTag(10);
        tile12.setTag(11);
        tile13.setTag(12);
        tile14.setTag(13);
        tile15.setTag(14);
        tile16.setTag(15);

        tileFrontResources();

        Collections.shuffle(Arrays.asList(tileArray));

        //setting listeners for all the tiles
        tile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt((String) v.getTag());
                flipTile(ImageView img, int tile);
            }
        });

        tile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tile14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tile16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }//end of onCreate

    public void tileFrontResources(){
        image1 = R.drawable.bowtie_tile;
        image2 = R.drawable.cloak_tile;
        image3 = R.drawable.tuxedo_tile;
        image4 = R.drawable.crown_tile;
        image5 = R.drawable.tie_tile;
        image6 = R.drawable.tophat_tile;
        image7 = R.drawable.heart_tile;
        image8 = R.drawable.coin_tile;

        image9 = R.drawable.bowtie_tile;
        image10 = R.drawable.bowtie_tile;
        image11 = R.drawable.bowtie_tile;
        image12 = R.drawable.bowtie_tile;
        image13 = R.drawable.bowtie_tile;
        image14 = R.drawable.bowtie_tile;
        image15 = R.drawable.bowtie_tile;
        image16 = R.drawable.bowtie_tile;

    } // front tile resources

    public void flipTile(ImageView img, int tile){

    }

}//end of class