package com.mi.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.CheckBox;

import com.mi.test.R;

public class SwitchButton extends CheckBox {
    protected static final int FULL_ALPHA = 255;
    protected static final int ANIMATION_DURATION = 180;
    protected static final int SCALE_ANIMATION_DELAY = 100;
    protected Drawable mFrame;
    protected Drawable mSliderOn;
    protected int mSliderOnAlpha;
    protected Drawable mSliderOff;
    protected Bitmap mBarOff;
    protected Paint mBarOffPaint;
    protected Bitmap mBarOn;
    protected Paint mBarOnPaint;
    protected Bitmap mMask;
    protected int mWidth;
    protected int mHeight;
    protected int mSliderWidth;
    protected int mSliderPositionStart;
    protected int mSliderPositionEnd;
    protected int mSliderOffset;
    protected int mLastX;
    protected int mOriginalTouchPointX;
    protected boolean mTracking;
    protected boolean mSliderMoved;
    protected int mTapThreshold;
    protected OnCheckedChangeListener mOnPerformCheckedChangeListener;
    protected Rect mTmpRect = new Rect();
    protected Animator mAnimator;
    protected Animator.AnimatorListener mAnimatorListener;
    protected boolean mOnTouchEventEnable = true;

    public SwitchButton(Context paramContext) {
        this(paramContext, null);
    }

    public SwitchButton(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    @SuppressLint({"NewApi"})
    public SwitchButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);

        this.mAnimatorListener = new AnimatorListenerAdapter() {
            private boolean mCanceled;

            public void onAnimationStart(Animator paramAnonymousAnimator) {
                this.mCanceled = false;
            }

            public void onAnimationCancel(Animator paramAnonymousAnimator) {
                this.mCanceled = true;
            }

            public void onAnimationEnd(Animator paramAnonymousAnimator) {
                if (this.mCanceled) {
                    return;
                }
                SwitchButton.this.mAnimator = null;
                final boolean bool = SwitchButton.this.mSliderOffset >= SwitchButton.this.mSliderPositionEnd;
                if (bool != SwitchButton.this.isChecked()) {
                    SwitchButton.this.setChecked(bool);
                    if (SwitchButton.this.mOnPerformCheckedChangeListener != null) {
                        SwitchButton.this.post(new Runnable() {
                            public void run() {
                                if (SwitchButton.this.mOnPerformCheckedChangeListener != null) {
                                    SwitchButton.this.mOnPerformCheckedChangeListener.onCheckedChanged(SwitchButton.this, bool);
                                }
                            }
                        });
                    }
                }
            }
        };

        setDrawingCacheEnabled(false);
        this.mTapThreshold = (ViewConfiguration.get(paramContext).getScaledTouchSlop() / 2);

        this.mFrame = getResources().getDrawable(R.drawable.sliding_btn_frame_light);
        this.mSliderOn = getResources().getDrawable(R.drawable.sliding_btn_slider_on_light);
        this.mSliderOff = getResources().getDrawable(R.drawable.sliding_btn_slider_off_light);
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

    public void setOnPerformCheckedChangeListener(OnCheckedChangeListener paramOnCheckedChangeListener) {
        this.mOnPerformCheckedChangeListener = paramOnCheckedChangeListener;
    }

    protected Bitmap convertToAlphaMask(Drawable paramDrawable) {
        Rect localRect = paramDrawable.getBounds();
        Bitmap localBitmap = Bitmap.createBitmap(localRect.width(), localRect.height(), Bitmap.Config.ALPHA_8);
        Canvas localCanvas = new Canvas(localBitmap);
        paramDrawable.draw(localCanvas);
        return localBitmap;
    }

    public void setChecked(boolean paramBoolean) {
        boolean bool = isChecked();

        if (bool != paramBoolean) {
            super.setChecked(paramBoolean);
            this.mSliderOffset = (paramBoolean ? this.mSliderPositionEnd : this.mSliderPositionStart);
            this.mBarOnPaint.setAlpha(paramBoolean ? 255 : 0);
            this.mBarOffPaint.setAlpha(!paramBoolean ? 255 : 0);
            this.mSliderOnAlpha = (paramBoolean ? 255 : 0);
            invalidate();
        }
    }

