package com.example.iat359_project;

import static com.example.iat359_project.MainActivity.DEFAULT;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<String> list;
    private String itemData;

    public CustomAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomAdapter.MyViewHolder holder, int position) {
        //getting item data
        String[]  results = (list.get(position).split(","));

        itemData = list.get(position).toString();
        holder.nameTextView.setText(results[0]);
        holder.typeTextView.setText(results[1]);
        holder.itemVarText.setText(results[2]);

        holder.setImage(results[0]);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView, typeTextView, itemVarText;
        public ImageView itemImageView;
        public LinearLayout myLayout;
        public Button buyWearButton;
        Context context;
        public MyDatabase db;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;
            buyWearButton = (Button) itemView.findViewById(R.id.buyWearButton);

            nameTextView = (TextView) itemView.findViewById(R.id.itemNameText);
            typeTextView = (TextView) itemView.findViewById(R.id.itemTypeText);
            itemVarText = (TextView) itemView.findViewById(R.id.itemVarText);
            itemImageView = (ImageView) itemView.findViewById(R.id.itemImageView);

            buyWearButton.setOnClickListener(this);
            context = itemView.getContext();
            db = new MyDatabase(context);

            //changing button text based on the activity the user is in
            if(context instanceof Shop){
                buyWearButton.setText("Buy");
            }
            if (context instanceof Customization) {
                //hide this column
                itemVarText.setVisibility(View.GONE);
            }
        } // end of my view holder view

        // to set the images in the recycler view
        public void setImage(String name) {
            String file = name.toLowerCase();
            file = file.replace(" ", "_");
            file = file + "_icon";
            int id = context.getResources().getIdentifier(file, "drawable", context.getPackageName());
            itemImageView.setImageResource(id);
        } // end of setImage

        // button function for the recycler view
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.buyWearButton) {
                if(context instanceof Customization){
                    // clothing sfx
                    MediaPlayer mp = MediaPlayer.create(context, R.raw.cloth);
                    mp.start();
                    // wear item
                    db.changeItem(typeTextView.getText().toString(), nameTextView.getText().toString());
                    db.wearItem(nameTextView.getText().toString());
                    ((Customization)context).recreate();
                }else if(context instanceof Shop){
                    // tap sfx
                    MediaPlayer mp = MediaPlayer.create(context, R.raw.tap);
                    mp.start();
                    // checks if user has enough money, then buy the item
                    SharedPreferences sharedPref = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
                    int currentMoney = sharedPref.getInt("coin", 100);
                    int cost = Integer.parseInt(itemVarText.getText().toString());
                    if(currentMoney >= cost){
                        mp = MediaPlayer.create(context, R.raw.coin_drop);
                        mp.start();
                        // removes bought item from the shop table
                        db.deleteShopData(nameTextView.getText().toString());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        int spent = currentMoney-cost;
                        editor.putInt("coin", spent);
                        Toast.makeText(context, "Spent "+ cost, Toast.LENGTH_SHORT).show();
                        editor.commit();
                    }else{
                        Toast.makeText(context, "Not enough coins.", Toast.LENGTH_SHORT).show();
                    }
                    ((Shop)context).recreate();
                }
            }
        }//end of onClick
    } // end of my view holder with the on click listener
} // end of custom adapter


