package com.example.bach0.hustplant.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.bach0.hustplant.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/** Created by bach0 on 4/13/2018. */
public class MapView extends ViewGroup implements MapViewport.Listener {
    MapViewport mViewport;
    List<Place> mPlaceList = new ArrayList<>();
    PathFinder mPathFinder;
    boolean showingDirection = false;
    List<Place> mDirection = new ArrayList<>();
    List<Place> mOther = new ArrayList<>();
    List<Path> mPath = new ArrayList<>();
    Timer mTimer = new Timer();
    Paint mPaint = new Paint();
    List<Point> mPoints = new ArrayList<>();

    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.map_content, this, true);
        mViewport = findViewById(R.id.map_viewport);
        mViewport.setListener(this);
        addPlace(412, 430, R.drawable.ic_menu_slideshow);
        AsyncTask.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inScaled = false;
                        Bitmap routeBitmap =
                                BitmapFactory.decodeResource(
                                        context.getResources(), R.drawable.ic_route, options);
                        int[] routePixels =
                                new int[routeBitmap.getWidth() * routeBitmap.getHeight()];
                        routeBitmap.getPixels(
                                routePixels,
                                0,
                                routeBitmap.getWidth(),
                                0,
                                0,
                                routeBitmap.getWidth(),
                                routeBitmap.getHeight());
                        int[][] routeData =
                                new int[routeBitmap.getHeight()][routeBitmap.getWidth()];
                        for (int i = 0; i < routeBitmap.getWidth(); i++) {
                            for (int j = 0; j < routeBitmap.getHeight(); j++) {
                                routeData[j][i] = routePixels[j * routeBitmap.getWidth() + i];
                            }
                        }
                        mPathFinder = new PathFinder(routeData);
                    }
                });
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
        if (showingDirection) {
            for (Place place : mOther) {
                place.setAlpha(0.3f);
            }
            for (Place place :mDirection) {
                place.setAlpha(1f);
            }
        }
        super.dispatchDraw(canvas);
        if (showingDirection) {
            mPaint.setColor(Color.BLUE);
            Path circle = new Path();
            int circleSize = 6;
            circle.addCircle(0, 0, circleSize * mViewport.getScaleFactor(), Path.Direction.CCW);
            mPaint.setStyle(Paint.Style.STROKE);
            for (int i = 0; i < mPath.size(); i++) {
                if (i == 0) {
                    mPaint.setAlpha(255);
                    mPaint.setPathEffect(
                            new PathDashPathEffect(
                                    circle,
                                    20 * mViewport.getScaleFactor(),
                                    -((System.currentTimeMillis() / 25)
                                            % 20
                                            * mViewport.getScaleFactor()),
                                    PathDashPathEffect.Style.TRANSLATE));
                } else {
                    mPaint.setAlpha(128);
                    if (i == 1) {
                        mPaint.setPathEffect(
                                new PathDashPathEffect(
                                        circle,
                                        20 * mViewport.getScaleFactor(),
                                        0,
                                        PathDashPathEffect.Style.TRANSLATE));
                    }
                }
                Path transformedPath = new Path();
                Matrix matrix = new Matrix();
                Point p = mViewport.viewportToMap(0, 0);
                matrix.postTranslate(-p.x, -p.y);
                matrix.postScale(mViewport.getScaleFactor(), mViewport.getScaleFactor());
                mPath.get(i).transform(matrix, transformedPath);
                canvas.drawPath(transformedPath, mPaint);
            }
        }
        postInvalidate();
    }

    @Override
    public void onViewportChanged(RectF viewport) {
        update();
    }

    public void update() {
        for (Place place : mPlaceList) {
            int size = 60;
            Point topLeft = new Point(place.position.x - size / 2, place.position.y - size);
            Point bottomRight = new Point((place.position.x + size / 2), place.position.y);
            topLeft = mViewport.mapToViewport(topLeft.x, topLeft.y);
            bottomRight = mViewport.mapToViewport(bottomRight.x, bottomRight.y);
            place.setTop(topLeft.y);
            place.setBottom(bottomRight.y);
            place.setLeft(topLeft.x);
            place.setRight(bottomRight.x);
        }
    }

    public Place findNearest(Point pos, int type) {
        Place p = null;
        float distance = Float.MAX_VALUE;
        for (Place place : mPlaceList) {
            if (place.getType() == type && place.distance(pos) < distance) {
                p = place;
                distance = place.distance(pos);
            }
        }
        return p;
    }

    public Place getCurrentPlace() {
        return mPlaceList.get(0);
    }

    public void setCurrentPlace(Point p) {
        mPlaceList.get(0).setPosition(p.x, p.y);
    }

    public void showDirection(List<Place> places) {
        mDirection.clear();
        mDirection.addAll(places);
        mDirection.add(0, mPlaceList.get(0));
        showingDirection = true;
        mOther.clear();
        mOther.addAll(mPlaceList);
        mOther.removeAll(mDirection);
        mTimer.cancel();
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        List<Path> paths = new ArrayList<>();
                        List<Point> p = new ArrayList<>();
                        for (int i = 0; i < mDirection.size() - 1; i++) {
                            Point start =
                                    new Point(
                                            mDirection.get(i).getPosition().x / 2,
                                            mDirection.get(i).getPosition().y / 2);
                            Point end =
                                    new Point(
                                            mDirection.get(i + 1).getPosition().x / 2,
                                            mDirection.get(i + 1).getPosition().y / 2);

                            List<Point> points = mPathFinder.findPath(start, end);
                            p.addAll(points);
                            Path path = new Path();
                            path.moveTo(points.get(0).x * 2, points.get(0).y * 2);
                            for (int j = 1; j < points.size(); j++) {
                                path.lineTo(points.get(j).x * 2, points.get(j).y * 2);
                            }
                            paths.add(path);
                        }
                        mPath = paths;
                        mPoints = p;
                        postInvalidate();
                    }
                },
                0,
                5000);
    }
}
