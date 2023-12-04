package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Collections;

public class MatchingGame extends AppCompatActivity {

    ImageView tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13, tile14, tile15, tile16;
    Integer[] tileArray = {101,102,103,104,105,106,107,108,201,202,203,204,205,206,207,208};
    int firstTile, secondTile, clickedFirst, clickedSecond;
    int tileNumber = 1;
    int turn = 1;
    int img101, img102, img103, img104, img105, img106, img107, img108, img201, img202, img203, img204, img205, img206, img207, img208;

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

        //shuffles the array to randomize the tiles
        Collections.shuffle(Arrays.asList(tileArray));

        //setting listeners for all the tiles
        tile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile1, tile);
            }
        });

        tile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile2, tile);
            }
        });

        tile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile3, tile);
            }
        });

        tile4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile4, tile);
            }
        });

        tile5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile5, tile);
            }
        });

        tile6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile6, tile);
            }
        });

        tile7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile7, tile);
            }
        });

        tile8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile8, tile);
            }
        });

        tile9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile9, tile);
            }
        });

        tile10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile10, tile);
            }
        });

        tile11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile11, tile);
            }
        });

        tile12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile12, tile);
            }
        });

        tile13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile13, tile);
            }
        });
        tile14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile14, tile);
            }
        });

        tile15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile15, tile);
            }
        });

        tile16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tile = Integer.parseInt(v.getTag().toString());
                flipTile(tile16, tile);
            }
        });

    }//end of onCreate

    //images for the front of the tile
    public void tileFrontResources(){
        img101 = R.drawable.tile_img101;
        img102 = R.drawable.tile_img102;
        img103 = R.drawable.tile_img103;
        img104 = R.drawable.tile_img104;
        img105 = R.drawable.tile_img105;
        img106 = R.drawable.tile_img106;
        img107 = R.drawable.tile_img107;
        img108 = R.drawable.tile_img108;

        img201 = R.drawable.tile_img201;
        img202 = R.drawable.tile_img202;
        img203 = R.drawable.tile_img203;
        img204 = R.drawable.tile_img204;
        img205 = R.drawable.tile_img205;
        img206 = R.drawable.tile_img206;
        img207 = R.drawable.tile_img207;
        img208 = R.drawable.tile_img208;
    } // front tile resources

    public void flipTile(ImageView img, int tile){
        //setting the tile images based on the shuffled array
        if(tileArray[tile] == 101){
            img.setImageResource(img101);
        } else if(tileArray[tile] == 102){
            img.setImageResource(img102);
        } else if(tileArray[tile] == 103){
            img.setImageResource(img103);
        } else if(tileArray[tile] == 104){
            img.setImageResource(img102);
        } else if(tileArray[tile] == 105){
            img.setImageResource(img105);
        } else if(tileArray[tile] == 106){
            img.setImageResource(img106);
        } else if(tileArray[tile] == 107){
            img.setImageResource(img107);
        } else if(tileArray[tile] == 108){
            img.setImageResource(img108);
        } else if(tileArray[tile] == 201){
            img.setImageResource(img201);
        } else if(tileArray[tile] == 202){
            img.setImageResource(img202);
        } else if(tileArray[tile] == 203){
            img.setImageResource(img203);
        } else if(tileArray[tile] == 204){
            img.setImageResource(img204);
        } else if(tileArray[tile] == 205){
            img.setImageResource(img205);
        } else if(tileArray[tile] == 206){
            img.setImageResource(img206);
        } else if(tileArray[tile] == 207){
            img.setImageResource(img207);
        } else if(tileArray[tile] == 208){
            img.setImageResource(img208);
        } // end of if statements

        // checks which tiles are selected and the order of the tile flips
        if(tileNumber == 1){
            firstTile = tileArray[tile];
            if(firstTile > 200){
                firstTile = firstTile-100;
            }
            tileNumber=2;
            clickedFirst = tile;

            img.setEnabled(false);
        } else if(tileNumber == 2){
            secondTile = tileArray[tile];
            if(secondTile > 200){
                secondTile = secondTile-100;
            }
            tileNumber=1;
            clickedSecond = tile;

            tile1.setEnabled(false);
            tile2.setEnabled(false);
            tile3.setEnabled(false);
            tile4.setEnabled(false);
            tile5.setEnabled(false);
            tile6.setEnabled(false);
            tile7.setEnabled(false);
            tile8.setEnabled(false);
            tile9.setEnabled(false);
            tile10.setEnabled(false);
            tile11.setEnabled(false);
            tile12.setEnabled(false);
            tile13.setEnabled(false);
            tile14.setEnabled(false);
            tile15.setEnabled(false);
            tile16.setEnabled(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkTile();
                }
            }, 1000);

        } // end of tile selects

    } // end of flipTile

    public void checkTile(){
        //checks if the tiles are the same, if they are remove them from view
        if(firstTile == secondTile){
            if(clickedFirst == 0){
                tile1.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 1){
                tile2.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 2){
                tile3.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 3){
                tile4.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 4){
                tile5.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 5){
                tile6.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 6){
                tile7.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 7){
                tile8.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 8){
                tile9.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 9){
                tile10.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 10){
                tile11.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 11){
                tile12.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 12){
                tile13.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 13){
                tile14.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 14){
                tile15.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 15){
                tile16.setVisibility(View.INVISIBLE);
            }
            //second tile
            if(clickedSecond == 0){
                tile1.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 1){
                tile2.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 2){
                tile3.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 3){
                tile4.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 4){
                tile5.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 5){
                tile6.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 6){
                tile7.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 7){
                tile8.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 8){
                tile9.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 9){
                tile10.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 10){
                tile11.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 11){
                tile12.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 12){
                tile13.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 13){
                tile14.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 14){
                tile15.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 15){
                tile16.setVisibility(View.INVISIBLE);
            }
        } else{
            tile1.setImageResource(R.drawable.back_tile);
            tile2.setImageResource(R.drawable.back_tile);
            tile3.setImageResource(R.drawable.back_tile);
            tile4.setImageResource(R.drawable.back_tile);
            tile5.setImageResource(R.drawable.back_tile);
            tile6.setImageResource(R.drawable.back_tile);
            tile7.setImageResource(R.drawable.back_tile);
            tile8.setImageResource(R.drawable.back_tile);
            tile9.setImageResource(R.drawable.back_tile);
            tile10.setImageResource(R.drawable.back_tile);
            tile11.setImageResource(R.drawable.back_tile);
            tile12.setImageResource(R.drawable.back_tile);
            tile13.setImageResource(R.drawable.back_tile);
            tile14.setImageResource(R.drawable.back_tile);
            tile15.setImageResource(R.drawable.back_tile);
            tile16.setImageResource(R.drawable.back_tile);
        }// end of if the tiles are equal

        tile1.setEnabled(true);
        tile2.setEnabled(true);
        tile3.setEnabled(true);
        tile4.setEnabled(true);
        tile5.setEnabled(true);
        tile6.setEnabled(true);
        tile7.setEnabled(true);
        tile8.setEnabled(true);
        tile9.setEnabled(true);
        tile10.setEnabled(true);
        tile11.setEnabled(true);
        tile12.setEnabled(true);
        tile13.setEnabled(true);
        tile14.setEnabled(true);
        tile15.setEnabled(true);
        tile16.setEnabled(true);

        checkEnd();
    } // end of checkTile

    public void checkEnd(){
        if(tile1.getVisibility() == View.INVISIBLE &&
                tile2.getVisibility() == View.INVISIBLE &&
                tile3.getVisibility() == View.INVISIBLE &&
                tile4.getVisibility() == View.INVISIBLE &&
                tile5.getVisibility() == View.INVISIBLE &&
                tile6.getVisibility() == View.INVISIBLE &&
                tile7.getVisibility() == View.INVISIBLE &&
                tile8.getVisibility() == View.INVISIBLE &&
                tile9.getVisibility() == View.INVISIBLE &&
                tile10.getVisibility() == View.INVISIBLE &&
                tile11.getVisibility() == View.INVISIBLE &&
                tile12.getVisibility() == View.INVISIBLE &&
                tile13.getVisibility() == View.INVISIBLE &&
                tile14.getVisibility() == View.INVISIBLE &&
                tile15.getVisibility() == View.INVISIBLE &&
                tile16.getVisibility() == View.INVISIBLE){

            Intent i = new Intent(this, MainActivity.class);
            //code for rewards
            startActivity(i);
        }
    } // end of checkEnd

    //back button to main activity
    public void backButton(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}//end of class