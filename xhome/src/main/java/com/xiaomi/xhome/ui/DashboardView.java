package com.xiaomi.xhome.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.xiaomi.xhome.XConfig;
import com.xiaomi.xhome.XHomeApplication;
import com.xiaomi.xhome.data.Dashboard;
import com.xiaomi.xhome.data.ModelThemeManager;
import com.xiaomi.xhome.maml.LifecycleResourceManager;
import com.xiaomi.xhome.maml.ResourceLoader;
import com.xiaomi.xhome.maml.ResourceManager;
import com.xiaomi.xhome.maml.animation.interpolater.ExpoEaseOutInterpolater;
import com.xiaomi.xhome.maml.util.ZipResourceLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import miot.api.DeviceManipulator;
import miot.api.MiotManager;
import miot.typedef.exception.MiotException;

/**
 * Created by ruijun on 3/22/16.
 */
public class DashboardView extends FreeLayout implements Dashboard.DeviceChangedListener {
    public static final int DEF_DENSITY = 480;

    public static final String TAG = "DashboardView";
    public static final int LONG_CLICK_DETECT_TIME = 500;
    private final float mScale;
    private final int mHeightPixels;
    // blocks in screen width
    public int BLOCKS_CX;
    private int BLOCKS_CY;
    // block size for density 480, in pixels
    public int BLOCK_SIZE;

    private Dashboard mDashboard;
    private boolean mEditMode;
    private boolean mDeviceReady;
    private Rect mTmpRect = new Rect();
    private HashMap<String, ResourceManager> mThemeResourceManagers = new HashMap<String, ResourceManager>();
    private boolean mIsVibrate;

    public DashboardView(Context context) {
        super(context);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mScale = (float) outMetrics.densityDpi / DEF_DENSITY;
        mHeightPixels = outMetrics.heightPixels;
    }

