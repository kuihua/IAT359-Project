package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Naming extends AppCompatActivity {

    EditText petNameEdit;
    boolean rename=false;
    public static final boolean DEFAULT = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        boolean first = sharedPrefs.getBoolean("firstTime", DEFAULT);
        if (!first && !rename) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        petNameEdit = (EditText)findViewById(R.id.petNameEdit);

    }//end of onCreate

    public void name (View view){
        rename=false;
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("petName", petNameEdit.getText().toString());
        Toast.makeText(this, "Your pet's name was saved to Preferences", Toast.LENGTH_LONG).show();
        editor.commit();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }//end of naming the pet

}//end of activity