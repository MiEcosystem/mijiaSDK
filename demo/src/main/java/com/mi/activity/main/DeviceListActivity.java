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
import miot.typedef.exception.MiotException;

public class DeviceListActivity extends BaseActivity {
    private static final String TAG = DeviceListActivity.class.getSimpleName();

    @InjectView(R.id.lv_devices)
    ListView mLvDevices;
    @InjectView(R.id.btn_get_remote_devices)
    Button mBtnGetRemoteDevices;
    @InjectView(R.id.btn_start_scan)
    Button mBtnStartScan;
    @InjectView(R.id.btn_stop_scan)
    Button mBtnStopScan;

    private DeviceAdapter mDeviceAdapter;
    private MiDeviceManager mMiDeviceManager;
    private LocalBroadcastManager mBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.inject(this);

        mDeviceAdapter = new DeviceAdapter(this);
        mLvDevices.setAdapter(mDeviceAdapter);
        mLvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AbstractDevice device = (AbstractDevice) adapterView.getItemAtPosition(position);
                if (device != null) {
                    clickDevice(device);
                }
            }
        });

        mBtnGetRemoteDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMiDeviceManager.getWanDeviceList();
            }
        });

        mBtnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMiDeviceManager.startScan();
            }
        });
        mBtnStopScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMiDeviceManager.stopScan();
            }
        });

        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mMiDeviceManager = MiDeviceManager.getInstance();
        mMiDeviceManager.getWanDeviceList();
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
            intent = new Intent(this, SmartsocketActivity.class);
        }
        intent.putExtra(TestConstants.EXTRA_DEVICE, device);
        startActivity(intent);
    }

    private void connectDevice(AbstractDevice device) {
        try {
            MiotManager.getDeviceConnector().connectToCloud(device, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    Log.d(TAG, "connect device onSucceed");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    Log.e(TAG, "connect device onFailed: " + errCode + description);
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
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
                    showToast("discovery device succeed");
                    List<AbstractDevice> wanDevices = mMiDeviceManager.getWanDevices();
                    mDeviceAdapter.setItems(wanDevices);
                    List<AbstractDevice> wifiDevices = mMiDeviceManager.getWifiDevices();
                    mDeviceAdapter.addItems(wifiDevices);
                    break;
                case TestConstants.ACTION_DISCOVERY_DEVICE_FAILED:
                    showToast("discovery device failed");
                    break;
            }
        }
    };
}
