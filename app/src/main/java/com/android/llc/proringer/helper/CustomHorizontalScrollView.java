package com.android.llc.proringer.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.android.llc.proringer.utils.Logger;

/**
 * Created by su on 8/8/17.
 */

public class CustomHorizontalScrollView extends HorizontalScrollView {

    public interface OnScrollChangedListener {
        // Developer must implement these methods.
        void onScrollStart();

        void onScrollEnd();
    }


    @Override
    public void scrollTo(int x, int y) {
        Logger.printMessage("scrollTo", "scrollTo");
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Logger.printMessage("onTouchEvent", "onTouchEvent");
        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Logger.printMessage("check_release","Action was CANCEL");
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Logger.printMessage("onInterceptTouchEvent", "onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        Logger.printMessage("onOverScrolled", "onOverScrolled");
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    private long lastScrollUpdate = -1;
    private int scrollTaskInterval = 100;
    private Runnable mScrollingRunnable;
    public OnScrollChangedListener mOnScrollListener;

    public CustomHorizontalScrollView(Context context) {
        this(context, null, 0);
        init(context);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        // Check for scrolling every scrollTaskInterval milliseconds
        mScrollingRunnable = new Runnable() {
            public void run() {
                if ((System.currentTimeMillis() - lastScrollUpdate) > scrollTaskInterval) {
                    // Scrolling has stopped.
                    lastScrollUpdate = -1;
                    //CustomHorizontalScrollView.this.onScrollEnd();
                    mOnScrollListener.onScrollEnd();
                } else {
                    // Still scrolling - Check again in scrollTaskInterval milliseconds...
                    postDelayed(this, scrollTaskInterval);
                }
            }
        };
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.mOnScrollListener = onScrollChangedListener;
    }

    public void setScrollTaskInterval(int scrollTaskInterval) {
        this.scrollTaskInterval = scrollTaskInterval;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // Grab the last child placed in the ScrollView, we need it to determinate the bottom position.
        View view = (View) getChildAt(getChildCount() - 1);

        // Calculate the scroll diff
        int diff1 = (view.getRight() - (getWidth() + getScrollX()));

        // if diff is zero, then the bottom has been reached
        if (diff1 == 0) {
            // notify that we have reached the bottom
            Logger.printMessage("Scroll_LOG_TAG", "MyScrollView: right most has been reached");
        }


        // if diff is zero, then the bottom has been reached
        if (diff1 == view.getRight()) {
            // notify that we have reached the bottom
            Logger.printMessage("Scroll_LOG_TAG", "MyScrollView: left most has been reached");
        }

        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollListener != null) {
            if (lastScrollUpdate == -1) {
                //CustomHorizontalScrollView.this.onScrollStart();
                mOnScrollListener.onScrollStart();
                postDelayed(mScrollingRunnable, scrollTaskInterval);
            }
            lastScrollUpdate = System.currentTimeMillis();
        }
    }
}