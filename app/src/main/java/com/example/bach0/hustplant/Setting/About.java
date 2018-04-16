package com.example.bach0.hustplant.Setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.bach0.hustplant.R;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Intent intent=getIntent();
        String type=intent.getDataString();
        ((RelativeLayout)findViewById(R.id.policy_and_about)).addView(getLayoutInflater().inflate(R.layout.about, null));

    }
}
