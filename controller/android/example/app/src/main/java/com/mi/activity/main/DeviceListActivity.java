package com.mi.activity.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.mi.activity.universal.UniversalDeviceActivity;
import com.mi.adapter.DeviceAdapter;
import com.mi.adapter.MiDeviceManager;
import com.mi.device.ChuangmiPlugM1;
import com.mi.test.R;
import com.mi.utils.TestConstants;
import com.mi.utils.ToolbarActivity;
import com.miot.api.CompletionHandler;
import com.miot.api.MiotManager;
import com.miot.common.abstractdevice.AbstractDevice;
import com.miot.common.exception.MiotException;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DeviceListActivity extends ToolbarActivity {
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
                mMiDeviceManager.queryWanDeviceList();
            }
        });

        mBtnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission()) {
                    mMiDeviceManager.startScan();
                } else {
                    requestPermission();
                }
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
    }

    @Override
    protected Pair<Integer, Boolean> getCustomTitle() {
        return new Pair<>(R.string.title_toolbar_devicemanager, true);
    }

    private static final int REQUEST_LOCATION_PERMISSION = 10000;

    @TargetApi(23)
    private boolean hasPermission() {
        return Build.VERSION.SDK_INT < 23 ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(23)
    private void requestPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MiDeviceManager.getInstance().startScan();
                } else {
                    Log.e(TAG, "on permission to scan device");
                }
        }
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
        if (device instanceof ChuangmiPlugM1) {
//            intent = new Intent(this, PlugActivity.class);
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
        mMiDeviceManager.queryWanDeviceList();
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
                    mDeviceAdapter.notifyDataSetChanged();
                    break;
                case TestConstants.ACTION_DISCOVERY_DEVICE_FAILED:
                    showToast("discovery device failed");
                    break;
            }
        }
    };
}
