package com.mi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mi.application.TestApplication;
import com.mi.utils.TestConstants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import miot.api.CompletionHandler;
import miot.api.DeviceManager;
import miot.api.MiotManager;
import miot.api.device.AbstractDevice;
import miot.service.common.utils.Logger;
import miot.service.common.utils.NetworkUtils;
import miot.typedef.ReturnCode;
import miot.typedef.device.ConnectionType;
import miot.typedef.device.Device;

public class MiDeviceManager {
    private static final String TAG = MiDeviceManager.class.getSimpleName();

    private static MiDeviceManager sInstance;

    public static synchronized MiDeviceManager getInstance() {
        if (sInstance == null) {
            sInstance = new MiDeviceManager();
        }
        return sInstance;
    }

    private Context mContext;
    private LocalBroadcastManager mBroadcastManager;
    private Map<String, AbstractDevice> mWanDevices = new Hashtable<>();
    private Map<String, AbstractDevice> mWifiDevices = new Hashtable<>();

    private MiDeviceManager() {
        mContext = TestApplication.getAppContext();
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);
    }

    public List<AbstractDevice> getWanDevices() {
        List<AbstractDevice> devices = new ArrayList<>();
        devices.addAll(mWanDevices.values());
        return devices;
    }

    public AbstractDevice getWanDevice(String deviceId) {
        return mWanDevices.get(deviceId);
    }

    public synchronized void removeWanDevice(String deviceId) {
        mWanDevices.remove(deviceId);
    }

    public synchronized void putWanDevice(String deviceId, AbstractDevice device) {
        mWanDevices.put(deviceId, device);
    }

    public List<AbstractDevice> getWifiDevices() {
        List<AbstractDevice> devices = new ArrayList<>();
        for (AbstractDevice device : mWifiDevices.values()) {
            devices.add(device);
        }
        return devices;
    }

    public AbstractDevice getWifiDevice(String deviceId) {
        return mWifiDevices.get(deviceId);
    }

    public void clearWifiDevices() {
        mWifiDevices.clear();
    }

    public void clearDevices() {
        mWanDevices.clear();
        mWifiDevices.clear();
    }

    public int startDiscovery() {
        int result = ReturnCode.E_SERVICE_NOT_BOUND;
        do {
            if (!NetworkUtils.isNetworkAvailable(mContext)) {
                result = ReturnCode.E_NETWORK_UNAVAILABLE;
                break;
            }

            if (MiotManager.getDeviceManager() != null) {
                result = MiotManager.getDeviceManager().startDiscovery(mCompletionHandler, mDeviceListener);
            }
        } while (false);

        Log.d(TAG, "startDiscovery ret: " + result);
        return result;
    }

    public int refreshDiscovery() {
        int result = ReturnCode.E_SERVICE_NOT_BOUND;

        do {
            if (!NetworkUtils.isNetworkAvailable(mContext)) {
                result = ReturnCode.E_NETWORK_UNAVAILABLE;
                break;
            }

            if (MiotManager.getDeviceManager() != null) {
                result = MiotManager.getDeviceManager().refreshDiscovery(mCompletionHandler);
            }
        } while (false);

        Log.d(TAG, "refreshDiscovery ret: " + result);
        return result;
    }

    public int stopDiscovery() {
        int result = ReturnCode.E_SERVICE_NOT_BOUND;
        if (MiotManager.getDeviceManager() != null) {
            result = MiotManager.getDeviceManager().stopDiscovery(new CompletionHandler() {
                @Override
                public void onSucceed() {
                }

                @Override
                public void onFailed(int errCode, String description) {
                    Log.e(TAG, "stopDiscovery onFailed " + errCode + description);
                }
            });
        }
        return result;
    }

    private CompletionHandler mCompletionHandler = new CompletionHandler() {
        @Override
        public void onSucceed() {
            Log.d(TAG, "discovery onSucceed");
            Intent intent = new Intent(TestConstants.ACTION_DISCOVERY_DEVICE_SUCCEED);
            mBroadcastManager.sendBroadcast(intent);
        }

        @Override
        public void onFailed(int errCode, String description) {
            Log.e(TAG, "discovery onFailed " + errCode + description);
            Intent intent = new Intent(TestConstants.ACTION_DISCOVERY_DEVICE_FAILED);
            mBroadcastManager.sendBroadcast(intent);
        }
    };

    private DeviceManager.DeviceListener mDeviceListener = new DeviceManager.DeviceListener() {
        @Override
        public void onDeviceFound(List<AbstractDevice> devices) {
            foundDevices(devices);
            Intent intent = new Intent(TestConstants.ACTION_DEVICE_FOUND);
            mBroadcastManager.sendBroadcast(intent);
        }

        @Override
        public void onDeviceLost(List<AbstractDevice> devices) {
            lostDevices(devices);
            Intent intent = new Intent(TestConstants.ACTION_DEVICE_LOST);
            mBroadcastManager.sendBroadcast(intent);
        }

        @Override
        public void onDeviceUpdate(List<AbstractDevice> devices) {
            updateDevices(devices);
            Intent intent = new Intent(TestConstants.ACTION_DEVICE_UPDATE);
            mBroadcastManager.sendBroadcast(intent);
        }
    };

    private void foundDevices(List<AbstractDevice> devices) {
        for (AbstractDevice abstractDevice : devices) {
            ConnectionType connectionType = abstractDevice.getDevice().getConnectionType();
            Log.d(TAG, "found abstractDevice: " + abstractDevice.getName() + " " + abstractDevice.getDeviceId() + " " + connectionType);

            switch (connectionType) {
                case MIOT_WAN:

                    if (abstractDevice.getOwnership() == Device.Ownership.NOONES) {
                        bindDevice(abstractDevice);
                    }

                    mWanDevices.put(abstractDevice.getDeviceId(), abstractDevice);
                    break;

                case MIOT_WIFI:
                    if (!mWifiDevices.containsKey(abstractDevice.getDeviceId())) {
                        mWifiDevices.put(abstractDevice.getDeviceId(), abstractDevice);
                    }
                    break;
            }
        }
    }

    private void lostDevices(List<AbstractDevice> devices) {
        for (AbstractDevice device : devices) {

            //filter aux devices
            ConnectionType connectionType = device.getDevice().getConnectionType();
            Log.d(TAG, "lost device: " + device.getName() + " " + device.getDeviceId() + " " + connectionType);

            switch (connectionType) {
                case MIOT_WAN:
                    mWanDevices.remove(device.getDeviceId());
                    break;

                case MIOT_WIFI:
                    mWifiDevices.remove(device.getDeviceId());
                    break;
            }
        }
    }

    private void updateDevices(List<AbstractDevice> devices) {
        for (AbstractDevice abstractDevice : devices) {

            ConnectionType connectionType = abstractDevice.getDevice().getConnectionType();
            Log.d(TAG, "update abstractDevice: " + abstractDevice.getName() + " " + abstractDevice.getDeviceId() + " " + connectionType);

            switch (connectionType) {
                case MIOT_WAN:

                    mWifiDevices.remove(abstractDevice.getDeviceId());

                    if (abstractDevice.getOwnership() == Device.Ownership.NOONES) {
                        bindDevice(abstractDevice);
                    } else {
                        mWanDevices.put(abstractDevice.getDeviceId(), abstractDevice);
                    }

                    break;
                case MIOT_WIFI:
                    break;
            }
        }
    }

    private void bindDevice(final AbstractDevice device) {
        int ret = MiotManager.getDeviceManager().takeOwnership(device, new CompletionHandler() {
            @Override
            public void onSucceed() {
                String log = "takeOwnership succeed";
                Logger.saveLog(log);

                Intent intent = new Intent(TestConstants.ACTION_TAKE_OWNERSHIP_SUCCEED);
                intent.putExtra(TestConstants.MI_DEVICE_ID, device.getDeviceId());
                mBroadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFailed(int errCode, String description) {
                String log = "takeOwnership onFailed " + errCode + description;
                Log.e(TAG, log);
                Logger.saveLog(log);

                Intent intent = new Intent(TestConstants.ACTION_TAKE_OWNERSHIP_FAILED);
                intent.putExtra(TestConstants.MI_DEVICE_ID, device.getDeviceId());
                mBroadcastManager.sendBroadcast(intent);
            }
        });
    }
}
