package com.example.bach0.hustplant.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.bach0.hustplant.R;

public class About extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);

    Intent intent = getIntent();
    String type = intent.getDataString();
    RelativeLayout relay = ((RelativeLayout) findViewById(R.id.policy_and_about));
    switch (type) {
      case "about":
        relay.addView(getLayoutInflater().inflate(R.layout.about, null));
        toolbar.setTitle(R.string.about);
        break;
      case "licence":
        relay.addView(getLayoutInflater().inflate(R.layout.licence, null));
        toolbar.setTitle(R.string.licence);
        break;
      default:
        break;
    }
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // handle arrow click here
    if (item.getItemId() == android.R.id.home) {
      finish(); // close this activity and return to preview activity (if there is any)
    }

    return super.onOptionsItemSelected(item);
  }
}
