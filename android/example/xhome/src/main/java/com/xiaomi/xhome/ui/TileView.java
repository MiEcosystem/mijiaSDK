package com.xiaomi.xhome.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.xiaomi.xhome.R;
import com.xiaomi.xhome.XHomeApplication;
import com.xiaomi.xhome.data.Dashboard;
import com.xiaomi.xhome.data.ModelThemeManager;
import com.xiaomi.xhome.maml.ObjectFactory;
import com.xiaomi.xhome.maml.ResourceManager;
import com.xiaomi.xhome.maml.ScreenContext;
import com.xiaomi.xhome.maml.ScreenElementRoot;
import com.xiaomi.xhome.maml.animation.interpolater.BounceEaseOutInterpolater;
import com.xiaomi.xhome.maml.animation.interpolater.QuadEaseInInterpolater;
import com.xiaomi.xhome.maml.component.MamlView;

import java.util.ArrayList;

/**
 * Created by ruijun on 3/17/16.
 */
public class TileView extends MamlView {
    private static final String TAG = "TileView";

    // read properties
    public int READ_INTERVAL = 60000;
    public static final int RESUME_POLLING_INTERVAL = 30000;
    protected final Dashboard.DeviceInfo mDeviceInfo;
    private final DashboardView mDashboardView;
    private ImageView mDeleteButton;
    private Runnable mUpdater = new Runnable() {
        @Override
        public void run() {
            doPoll();
            mLastRefreshTime = SystemClock.elapsedRealtime();
            postDelayed(this, READ_INTERVAL);
        }
    };
    private boolean mPolling;
    private boolean mDeviceReady;
    private long mLastRefreshTime;
    private ImageView mThemeButton;

    public TileView(Context context, DashboardView dv, ScreenElementRoot root, Dashboard.DeviceInfo di) {
        super(context, root);
        mDeviceInfo = di;
        mDashboardView = dv;
        setupRoot(root);
        setClipChildren(false);
        setupEditButtons(context);
    }

    private void setupEditButtons(Context context) {
        setupDeleteButton(context);
        setupThemeButton(context);
    }

    protected void setRoot(ScreenElementRoot root) {
        init(root);
        setupRoot(root);
        mDashboardView.updateTileViewSize(this, mDeviceInfo.x, mDeviceInfo.y, mDeviceInfo.modelTheme.w, mDeviceInfo.modelTheme.h);
        init();
        onResume();
    }

    protected void setupRoot(ScreenElementRoot root) {
        root.getContext().mVariables.put("__deviceName", mDeviceInfo.name);
        root.getContext().mVariables.put("__deviceModel", mDeviceInfo.model);
//        root.init();

//        root.setDeviceView(this);

        try {
            // in seconds
            int readInterval = Integer.parseInt(root.getRawAttr("readInterval"));
            if (readInterval >= 5) {
                READ_INTERVAL = readInterval * 1000;
            }
        } catch (NumberFormatException e) {

        }
        ScreenElementRoot.HapticPerformer hp = new ScreenElementRoot.HapticPerformer() {
            @Override
            public void performHapticFeedback(int feedbackConstant) {
                if (mDashboardView.isVibrateEnable()) {
                    TileView.this.performHapticFeedback(feedbackConstant);
                }
            }
        };
        root.setHapticPerformer(hp);
        onSetupRoot(root);
    }

    protected void onSetupRoot(ScreenElementRoot root) {
    }

    public static TileView create(Context context, DashboardView dv, Dashboard.DeviceInfo di) {
        ScreenElementRoot root = createRoot(context, dv, di.modelTheme);
        if (root == null) return null;

        if (di.model.equals(Dashboard.MODEL_SCENE)) {
            return new SceneView(context, dv, root, di);
        }else if (di.model.equals(Dashboard.MODEL_TIME)) {
            return new TileView(context, dv, root, di);
        } else {
            return new DeviceView(context, dv, root, di);
        }
    }

    private static ScreenElementRoot createRoot(Context context, DashboardView dv, ModelThemeManager.ModelThemeInfo modelTheme) {
        ResourceManager rm = dv.getResourceManager(modelTheme.pack);
        ScreenContext mamlContext = new ScreenContext(context, rm);
        // register action command factory
        mamlContext.registerObjectFactory(ObjectFactory.ActionCommandFactory.NAME, XHomeApplication.getInstance().getXHomeActionCommandFactory());
        ScreenElementRoot root = new ScreenElementRoot(mamlContext);
        root.setStringsPath(modelTheme.strings);
        if (!root.load(modelTheme.manifest)) {
            Log.e(TAG, "fail to load ScreenElementRoot");
            return null;
        }
        root.setScaleByDensity(true);
        return root;
    }

    public void onResume() {
        super.onResume();
        startPolling();
    }

    public void onPause() {
        super.onPause();
        stopPolling();
    }

    public void onExit() {
        removeCallbacks(mUpdater);
        cleanUp();
    }

