package com.mi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mi.application.TestApplication;
import com.mi.utils.TestConstants;
import com.miot.api.CompletionHandler;
import com.miot.api.DeviceManager;
import com.miot.api.MiotManager;
import com.miot.common.abstractdevice.AbstractDevice;
import com.miot.common.device.ConnectionType;
import com.miot.common.device.Device;
import com.miot.common.device.DiscoveryType;
import com.miot.common.exception.MiotException;
import com.miot.common.utils.Logger;
import com.miot.common.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
        return new ArrayList<>(mWanDevices.values());
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
        return new ArrayList<>(mWifiDevices.values());
    }

    public AbstractDevice getWifiDevice(String address) {
        return mWifiDevices.get(address);
    }

    public void clearWifiDevices() {
        mWifiDevices.clear();
    }

    public void clearDevices() {
        mWanDevices.clear();
        mWifiDevices.clear();
    }

    public void queryWanDeviceList() {
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
//        types.add(DiscoveryType.MIOT_BLE);
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
        Log.d(TAG, "foundDevices   size"+devices.size());
        clearDevices();
        for (AbstractDevice device : devices) {
            ConnectionType connectionType = device.getDevice().getConnectionType();
            Log.d(TAG, "found device: " + devices.size() + " " + device.getName() + " " + device.getDeviceId() + " " + connectionType);

            switch (connectionType) {
                case MIOT_WAN:

                    if (device.getOwnership() == Device.Ownership.NOONES) {
                        bindDevice(device);
                    }

                    mWanDevices.put(device.getDeviceId(), device);
                    break;

                case MIOT_WIFI:
                    if (!mWifiDevices.containsKey(device.getDeviceId())) {
                        mWifiDevices.put(device.getAddress(), device);
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
                }

                @Override
                public void onFailed(int errCode, String description) {
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }
}
