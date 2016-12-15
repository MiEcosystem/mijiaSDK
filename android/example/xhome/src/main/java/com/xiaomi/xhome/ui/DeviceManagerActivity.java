package com.xiaomi.xhome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.miot.api.CommonHandler;
import com.miot.api.DeviceManager;
import com.miot.api.MiotManager;
import com.miot.common.abstractdevice.AbstractDevice;
import com.miot.common.device.Action;
import com.miot.common.device.Argument;
import com.miot.common.device.Service;
import com.miot.common.exception.MiotException;
import com.miot.common.property.AllowedValue;
import com.miot.common.property.AllowedValueList;
import com.miot.common.property.AllowedValueRange;
import com.miot.common.property.Property;
import com.miot.common.property.PropertyDefinition;
import com.miot.common.scene.SceneBean;
import com.xiaomi.mistatistic.sdk.MiStatInterface;
import com.xiaomi.xhome.R;
import com.xiaomi.xhome.VirtualDevice;
import com.xiaomi.xhome.XHomeApplication;
import com.xiaomi.xhome.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class DeviceManagerActivity extends Activity {
    public static final String VD_TIME_ID = "100";
    public static final String VD_TIME_MODEL = "xhome.time";
    public static final String VD_TIME_NAME = "Time";
    private static String TAG = DeviceManagerActivity.class.getSimpleName();
    private ListView mListDevices;

    private DeviceListAdapter mDevicesAdapter = new DeviceListAdapter();
    private SceneListAdapter mScenesAdapter = new SceneListAdapter();
    private ArrayList<AbstractDevice> mDevices = new ArrayList<AbstractDevice>();
    private ArrayList<SceneBean> mScenes = new ArrayList<SceneBean>();
    private Handler mHandler = new Handler();
    private boolean mAllCheck;
    private TabHost mTabHost;
    private ListView mListScenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicelist);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mListDevices = (ListView) findViewById(R.id.lv_devices);
        mListDevices.setAdapter(mDevicesAdapter);
        mListDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListDevices.setItemChecked(i, mListDevices.isItemChecked(i));
            }
        });

        mListScenes = (ListView) findViewById(R.id.lv_scences);
        mListScenes.setAdapter(mScenesAdapter);
        mListScenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListScenes.setItemChecked(i, mListScenes.isItemChecked(i));
            }
        });


        Button btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentTab = mTabHost.getCurrentTab();

                SparseBooleanArray checked = currentTab == 0 ? mListDevices.getCheckedItemPositions() : mListScenes.getCheckedItemPositions();
                int size = checked.size();
                for (int i = 0; i < size; i++) {
                    int position = checked.keyAt(i);
                    if (checked.valueAt(i)) {
                        if (currentTab == 0) {
                            AbstractDevice device = mDevices.get(position);
                            if (device != null) {
                                Log.d(TAG, "Add device: " + device.getName() + " " + device.getDeviceId());
                                if (!XHomeApplication.getInstance().getDashboard().addDevice(device)) {
//                                Toast.makeText(DeviceManagerActivity.this, R.string.add_device_fail, Toast.LENGTH_SHORT).show();
                                }
                                MiStatInterface.recordNumericPropertyEvent("Device", "device count",
                                        XHomeApplication.getInstance().getDashboard().getDeviceList().size());
                            }
                        } else if (currentTab == 1) {
                            SceneBean scene = mScenes.get(position);
                            if (scene != null) {
                                Log.d(TAG, "Add scene: " + scene.getName() + " " + scene.getSceneId());
                                if (!XHomeApplication.getInstance().getDashboard().addScene(scene.getSceneId(), scene.getName())) {
//                                Toast.makeText(DeviceManagerActivity.this, R.string.add_device_fail, Toast.LENGTH_SHORT).show();
                                }
                                MiStatInterface.recordNumericPropertyEvent("Device", "device count",
                                        XHomeApplication.getInstance().getDashboard().getDeviceList().size());
                            }
                        }
                    }
                }
                if (size > 0)
                    finish();

            }
        });
        Button btnAddAll = (Button) findViewById(R.id.btn_addall);

        btnAddAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAllCheck = !mAllCheck;
                for (int i = 0; i < mDevices.size(); i++) {
                    mListDevices.setItemChecked(i, mAllCheck);
                }
            }
        });
        synchronized (mDevices) {
            mDevices.add(new VirtualDevice(VD_TIME_NAME, VD_TIME_MODEL, VD_TIME_ID));
            // test
//            mDevices.add(new VirtualDevice("网络收音机", "chuangmi.radio.v1", "200"));
//            mDevices.add(new VirtualDevice("墙壁开关单开", "lumi.ctrl_neutral1.v1", "201"));
//            mDevices.add(new VirtualDevice("墙壁开关双开", "lumi.ctrl_neutral2.v1", "202"));
//            mDevices.add(new VirtualDevice("智米风扇", "zhimi.fan.v2", "202"));
        }

        getDevices();
        getScenes();
        setTitle(R.string.device_activity);

        mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();
        mTabHost.addTab(mTabHost.newTabSpec("tab_devices")
                .setContent(R.id.lv_devices)
                .setIndicator(getString(R.string.tab_devices)));
        mTabHost.addTab(mTabHost.newTabSpec("tab_scenes")
                .setContent(R.id.lv_scences)
                .setIndicator(getString(R.string.tab_scenes)));

        ////////////////////////////////////////////////////
        MiStatInterface.recordCountEvent("UI", "device manager launch");
    }

    private void getScenes() {
        try {
            MiotManager.getDeviceManager().querySceneList(new CommonHandler<List<SceneBean>>() {
                @Override
                public void onSucceed(List<SceneBean> scenes) {
                    onFoundScenes(scenes);

                }

                @Override
                public void onFailed(int errCode, String description) {
                    Log.d(TAG, String.format("querySceneList onFailed: %d %s", errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private void getDevices() {
        try {
            MiotManager.getDeviceManager().getRemoteDeviceList(new DeviceManager.DeviceHandler() {
                @Override
                public void onSucceed(List<AbstractDevice> devices) {
                    onFoundDevices(devices);
                }

                @Override
                public void onFailed(int errCode, String description) {
                    Log.d(TAG, String.format("getRemoteDeviceList onFailed: %d %s", errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }


    public class DeviceListAdapter extends BaseAdapter {
        public DeviceListAdapter() {
        }

        @Override
        public int getCount() {
            synchronized (mDevices) {
                return mDevices.size();
            }
        }

        @Override
        public Object getItem(int index) {
            return mDevices.get(index);
        }

        @Override
        public long getItemId(int index) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(DeviceManagerActivity.this).inflate(R.layout.devicelist_item, null);
            }

            AbstractDevice device;
            synchronized (mDevices) {
                device = mDevices.get(position);
            }
            TextView temp = (TextView) view.findViewById(R.id.tv_device_name);
            temp.setText(device.getName());

            boolean added = XHomeApplication.getInstance().getDashboard().findDevice(device.getDeviceId()) != null;
            temp = (TextView) view.findViewById(R.id.tv_device_extra);
            if (added) {
                temp.setVisibility(View.VISIBLE);
                temp.setText(R.string.device_added);
            } else {
                temp.setVisibility(View.GONE);
                temp.setText("");
            }
            ImageView check = (ImageView) view.findViewById(R.id.checkbox);
            check.setImageResource(mListDevices.isItemChecked(position) ? R.drawable.check_o : R.drawable.check_n);
//            temp = (TextView) view.findViewById(R.id.tv_device_id);
//            temp.setText(device.getDeviceId());
//            temp = (TextView) view.findViewById(R.id.tv_device_model);
//            temp.setText(device.getDeviceModel());
            temp = (TextView) view.findViewById(R.id.tv_device_loca);
            String ssid = "N/A";
            if (device.getDevice() != null) {
                ssid = device.getDevice().getConnectionInfo().getSsid();
            }
            temp.setText(ssid);
            return view;
        }
    }

    public class SceneListAdapter extends BaseAdapter {
        public SceneListAdapter() {
        }

        @Override
        public int getCount() {
            synchronized (mScenes) {
                return mScenes.size();
            }
        }

        @Override
        public Object getItem(int index) {
            return mScenes.get(index);
        }

        @Override
        public long getItemId(int index) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(DeviceManagerActivity.this).inflate(R.layout.scenelist_item, null);
            }

            SceneBean device;
            synchronized (mScenes) {
                device = mScenes.get(position);
            }
            TextView temp = (TextView) view.findViewById(R.id.scene_name);
            temp.setText(device.getName());

            boolean added = XHomeApplication.getInstance().getDashboard().findDevice(Utils.transSceneId(device.getSceneId())) != null;
            temp = (TextView) view.findViewById(R.id.scene_extra);
            if (added) {
                temp.setVisibility(View.VISIBLE);
                temp.setText(R.string.device_added);
            } else {
                temp.setVisibility(View.GONE);
                temp.setText("");
            }
            ImageView check = (ImageView) view.findViewById(R.id.checkbox);
            check.setImageResource(mListScenes.isItemChecked(position) ? R.drawable.check_o : R.drawable.check_n);
//            temp = (TextView) view.findViewById(R.id.tv_device_id);
//            temp.setText(device.getDeviceId());
//            temp = (TextView) view.findViewById(R.id.tv_device_model);
//            temp.setText(device.getDeviceModel());
            return view;
        }
    }

    private void onFoundDevices(List<AbstractDevice> devices) {
        Log.d(TAG, String.format("onDeviceFound: %d", devices.size()));

        synchronized (mDevices) {
            for (AbstractDevice device : devices) {
                if (device == null) {
                    Log.e(TAG, "DeviceFound: null");
                    continue;
                }
                Log.d(TAG, String.format("DeviceFound: %s", device.getName() + " " + device.getDeviceId()));
                processDevice(device);
                mDevices.add(device);
            }
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDevicesAdapter.notifyDataSetChanged();
            }
        });
    }

    private void onFoundScenes(List<SceneBean> scenes) {

        synchronized (mScenes) {
            for (SceneBean sc : scenes) {
                if (sc == null) {
                    Log.e(TAG, "Scene: null");
                    continue;
                }
                Log.d(TAG, String.format("Scene Found: %s", sc.getName() + " " + sc.getSceneId()));
                mScenes.add(sc);
            }
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mScenesAdapter.notifyDataSetChanged();
            }
        });
    }

    private void processDevice(AbstractDevice device) {
        for (AbstractDevice de : mDevices) {
            // only print one time for one model
            if (de.getDeviceModel().equals(device.getDeviceModel())) {
                return;
            }
        }

        for (Service service : device.getDevice().getServices()) {
            Log.d(TAG, "service: " + service.getType().getName() + " " + service.getType().toString());

            for (Action action : service.getActions()) {
                Log.d(TAG, "service actions: " + action.getFriendlyName() + " " + action.getDescription());
                for (Argument arg : action.getArguments()) {
                    Log.d(TAG, "service actions arguments: " + arg.getName());
                }
                Log.d(TAG, " ");
            }
            Log.d(TAG, " ");
            for (Property property : service.getProperties()) {

                PropertyDefinition definition = property.getDefinition();
                Log.d(TAG, "service properties: " + definition.getName() + " " + definition.getFriendlyName());

                AllowedValue allowedValue = definition.getAllowedValue();
                if (allowedValue != null) {
                    if (allowedValue instanceof AllowedValueList) {
                        String values = "allowed values: ";
                        for (Object v : ((AllowedValueList) allowedValue).getAllowedValues()) {
                            values += v.toString() + " ";
                        }
                        Log.d(TAG, values);
                    } else if (allowedValue instanceof AllowedValueRange) {
                        AllowedValueRange range = (AllowedValueRange) allowedValue;
                        Log.d(TAG, "allowed range: " + range.getMinValue() + " " + range.getMaxValue());
                    }
                }
            }
            Log.d(TAG, " ");
        }
    }

}
