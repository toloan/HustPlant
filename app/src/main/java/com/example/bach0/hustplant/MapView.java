package com.example.bach0.hustplant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bach0 on 4/11/2018.
 */

public class MapView extends View {
    private static final float AXIS_X_MIN = 0f;
    private static final float AXIS_X_MAX = 1f;
    private static final float AXIS_Y_MIN = 0f;
    private static final float AXIS_Y_MAX = 776f / 1199f;

    Paint paint;
    Rect contentRect = new Rect();
    RectF currentViewportRect = new RectF(0f, 0f, 0.1f, 0.1f);
    RectF scrollerStartViewportRect = new RectF();
    Point surfaceSizeBuffer = new Point();

    OverScroller scroller;

    GestureDetector gestureDetector;
    ScaleGestureDetector scaleGestureDetector;

    Bitmap mapBitmap;
    List<GraphNode> mapNodes = new ArrayList<>();
    PointF coord = new PointF();
    GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            coord.set(e.getX(), e.getY());
            scrollerStartViewportRect.set(currentViewportRect);
            scroller.forceFinished(true);
            postInvalidate();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float viewportOffsetX = distanceX * currentViewportRect.width() / contentRect.width();
            float viewportOffsetY = distanceY * currentViewportRect.height() / contentRect.height();
            computeScrollSurfaceSize(surfaceSizeBuffer);
            setViewportBottomLeft(
                    currentViewportRect.left + viewportOffsetX,
                    currentViewportRect.bottom + viewportOffsetY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            fling((int) -velocityX, (int) velocityY);
            return true;
        }
    };

    ScaleGestureDetector.SimpleOnScaleGestureListener scaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private PointF viewportFocus = new PointF();
        private float lastSpan;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            lastSpan = scaleGestureDetector.getCurrentSpan();
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float span = scaleGestureDetector.getCurrentSpan();

            float newWidth = lastSpan / span * currentViewportRect.width();
            float newHeight = lastSpan / span * currentViewportRect.height();

            float focusX = scaleGestureDetector.getFocusX();
            float focusY = scaleGestureDetector.getFocusY();
            hitTest(focusX, focusY, viewportFocus);

            currentViewportRect.set(
                    viewportFocus.x
                            - newWidth * (focusX - contentRect.left)
                            / contentRect.width(),
                    viewportFocus.y
                            - newHeight * (contentRect.bottom - focusY)
                            / contentRect.height(),
                    0,
                    0);
            currentViewportRect.right = currentViewportRect.left + newWidth;
            currentViewportRect.bottom = currentViewportRect.top + newHeight;
            constrainViewport();
            postInvalidate();

            lastSpan = span;
            return true;
        }
    };

    boolean hitTest(float x, float y, PointF dest) {
        if (!contentRect.contains((int) x, (int) y)) {
            return false;
        }

        dest.set(
                currentViewportRect.left
                        + currentViewportRect.width()
                        * (x - contentRect.left) / contentRect.width(),
                currentViewportRect.top
                        + currentViewportRect.height()
                        * (y - contentRect.bottom) / -contentRect.height());
        return true;
    }

    void computeScrollSurfaceSize(Point out) {
        out.set(
                (int) (contentRect.width() * (AXIS_X_MAX - AXIS_X_MIN)
                        / currentViewportRect.width()),
                (int) (contentRect.height() * (AXIS_Y_MAX - AXIS_Y_MIN)
                        / currentViewportRect.height()));
    }

    private void constrainViewport() {
        float ratio = (float) contentRect.width() / contentRect.height();
        currentViewportRect.left = Math.max(AXIS_X_MIN, currentViewportRect.left);
        currentViewportRect.top = Math.max(AXIS_Y_MIN, currentViewportRect.top);
        if (ratio > 1) {
            currentViewportRect.right = Math.max(Math.nextUp(currentViewportRect.left),
                    Math.min(AXIS_X_MAX, currentViewportRect.right));
            currentViewportRect.bottom = currentViewportRect.top + currentViewportRect.width() / ratio;
        } else {
            currentViewportRect.bottom = Math.max(Math.nextUp(currentViewportRect.top),
                    Math.min(AXIS_Y_MAX, currentViewportRect.bottom));
            currentViewportRect.right = currentViewportRect.left + currentViewportRect.height() * ratio;
        }
    }

    void setViewportBottomLeft(float x, float y) {

        float curWidth = currentViewportRect.width();
        float curHeight = currentViewportRect.height();
        x = Math.max(AXIS_X_MIN, Math.min(x, AXIS_X_MAX - curWidth));
        y = Math.max(AXIS_Y_MIN + curHeight, Math.min(y, AXIS_Y_MAX));

        currentViewportRect.set(x, y - curHeight, x + curWidth, y);
        postInvalidate();
    }

    void fling(int velocityX, int velocityY) {
        // Flings use math in pixels (as opposed to math based on the viewport).
        computeScrollSurfaceSize(surfaceSizeBuffer);
        scrollerStartViewportRect.set(currentViewportRect);
        int startX = (int) (surfaceSizeBuffer.x * (scrollerStartViewportRect.left - AXIS_X_MIN) / (
                AXIS_X_MAX - AXIS_X_MIN));
        int startY = (int) (surfaceSizeBuffer.y * (AXIS_Y_MAX - scrollerStartViewportRect.bottom) / (
                AXIS_Y_MAX - AXIS_Y_MIN));
        scroller.forceFinished(true);
        scroller.fling(
                startX,
                startY,
                velocityX,
                velocityY,
                0, surfaceSizeBuffer.x - contentRect.width(),
                0, surfaceSizeBuffer.y - contentRect.height(),
                contentRect.width() / 2,
                contentRect.height() / 2);
        postInvalidate();
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        scroller = new OverScroller(context);
        gestureDetector = new GestureDetector(context, gestureListener);
        scaleGestureDetector = new ScaleGestureDetector(context, scaleGestureListener);
        mapBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_map);
        mapNodes.add(new GraphNode(370, 1700)); // 0
        mapNodes.add(new GraphNode(370, 1524)); // 1
        mapNodes.add(new GraphNode(714, 1524)); // 2
        mapNodes.add(new GraphNode(370, 950)); // 3
        mapNodes.add(new GraphNode(370, 570)); // 4
        mapNodes.add(new GraphNode(526, 460)); // 5
        mapNodes.add(new GraphNode(513, 952)); // 6
        mapNodes.add(new GraphNode(878, 391)); // 7
        mapNodes.add(new GraphNode(904, 482)); // 8
        mapNodes.add(new GraphNode(1038, 298)); // 9
        mapNodes.add(new GraphNode(1160, 413)); // 10
        mapNodes.add(new GraphNode(1295, 297)); // 11
        mapNodes.add(new GraphNode(1560, 397)); // 12
        mapNodes.add(new GraphNode(1720, 423)); // 13
        mapNodes.add(new GraphNode(2054, 652)); // 14
        mapNodes.add(new GraphNode(2054, 904)); // 15
        mapNodes.add(new GraphNode(2205, 903)); // 16
        mapNodes.add(new GraphNode(2066, 1317)); // 17
        mapNodes.add(new GraphNode(2063, 1711)); // 18
        mapNodes.add(new GraphNode(1901, 1566)); // 19
        mapNodes.add(new GraphNode(1645, 1701)); // 20
        mapNodes.add(new GraphNode(1401, 1552)); // 21
        mapNodes.add(new GraphNode(1159, 1722)); // 22
        mapNodes.add(new GraphNode(1069, 1551)); // 23
        mapNodes.add(new GraphNode(881, 1419)); // 24
        mapNodes.add(new GraphNode(1281, 1666)); // 25
        mapNodes.add(new GraphNode(1161, 1341)); // 26
        mapNodes.add(new GraphNode(715, 1715)); // 27
        mapNodes.add(new GraphNode(557, 609)); // 28
        mapNodes.add(new GraphNode(1548, 297)); // 29
        mapNodes.add(new GraphNode(1427, 478)); // 30
        mapNodes.add(new GraphNode(1087, 1200)); // 31
        mapNodes.add(new GraphNode(1238, 1200)); // 32
        mapNodes.add(new GraphNode(2187, 1560)); // 33
        mapNodes.add(new GraphNode(934, 1663)); // 34
        mapNodes.add(new GraphNode(557, 1666)); // 35
        mapNodes.add(new GraphNode(557, 1763)); // 36
        mapNodes.add(new GraphNode(934, 1773)); // 37
        mapNodes.add(new GraphNode(1276, 1774)); // 38
        mapNodes.add(new GraphNode(1549, 1889)); // 39
        mapNodes.add(new GraphNode(1742, 1887)); // 40
        mapNodes.add(new GraphNode(1944, 1887)); // 41
        mapNodes.add(new GraphNode(2051, 1896)); // 42
        mapNodes.add(new GraphNode(2374, 1560)); // 43
        mapNodes.add(new GraphNode(2139, 1998)); // 44
        mapNodes.add(new GraphNode(2299, 1998)); // 45
        mapNodes.add(new GraphNode(2049, 2234)); // 46
        mapNodes.add(new GraphNode(2148, 2246)); // 47
        mapNodes.add(new GraphNode(1333, 1914)); // 48
        mapNodes.add(new GraphNode(810, 1667)); // 49
        mapNodes.add(new GraphNode(471, 1386)); // 50
        mapNodes.add(new GraphNode(686, 1379)); // 51

        paint.setTextSize(50);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentRect.set(0, 0, w, h);
        currentViewportRect.set(currentViewportRect.left, currentViewportRect.top,
                currentViewportRect.right, currentViewportRect.top + currentViewportRect.width() * h / w);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) | scaleGestureDetector.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (scroller.computeScrollOffset()) {
            // The scroller isn't finished, meaning a fling or programmatic pan operation is
            // currently active.

            computeScrollSurfaceSize(surfaceSizeBuffer);
            int currX = scroller.getCurrX();
            int currY = scroller.getCurrY();

            float currXRange = AXIS_X_MIN + (AXIS_X_MAX - AXIS_X_MIN)
                    * currX / surfaceSizeBuffer.x;
            float currYRange = AXIS_Y_MAX - (AXIS_Y_MAX - AXIS_Y_MIN)
                    * currY / surfaceSizeBuffer.y;
            setViewportBottomLeft(currXRange, currYRange);
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int mapWidth = (int) (mapBitmap.getWidth() * currentViewportRect.width());
        int mapHeight = (int) (mapBitmap.getWidth() * currentViewportRect.height());

        int mapX = (int) (mapBitmap.getWidth() * currentViewportRect.left);
        int mapY = (int) (mapBitmap.getWidth() * currentViewportRect.top);
        int cX = (int) (coord.x / contentRect.width() * mapWidth) + mapX;
        int cY = (int) (coord.y / contentRect.height() * mapHeight) + mapY;
        mapWidth = Math.min(mapWidth, mapBitmap.getWidth() - mapX);
        mapHeight = Math.min(mapHeight, mapBitmap.getHeight() - mapY);
        Bitmap croppedMap = Bitmap.createBitmap(mapBitmap, mapX, mapY, mapWidth, mapHeight);
        canvas.drawBitmap(croppedMap, null, contentRect, paint);
        canvas.drawText("" + cX + " " + cY, 500, 500, paint);
        canvas.drawCircle(coord.x, coord.y, 20, paint);
        fillArrow(paint, canvas, 0, 0, 500, 500, 100);
        int i = 0;
        for (GraphNode n : mapNodes) {
            int nX = (n.x - mapX) * contentRect.width() / mapWidth;
            int nY = (n.y - mapY) * contentRect.height() / mapHeight;
            canvas.drawCircle(nX, nY, 5f / currentViewportRect.width(), paint);
            canvas.drawText("" + i, nX, nY - 5 / currentViewportRect.width(), paint);
            i++;
        }
    }

    private void fillArrow(Paint paint, Canvas canvas, float x0, float y0, float x1, float y1, float size) {
        int arrowHeadAngle = 45;
        float[] linePts = new float[]{x1 - size, y1, x1, y1};
        float[] linePts2 = new float[]{x1, y1, x1, y1 + size};
        Matrix rotateMat = new Matrix();

        //get the center of the line
        float centerX = x1;
        float centerY = y1;

        //set the angle
        double angle = Math.atan2(y1 - y0, x1 - x0) * 180 / Math.PI + arrowHeadAngle;

        //rotate the matrix around the center
        rotateMat.setRotate((float) angle, centerX, centerY);
        rotateMat.mapPoints(linePts);
        rotateMat.mapPoints(linePts2);
        Path path = new Path();
        path.moveTo(linePts[0], linePts[1]);
        path.lineTo(linePts[2], linePts[3]);
        path.lineTo(linePts2[2], linePts2[3]);
        path.close();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);
    }
}
