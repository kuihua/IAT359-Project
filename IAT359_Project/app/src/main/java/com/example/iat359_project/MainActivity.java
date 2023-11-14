package com.example.iat359_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    int affection=0;
    int currency=100;

    int main_bg;

    public static final enum ImageView.ScaleType extends Enum<ImageView.ScaleType>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView.main_bg(FIT_XY);
    }
}