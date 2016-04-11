package com.mi.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.mi.activity.device.SmartsocketActivity;
import com.mi.activity.universal.UniversalDeviceActivity;
import com.mi.adapter.DeviceAdapter;
import com.mi.adapter.MiDeviceManager;
import com.mi.device.SmartSocketBase;
import com.mi.test.R;
import com.mi.utils.BaseActivity;
import com.mi.utils.TestConstants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import miot.api.CompletionHandler;
import miot.api.MiotManager;
import miot.api.device.AbstractDevice;

public class DeviceActivity extends BaseActivity {
    private static final String TAG = DeviceActivity.class.getSimpleName();

    @InjectView(R.id.lv_devices)
    ListView lvDevices;
    @InjectView(R.id.btn_start)
    Button btnStart;
    @InjectView(R.id.btn_stop)
    Button btnStop;
    @InjectView(R.id.btn_refresh)
    Button btnRefresh;

    private DeviceAdapter mDeviceAdapter;
    private MiDeviceManager mMiDeviceManager;
    private LocalBroadcastManager mBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.inject(this);

        mDeviceAdapter = new DeviceAdapter(this);
        lvDevices.setAdapter(mDeviceAdapter);
        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AbstractDevice device = (AbstractDevice) adapterView.getItemAtPosition(position);
                if (device != null) {
                    clickDevice(device);
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMiDeviceManager.startDiscovery();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMiDeviceManager.stopDiscovery();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMiDeviceManager.refreshDiscovery();
            }
        });

        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mMiDeviceManager = MiDeviceManager.getInstance();
        mMiDeviceManager.startDiscovery();
    }

    private void clickDevice(AbstractDevice device) {
        switch (device.getConnectionType()) {
            case MIOT_WAN:
                gotoDevicePage(device);
                break;
            case MIOT_WIFI:
                connectDevice(device);
                break;
        }
    }

    private void gotoDevicePage(AbstractDevice device) {
        Intent intent = new Intent(this, UniversalDeviceActivity.class);
        if (device instanceof SmartSocketBase) {
            Log.d(TAG, "smartSocket");
            intent = new Intent(this, SmartsocketActivity.class);
        }
        intent.putExtra(TestConstants.EXTRA_DEVICE, device);
        startActivity(intent);

    }

    private void connectDevice(AbstractDevice device) {
        int ret = MiotManager.getDeviceConnector().connectToCloud(device, new CompletionHandler() {
            @Override
            public void onSucceed() {
                Log.d(TAG, "connect device onSucceed");
            }

            @Override
            public void onFailed(int errCode, String description) {
                Log.e(TAG, "connect device onFailed: " + errCode + description);
            }
        });
        Log.d(TAG, "connectDevice ret: " + ret);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
//        mMiDeviceManager.refreshDiscovery();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBroadcastManager.unregisterReceiver(mReceiver);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(TestConstants.ACTION_BIND_SERVICE_SUCCEED);
        filter.addAction(TestConstants.ACTION_BIND_SERVICE_FAILED);
        filter.addAction(TestConstants.ACTION_DISCOVERY_DEVICE_SUCCEED);
        filter.addAction(TestConstants.ACTION_DISCOVERY_DEVICE_FAILED);
        filter.addAction(TestConstants.ACTION_DEVICE_FOUND);
        filter.addAction(TestConstants.ACTION_DEVICE_LOST);
        filter.addAction(TestConstants.ACTION_DEVICE_UPDATE);
        mBroadcastManager.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "action: " + intent.getAction());
            switch (intent.getAction()) {
                //TODO: for service restart
                case TestConstants.ACTION_BIND_SERVICE_SUCCEED:
                    break;
                case TestConstants.ACTION_BIND_SERVICE_FAILED:
                    break;

                case TestConstants.ACTION_DISCOVERY_DEVICE_SUCCEED:
                    break;
                case TestConstants.ACTION_DISCOVERY_DEVICE_FAILED:
                    break;

                //TODO: currently same logic
                case TestConstants.ACTION_DEVICE_FOUND:
                case TestConstants.ACTION_DEVICE_LOST:
                case TestConstants.ACTION_DEVICE_UPDATE:
                    List<AbstractDevice> wanDevices = mMiDeviceManager.getWanDevices();
                    mDeviceAdapter.setItems(wanDevices);
                    List<AbstractDevice> wifiDevices = mMiDeviceManager.getWifiDevices();
                    mDeviceAdapter.addItems(wifiDevices);
                    break;
            }
        }
    };
}