    private void setupBlocks() {
        BLOCK_SIZE = mDashboard.getModelThemeManager().getBlockSize();
        BLOCKS_CX = 1080 / BLOCK_SIZE;
        BLOCKS_CY = Math.round(mHeightPixels / BLOCK_SIZE / mScale) - 1;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDashboard.unregisterDeviceChangedListener(this);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public Dashboard getDashboard() {
        return mDashboard;
    }

    public void setDashboard(Dashboard db) {
        if (mDashboard == db)
            return;
        if (mDashboard != null) {
            mDashboard.unregisterDeviceChangedListener(this);
        }
        mDashboard = db;
        mDashboard.registerDeviceChangedListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 100) {
//                Dashboard.DeviceInfo di = (Dashboard.DeviceInfo) msg.obj;
//                addDevice(di);
//            }
//            super.handleMessage(msg);
//        }
//    };

    public void loadViews() {
        removeAllTileViews();
        releaseThemeResourceManagers();
        setupBlocks();
        for (Dashboard.DeviceInfo di : mDashboard.getDeviceList()) {
            addDevice(di);
        }
    }

    @Override
    public boolean onDeviceAdd(Dashboard.DeviceInfo di) {
        return addDevice(di) != null;
    }

    private TileView addDevice(Dashboard.DeviceInfo di) {
        Log.d(TAG, "add device： " + di.toString());

        if (di.modelTheme == null) {
            Log.d(TAG, "add device  failed, no model theme found for " + di.model);
            return null;
        }

        final LayoutParams lp = new LayoutParams();
        lp.width = getRealSize(di.modelTheme.w);
        lp.height = getRealSize(di.modelTheme.h);

        if (di.x <= 0 && di.y <= 0) {
            findPlace(di);
        }
        lp.left = getRealSize(di.x);
        lp.top = getRealSize(di.y);
        final TileView dv = TileView.create(getContext(), this, di);
        if (dv != null) {
            addView(dv, lp);
            if (mDeviceReady) {
                dv.onDeviceReady();
            }
        }
        return dv;
    }

    public void updateTileViewSize(View dv, int x, int y, int w, int h) {
        LayoutParams lp = (LayoutParams) dv.getLayoutParams();
        dv.setX(getRealSize(x));
        dv.setY(getRealSize(y));
        lp.width = getRealSize(w);
        lp.height = getRealSize(h);
        updateViewLayout(dv, lp);
    }

    private void findPlace(Dashboard.DeviceInfo di) {
        // find place
        ArrayList<Dashboard.DeviceInfo> deviceList = mDashboard.getDeviceList();
        int ind = deviceList.indexOf(di);

        int cw = di.modelTheme.w;
        int ch = di.modelTheme.h;
        if (di.x >= 0 && di.y >= 0 && hasSpace(di.x, di.y, cw, ch, ind)) {
            return;
        }

        boolean found = false;
        for (int x = 0; !found; x++) {
            for (int y = 0; !found && y < BLOCKS_CY; y++) {
                for (int xx = 0; !found && xx < BLOCKS_CX - cw + 1; xx++) {
                    int fx = x * BLOCKS_CX + xx;
                    if (hasSpace(fx, y, cw, ch, ind)) {
                        di.x = fx;
                        di.y = y;
                        found = true;
                        Log.d(TAG, "found place: " + fx + "," + y);
                        break;
                    }
                }
            }
        }
    }

    // translate block unit to pixel
    private int getRealSize(int block) {
        return (int) (block * BLOCK_SIZE * mScale);
    }

    private int getBlockUnit(int pixel) {
        return (int) (pixel / mScale / BLOCK_SIZE);
    }

    // whether the tile specified by x,y,w,h can be placed
    // ind: only check indices before ind
    private boolean hasSpace(int x, int y, int w, int h, int ind) {
        ArrayList<Dashboard.DeviceInfo> deviceList = mDashboard.getDeviceList();
        for (int i = 0; i < ind; i++) {
            Dashboard.DeviceInfo deviceInfo = deviceList.get(i);
            if (tileOverlapped(deviceInfo, x, y, w, h)) {
                return false;
            }
        }
        return true;
    }

    // whether the tile specified by x,y,w,h can be placed
    // exceptInd: the index target should be ignored, which could be the current tile itself,  -1 means check all
    public boolean hasSpaceExcept(int x, int y, int w, int h, Dashboard.DeviceInfo di) {
        ArrayList<Dashboard.DeviceInfo> deviceList = mDashboard.getDeviceList();
        for (int i = 0; i < deviceList.size(); i++) {
            Dashboard.DeviceInfo deviceInfo = deviceList.get(i);
            if (deviceInfo == di)
                continue;
            if (tileOverlapped(deviceInfo, x, y, w, h)) {
                return false;
            }
        }
        return true;
    }

    //
    private boolean tileOverlapped(Dashboard.DeviceInfo target, int x, int y, int w, int h) {
        ModelThemeManager.ModelThemeInfo themeInfo = target.modelTheme;
        if (themeInfo == null) {
            Log.e(TAG, "no device model theme: " + target.toString());
            return false;
        }
        return (target.x < x + w) && (target.x + themeInfo.w > x) && (target.y < y + h) && (target.y + themeInfo.h > y);
    }


    // miot device object is ready
    public void onDeviceReady() {
        mDeviceReady = true;

        setupUseLan();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof TileView) {
                final TileView dv = (TileView) child;
                dv.onDeviceReady();
            }
        }
    }

    private void setupUseLan() {
        boolean useLan = XHomeApplication.getInstance().getConfig().getBoolean(XConfig.PREF_KEY_USE_LAN, false);
        DeviceManipulator deviceManipulator = MiotManager.getDeviceManipulator();
        if (deviceManipulator != null) {
            try {
                Log.d(TAG, "enable LAN control: " + useLan);
                deviceManipulator.enableLanCtrl(useLan);
            } catch (MiotException e) {
            }
        }
    }

