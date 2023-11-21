package com.example.iat359_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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

        //setting the display as from the arraylist
        holder.nameTextView.setText(results[0]);
        holder.typeTextView.setText(results[1]);
        holder.itemVarText.setText(results[2]);

//        holder.itemImageView.setImageResource(holder.getResources().getIdentifier(results[3], "drawable", getPackageName()));

        //for item icon
//        int id = getResources().getIdentifier("com.example.iat359:drawable/" + results[3], null, null);
//        holder.itemImageView.setImageResource(id);
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

            //initializing
            nameTextView = (TextView) itemView.findViewById(R.id.itemNameText);
            typeTextView = (TextView) itemView.findViewById(R.id.itemTypeText);
            itemVarText = (TextView) itemView.findViewById(R.id.itemVarText);
            itemImageView = (ImageView) itemView.findViewById(R.id.itemImageView);

//            itemView.setOnClickListener(this);
            buyWearButton.setOnClickListener(this);
            context = itemView.getContext();
            db = new MyDatabase(context);

            //if user is on customization screen, change button text
            if(context instanceof Customization){
                buyWearButton.setText("Wear");
            }
        } // end of MyViewHolder

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.buyWearButton) {
                //actions depending on where the user clicked the button
                if(context instanceof Customization){

                }else if(context instanceof Shop){
                    //if buying an item from the shop, delete the item from the shop and add to the player table
                    db.deleteShopData(nameTextView.getText().toString());
                }
                Toast.makeText(context,"You have clicked " + nameTextView.getText(), Toast.LENGTH_SHORT).show();
            }
        } // end of onClick
    } //end of MyViewHolder extends recycler
} // end of class


