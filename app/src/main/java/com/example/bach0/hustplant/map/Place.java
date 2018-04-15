package com.example.bach0.hustplant.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.bach0.hustplant.R;

/** Created by bach0 on 4/12/2018. */
public class Place extends View {
    static final float ICON_SIZE = 0.2f;
    Drawable mFrame;
    Drawable mBackground;
    Drawable mIcon;
    Rect mContentRect = new Rect();
    Rect mIconRect = new Rect();
    Point position = new Point();
    int mColor = Color.BLACK;

    public Place(Context context) {
        this(context, null, 0);
    }

    public Place(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Place(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFrame = ContextCompat.getDrawable(context, R.drawable.ic_place).mutate();
        mBackground = ContextCompat.getDrawable(context, R.drawable.ic_place_background);
    }

    public void setColor(int color) {
        mColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBackground.setBounds(mContentRect);
        mFrame.setBounds(mContentRect);
        mFrame.setColorFilter(mColor, PorterDuff.Mode.MULTIPLY);
        mFrame.draw(canvas);
        mBackground.draw(canvas);
        if (mIcon != null) {
            mIcon.setBounds(mIconRect);
            mIcon.draw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mContentRect.set(0, 0, w, h);
        mIconRect.set(
                (int) (w * (0.5f - ICON_SIZE)),
                (int) (h * (0.3125f - ICON_SIZE)),
                (int) (w * (0.5f + ICON_SIZE)),
                (int) (h * (0.3125f + ICON_SIZE)));
    }

    public void setPosition(int x, int y) {
        position.set(x, y);
    }

    public void setIcon(int resource) {
        mIcon = ContextCompat.getDrawable(getContext(), resource);
    }
}
