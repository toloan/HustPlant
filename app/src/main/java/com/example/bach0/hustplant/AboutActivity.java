package com.example.bach0.hustplant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RelativeLayout re=(RelativeLayout)findViewById(R.id.about_content);
        Intent intent = getIntent();
        String value=intent.getStringExtra("Value");
        LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (value){
            case "About":
                re.addView(getLayoutInflater()
                        .inflate(R.layout.about, re, false));
                toolbar.setTitle("About");
                break;
            case "Policy":
                re.addView(getLayoutInflater()
                        .inflate(R.layout.policy, re, false));
                toolbar.setTitle("Policy");
                break;
            default:
                break;
        }

    }

}
