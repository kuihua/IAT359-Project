package com.example.iat359_project;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyHelper extends SQLiteOpenHelper {

    private Context context;

    //sql for creating the shop table
    private static final String CREATE_SHOP_TABLE =
            "CREATE TABLE "+
                    Constants.SHOP_TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.NAME + " TEXT, " +
                    Constants.TYPE + " TEXT, " +
                    Constants.PRICE + " TEXT, " +
                    Constants.IMAGE + " TEXT);" ;

    //sql for creating the player table, for player's inventory
    private static final String CREATE_PLAYER_TABLE =
            "CREATE TABLE "+
                    Constants.PLAYER_TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.NAME + " TEXT, " +
                    Constants.TYPE + " TEXT, " +
                    Constants.WEARING + " TEXT, " +
                    Constants.IMAGE + " TEXT);" ;

    //if the tables exist, don't create another one
    private static final String DROP_SHOP_TABLE = "DROP TABLE IF EXISTS " + Constants.SHOP_TABLE_NAME;
    private static final String DROP_PLAYER_TABLE = "DROP TABLE IF EXISTS " + Constants.PLAYER_TABLE_NAME;


    public MyHelper(Context context){
        super (context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //executing the sql queries
            db.execSQL(CREATE_SHOP_TABLE);
            db.execSQL(CREATE_PLAYER_TABLE);
//            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
//            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            //executing the sql queries
            db.execSQL(DROP_SHOP_TABLE);
            db.execSQL(DROP_PLAYER_TABLE);
            onCreate(db);
//            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
//            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}

