package com.mi.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.mi.test.R;

public class SwitchButtonLocked extends SwitchButton {
    public SwitchButtonLocked(Context paramContext) {
        super(paramContext);
        init();
    }

    public SwitchButtonLocked(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public SwitchButtonLocked(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void init() {
        this.mFrame = getResources().getDrawable(R.drawable.lock_switch_bg);
        this.mSliderOn = getResources().getDrawable(R.drawable.lock_switch_point_on_normal);
        this.mSliderOff = getResources().getDrawable(R.drawable.lock_switch_point_off_normal);
        setBackgroundResource(R.drawable.sliding_btn_bg_light);

        this.mWidth = this.mFrame.getIntrinsicWidth();
        this.mHeight = this.mFrame.getIntrinsicHeight();

        this.mSliderWidth = Math.min(this.mWidth, this.mSliderOn.getIntrinsicWidth());
        this.mSliderPositionStart = 0;
        this.mSliderPositionEnd = (this.mWidth - this.mSliderWidth);
        this.mSliderOffset = this.mSliderPositionStart;

        BitmapDrawable localBitmapDrawable1 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.sliding_btn_bar_off_light));
        this.mBarOff = Bitmap.createScaledBitmap(localBitmapDrawable1.getBitmap(), this.mWidth * 2 - this.mSliderWidth, this.mHeight, true);

        BitmapDrawable localBitmapDrawable2 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.sliding_btn_bar_on_light));
        this.mBarOn = Bitmap.createScaledBitmap(localBitmapDrawable2.getBitmap(), this.mWidth * 2 - this.mSliderWidth, this.mHeight, true);

        this.mFrame.setBounds(0, 0, this.mWidth, this.mHeight);

        Drawable localDrawable = getResources().getDrawable(R.drawable.sliding_btn_mask_light);
        localDrawable.setBounds(0, 0, this.mWidth, this.mHeight);
        this.mMask = convertToAlphaMask(localDrawable);

        this.mBarOffPaint = new Paint();
        this.mBarOnPaint = new Paint();
        this.mBarOnPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        this.mBarOffPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }
}
