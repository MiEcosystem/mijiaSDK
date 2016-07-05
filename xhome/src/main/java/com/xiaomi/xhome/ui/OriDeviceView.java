package com.xiaomi.xhome.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.text.TextUtils;
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
import com.xiaomi.xhome.mamlplugin.DeviceViewScreenElementRoot;
import com.xiaomi.xhome.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import miot.api.CommonHandler;
import miot.api.CompletionHandler;
import miot.api.DeviceManipulator;
import miot.api.MiotManager;
import miot.api.device.AbstractDevice;
import miot.typedef.ReturnCode;
import miot.typedef.device.Service;
import miot.typedef.device.invocation.ActionInfo;
import miot.typedef.device.invocation.ActionInfoFactory;
import miot.typedef.device.invocation.PropertyInfo;
import miot.typedef.device.invocation.PropertyInfoFactory;
import miot.typedef.exception.MiotException;
import miot.typedef.property.Property;
import miot.typedef.property.PropertyDefinition;
import miot.typedef.scene.SceneBean;

/**
 * Created by ruijun on 3/17/16.
 */
public class OriDeviceView extends MamlView {
    private static final String TAG = "DeviceView";

    // read properties
    public int READ_INTERVAL = 60000;
    public static final int RESUME_POLLING_INTERVAL = 30000;

    private static String SERVICE_PREFIX = "urn:schemas-mi-com:service:";
    private final Dashboard.DeviceInfo mDeviceInfo;
    private final DashboardView mDashboardView;
    private ImageView mDeleteButton;
    private ArrayList<PropertiesOfService> mPropertiesOfServiceList;
    private HashMap<String, Service> mServicesCache = new HashMap<String, Service>();
    private Runnable mUpdater = new Runnable() {
        @Override
        public void run() {
            readAllProperties();
            postDelayed(this, READ_INTERVAL);
        }
    };
    private boolean mPolling;
    private boolean mDeviceReady;
    private long mLastRefreshTime;
    private ImageView mThemeButton;

