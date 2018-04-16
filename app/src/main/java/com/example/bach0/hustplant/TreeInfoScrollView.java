package com.example.bach0.hustplant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/** Created by bach0 on 4/16/2018. */
public class TreeInfoScrollView extends NestedScrollView {
    boolean canDispatch = false;

    public TreeInfoScrollView(@NonNull Context context) {
        super(context);
    }

    public TreeInfoScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TreeInfoScrollView(
            @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(
            int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        return false;
    }
}