    public void setButtonDrawable(Drawable paramDrawable) {
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.mSliderOn.setState(getDrawableState());
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        if ((!isEnabled()) || (!this.mOnTouchEventEnable)) {
            return false;
        }

        int i = paramMotionEvent.getAction();
        int j = (int) paramMotionEvent.getX();
        int k = (int) paramMotionEvent.getY();
        Rect localRect = this.mTmpRect;
        localRect.set(this.mSliderOffset, 0, this.mSliderOffset + this.mSliderWidth, this.mHeight);

        switch (i) {
            case 0:
                if (localRect.contains(j, k)) {
                    this.mTracking = true;
                    setPressed(true);
                } else {
                    this.mTracking = false;
                }
                this.mLastX = j;
                this.mOriginalTouchPointX = j;
                this.mSliderMoved = false;
                break;
            case 2:
                if (this.mTracking) {
                    moveSlider(j - this.mLastX);
                    this.mLastX = j;
                    if (Math.abs(j - this.mOriginalTouchPointX) >= this.mTapThreshold) {
                        this.mSliderMoved = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case 1:
                if (this.mTracking) {
                    if (!this.mSliderMoved)
                        animateToggle();
                    else
                        animateToState(this.mSliderOffset >= this.mSliderPositionEnd / 2);
                } else {
                    animateToggle();
                }
                this.mTracking = false;
                this.mSliderMoved = false;
                setPressed(false);
                break;
            case 3:
                this.mTracking = false;
                this.mSliderMoved = false;
                setPressed(false);
                animateToState(this.mSliderOffset >= this.mSliderPositionEnd / 2);
        }

        return true;
    }

    public void setPressed(boolean paramBoolean) {
        super.setPressed(paramBoolean);
        invalidate();
    }

    protected void animateToggle() {
        animateToState(!isChecked());
    }

    @SuppressLint({"NewApi"})
    protected void animateToState(final boolean paramBoolean) {
        if (this.mAnimator != null) {
            this.mAnimator.cancel();
            this.mAnimator = null;
        }
        AnimatorSet localAnimatorSet = new AnimatorSet();
        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofInt(this, "SliderOffset", new int[]{paramBoolean ? this.mSliderPositionEnd : this.mSliderPositionStart});

        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofInt(this, "SliderOnAlpha", new int[]{paramBoolean ? 'ÿ' : 0});
        localObjectAnimator2.setDuration(180L);
        localObjectAnimator1.setDuration(180L);
        localAnimatorSet.play(localObjectAnimator2).after(localObjectAnimator1).after(100L);
        this.mAnimator = localAnimatorSet;
        this.mAnimator.addListener(this.mAnimatorListener);
        this.mAnimator.start();
    }

    protected void moveSlider(int paramInt) {
        this.mSliderOffset += paramInt;
        if (this.mSliderOffset < this.mSliderPositionStart)
            this.mSliderOffset = this.mSliderPositionStart;
        else if (this.mSliderOffset > this.mSliderPositionEnd) {
            this.mSliderOffset = this.mSliderPositionEnd;
        }
        setSliderOffset(this.mSliderOffset);
    }

    public int getSliderOffset() {
        return this.mSliderOffset;
    }

    public void setSliderOffset(int paramInt) {
        this.mSliderOffset = paramInt;
        invalidate();
    }

    public int getSliderOnAlpha() {
        return this.mSliderOnAlpha;
    }

    public void setSliderOnAlpha(int paramInt) {
        this.mSliderOnAlpha = paramInt;
        invalidate();
    }

    public void setOnTouchEnable(boolean paramBoolean) {
        this.mOnTouchEventEnable = paramBoolean;
    }

    protected void onDraw(Canvas paramCanvas) {
        int i = isEnabled() ? 255 : 127;
        paramCanvas.saveLayerAlpha(0.0F, 0.0F, this.mMask.getWidth(), this.mMask.getHeight(), i, 31);

        paramCanvas.drawBitmap(this.mMask, 0.0F, 0.0F, null);

        drawBar(paramCanvas);

        this.mFrame.draw(paramCanvas);

        if (this.mSliderOnAlpha <= 255) {
            this.mSliderOff.setBounds(this.mSliderOffset, 0, this.mSliderWidth + this.mSliderOffset, this.mHeight);
            this.mSliderOff.draw(paramCanvas);
        }

        this.mSliderOn.setAlpha(this.mSliderOnAlpha);
        this.mSliderOn.setBounds(this.mSliderOffset, 0, this.mSliderWidth + this.mSliderOffset, this.mHeight);
        this.mSliderOn.draw(paramCanvas);
        paramCanvas.restore();
    }

    protected void drawBar(Canvas paramCanvas) {
        if (this.mBarOnPaint.getAlpha() != 0) {
            paramCanvas.drawBitmap(this.mBarOn, 0.0F, 0.0F, this.mBarOnPaint);
        }

        if (this.mBarOffPaint.getAlpha() != 0)
            paramCanvas.drawBitmap(this.mBarOff, 0.0F, 0.0F, this.mBarOffPaint);
    }
}
