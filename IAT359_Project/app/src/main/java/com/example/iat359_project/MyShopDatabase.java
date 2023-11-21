package com.example.iat359_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyShopDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyShopHelper shophelper;

    public MyShopDatabase(Context c){
        context = c;
        shophelper = new MyShopHelper(context);
    }

    public long insertData (String name, String type, String price, String image)
    {
        db = shophelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.PRICE, price);
        contentValues.put(Constants.IMAGE, image);
        long id = db.insert(Constants.SHOP_TABLE_NAME, null, contentValues);
        return id;
    }

    public Cursor getData()
    {
        SQLiteDatabase db = shophelper.getWritableDatabase();

        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.PRICE, Constants.IMAGE};
        Cursor cursor = db.query(Constants.SHOP_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }


    public String getSelectedData(String type)
    {
        //select plants from database of type 'herb'
        SQLiteDatabase db = shophelper.getWritableDatabase();
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

    public Cursor getQueryData(String type) {
        SQLiteDatabase db = shophelper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.PRICE, Constants.IMAGE};

        String selection = Constants.TYPE + "='" +type+ "'";  //Constants.TYPE = 'type'
        Cursor cursor = db.query(Constants.SHOP_TABLE_NAME, columns, selection, null, null, null, null);
        return cursor;
    }


}


