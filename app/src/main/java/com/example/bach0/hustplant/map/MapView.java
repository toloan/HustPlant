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
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bach0.hustplant.MainActivity;
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
  ImageView waterButton;
  TextView waterText;
  boolean blinking = false;
  float water = 0f;
  FrameLayout overlayLayout;

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
    addPlace(412, 430, R.drawable.icons8_user_80);
    waterButton = new ImageView(context);
    waterButton.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View v) {
            if (showingDirection) {
              mDirection.add(1, findNearest(mDirection.get(0).getPosition(), 2));
            }
          }
        });
    waterText = new TextView(context);
    this.addView(waterButton);
    this.addView(waterText);
    waterButton.setBackgroundResource(R.drawable.ic_water_can);
    AsyncTask.execute(
        new Runnable() {
          @Override
          public void run() {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            Bitmap routeBitmap =
                BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_route, options);
            int[] routePixels = new int[routeBitmap.getWidth() * routeBitmap.getHeight()];
            routeBitmap.getPixels(
                routePixels,
                0,
                routeBitmap.getWidth(),
                0,
                0,
                routeBitmap.getWidth(),
                routeBitmap.getHeight());
            int[][] routeData = new int[routeBitmap.getHeight()][routeBitmap.getWidth()];
            for (int i = 0; i < routeBitmap.getWidth(); i++) {
              for (int j = 0; j < routeBitmap.getHeight(); j++) {
                routeData[j][i] = routePixels[j * routeBitmap.getWidth() + i];
              }
            }
            mPathFinder = new PathFinder(routeData);
          }
        });
    mTimer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            if (!showingDirection) return;
            List<Path> paths = new ArrayList<>();
            for (int i = 0; i < mDirection.size() - 1; i++) {
              Point start =
                  new Point(
                      (int) (mDirection.get(i).getPosition().x / 2),
                      (int) (mDirection.get(i).getPosition().y / 2));
              Point end =
                  new Point(
                      (int) (mDirection.get(i + 1).getPosition().x / 2),
                      (int) (mDirection.get(i + 1).getPosition().y / 2));

              List<Point> points = mPathFinder.findPath(start, end);
              Path path = new Path();
              if (points != null && points.size() > 0) {
                path.moveTo(points.get(0).x * 2, points.get(0).y * 2);
                for (int j = 1; j < points.size(); j++) {
                  path.lineTo(points.get(j).x * 2, points.get(j).y * 2);
                }
              }
              paths.add(path);
            }
            mPath = paths;
            postInvalidate();
          }
        },
        0,
        1000);
  }

  public FrameLayout getOverlayLayout() {
    return overlayLayout;
  }

  public void setOverlayLayout(FrameLayout overlayLayout) {
    this.overlayLayout = overlayLayout;
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
      for (Place place : mDirection) {
        place.setAlpha(1f);
      }
    }
    super.dispatchDraw(canvas);
    if (showingDirection) {
      if (mDirection.size() > 1
          && mDirection.get(0).distance(mDirection.get(1).getPosition()) < 15f) {
        if (mDirection.get(1).getType() == 2 && water > 0.2f) {
          mDirection.remove(1);
        } else if (mDirection.get(1).getType() == 1) {
          if (mDirection.get(1).getValue() > mDirection.get(1).getMaxValue() * 0.9) {
            mDirection.remove(1);
          }
        }
      }
      if (findNearest(mDirection.get(0).getPosition(), 2).distance(mDirection.get(0).getPosition())
              < 15f
          && water < 5f) {
        water += 0.001f;
      }
      Place place = findNearest(mDirection.get(0).getPosition(), 1);
      if (place.distance(mDirection.get(0).getPosition()) < 15f) {
        if (place.getValue() < place.getMaxValue() - 0.002 && water > 0.002f) {
          water -= 0.002f;
          place.setValue(place.getValue() + 0.002f);
          place.setColor(
              MainActivity.blendColors(
                  Color.GREEN, Color.RED, place.getValue() / place.getMaxValue()));
        }
      }
      if (water < 0.2f) {
        if (!blinking) {
          if (mDirection.get(1).getType() != 2) {
            Snackbar.make(this, "Press the watering can to find water source", Snackbar.LENGTH_LONG)
                .show();
          }
          Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_blink);
          waterButton.startAnimation(anim);
          blinking = true;
        }
        waterText.setTextColor(Color.RED);
      } else {
        waterText.setTextColor(Color.BLACK);
        waterButton.clearAnimation();
        blinking = false;
      }
      if (water > 5f) water = 5f;
      int circleSize = 6;
      mPaint.setColor(Color.BLUE);
      mPaint.setStrokeWidth(circleSize * mViewport.getScaleFactor());
      Path circle = new Path();
      circle.addCircle(0, 0, circleSize * mViewport.getScaleFactor(), Path.Direction.CCW);
      mPaint.setStyle(Paint.Style.FILL);
      for (int i = 0; i < mPath.size(); i++) {
        if (i == 0) {
          mPaint.setAlpha(255);
          mPaint.setPathEffect(
              new PathDashPathEffect(
                  circle,
                  20 * mViewport.getScaleFactor(),
                  -((System.currentTimeMillis() / 25) % 20 * mViewport.getScaleFactor()),
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
      update();
      postInvalidate();
    }
    if (mDirection.size() == 1 && showingDirection) {
      blinking = false;
      waterButton.clearAnimation();
      waterText.setTextColor(Color.BLACK);
      showingDirection = false;
      for (Place place : mPlaceList) {
        place.setAlpha(1f);
      }
      overlayLayout.removeAllViews();
      Snackbar.make(
              this,
              "You have reached your last destination. Congratulations!!",
              Snackbar.LENGTH_LONG)
          .show();
    }
  }

  @Override
  public void onViewportChanged(RectF viewport) {
    update();
  }

  public void update() {
    for (Place place : mPlaceList) {
      int size = 60;
      Point topLeft =
          new Point((int) (place.position.x - size / 2), (int) (place.position.y - size));
      Point bottomRight = new Point((int) (place.position.x + size / 2), (int) (place.position.y));
      topLeft = mViewport.mapToViewport(topLeft.x, topLeft.y);
      bottomRight = mViewport.mapToViewport(bottomRight.x, bottomRight.y);
      place.setTop(topLeft.y);
      place.setBottom(bottomRight.y);
      place.setLeft(topLeft.x);
      place.setRight(bottomRight.x);
      if (showingDirection) {
        place.postInvalidate();
      }
    }
    bringChildToFront(waterButton);
    bringChildToFront(waterText);
    waterText.setGravity(Gravity.CENTER);
    waterText.setText(String.format("%.0fml", water * 1000));
    waterText.setTextSize(20);

    waterButton.setTop(
        (int)
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
    waterButton.setLeft(
        (int)
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
    waterButton.setRight(
        (int)
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 72, getResources().getDisplayMetrics()));
    waterButton.setBottom(
        (int)
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics()));
    waterText.setTop(
        (int)
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
    waterText.setLeft(
        (int)
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
    waterText.setRight(
        (int)
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics()));
    waterText.setBottom(
        (int)
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
  }

  public Place findNearest(PointF pos, int type) {
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

  public void setCurrentPlace(PointF p) {
    mPlaceList.get(0).setPosition(p.x, p.y);
  }

  public void showDirection(List<Place> places) {
    mDirection.clear();
    mDirection.add(mPlaceList.get(0));
    mDirection.addAll(places);
    mOther.clear();
    mOther.addAll(mPlaceList);
    mOther.removeAll(mDirection);
    showingDirection = true;
  }
}
