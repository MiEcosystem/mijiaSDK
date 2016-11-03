package com.mi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * This view is for disable touch event
 */
public class DisableImageView extends ImageView {

    public DisableImageView(Context context) {
        super(context);
    }

    public DisableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}