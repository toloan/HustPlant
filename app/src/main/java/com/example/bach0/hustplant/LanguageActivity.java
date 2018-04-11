package com.example.bach0.hustplant;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class LanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Language");
        final LinearLayout english=(LinearLayout)findViewById(R.id.en);
        final LinearLayout vi=(LinearLayout)findViewById(R.id.vn);
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.tick_en).setVisibility(View.VISIBLE);
                vi.findViewById(R.id.tick_vn).setVisibility(View.INVISIBLE);

            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.tick_vn).setVisibility(View.VISIBLE);
                english.findViewById(R.id.tick_en).setVisibility(View.INVISIBLE);
            }
        });
    }

}
