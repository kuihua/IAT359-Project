package com.example.iat359_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
        holder.typeTextView.setText("");
        holder.itemVarText.setText(results[2]);

        //for item icon
//        int id = this.getResources().getIdentifier("com.example.iat359:drawable/" + results[3], null, null);
//        holder.itemImageView.setImageResource(id);
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

//            int id = context.getResources().getIdentifier("com.example.iat359:drawable/" + results[3], null, null);
//            itemImageView.setImageResource(id);

            //changing button text based on the activity the user is in
            if(context instanceof Shop){
                buyWearButton.setText("Buy");
            }
        } // end of my view holder view

        public void setImage(String name) {
            String file = name.toLowerCase();
            file = file.replace(" ", "_");
            file = file + "_icon";
            int id = context.getResources().getIdentifier(file, "drawable", context.getPackageName());
            itemImageView.setImageResource(id);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.buyWearButton) {

                if(context instanceof Customization){
                    db.wearItem(nameTextView.getText().toString());
                    ((Customization)context).recreate();
//                    not working as intended, add if time
//                    //change button text depending if item is worn
//                    if(buyWearButton.getText().toString().equals("Wear")){
//                        buyWearButton.setText("Worn");
//                    }else{
//                        buyWearButton.setText("Wear");
//                    }
                }else if(context instanceof Shop){
                    db.deleteShopData(nameTextView.getText().toString());
                    ((Shop)context).recreate();
                }
            }
        }//end of onClick
    } // end of my view holder with the on click listener
} // end of custom adapter


