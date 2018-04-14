package com.example.bach0.hustplant.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.bach0.hustplant.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by bach0 on 4/13/2018.
 */

public class MapView extends ViewGroup implements MapViewport.Listener {
    MapViewport mViewport;
    List<Place> mPlaceList = new ArrayList<>();
    PathFinder mPathFinder;
    List<Point> mPath;

    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.map_content, this, true);
        mViewport = findViewById(R.id.map_viewport);
        mViewport.setListener(this);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap routeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable
                .ic_route, options);
        int[] routePixels = new int[routeBitmap.getWidth() * routeBitmap.getHeight()];
        routeBitmap.getPixels(routePixels, 0, routeBitmap.getWidth(), 0, 0, routeBitmap.getWidth
                (), routeBitmap.getHeight());
        int[][] routeData = new int[routeBitmap.getHeight()][routeBitmap.getWidth()];
        for (int i = 0; i < routeBitmap.getWidth(); i++) {
            for (int j = 0; j < routeBitmap.getHeight(); j++) {
                routeData[j][i] = routePixels[j * routeBitmap.getWidth() + i];
            }
        }
        mPathFinder = new PathFinder(routeData);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        mViewport.layout(i, i1, i2, i3);
    }

    public Place addPlace(final int x, final int y, final int resource) {
        Place place = new Place(getContext());
        place.setPosition(x, y);
        place.setIcon(resource);
        mPlaceList.add(place);
        addView(place);
        return place;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
//        Paint paint = new Paint();
//        paint.setColor(Color.BLUE);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeWidth(20);
//        for (Point p : mPath) {
//            Point viewport = mViewport.mapToViewport(p.x*2, p.y*2);
//            canvas.drawPoint(viewport.x, viewport.y, paint);
//        }
    }

    @Override
    public void onViewportChanged(RectF viewport) {
        for (Place place : mPlaceList) {
            int size = 60;
            Point topLeft = new Point(place.position.x - size / 2, place.position.y - size);
            Point bottomRight = new Point((place.position.x + size / 2), place.position.y);
            Log.d(TAG, "onViewportChanged: " + topLeft + " " + bottomRight);
            topLeft = mViewport.mapToViewport(topLeft.x, topLeft.y);
            bottomRight = mViewport.mapToViewport(bottomRight.x, bottomRight.y);
            place.setTop(topLeft.y);
            place.setBottom(bottomRight.y);
            place.setLeft(topLeft.x);
            place.setRight(bottomRight.x);
        }
    }
}
