package com.example.bach0.hustplant;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.example.bach0.hustplant.map.Place;
import com.thesurix.gesturerecycler.GestureManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/** Created by bach0 on 4/16/2018. */
public class CustomizeView {
    BottomSheetBehavior sheetBehavior;
    TextView totalDistanceView;
    TextView subtitleView;
    TextView statusView;
    DestinationAdapter mAdapter;
    CircleImageView imageView;

    public CustomizeView(View view, Place current) {
        mAdapter =
                new DestinationAdapter(
                        (TextView) view.findViewById(R.id.distance),
                        view.findViewById(R.id.distance_view),
                        (TextView) view.findViewById(R.id.total_distance),
                        current);
        sheetBehavior = BottomSheetBehavior.from(view);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setSkipCollapsed(true);
        totalDistanceView = view.findViewById(R.id.total_distance);
        RecyclerView rv = view.findViewById(R.id.destination_list);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        new GestureManager.Builder(rv)
                // Enable swipe
                .setSwipeEnabled(true)
                // Enable long press drag and drop
                .setLongPressDragEnabled(true)
                // Enable manual drag from the beginning, you need to provide View inside
                // your GestureViewHolder
                //                .setManualDragEnabled(true)
                // Use custom gesture flags
                // Do not use those methods if you want predefined flags for RecyclerView
                // layout manager
                .setSwipeFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
                .setDragFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN)
                .build();
    }

    void addPlace(Place place) {
        mAdapter.add(place);
    }

    List<Place> getPlaceList() {
        return mAdapter.getData();
    }

    void setPlaceList(List<Place> places) {
        mAdapter.setData(places);
    }

    void show() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    void hide() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}
