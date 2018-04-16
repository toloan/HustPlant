package com.example.bach0.hustplant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.bach0.hustplant.Setting.Setting;
import com.example.bach0.hustplant.database.PlantDao;
import com.example.bach0.hustplant.database.entity.Plant;
import com.example.bach0.hustplant.database.entity.Water;
import com.example.bach0.hustplant.map.MapView;
import com.example.bach0.hustplant.map.Place;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MapView mMapView;
    private TreeInfoView treeInfoView;
    private CustomizeView customizeView;

    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    private void addAllWaters() {
        for (final Water water : App.get().getDatabase().waterDao().getAll()) {
            new Handler(App.get().getMainLooper())
                    .post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    final Place place =
                                            mMapView.addPlace(
                                                    water.getPosition().x,
                                                    water.getPosition().y,
                                                    R.drawable.ic_menu_manage);
                                    place.setId(water.getId());
                                    place.setType(2);
                                    place.setColor(Color.BLUE);
                                    place.setOnClickListener(
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (customizeView.sheetBehavior.getState()
                                                            == BottomSheetBehavior
                                                                    .STATE_COLLAPSED) {
                                                        customizeView.addPlace(place);
                                                        customizeView.show();
                                                    }
                                                }
                                            });
                                }
                            });
        }
    }

    private void addAllPlants() {
        for (final Plant plant : App.get().getDatabase().plantDao().getAll()) {
            new Handler(App.get().getMainLooper())
                    .post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    final Place place =
                                            mMapView.addPlace(
                                                    plant.getPosition().x,
                                                    plant.getPosition().y,
                                                    plant.getResourceId());
                                    place.setType(1);
                                    place.setId(plant.getId());
                                    place.setColor(
                                            blendColors(
                                                    Color.GREEN,
                                                    Color.RED,
                                                    plant.getWaterLevel()
                                                            / plant.getTargetWaterLevel()));
                                    place.setOnClickListener(
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Animation anim =
                                                            AnimationUtils.loadAnimation(
                                                                    MainActivity.this,
                                                                    R.anim.test2_anim);
                                                    place.startAnimation(anim);
                                                    if (customizeView.sheetBehavior.getState()
                                                            != BottomSheetBehavior
                                                                    .STATE_COLLAPSED) {

                                                        treeInfoView.setTree(plant);
                                                        treeInfoView.show();
                                                        customizeView.hide();
                                                        List<Place> places = new ArrayList<>();
                                                        places.add(
                                                                mMapView.findNearest(
                                                                        plant.getPosition(), 2));
                                                        places.add(place);
                                                        customizeView.setPlaceList(places);
                                                    } else {
                                                        customizeView.addPlace(place);
                                                        customizeView.show();
                                                    }
                                                }
                                            });
                                }
                            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        treeInfoView.show();
                    }
                });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawer,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mMapView = findViewById(R.id.map_view);
        treeInfoView = new TreeInfoView(findViewById(R.id.tree_info_layout));
        customizeView =
                new CustomizeView(findViewById(R.id.customize_layout), mMapView.getCurrentPlace());
        AsyncTask.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        addAllPlants();
                        addAllWaters();
                        new Handler(App.get().getMainLooper())
                                .post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                mMapView.update();
                                            }
                                        });
                    }
                });
        final Button customizeBtn = findViewById(R.id.customize_btn);
        customizeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        treeInfoView.hide();
                        customizeView.show();
                    }
                });
        Button addDestinationBtn = findViewById(R.id.add_destination_btn);
        addDestinationBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customizeView.sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
        navigationView.setCheckedItem(R.id.map);

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

        if (id == R.id.map) {
            // Handle the camera action
        } else if (id == R.id.setting) {
            Intent intent=new Intent(this, Setting.class);
            startActivity(intent);
        } else if (id == R.id.history) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
