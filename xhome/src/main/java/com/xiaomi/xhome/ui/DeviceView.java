package com.xiaomi.xhome.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xiaomi.xhome.data.Dashboard;
import com.xiaomi.xhome.maml.ScreenElementRoot;
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
public class DeviceView extends TileView {
    private static final String TAG = "DeviceView";

    private static String SERVICE_PREFIX = "urn:schemas-mi-com:service:";
    private ArrayList<PropertiesOfService> mPropertiesOfServiceList;
    private HashMap<String, Service> mServicesCache = new HashMap<String, Service>();

    public DeviceView(Context context, DashboardView dv, ScreenElementRoot root, Dashboard.DeviceInfo di) {
        super(context, dv, root, di);
    }

    protected void onSetupRoot(ScreenElementRoot root) {
        parseProperties(root.getRawAttr("properties"));
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

    public void onExit() {
        if (mDeviceInfo.device != null && mPropertiesOfServiceList != null) {
            // properties
            for (PropertiesOfService ps : mPropertiesOfServiceList) {
                unsubscribeProperties(mDeviceInfo.device, ps.propertyInfo);
            }
        }
        super.onExit();
    }

    public void runScene() {
        try {
            MiotManager.getDeviceManager().runScene(com.xiaomi.xhome.util.Utils.transSceneId(mDeviceInfo.id), new CompletionHandler() {
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
                MiotManager.getDeviceManager().enableScene(com.xiaomi.xhome.util.Utils.transSceneId(mDeviceInfo.id), completionHandler);
            } else {
                MiotManager.getDeviceManager().disableScene(com.xiaomi.xhome.util.Utils.transSceneId(mDeviceInfo.id), completionHandler);
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

    @Override
    public void onDeviceReady() {
        super.onDeviceReady();

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

    @Override
    protected void doPoll() {
        readAllProperties();
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
                        DeviceView.this.onPropertyChanged(p);
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
                            DeviceView.this.onPropertyChanged(propertyInfo.getProperty(s));
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
