package com.example.bach0.hustplant;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bach0.hustplant.database.PlantDao;
import com.example.bach0.hustplant.database.WaterDao;
import com.example.bach0.hustplant.map.Place;
import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureViewHolder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/** Created by bach0 on 4/16/2018. */
public class DestinationAdapter extends GestureAdapter<Place, DestinationAdapter.ViewHolder>
        implements GestureAdapter.OnDataChangeListener<Place> {
    TextView distance;
    View view;
    Place current;

    public DestinationAdapter(TextView distance, View view, Place current) {
        setDataChangeListener(this);
        this.distance = distance;
        this.view = view;
        this.current = current;
    }

    @Override
    public void setData(List<Place> data) {
        super.setData(data);
        distance.setText("" + current.distance(getItem(0).getPosition()) + "m");
    }

    @Override
    public boolean add(Place item) {
        boolean result = super.add(item);
        if (getItemCount() > 0) {
            distance.setText("" + current.distance(getItem(0).getPosition()) + "m");
        }
        notifyItemChanged(getItemCount() - 2);
        return result;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DestinationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.destination_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(
            @NonNull final DestinationAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        distance.setText("" + current.distance(getItem(0).getPosition()) + "m");
        final ViewHolder vh = holder;
        final Place current = getData().get(position);
        if (position == getItemCount() - 1) {
            holder.mDistanceView.setVisibility(View.GONE);
        } else {
            holder.mDistanceView.setVisibility(View.VISIBLE);
            Place next = getData().get(position + 1);
            holder.mDistance.setText("" + current.distance(next.getPosition()) + "m");
        }
        holder.mIcon.setImageDrawable(current.getIcon());
        AsyncTask.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        if (current.getType() == 1) {
                            final PlantDao plantDao = App.get().getDatabase().plantDao();
                            final String name = plantDao.loadById(current.getId()).getName();
                            new Handler(App.get().getMainLooper())
                                    .post(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    holder.mName.setText(name);
                                                }
                                            });
                        } else if (current.getType() == 2) {
                            final WaterDao waterDao = App.get().getDatabase().waterDao();
                            final Point name = waterDao.loadById(current.getId()).getPosition();
                            new Handler(App.get().getMainLooper())
                                    .post(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    holder.mName.setText(
                                                            "Water (" + name.x + "," + name.y
                                                                    + ")");
                                                }
                                            });
                        }
                    }
                });
    }

    @Override
    public void onItemRemoved(Place item, int position) {
        if (getItemCount() == 0) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            distance.setText("" + current.distance(getItem(0).getPosition()) + "m");
        }
    }

    @Override
    public void onItemReorder(Place item, int fromPos, int toPos) {
        notifyItemChanged(fromPos);
        notifyItemChanged(toPos);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends GestureViewHolder {
        // each data item is just a string in this case
        public TextView mName;
        public TextView mDistance;
        public CircleImageView mIcon;
        public View mDistanceView;

        public ViewHolder(View v) {
            super(v);
            mName = v.findViewById(R.id.destination_name);
            mDistance = v.findViewById(R.id.distance);
            mIcon = v.findViewById(R.id.destination_icon);
            mDistanceView = v.findViewById(R.id.distance_view);
        }

        @Override
        public boolean canDrag() {
            return true;
        }

        @Override
        public boolean canSwipe() {
            return true;
        }
    }
}
