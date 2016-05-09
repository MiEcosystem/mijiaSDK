package com.mi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

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
import miot.typedef.device.ConnectionType;
import miot.typedef.device.Device;
import miot.typedef.device.DiscoveryType;
import miot.typedef.exception.MiotException;

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

    public void getWanDeviceList() {
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            return;
        }

        if (MiotManager.getDeviceManager() == null) {
            return;
        }

        try {
            mWanDevices.clear();
            MiotManager.getDeviceManager().getRemoteDeviceList(mDeviceHandler);
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void startScan() {
        if (MiotManager.getDeviceManager() == null) {
            return;
        }
        List<DiscoveryType> types = new ArrayList<>();
        types.add(DiscoveryType.MIOT_WIFI);
        try {
            MiotManager.getDeviceManager().startScan(types, mDeviceHandler);
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void stopScan() {
        if (MiotManager.getDeviceManager() == null) {
            return;
        }
        try {
            MiotManager.getDeviceManager().stopScan();
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private DeviceManager.DeviceHandler mDeviceHandler = new DeviceManager.DeviceHandler() {
        @Override
        public void onSucceed(List<AbstractDevice> devices) {
            foundDevices(devices);

            Intent intent = new Intent(TestConstants.ACTION_DISCOVERY_DEVICE_SUCCEED);
            mBroadcastManager.sendBroadcast(intent);
        }

        @Override
        public void onFailed(int errCode, String description) {
            Logger.e(TAG, "getRemoteDeviceList onFailed: " + errCode + description);
            Intent intent = new Intent(TestConstants.ACTION_DISCOVERY_DEVICE_FAILED);
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

    private void bindDevice(final AbstractDevice device) {
        try {
            MiotManager.getDeviceManager().takeOwnership(device, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    String log = "takeOwnership succeed";

                    Intent intent = new Intent(TestConstants.ACTION_TAKE_OWNERSHIP_SUCCEED);
                    intent.putExtra(TestConstants.MI_DEVICE_ID, device.getDeviceId());
                    mBroadcastManager.sendBroadcast(intent);
                }

                @Override
                public void onFailed(int errCode, String description) {
                    String log = "takeOwnership onFailed " + errCode + description;
                    Log.e(TAG, log);

                    Intent intent = new Intent(TestConstants.ACTION_TAKE_OWNERSHIP_FAILED);
                    intent.putExtra(TestConstants.MI_DEVICE_ID, device.getDeviceId());
                    mBroadcastManager.sendBroadcast(intent);
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }
}
