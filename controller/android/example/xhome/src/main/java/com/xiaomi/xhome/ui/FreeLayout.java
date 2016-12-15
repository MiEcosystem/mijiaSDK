package com.xiaomi.xhome.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FreeLayout extends ViewGroup {

    public static class LayoutParams extends MarginLayoutParams {

        public int top, left;

        public LayoutParams() {
            super(WRAP_CONTENT, WRAP_CONTENT);
        }
    }

    private static Rect mTmpRect = new Rect();

    public FreeLayout(Context context) {
        super(context);
    }

    public FreeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FreeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        // find the minimal size
        int width = 0;
        int height = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int x = lp.left;
            int y = lp.top;
            int w = lp.width;
            int h = lp.height;
            if (w > 0 && h > 0) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
                childHeightSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
            }
            child.measure(childWidthSpec, childHeightSpec);
            if (x + w > width) {
                width = x + w;
            }
            if (y + h > height) {
                height = y + h;
            }
        }

        int minWidth = getMinimumWidth();
        if (minWidth > width) {
            width = minWidth;
        }

        int minHeight = getMinimumHeight();
        if (minHeight > height) {
            height = minHeight;
        }

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.UNSPECIFIED) {
            widthSpecSize = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
        }
        if (heightSpecMode == MeasureSpec.UNSPECIFIED) {
            heightSpecSize = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        }

        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int childLeft = lp.left;
                int childTop = lp.top;

                child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(),
                        childTop + child.getMeasuredHeight());
            }
        }
    }


    boolean isChildVisibleNow(View child) {
        child.getHitRect(mTmpRect);
        return mTmpRect.intersect(getLeft() + getScrollX(), getTop(),
                getRight() + getScrollX(), getBottom());
    }


    public void addView(View v, int left, int top) {
        LayoutParams lp = new LayoutParams();
        lp.left = left;
        lp.top = top;
        if (v.getWidth() > 0 && v.getHeight() > 0) {
            lp.width = v.getWidth();
            lp.height = v.getHeight();
        }

        this.addView(v, lp);
    }
}
