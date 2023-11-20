package com.example.iat359_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyPlayerDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyPlayerHelper helper;

    public MyPlayerDatabase(Context c){
        context = c;
        helper = new MyPlayerHelper(context, Constants.PLAYER_DATABASE_NAME, Constants.PLAYER_DATABASE_VERSION);
    }

    public long insertData (String name, String type, String location, String latinName)
    {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.IMAGE, location);
        contentValues.put(Constants.WEARING, latinName);
        long id = db.insert(Constants.PLAYER_TABLE_NAME, null, contentValues);
        return id;
    }

    public Cursor getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.IMAGE, Constants.WEARING};
        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }


    public String getSelectedData(String type)
    {
        //select plants from database of type 'herb'
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE, Constants.WEARING};

        String selection = Constants.TYPE + "='" +type+ "'";  //Constants.TYPE = 'type'
        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, selection, null, null, null, null);

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {

            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.IMAGE);
            int index4 = cursor.getColumnIndex(Constants.WEARING);
            String plantName = cursor.getString(index1);
            String plantType = cursor.getString(index2);
            String plantLocation = cursor.getString(index3);
            String plantLatinName = cursor.getString(index4);
            buffer.append(plantName + " " + plantType + " " + plantLocation + " " + plantLatinName + "\n");
        }
        return buffer.toString();
    }

    public Cursor getQueryData(String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE, Constants.WEARING};

        String selection = Constants.TYPE + "='" +type+ "'";  //Constants.TYPE = 'type'
        Cursor cursor = db.query(Constants.PLAYER_TABLE_NAME, columns, selection, null, null, null, null);
        return cursor;
    }


}

