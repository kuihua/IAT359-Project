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

    public long wearItem (String name) {
        db = helper.getWritableDatabase();

        String selection = Constants.NAME + "='" + name + "'";

        String[] columns = {Constants.NAME, Constants.TYPE, Constants.WEARING, Constants.IMAGE};

        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, selection, null, null, null, null);

        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.TYPE);
        int index3 = cursor.getColumnIndex(Constants.WEARING);
        int index4 = cursor.getColumnIndex(Constants.IMAGE);

        ArrayList<String> mArrayList = new ArrayList<String>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String itemName = cursor.getString(index1);
            String itemType = cursor.getString(index2);
            String itemWear= cursor.getString(index3);
            String itemImage = cursor.getString(index4);
            mArrayList.add(itemName);
            mArrayList.add(itemType);
            mArrayList.add(itemWear);
            mArrayList.add(itemImage);
            cursor.moveToNext();
        }

//        // taking off previous item if the item user is trying to wear is the same type
//        // before putting on the desired item
//        ArrayList<String> newArrayList = new ArrayList<String>();
//        //query items of the same type as the item the user is trying to wear
//        String selection2 = Constants.TYPE + "='" + type + "'";
//        Cursor c = db.query(Constants.PLAYER_TABLE_NAME, columns, "Type=?", new String[]{type}, null, null, null);
//        c.moveToFirst();
//        while (!c.isAfterLast()) {
//            int i1 = c.getColumnIndex(Constants.NAME);
//            int i2 = c.getColumnIndex(Constants.TYPE);
//            int i3 = c.getColumnIndex(Constants.WEARING);
//            int i4 = c.getColumnIndex(Constants.IMAGE);
//
//            String itemName = c.getString(i1);
//            String itemType = c.getString(i2);
//            String itemWear= c.getString(i3);
//            String itemImage = c.getString(i4);
//
//            // if the creature was wearing anything of the same type, take it off (false)
//            if (itemWear.equals("True")) {
//                newArrayList.add(itemName);
//                newArrayList.add(itemType);
//                newArrayList.add(itemWear);
//                newArrayList.add(itemImage);
//                ContentValues cv = new ContentValues();
//                cv.put(Constants.NAME, newArrayList.get(0));
//                cv.put(Constants.TYPE, newArrayList.get(1));
//                cv.put(Constants.WEARING, "False");
//                cv.put(Constants.IMAGE, newArrayList.get(3));
//                long id2 = db.update(Constants.PLAYER_TABLE_NAME, cv, Constants.NAME+"=?", new String[]{newArrayList.get(0).toString()} );
//                //after modifying one item, clear the array to make room for the next item
//                newArrayList.clear();
//            }
//            cursor.moveToNext();
//        } // end of cursor


        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, mArrayList.get(1));

        //actions depending if the item is being worn
        if(mArrayList.get(2).toString().equals("False")){
            contentValues.put(Constants.WEARING, "True");
        }else{
            contentValues.put(Constants.WEARING, "False");
        }
        contentValues.put(Constants.IMAGE, mArrayList.get(3));

        //updating whether or not item has been worn
        long id = db.update(Constants.PLAYER_TABLE_NAME, contentValues, Constants.NAME+"=?", new String[]{name} );
        return id;
    }

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