    public OriDeviceView(Context context, DashboardView dv, DeviceViewScreenElementRoot root, Dashboard.DeviceInfo di) {
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

    protected void setRoot(DeviceViewScreenElementRoot root) {
        init(root);
        setupRoot(root);
        mDashboardView.updateTileViewSize(this, mDeviceInfo.x, mDeviceInfo.y, mDeviceInfo.modelTheme.w, mDeviceInfo.modelTheme.h);
        init();
        onResume();
    }

    protected void setupRoot(DeviceViewScreenElementRoot root) {
        root.getContext().mVariables.put("__deviceName", mDeviceInfo.name);
        root.getContext().mVariables.put("__deviceModel", mDeviceInfo.model);
        root.init();
        root.setOnExternCommandListener(new ScreenElementRoot.OnExternCommandListener() {
            @Override
            public void onCommand(String command, Double para1, String para2) {
                if ("readAll".equals(command)) {
                    readAllProperties();
                }
            }
        });
        root.setDeviceView(this);

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
                    OriDeviceView.this.performHapticFeedback(feedbackConstant);
                }
            }
        };
        root.setHapticPerformer(hp);
        parseProperties(root.getRawAttr("properties"));
    }

    public static OriDeviceView create(Context context, DashboardView dv, Dashboard.DeviceInfo di) {
        DeviceViewScreenElementRoot root = createRoot(context, dv, di.modelTheme);
        if (root == null) return null;

        return new OriDeviceView(context, dv, root, di);
    }

    private static DeviceViewScreenElementRoot createRoot(Context context, DashboardView dv, ModelThemeManager.ModelThemeInfo modelTheme) {
        ResourceManager rm = dv.getResourceManager(modelTheme.pack);
        ScreenContext mamlContext = new ScreenContext(context, rm);
        // register action command factory
        mamlContext.registerObjectFactory(ObjectFactory.ActionCommandFactory.NAME, XHomeApplication.getInstance().getXHomeActionCommandFactory());
        DeviceViewScreenElementRoot root = new DeviceViewScreenElementRoot(mamlContext);
        root.setStringsPath(modelTheme.strings);
        if (!root.load(modelTheme.manifest)) {
            Log.e(TAG, "fail to load ScreenElementRoot");
            return null;
        }
        root.setScaleByDensity(true);
        return root;
    }

    private Service getService(String name) {
        if (mDeviceInfo.device == null || mDeviceInfo.device.getDevice() == null)
            return null;

        Service service = mServicesCache.get(name);
        if (service != null) {
            return service;
        }
        if (!name.startsWith(SERVICE_PREFIX)) {
            name = SERVICE_PREFIX + name;
        }
        service = mDeviceInfo.device.getDevice().getService(name);
        if (service == null) {
            Log.e(TAG, "fail to get service: " + name);
            return null;
        }
        mServicesCache.put(name, service);
        return service;
    }

    // setup properties, "Service1#PropertyName1,PropertyName2|Service2#PropertyName3,PropertyName4"
    private void parseProperties(String pAttr) {
        mPropertiesOfServiceList = null;
        if (!TextUtils.isEmpty(pAttr)) {
            mPropertiesOfServiceList = new ArrayList<PropertiesOfService>();
            String[] pl = pAttr.split("\\|");
            for (int i = 0; i < pl.length; i++) {
                PropertiesOfService ps = PropertiesOfService.parse(pl[i]);
                if (ps == null) {
                    Log.e(TAG, "invalid properties format: " + pl[i]);
                } else {
                    mPropertiesOfServiceList.add(ps);
                }
            }
        }
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
        if (mDeviceInfo.device != null && mPropertiesOfServiceList != null) {
            // properties
            for (PropertiesOfService ps : mPropertiesOfServiceList) {
                unsubscribeProperties(mDeviceInfo.device, ps.propertyInfo);
            }
        }
        removeCallbacks(mUpdater);
        cleanUp();
    }

    public void runScene() {
        try {
            MiotManager.getDeviceManager().runScene(Utils.transSceneId(mDeviceInfo.id), new CompletionHandler() {
                @Override
                public void onSucceed() {
                }

                @Override
                public void onFailed(int errCode, String description) {
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void enableScene(final boolean enable) {
        try {

            CompletionHandler completionHandler = new CompletionHandler() {
                @Override
                public void onSucceed() {
                    getRoot().getVariables().put("SceneEnable", enable ? 1 : 0);
                    Log.d(TAG, "enable/disable scene: " + mDeviceInfo.name + " " + mDeviceInfo.id);
                }

                @Override
                public void onFailed(int errCode, String description) {
                    Log.e(TAG, "fail to enable/disable scene: " + mDeviceInfo.name + " " + mDeviceInfo.id);
                }
            };
            if (enable) {
                MiotManager.getDeviceManager().enableScene(Utils.transSceneId(mDeviceInfo.id), completionHandler);
            } else {
                MiotManager.getDeviceManager().disableScene(Utils.transSceneId(mDeviceInfo.id), completionHandler);
            }
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private static class PropertiesOfService {
        public String service;
        public String[] properties;
        public PropertyInfo propertyInfo;

        public static PropertiesOfService parse(String str) {
            int ind = str.indexOf("#");
            if (ind == -1)
                return null;
            try {
                PropertiesOfService ps = new PropertiesOfService();
                ps.service = str.substring(0, ind);
                ps.properties = str.substring(ind + 1).split(",");
                return ps;
            } catch (IndexOutOfBoundsException e) {
            }
            return null;
        }
    }

    private void setupDeleteButton(Context context) {
        mDeleteButton = new ImageView(context);
        mDeleteButton.setBackgroundResource(R.drawable.deviceview_button_delete);
        mDeleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                mDashboardView.onRemoveDevice(OriDeviceView.this);
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
                        DeviceViewScreenElementRoot root = createRoot(context, mDashboardView, mDeviceInfo.modelTheme);
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
        if (mDeviceInfo.model.equals(Dashboard.MODEL_SCENE)) {
            // scene
        } else {
            // resolve device
            AbstractDevice device = getDevice();
            if (device != null) {
                getRoot().getVariables().put("__objDevice", device);
                // TODO: refresh device name
                //mDeviceInfo.name = device.getName();
                //getRoot().getContext().mVariables.put("__deviceName", mDeviceInfo.name);

                if (mPropertiesOfServiceList != null) {
                    for (int i = mPropertiesOfServiceList.size() - 1; i >= 0; i--) {
                        PropertiesOfService ps = mPropertiesOfServiceList.get(i);
                        Service service = getService(ps.service);
                        if (service == null) {
                            // remove invalid services
                            mPropertiesOfServiceList.remove(i);
                            Log.e(TAG, "createPropertyInfo, fail to get service");
                            continue;
                        }

                        ps.propertyInfo = createPropertyInfo(service, ps.properties);
                        subscribeProperties(mDeviceInfo.device, ps.propertyInfo);
                        readProperties(mDeviceInfo.device, ps.propertyInfo);
                    }
                }
            }
        }
        startPolling();
    }

    private void startPolling() {
        if (mPolling || !mDeviceReady) return;

        mPolling = true;
        if (SystemClock.elapsedRealtime() - mLastRefreshTime > RESUME_POLLING_INTERVAL) {
            readAllProperties();
        }
        postDelayed(mUpdater, (long) (READ_INTERVAL * Math.random()));

    }

    private void stopPolling() {
        if (!mPolling) return;

        mPolling = false;
        removeCallbacks(mUpdater);
    }

    private static PropertyInfo createPropertyInfo(Service service, String[] properties) {
        PropertyInfo info = PropertyInfoFactory.create(service);

        Log.d(TAG, "createPropertyInfo service: " + service + "");
        for (String propertyName : properties) {
            Log.d(TAG, "createPropertyInfo property:" + propertyName);
            Property p = service.getProperty(propertyName);
            info.addProperty(p);
        }
        return info;
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

    private AbstractDevice getDevice() {
        if (mDeviceInfo.device == null) {
            try {
                mDeviceInfo.device = MiotManager.getDeviceManager().getDevice(mDeviceInfo.id, mDeviceInfo.model);
                if (mDeviceInfo.device == null) {
                    Log.e(TAG, "getDevice: null");
                }
            } catch (MiotException e) {
                Log.e(TAG, "getDevice: " + e.toString());
            }
        }
        return mDeviceInfo.device;
    }

    public int invokeAction(String serviceName, String action, Map<String, Object> params) {
        Log.d(TAG, "invokeAction: " + serviceName + " " + action);

        if (TextUtils.isEmpty(serviceName) || TextUtils.isEmpty(action)) {
            return ReturnCode.E_INVALID_PARAM;
        }

        if (!serviceName.startsWith(SERVICE_PREFIX)) {
            serviceName = SERVICE_PREFIX + serviceName;
        }
        Service service = getService(serviceName);
        if (service == null) {
            Log.d(TAG, "fail to get service");
            return ReturnCode.E_INVALID_PARAM;
        }

        int result = exeCommand(service, action, params);
        Log.d(TAG, "invokeAction, result: " + result);
        return result;
    }


    private int exeCommand(final Service service, String actionName, Map<String, Object> params) {
        final ActionInfo actionInfo = ActionInfoFactory.create(service, actionName);
        if (actionInfo == null) {
            return ReturnCode.E_ACTION_NOT_SUPPORT;
        }

        if (params != null) {
            for (Property property : actionInfo.getArguments()) {
                PropertyDefinition def = property.getDefinition();
                String propName = def.getName();
                Log.d(TAG, "set argument: " + propName);
                Object value = params.get(propName);

                if (value == null) {
                    Log.e(TAG, "null argument: " + propName);
                    return ReturnCode.E_ACTION_ARGUMENT_INVALID;
                }
                if (!actionInfo.setArgumentValue(propName, value)) {
                    return ReturnCode.E_ACTION_ARGUMENT_INVALID;
                }
            }
        }

        Log.d(TAG, "invoke: " + actionName);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        try {
            op.invoke(actionInfo, new DeviceManipulator.InvokeCompletionHandler() {

                @Override
                public void onSucceed(ActionInfo actionInfo) {
                    Log.d(TAG, "succeed: " + actionInfo.getFriendlyName());
                    readAllProperties();
                }

                @Override
                public void onFailed(final int errCode, final String description) {
                    Log.d(TAG, actionInfo.getInternalName() + " failed: " + errCode + " " + description);
                }
            });
        } catch (MiotException e) {
            Log.d(TAG, e.toString());
            return -1;
        }
        return ReturnCode.OK;
    }

    private void readAllProperties() {
        if (mDeviceInfo.model.equals(Dashboard.MODEL_SCENE)) {
            // scene
            try {
                MiotManager.getDeviceManager().queryScene(Utils.transSceneId(mDeviceInfo.id), new CommonHandler<SceneBean>() {
                    @Override
                    public void onSucceed(SceneBean result) {
                        if (result != null) {
                            getRoot().getVariables().put("SceneEnable", result.isEnable() ? 1 : 0);
                        }
                    }

                    @Override
                    public void onFailed(int errCode, String description) {
                    }
                });
            } catch (MiotException e) {
                e.printStackTrace();
            }
        } else {
            if (mDeviceInfo.device != null && mDeviceInfo.device.getDevice() != null) {
                if (mPropertiesOfServiceList != null) {
                    for (PropertiesOfService ps : mPropertiesOfServiceList) {
                        readProperties(mDeviceInfo.device, ps.propertyInfo);
                    }
                }
            }
        }
        mLastRefreshTime = SystemClock.elapsedRealtime();
    }

    private void readProperties(AbstractDevice device, PropertyInfo info) {
        if (device == null || info == null) {
            Log.e(TAG, "readProperties null arguments");
            return;
        }

        if (!MiotManager.getInstance().isBound()) {
            Log.e(TAG, "service not bind yet");
            return;
        }
        try {
            DeviceManipulator manipulator = MiotManager.getDeviceManipulator();
            manipulator.readProperty(info, new DeviceManipulator.ReadPropertyCompletionHandler() {
                @Override
                public void onSucceed(PropertyInfo propertyInfo) {
                    for (Property p : propertyInfo.getProperties()) {
                        OriDeviceView.this.onPropertyChanged(p);
                    }
                    Log.d(TAG, "readProperties succeed");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    Log.d(TAG, "readProperties failed, " + errCode + " " + description);
                }
            });
        } catch (MiotException e) {
            Log.d(TAG, "readProperties failed: " + e.toString());
        }

    }

    private void subscribeProperties(AbstractDevice device, PropertyInfo info) {
        Log.d(TAG, "subscribeProperties: ");
        if (device == null || info == null) {
            Log.e(TAG, "subscribeProperties, null arguments");
            return;
        }

        DeviceManipulator manipulator = MiotManager.getDeviceManipulator();

        try {
            manipulator.addPropertyChangedListener(info,
                    new DeviceManipulator.CompletionHandler() {

                        @Override
                        public void onSucceed() {
                            Log.d(TAG, "succeed");
                        }

                        @Override
                        public void onFailed(int errCode, String description) {
                            Log.d(TAG, String.format("failed： Code: %d %s！", errCode, description));
                        }
                    },
                    new DeviceManipulator.PropertyChangedListener() {
                        @Override
                        public void onPropertyChanged(PropertyInfo propertyInfo, String s) {
                            OriDeviceView.this.onPropertyChanged(propertyInfo.getProperty(s));
                        }
                    }
            );
        } catch (MiotException e) {
            Log.d(TAG, "subscribeProperty failed: " + e.toString());
        }

    }

    private static void unsubscribeProperties(AbstractDevice device, PropertyInfo info) {
        Log.d(TAG, "unsubscribeProperties: ");
        if (device == null || info == null) {
            Log.e(TAG, "subscribeProperties, null arguments");
            return;
        }

        DeviceManipulator manipulator = MiotManager.getDeviceManipulator();
        try {
            manipulator.removePropertyChangedListener(info,
                    new DeviceManipulator.CompletionHandler() {

                        @Override
                        public void onSucceed() {
                            Log.d(TAG, "unsubscribeProperties succeed");
                        }

                        @Override
                        public void onFailed(int errCode, String description) {
                            Log.d(TAG, "unsubscribeProperties failed: " + errCode + " " + description);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "unsubscribeProperties failed: " + e.toString());
        }
    }

    private void onPropertyChanged(Property property) {
        String name = property.getDefinition().getName();
        Object value = property.getValue();
        Log.d(TAG, "property changed: " + name + " " + value.toString());

        if (value instanceof Number) {
            getRoot().getVariables().put(name, ((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            getRoot().getVariables().put(name, ((Boolean) value) ? 1 : 0);
        } else {
            getRoot().getVariables().put(name, value);
        }
        getRoot().requestUpdate();
    }
}
