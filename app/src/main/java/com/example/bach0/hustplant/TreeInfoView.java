package com.example.bach0.hustplant;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.example.bach0.hustplant.database.entity.Plant;
import com.example.bach0.hustplant.database.entity.WaterHistory;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/** Created by bach0 on 4/15/2018. */
public class TreeInfoView {
  BottomSheetBehavior sheetBehavior;
  TextView titleView;
  TextView subtitleView;
  TextView statusView;
  TreeInfoAdapter mAdapter = new TreeInfoAdapter();
  CircleImageView imageView;

  public TreeInfoView(View view) {
    sheetBehavior = BottomSheetBehavior.from(view);
    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    titleView = view.findViewById(R.id.tree_title);
    subtitleView = view.findViewById(R.id.tree_subtitle);
    statusView = view.findViewById(R.id.status_text);
    imageView = view.findViewById(R.id.tree_image);
    RecyclerView rv = view.findViewById(R.id.water_history);
    rv.setAdapter(mAdapter);
    rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
  }

  void show() {
    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
  }

  void hide() {
    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
  }

  void setTree(final Plant plant) {
    titleView.setText(plant.getName());
    String status = "Healthy";
    if (plant.getWaterLevel() / plant.getTargetWaterLevel() < 0.5) {
      status = "Need watering";
    } else if (plant.getWaterLevel() / plant.getTargetWaterLevel() < 0.1) {
      status = "Dead";
    }
    Spannable wordToSpan = new SpannableString("Status: " + status);
    if (status.equals("Need watering")) {
      wordToSpan.setSpan(
          new ForegroundColorSpan(Color.BLUE),
          8,
          8 + status.length(),
          Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    } else if (status.equals("Healthy")) {
      wordToSpan.setSpan(
          new ForegroundColorSpan(0XFF00AA00),
          8,
          8 + status.length(),
          Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    statusView.setText(wordToSpan);
    imageView.setImageDrawable(imageView.getResources().getDrawable(plant.getResourceId()));
    AsyncTask.execute(
        new Runnable() {
          @Override
          public void run() {
            final List<WaterHistory> waterHistories =
                App.get().getDatabase().waterHistoryDao().findByPlant(plant.getId());
            new Handler(App.get().getMainLooper())
                .post(
                    new Runnable() {
                      @Override
                      public void run() {
                        mAdapter.setDataset(waterHistories);
                      }
                    });
          }
        });
  }

  void setDistance(float distance) {
    subtitleView.setText(String.format("%.1fm", distance));
  }
}