    public void setEditMode(boolean edit) {
        if (mEditMode == edit)
            return;
        mEditMode = edit;
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof TileView) {
                final TileView dv = (TileView) child;
                dv.setEditMode(edit);
            }
        }
    }

    public boolean getEditMode() {
        return mEditMode;
    }

    public void onRemoveDevice(TileView dv) {
        mDashboard.removeDevice(dv.getDeviceInfo());
        removeTileView(dv);

        if (getChildCount() == 0) {
            mEditMode = false;
        }
    }

    private void removeTileView(TileView dv) {
        dv.onExit();
        removeView(dv);
    }

    private void removeAllTileViews() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof TileView) {
                final TileView dv = (TileView) child;
                dv.onExit();
            }
        }
        removeAllViews();
    }

    public void onResume() {
        mIsVibrate = XHomeApplication.getInstance().getConfig().getBoolean(XConfig.PREF_KEY_VIBRATE, true);
        setupUseLan();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof TileView) {
                final TileView dv = (TileView) child;
                dv.onResume();
            }
        }
    }

    public ResourceManager getResourceManager(String pack) {
        ResourceManager rm = mThemeResourceManagers.get(pack);

        if (rm != null)
            return rm;

        String path = mDashboard.getModelThemePath(pack);
        if (path == null || !new File(path).exists()) {
            Log.e(TAG, "model theme does not exist, " + path);
            return null;
        }
        ResourceLoader loader = new ZipResourceLoader(path).setLocal(getContext().getResources().getConfiguration().locale);

        rm = new LifecycleResourceManager(loader,
                LifecycleResourceManager.TIME_HOUR, LifecycleResourceManager.TIME_HOUR / 10);

        return rm;
    }

    public boolean isVibrateEnable() {
        return mIsVibrate;
    }

    private class DragDetector {
        public float x;
        public float y;
        public float offsetX;
        public float offsetY;
        public boolean mDragMode;
        public long mLastExpendTime;

        public HitTestResult mHr;
        private Rect rect = new Rect();
        public boolean mLongClickTriggered;
        private Runnable mAction = new Runnable() {
            @Override
            public void run() {
                mHr = hitTest(x, y);
                if (mHr != null) {
                    performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    boolean supportLongClick = mDashboard.getModelThemeManager().supportLongClick();
                    if (!supportLongClick || getEditMode()) {
                        mDragMode = true;
                        mHr.dv.getHitRect(rect);
                        lastLeft = rect.left;
                        lastTop = rect.top;
                        offsetX = x - rect.left;
                        offsetY = y - rect.top;
                        mHr.dv.bringToFront();
                        mHr.dv.setAlpha(0.6f);
                        Log.d(TAG, "DragDetector: hit view: " + mHr.dv.getDeviceInfo().toString());
                        Log.d(TAG, "DragDetector: " + x + " " + y + " , " + offsetX + " " + offsetY);
                        if (!getEditMode()) {
                            setEditModeDelayedCancel();
                        }
                    } else if (supportLongClick) {
                        mHr.dv.getRoot().onCommand("long_click");
                        mLongClickTriggered = true;
                        Log.d(TAG, "MAML long_click");
                    }
                }
            }
        };
        public boolean floating;
        public int lastLeft;
        public int lastTop;

        // sx,sy: the down touch x,y
        public void start(float sx, float sy) {
            x = sx;
            y = sy;
            postDelayed(mAction, getEditMode() ? 100 : LONG_CLICK_DETECT_TIME);
            mLongClickTriggered = false;
        }

        public void cancel() {
            if (mHr != null) {
                mHr.dv.setAlpha(1.0f);
                mHr = null;
            }
            mDragMode = false;
            mLastExpendTime = 0;
            floating = false;
            mLongClickTriggered = false;
            removeCallbacks(mAction);
        }
    }

    private Runnable cancelEditMode = new Runnable() {
        @Override
        public void run() {
            setEditMode(false);
        }
    };

    private void setEditModeDelayedCancel() {
        setEditMode(true);
        removeCallbacks(cancelEditMode);
        postDelayed(cancelEditMode, 5000);
    }

    //
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDragDetector.start(ev.getX(), ev.getY());
                Log.d(TAG, "DragDetector: down " + ev.getX() + " " + ev.getY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                boolean b = mDragDetector.mLongClickTriggered;
                mDragDetector.cancel();
                if (b)
                    return true;
                break;
            }
            case MotionEvent.ACTION_MOVE:
                if (mDragDetector.mDragMode) {
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    return true;
                }

                Log.d(TAG, "DragDetector: intercept move " + ev.getX() + " " + ev.getY());
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private DragDetector mDragDetector = new DragDetector();

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragDetector.mDragMode) {
                    HitTestResult hr = mDragDetector.mHr;
                    if (hr != null) {
                        float x = ev.getX();
                        float y = ev.getY();

                        scrollAndExpend(x, y);
                        int xx = getBlockUnit((int) x) - hr.x;
                        int yy = getBlockUnit((int) y) - hr.y;

                        if (xx < 0)
                            xx = 0;
                        if (yy < 0)
                            yy = 0;

                        TileView dv = hr.dv;
                        Dashboard.DeviceInfo deviceInfo = dv.getDeviceInfo();

                        boolean anotherBlock = xx != deviceInfo.x || yy != deviceInfo.y;
                        if (anotherBlock || mDragDetector.floating) {
                            if (hasSpaceExcept(xx, yy, deviceInfo.modelTheme.w, deviceInfo.modelTheme.h, deviceInfo)) {
                                Log.i(TAG, "DragDetector found drag place: " + xx + " " + yy);
                                deviceInfo.x = xx;
                                deviceInfo.y = yy;
                                mDashboard.setDirty();
                                mDragDetector.lastLeft = getRealSize(xx);
                                mDragDetector.lastTop = getRealSize(yy);
                                moveTileViewAni(dv, mDragDetector.lastLeft, mDragDetector.lastTop);
                                mDragDetector.floating = false;
                            } else {
                                int left = (int) (x - mDragDetector.offsetX);
                                int top = (int) (y - mDragDetector.offsetY);
                                moveTileView(dv, left, top);
                                mDragDetector.floating = true;

                            }
                        }
                        setEditModeDelayedCancel();
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mDragDetector.floating) {
                    moveTileViewAni(mDragDetector.mHr.dv, mDragDetector.lastLeft, mDragDetector.lastTop);
                }
                if (mDragDetector.mLongClickTriggered) {

                }
                mDragDetector.cancel();
                Log.i(TAG, "drag cancel");
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void scrollAndExpend(float x, float y) {
        long currentTime = SystemClock.elapsedRealtime();
        final int blockSize = getRealSize(1);

        if (currentTime - mDragDetector.mLastExpendTime > 500) {
            final HorizontalScrollView parentH = (HorizontalScrollView) getParent();

            float onScreenX = x - parentH.getScrollX();
            if (onScreenX > parentH.getWidth() - 100) {
                if (x > getWidth() - 100) {
                    setMinimumWidth(getWidth() + blockSize);
                    Log.i(TAG, "setMinimumWidth: " + (getWidth() + blockSize));
                }
                parentH.smoothScrollBy(blockSize, 0);
            } else if (onScreenX < 100) {
                parentH.smoothScrollBy(-blockSize, 0);
            }

            final ScrollView parentV = (ScrollView) getParent().getParent();

            float onScreenY = y - parentV.getScrollY();
            if (onScreenY > parentV.getHeight() - 100) {
                Log.i(TAG, "getHeight: " + getHeight());
                if (y > getHeight() - 100) {
                    setMinimumHeight(getHeight() + blockSize);
//                    parentH.setMinimumHeight(parentH.getHeight() + blockSize);
                    Log.i(TAG, "setMinimumHeight: " + (getHeight() + blockSize));
                }
                parentV.smoothScrollBy(0,blockSize);
            } else if (onScreenY < 100) {
                parentV.smoothScrollBy(0,-blockSize);
            }

            mDragDetector.mLastExpendTime = currentTime;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        if (disallowIntercept) {
            mDragDetector.cancel();
        }
    }

    //x,y block position
    private void moveTileViewAni(final TileView dv, final int left, final int top) {
        int right = left + dv.getWidth();
        if (right > getWidth()) {
            setMinimumWidth(right);
        }
/*
        int bottom = top + dv.getHeight();
        if (bottom > ((HorizontalScrollView)getParent()).getHeight()) {
            setMinimumHeight(bottom);
        }

*/

        dv.animate().cancel();
        dv.animate().x(left).y(top).setDuration(300).
                setInterpolator(new ExpoEaseOutInterpolater()).start();
    }

    private void moveTileView(final TileView dv, int left, int top) {
        dv.setX(left);
        dv.setY(top);
    }

    private static class HitTestResult {
        public TileView dv;
        // the hit x,y block in TileView
        public int x;
        public int y;
    }

    private HitTestResult hitTest(float x, float y) {
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child instanceof TileView) {
                final TileView dv = (TileView) child;
                dv.getHitRect(mTmpRect);
                if (mTmpRect.contains((int) x, (int) y)) {
                    HitTestResult hr = new HitTestResult();
                    hr.dv = dv;
                    hr.x = getBlockUnit((int) (x - mTmpRect.left));
                    hr.y = getBlockUnit((int) (y - mTmpRect.top));
                    return hr;
                }
            }
        }
        return null;
    }

    public void onPause() {
        if (getEditMode()) {
            setEditMode(false);
        }

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof TileView) {
                final TileView dv = (TileView) child;
                dv.onPause();
            }
        }
        mDashboard.save();
    }

    public void onExit() {
        mDashboard.save();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof TileView) {
                final TileView dv = (TileView) child;
                dv.onExit();
            }
        }
        releaseThemeResourceManagers();
        mDashboard.unregisterDeviceChangedListener(this);
    }

    private void releaseThemeResourceManagers() {
        for (ResourceManager rm : mThemeResourceManagers.values()) {
            rm.finish(false);
        }
        mThemeResourceManagers.clear();
    }
}
