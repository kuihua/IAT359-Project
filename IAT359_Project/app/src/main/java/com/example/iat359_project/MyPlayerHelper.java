package com.example.iat359_project;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyPlayerHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String CREATE_PLAYER_TABLE =
            "CREATE TABLE "+
                    Constants.PLAYER_TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.NAME + " TEXT, " +
                    Constants.TYPE + " TEXT, " +
                    Constants.IMAGE + " TEXT, " +
                    Constants.WEARING + " TEXT);" ;

    private static final String DROP_PLAYER_TABLE = "DROP TABLE IF EXISTS " + Constants.PLAYER_TABLE_NAME;

    public MyPlayerHelper(Context context){
        super (context, Constants.PLAYER_TABLE_NAME, null, Constants.PLAYER_DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_PLAYER_TABLE);
            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_PLAYER_TABLE);
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}


