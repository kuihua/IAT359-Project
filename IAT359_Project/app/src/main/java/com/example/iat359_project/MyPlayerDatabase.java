package com.example.iat359_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class MyPlayerDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyPlayerHelper helper;

    public MyPlayerDatabase(Context c){
        context = c;
        helper = new MyPlayerHelper(context);
    }

    //getting the size of the player database
    public long getCount(){
        File f = context.getDatabasePath(Constants.PLAYER_TABLE_NAME);
        long dbSize = f.length();

        return dbSize;
    }

    public long insertData (String name, String type, String wearing, String image)
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

    public Cursor getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.WEARING, Constants.IMAGE};
        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }


    public String getSelectedData(String type)
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

    public Cursor getQueryData(String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.WEARING, Constants.IMAGE};

        String selection = Constants.TYPE + "='" +type+ "'";  //Constants.TYPE = 'type'
        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, selection, null, null, null, null);
        return cursor;
    }


}

