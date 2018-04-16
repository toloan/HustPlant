package com.example.bach0.hustplant;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bach0.hustplant.database.PersonDao;
import com.example.bach0.hustplant.database.entity.Person;
import com.example.bach0.hustplant.database.entity.WaterHistory;

import java.util.ArrayList;
import java.util.List;

/** Created by bach0 on 4/16/2018. */
public class TreeInfoAdapter extends RecyclerView.Adapter {
    private List<WaterHistory> mDataset = new ArrayList<>();

    public void setDataset(List<WaterHistory> mDataset) {
        this.mDataset = mDataset;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TreeInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.water_history_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(
            @NonNull final RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final ViewHolder vh = (ViewHolder) holder;
        final WaterHistory wh = mDataset.get(position);
        AsyncTask.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        PersonDao personDao = App.get().getDatabase().personDao();
                        final Person person = personDao.loadById(wh.getPersonId());
                        new Handler(App.get().getMainLooper())
                                .post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                vh.mName.setText(person.getName());
                                            }
                                        });
                    }
                });
        vh.mAmount.setText(String.format("%.1fml", wh.getWaterAmount()));
        vh.mTime.setText(DateFormat.format("dd-MM-yyyy hh:mm", wh.getDate()));
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mName;
        public TextView mAmount;
        public TextView mTime;

        public ViewHolder(View v) {
            super(v);
            mName = v.findViewById(R.id.water_history_name);
            mAmount = v.findViewById(R.id.water_history_amount);
            mTime = v.findViewById(R.id.water_history_time);
        }
    }
}
