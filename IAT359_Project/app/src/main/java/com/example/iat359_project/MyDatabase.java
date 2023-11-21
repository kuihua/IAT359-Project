package com.example.iat359_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MyDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyHelper helper;

    public MyDatabase(Context c){
        context = c;
        helper = new MyHelper(context);
    }

    public long insertShopData (String name, String type, String price, String image)
    {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.PRICE, price);
        contentValues.put(Constants.IMAGE, image);
        long id = db.insert(Constants.SHOP_TABLE_NAME, null, contentValues);
        return id;
    }

    public long insertPlayerData (String name, String type, String wearing, String image)
    {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.WEARING, wearing);
        contentValues.put(Constants.IMAGE, image);
        long id = db.insert(Constants.PLAYER_TABLE_NAME, null, contentValues);
        return id;
    }

    public long deleteShopData(String name) {
        // remove a creature from the database table
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
//            String s = itemName +"," + itemType + "," + itemPrice + "," + itemImage;
//            mArrayList.add(s);
            cursor.moveToNext();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, mArrayList.get(1));
        contentValues.put(Constants.WEARING, "False");
        contentValues.put(Constants.IMAGE, mArrayList.get(3));

        long id = db.insert(Constants.PLAYER_TABLE_NAME, selection, contentValues);
        long id2 = db.delete(Constants.SHOP_TABLE_NAME, selection, null);

        return id;
    }

    public Cursor getShopData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.PRICE, Constants.IMAGE};
        Cursor cursor = db.query(Constants.SHOP_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }


    public Cursor getPlayerData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.WEARING, Constants.IMAGE};
        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }


    public String getShopSelectedData(String type)
    {
        //select plants from database of type 'herb'
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.PRICE, Constants.IMAGE};

        String selection = Constants.TYPE + "='" +type+ "'";  //Constants.TYPE = 'type'
        Cursor cursor = db.query(Constants.SHOP_TABLE_NAME, columns, selection, null, null, null, null);

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {

            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.PRICE);
            int index4 = cursor.getColumnIndex(Constants.IMAGE);
            String itemName = cursor.getString(index1);
            String itemType = cursor.getString(index2);
            String itemImage = cursor.getString(index3);
            String itemPrice = cursor.getString(index4);
            buffer.append(itemName + " " + itemType + " " + itemPrice + " " + itemImage + "\n");
        }
        return buffer.toString();
    }

    public String getPlayerSelectedData(String type)
    {
        //select plants from database of type 'herb'
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.WEARING, Constants.IMAGE};

        String selection = Constants.TYPE + "='" +type+ "'";  //Constants.TYPE = 'type'
        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, selection, null, null, null, null);

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {

            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.WEARING);
            int index4 = cursor.getColumnIndex(Constants.IMAGE);
            String itemName = cursor.getString(index1);
            String itemType = cursor.getString(index2);
            String itemImage = cursor.getString(index3);
            String itemWearing = cursor.getString(index4);
            buffer.append(itemName + " " + itemType + " " + itemWearing + " " + itemImage + "\n");
        }
        return buffer.toString();
    }

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


