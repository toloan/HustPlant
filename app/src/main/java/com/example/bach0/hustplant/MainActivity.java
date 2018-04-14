package com.example.bach0.hustplant;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.bach0.hustplant.map.MapView;
import com.example.bach0.hustplant.map.Place;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.DimEffect;
import com.mingle.sweetpick.Effect;
import com.mingle.sweetpick.SweetSheet;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MapView mapView = findViewById(R.id.map_view);
        final Place place1 = mapView.addPlace(266, 388, R.drawable.ic_menu_camera);
        final Place place2 = mapView.addPlace(393, 472, R.drawable.ic_menu_gallery);
        place1.setColor(Color.RED);
        place2.setColor(Color.GREEN);
        final Animation test_anim = AnimationUtils.loadAnimation(this, R.anim.test_anim);
        final Animation test2_anim = AnimationUtils.loadAnimation(this, R.anim.test2_anim);
        test_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                place1.startAnimation(test_anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        test2_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                place2.startAnimation(test2_anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        place1.startAnimation(test_anim);
        place2.startAnimation(test2_anim);
        place1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetSheet sweetSheet = new SweetSheet((ViewGroup)findViewById(R.id.main_layout));
                CustomDelegate customDelegate = new CustomDelegate(true,CustomDelegate.AnimationType.DuangLayoutAnimation);
                View treeInfoView = LayoutInflater.from(MainActivity.this).inflate(R.layout.tree_info_layout,null,false);
                customDelegate.setCustomView(treeInfoView);
                sweetSheet.setDelegate(customDelegate);
                sweetSheet.setBackgroundClickEnable(false);
                sweetSheet.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
