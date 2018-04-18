package com.example.bach0.hustplant.Helper;

import android.content.Context;
import android.media.MediaPlayer;


import com.example.bach0.hustplant.R;

import java.net.URI;

public class AudioVoice  {
    public MediaPlayer StopWaterRing(Context context){
        return MediaPlayer.create(context, R.raw.ket_thuc_tuoi);
    }

    public MediaPlayer StartWaterRing(Context context){
        return MediaPlayer.create(context, R.raw.bat_dau_tuoi);
    }

    public MediaPlayer StopRing(Context context){
        return MediaPlayer.create(context, R.raw.ket_thuc_hanh_trinh);
    }

    public MediaPlayer GoStraightRing(Context context){
        return MediaPlayer.create(context, R.raw.di_thang);
    }
    public MediaPlayer OutOfWater(Context context){
        return MediaPlayer.create(context,R.raw.het_nuoc);

    }
}
