package com.example.iat359_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MyDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyHelper helper;

    public MyDatabase(Context c){
        context = c;
        helper = new MyHelper(context);
    }

    //method for inserting data into the shop table
    public long insertShopData (String name, String type, String price, String image) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.PRICE, price);
        contentValues.put(Constants.IMAGE, image);
        long id = db.insert(Constants.SHOP_TABLE_NAME, null, contentValues);
        return id;
    }

    public long deleteShopData(String name) {
        // remove an item from the database table
        db = helper.getWritableDatabase();
        String selection = Constants.NAME + "='" + name + "'";

        String[] columns = {Constants.NAME, Constants.TYPE, Constants.PRICE, Constants.IMAGE};

        Cursor cursor = db.query(Constants.SHOP_TABLE_NAME, columns, selection, null, null, null, null);

        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.TYPE);
        int index3 = cursor.getColumnIndex(Constants.PRICE);
        int index4 = cursor.getColumnIndex(Constants.IMAGE);

        ArrayList<String> mArrayList = new ArrayList<String>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String itemName = cursor.getString(index1);
            String itemType = cursor.getString(index2);
            String itemPrice= cursor.getString(index3);
            String itemImage = cursor.getString(index4);
            mArrayList.add(itemName);
            mArrayList.add(itemType);
            mArrayList.add(itemPrice);
            mArrayList.add(itemImage);
            cursor.moveToNext();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, mArrayList.get(1));
        contentValues.put(Constants.WEARING, "False");
        contentValues.put(Constants.IMAGE, mArrayList.get(3));

        //removing an item from shop table, adding the item to the player table
        long id = db.insert(Constants.PLAYER_TABLE_NAME, selection, contentValues);
        long id2 = db.delete(Constants.SHOP_TABLE_NAME, selection, null);

        return id;
    }

    //wear the item
    public long wearItem (String name) {
        db = helper.getWritableDatabase();

        String selection = Constants.NAME + "='" + name + "'";

        String[] columns = {Constants.NAME, Constants.TYPE, Constants.WEARING, Constants.IMAGE};

        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, selection, null, null, null, null);

        //we only need to access the WEARING column
        int index = cursor.getColumnIndex(Constants.WEARING);
        int index2 = cursor.getColumnIndex(Constants.NAME);

        ArrayList<String> mArrayList = new ArrayList<String>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String itemWear = cursor.getString(index);
            String itemName = cursor.getString(index2);
            mArrayList.add(itemWear);
            mArrayList.add(itemName);
            cursor.moveToNext();
        }

        //we are only replacing the Constants.Wearing value
        ContentValues contentValues = new ContentValues();
        //actions depending if the item is being worn
        if(mArrayList.get(0).toString().equals("False") && mArrayList.get(1).toString().equals(name)){
            contentValues.put(Constants.WEARING, "True");
        }else if(mArrayList.get(1).toString().equals(name)){
            contentValues.put(Constants.WEARING, "False");
        }else{
            contentValues.put(Constants.WEARING, "False");
        }

        //updating whether or not the desired item has been worn or taken off
        long id = db.update(Constants.PLAYER_TABLE_NAME, contentValues, Constants.NAME + "=?", new String[]{name});

        return id;
    } // end of wearItem

    //method to take off all items sharing the same type the user is trying to put on
    //this is so it prevents clothes from overlapping when trying to swap outfits (of the same type)
    public long changeItem(String type, String name){
        db = helper.getWritableDatabase();

        //to ensure the selected item is not affected
        String selection = Constants.NAME + "='" + name + "'";
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.WEARING, Constants.IMAGE};
        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, selection, null, null, null, null);

        //we only need to access the WEARING column
        int index = cursor.getColumnIndex(Constants.WEARING);

        ArrayList<String> mArrayList = new ArrayList<String>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String itemWear= cursor.getString(index);
            mArrayList.add(itemWear);
            cursor.moveToNext();
        }

        //changing items of same type to false
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.WEARING, "False");

        //updating any items with the same item type as the user is about to wear to false to prevent image overlap
        long id = db.update(Constants.PLAYER_TABLE_NAME, contentValues, Constants.TYPE+"=?", new String[]{type} );

        //after the item strip happens, revert the selected item back
        ContentValues cv = new ContentValues();
        if(mArrayList.get(0).toString().equals("False")){
            cv.put(Constants.WEARING, "False");
        }else {
            cv.put(Constants.WEARING, "True");
        }
        long id2 = db.update(Constants.PLAYER_TABLE_NAME, cv, Constants.NAME+"=?", new String[]{name} );

        return id;
    } // end of changeItem

    //methods for accessing the shop table and player table
    public Cursor getShopData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.PRICE, Constants.IMAGE};
        Cursor cursor = db.query(Constants.SHOP_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor getPlayerData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.NAME, Constants.TYPE, Constants.WEARING, Constants.IMAGE};
        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    //methods for filtering either the shop table or the player table
    public Cursor getShopQueryData(String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.PRICE, Constants.IMAGE};

        String selection = Constants.TYPE + "='" +type+ "'";  //Constants.TYPE = 'type'
        Cursor cursor = db.query(Constants.SHOP_TABLE_NAME, columns, selection, null, null, null, null);
        return cursor;
    }

    public Cursor getPlayerQueryData(String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.WEARING, Constants.IMAGE};

        String selection = Constants.TYPE + "='" +type+ "'";  //Constants.TYPE = 'type'
        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, selection, null, null, null, null);
        return cursor;
    }

}//end of class