    private void setupDeleteButton(Context context) {
        mDeleteButton = new ImageView(context);
        mDeleteButton.setBackgroundResource(R.drawable.deviceview_button_delete);
        mDeleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDashboardView.onRemoveDevice(TileView.this);
            }
        });
        mDeleteButton.setVisibility(INVISIBLE);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        addView(mDeleteButton, params);
    }

    private void setupThemeButton(final Context context) {
        ModelThemeManager.ModelThemeInfo head = mDashboardView.getDashboard().getModelThemeManager().findModelInfo(mDeviceInfo.model, null);
        // must have at least 2 themes
        if (head == null || head.next == null)
            return;

        mThemeButton = new ImageView(context);
        mThemeButton.setBackgroundResource(R.drawable.deviceview_button_theme);
        mThemeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseTheme(context);
            }
        });
        mThemeButton.setVisibility(INVISIBLE);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        addView(mThemeButton, params);
    }

    private void onChooseTheme(final Context context) {
        ModelThemeManager.ModelThemeInfo head = mDashboardView.getDashboard().getModelThemeManager().findModelInfo(mDeviceInfo.model, null);
        ArrayList<String> mThemeList = new ArrayList<String>();
        while (head != null) {
            String tmp = head.w + " x " + head.h + "  " + head.desc;
            BlockPlace b = findBlockAround(mDeviceInfo.x, mDeviceInfo.y, head.w, head.h);
            if (b == null) {
                tmp += "  [" + context.getString(R.string.not_enough_space) + "]";
            }
            mThemeList.add(tmp);
            head = head.next;
        }
        if (mThemeList.size() < 2)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_device_theme);
        CharSequence arr[] = new CharSequence[mThemeList.size()];
        builder.setItems(mThemeList.toArray(arr), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                ModelThemeManager.ModelThemeInfo modelInfo = mDashboardView.getDashboard().getModelThemeManager().findModelInfo(mDeviceInfo.model, item);
                if (mDeviceInfo.modelTheme != modelInfo) {
                    // find every possible block
                    BlockPlace b = findBlockAround(mDeviceInfo.x, mDeviceInfo.y, modelInfo.w, modelInfo.h);
                    if (b != null) {
                        mDeviceInfo.x = b.x;
                        mDeviceInfo.y = b.y;
                        mDeviceInfo.modelTheme = modelInfo;
                        cleanUp();
                        mLastRefreshTime = 0;
                        ScreenElementRoot root = createRoot(context, mDashboardView, mDeviceInfo.modelTheme);
                        if (root != null) {
                            setRoot(root);
                            onDeviceReady();
                            mDashboardView.getDashboard().setDirty();
                        }
                    } else {
                        Toast.makeText(context, R.string.not_enough_space, Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }
        });
        builder.setCancelable(true).create().show();
    }

    //////////////////////////////////////////////////////////
    private static class BlockPlace {
        int x;
        int y;
    }

    // find a block around(x,y) on which can put the panel with size(w,h)
    private BlockPlace findBlockAround(int x, int y, int w, int h) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                boolean hasSpace = mDashboardView.hasSpaceExcept(x - i, y - j, w, h, mDeviceInfo);
                if (hasSpace) {
                    BlockPlace b = new BlockPlace();
                    b.x = x - i;
                    b.y = y - j;
                    return b;
                }
            }
        }
        return null;
    }

    public Dashboard.DeviceInfo getDeviceInfo() {
        return mDeviceInfo;
    }

    public void onDeviceReady() {
        mDeviceReady = true;
        startPolling();
    }

    private void startPolling() {
        if (mPolling || !mDeviceReady) return;

        mPolling = true;
        if (SystemClock.elapsedRealtime() - mLastRefreshTime > RESUME_POLLING_INTERVAL) {
            doPoll();
            mLastRefreshTime = SystemClock.elapsedRealtime();
        }
        postDelayed(mUpdater, (long) (READ_INTERVAL * Math.random()));

    }

    protected void doPoll() {
    }

    private void stopPolling() {
        if (!mPolling) return;

        mPolling = false;
        removeCallbacks(mUpdater);
    }

    public void setEditMode(boolean edit) {
        showButton(mDeleteButton, edit);
        showButton(mThemeButton, edit);
    }

    void showButton(final View v, boolean show) {
        if (v == null) {
            return;
        }

        if (show) {
            v.setVisibility(VISIBLE);
            ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, v.getWidth() / 2, v.getHeight() / 2);
            scaleAnimation.setInterpolator(new BounceEaseOutInterpolater());
            scaleAnimation.setDuration(300);
            v.startAnimation(scaleAnimation);
        } else {
            ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, v.getWidth() / 2, v.getHeight() / 2);
            scaleAnimation.setInterpolator(new QuadEaseInInterpolater());
            scaleAnimation.setDuration(300);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            v.startAnimation(scaleAnimation);
        }
    }
}
