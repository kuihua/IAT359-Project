package com.example.iat359_project;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MainMusicService extends Service {

    MediaPlayer mp;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        mp = MediaPlayer.create(this, R.raw.main_bgm);
        //have the bgm loop
        mp.setLooping(true);
        mp.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean stopService(Intent intent){
        mp.setLooping(false);
        mp.stop();
        return super.stopService(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mp.stop();
        mp.release();
        mp = null;
    }

} // end of class
