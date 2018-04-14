package com.example.bach0.hustplant.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import static android.content.ContentValues.TAG;

/**
 * Created by bach0 on 4/12/2018.
 */

public class MapViewport extends View {
    Paint paint = new Paint();
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private OverScroller mScroller;
    private Bitmap mMap;
    private RectF mCurrentViewport = new RectF(0, 0, 200, 200);
    private Rect mContentRect = new Rect();
    private Listener mListener;
    private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener
            = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        /**
         * This is the active focal point in terms of the viewport. Could be a local
         * variable but kept here to minimize per-frame allocations.
         */
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

            float newWidth = lastSpan / span * mCurrentViewport.width();
            float newHeight = lastSpan / span * mCurrentViewport.height();

            float focusX = scaleGestureDetector.getFocusX();
            float focusY = scaleGestureDetector.getFocusY();
            hitTest(focusX, focusY, viewportFocus);

            mCurrentViewport.set(
                    viewportFocus.x
                            - newWidth * (focusX - mContentRect.left)
                            / mContentRect.width(),
                    viewportFocus.y
                            - newHeight * (mContentRect.bottom - focusY)
                            / mContentRect.height(),
                    0,
                    0);
            mCurrentViewport.right = mCurrentViewport.left + newWidth;
            mCurrentViewport.bottom = mCurrentViewport.top + newHeight;
            constrainViewport();
            ViewCompat.postInvalidateOnAnimation(MapViewport.this);
            lastSpan = span;
            return true;
        }
    };
    private Point mSurfaceSizeBuffer = new Point();
    private RectF mScrollerStartViewport = new RectF();
    private final GestureDetector.SimpleOnGestureListener mGestureListener
            = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            mScrollerStartViewport.set(mCurrentViewport);
            mScroller.forceFinished(true);
            ViewCompat.postInvalidateOnAnimation(MapViewport.this);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float viewportOffsetX = distanceX * mCurrentViewport.width() / mContentRect.width();
            float viewportOffsetY = distanceY * mCurrentViewport.height() / mContentRect.height();
            computeScrollSurfaceSize(mSurfaceSizeBuffer);
            setViewportBottomLeft(
                    mCurrentViewport.left + viewportOffsetX,
                    mCurrentViewport.bottom + viewportOffsetY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            fling((int) -velocityX, (int) velocityY);
            return true;
        }
    };

    public MapViewport(Context context) {
        super(context);
    }

    public MapViewport(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapViewport(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int attrIDs[] = {android.R.attr.src};
        TypedArray a = context.obtainStyledAttributes(attrs, attrIDs);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        mMap = BitmapFactory.decodeResource(context.getResources(), a.getResourceId(0, 0),options);
        mCurrentViewport.set(0, 0, mMap.getWidth(), mMap
                .getHeight());
        a.recycle();
        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        mGestureDetector = new GestureDetector(context, mGestureListener);
        mScroller = new OverScroller(context);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mContentRect.set(
                getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());
        constrainViewport();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix cropMatrix = new Matrix();
        cropMatrix.postTranslate(-mCurrentViewport.left, -mCurrentViewport.top);
        cropMatrix.postScale((float) mContentRect.width() / mCurrentViewport.width(),
                mContentRect.height() / mCurrentViewport
                        .height());

        canvas.drawBitmap(mMap, cropMatrix, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retVal = mScaleGestureDetector.onTouchEvent(event);
        retVal = mGestureDetector.onTouchEvent(event) || retVal;
        return retVal || super.onTouchEvent(event);
    }

    private boolean hitTest(float x, float y, PointF dest) {
        if (!mContentRect.contains((int) x, (int) y)) {
            return false;
        }

        dest.set(
                mCurrentViewport.left
                        + mCurrentViewport.width()
                        * (x - mContentRect.left) / mContentRect.width(),
                mCurrentViewport.top
                        + mCurrentViewport.height()
                        * (y - mContentRect.bottom) / -mContentRect.height());
        return true;
    }

    private void constrainViewport() {
        mCurrentViewport.left = Math.max(0, mCurrentViewport.left);
        mCurrentViewport.top = Math.max(0, mCurrentViewport.top);
        mCurrentViewport.bottom = Math.max(Math.nextUp(mCurrentViewport.top),
                Math.min(mMap.getHeight(), mCurrentViewport.bottom));
        mCurrentViewport.right = Math.max(Math.nextUp(mCurrentViewport.left),
                Math.min(mMap.getWidth(), mCurrentViewport.right));
        float ratio = (float) mContentRect.width() / mContentRect.height();
        if (ratio > 1) {
            mCurrentViewport.bottom = mCurrentViewport.top + mCurrentViewport.width() / ratio;
        } else {
            mCurrentViewport.right = mCurrentViewport.left + mCurrentViewport.height() * ratio;
        }
        mListener.onViewportChanged(mCurrentViewport);
    }

    private void fling(int velocityX, int velocityY) {
        // Flings use math in pixels (as opposed to math based on the viewport).
        computeScrollSurfaceSize(mSurfaceSizeBuffer);
        mScrollerStartViewport.set(mCurrentViewport);
        int startX = (int) (mSurfaceSizeBuffer.x * (mScrollerStartViewport.left - 0) / (
                mMap.getWidth()));
        int startY = (int) (mSurfaceSizeBuffer.y * (mMap.getHeight() -
                mScrollerStartViewport.bottom) / (
                mMap.getHeight()));
        mScroller.forceFinished(true);
        mScroller.fling(
                startX,
                startY,
                velocityX,
                velocityY,
                0, mSurfaceSizeBuffer.x - mContentRect.width(),
                0, mSurfaceSizeBuffer.y - mContentRect.height(),
                mContentRect.width() / 2,
                mContentRect.height() / 2);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void computeScrollSurfaceSize(Point out) {
        out.set(
                (int) (mContentRect.width() * (mMap.getWidth())
                        / mCurrentViewport.width()),
                (int) (mContentRect.height() * (mMap.getHeight())
                        / mCurrentViewport.height()));
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()) {
            // The scroller isn't finished, meaning a fling or programmatic pan operation is
            // currently active.

            computeScrollSurfaceSize(mSurfaceSizeBuffer);
            int currX = mScroller.getCurrX();
            int currY = mScroller.getCurrY();

            float currXRange = (mMap.getWidth())
                    * currX / mSurfaceSizeBuffer.x;
            float currYRange = mMap.getHeight() - (mMap
                    .getHeight())
                    * currY / mSurfaceSizeBuffer.y;
            setViewportBottomLeft(currXRange, currYRange);
        }
    }

    private void setViewportBottomLeft(float x, float y) {
        float curWidth = mCurrentViewport.width();
        float curHeight = mCurrentViewport.height();
        x = Math.max(0, Math.min(x, mMap.getWidth() - curWidth));
        y = Math.max(0 + curHeight, Math.min(y, mMap.getHeight()));

        mCurrentViewport.set(x, y - curHeight, x + curWidth, y);
        mListener.onViewportChanged(mCurrentViewport);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * Returns the current viewport (visible extremes for the chart domain and range.)
     */
    public RectF getCurrentViewport() {
        return new RectF(mCurrentViewport);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //     Methods for programmatically changing the viewport
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Sets the chart's current viewport.
     *
     * @see #getCurrentViewport()
     */
    public void setCurrentViewport(RectF viewport) {
        mCurrentViewport = viewport;
        constrainViewport();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public Point viewportToMap(int x, int y) {
        return new Point((int) mCurrentViewport.left + x, (int) mCurrentViewport.top + y);
    }

    public Point mapToViewport(int x, int y) {
        Log.d(TAG, "mapToViewport: " + mContentRect.width() + " " + mCurrentViewport.width());
        Log.d(TAG, "MapViewport: " + mMap.getWidth() + " " + mMap.getHeight());
        return new Point((int) ((x - (int) mCurrentViewport.left) * getScaleFactor()), (int) ((y - (int)
                        mCurrentViewport.top) * getScaleFactor()));
    }

    public float getScaleFactor() {
        return (float) mContentRect.width() / mCurrentViewport.width();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.viewport = mCurrentViewport;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        mCurrentViewport = ss.viewport;
    }

    public interface Listener {
        void onViewportChanged(RectF viewport);
    }

    /**
     * Persistent state that is saved by InteractiveLineGraphView.
     */
    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        private RectF viewport;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            viewport = new RectF(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(viewport.left);
            out.writeFloat(viewport.top);
            out.writeFloat(viewport.right);
            out.writeFloat(viewport.bottom);
        }

        @Override
        public String toString() {
            return "InteractiveLineGraphView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " viewport=" + viewport.toString() + "}";
        }
    }
}
