package com.example.bach0.hustplant.Setting;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.example.bach0.hustplant.R;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle(R.string.action_settings);
        setSupportActionBar(toolbar);
        FrameLayout layout= (FrameLayout)findViewById(R.id.setting_content);
        SettingFragment fragment=new SettingFragment();
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.add(R.id.setting_content,fragment);
        transaction.commit();
    }
}
