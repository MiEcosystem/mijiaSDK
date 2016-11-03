package com.xiaomi.xhome.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.mistatistic.sdk.MiStatInterface;
import com.xiaomi.xhome.R;
import com.xiaomi.xhome.XHomeApplication;
import com.xiaomi.xhome.data.Dashboard;
import com.xiaomi.xhome.maml.animation.interpolater.QuadEaseOutInterpolater;

import miot.api.MiotManager;
import miot.typedef.ReturnCode;
import miot.typedef.model.DeviceModel;
import miot.typedef.model.DeviceModelException;
import miot.typedef.model.DeviceModelFactory;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class XHomeMain extends Activity {

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private static final String TAG = "XHomeMain";

    private DashboardView mDashboardView;
    // floating panel
    private boolean mPanelOpenned = true;
    private View mFloatingPanel;
    private View mFloatingButtonOpen;
    private long mResumeTime;
    private ViewGroup mViewHolder;

    @Override
    public void onProvideAssistData(Bundle data) {
        super.onProvideAssistData(data);
    }

    @Override
    public void onBackPressed() {
        if (mDashboardView.getEditMode()) {
            mDashboardView.setEditMode(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_xhome_main);

        setupFloatingpanel();
        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.

//        mBottomBar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getActionBar().hide();


        new MiotOpenTask().execute();
        Log.d(TAG, "正在初始化...");

        // setup dashboard
        mViewHolder = (ViewGroup) findViewById(R.id.scrollView);
        mDashboardView = new DashboardView(this);
        ViewGroup.LayoutParams lp =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT);
        mViewHolder.addView(mDashboardView, lp);
        onUpdateDashboard();

        XHomeApplication.getInstance().setOnDashboardUpdateListener(new XHomeApplication.OnDashboardUpdateListener() {
            @Override
            public void onUpdateDashboard() {
                XHomeMain.this.onUpdateDashboard();
            }
        });
    }

    private void setupButtons() {
//        TextView btnAccount = (TextView) findViewById(R.id.button_account);
//        btnAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(XHomeMain.this, AccountManagerActivity.class);
//                startActivity(intent);
//            }
//        });
        TextView btnAdd = (TextView) findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MiotManager.getPeopleManager() != null && MiotManager.getPeopleManager().isLogin()) {
                    Intent intent = new Intent(XHomeMain.this, DeviceManagerActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(XHomeMain.this, R.string.account_not_login, Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView btnEdit = (TextView) findViewById(R.id.button_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDashboardView.setEditMode(!mDashboardView.getEditMode());
            }
        });

        TextView btnSettings = (TextView) findViewById(R.id.button_setting);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XHomeMain.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
//        mButtonSpaces = (TextView) findViewById(R.id.button_spaces);
//        mButtonSpaces.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(XHomeMain.this, SpacesActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void onUpdateDashboard() {
        Dashboard dashboard = XHomeApplication.getInstance().getDashboard();
        int padding = dashboard.getModelThemeManager().getPadding();
        padding = Math.round(padding * getResources().getDisplayMetrics().density);
        mViewHolder.setPadding(padding, padding, padding, padding);
        mDashboardView.setDashboard(dashboard);
        mDashboardView.loadViews();
//        String space = dashboard.getSpace();
//        mButtonSpaces.setText(TextUtils.isEmpty(space) ? getString(R.string.default_space) : space);
    }

    private void setupFloatingpanel() {
        setupButtons();

        mFloatingPanel = findViewById(R.id.floating_panel);
        mFloatingButtonOpen = findViewById(R.id.button_open);
        mFloatingButtonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFloatingpanel(!mPanelOpenned);
            }
        });
        mFloatingPanel.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPanelOpenned && mDashboardView.getDashboard().getDeviceList().size() > 0) {
                    showFloatingpanel(false);
                }
            }
        }, 3000);
    }

    private void showFloatingpanel(boolean show) {
        mPanelOpenned = show;
        int cx = mFloatingPanel.getWidth() - mFloatingButtonOpen.getWidth();
        mFloatingPanel.animate().translationX(show ? 0 : cx).setDuration(300).
                setInterpolator(new QuadEaseOutInterpolater()).start();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        mDashboardView.onExit();
        new MiotCloseTask().execute();
        super.onDestroy();
    }

    private void onMiotOpenResult(boolean success) {
        Log.d(TAG, success ? "初始化成功" : "初始化失败");
        if (success) {
            // resolve devices
            mDashboardView.onDeviceReady();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDashboardView.onResume();

        MiStatInterface.recordPageStart(this, "主界面");
        ////////////////////////////////////////////////////
        mResumeTime = SystemClock.elapsedRealtime();
        MiStatInterface.recordCountEvent("UI", "main view launch");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDashboardView.onPause();

        MiStatInterface.recordPageEnd();
        ////////////////////////////////////////////////////
        long stayTime = SystemClock.elapsedRealtime() - mResumeTime;
        MiStatInterface.recordCalculateEvent("App", "main view stay time", stayTime);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    private static String[][] sDevModels = {
            {"chuangmi.plug.v1", "ddd_SmartSocket.xml"},
            {"chuangmi.plug.m1", "ddd_Chuangmi_plug_m1.xml"},
            {"zhimi.airpurifier.v3", "ddd_AirPurifier.xml"},
            {"zhimi.airpurifier.v1", "ddd_AirPurifier.xml"},
            {"zhimi.airpurifier.m1", "ddd_airpurifier_m1.xml"},

//            {"lumi.gateway.v3", "ddd_Lumi_Gateway_v1.xml"},
//            {"lumi.gateway.v2", "ddd_Lumi_Gateway_v1.xml"},
            {"lumi.sensor_ht.v1", "ddd_Lumi_sensor_ht.xml"},
            {"lumi.plug.v1", "ddd_Lumi_plug.xml"},
            {"lumi.ctrl_neutral1.v1", "ddd_Lumi_single_switch.xml"},
            {"lumi.ctrl_neutral2.v1", "ddd_Lumi_dual_switch.xml"},

            {"yeelink.light.color1", "YeelightColor1.xml"},
            {"yeelink.light.mono1", "YeelightMono1.xml"},
            {"yeelink.light.lamp1", "YeelightLamp1.xml"},

            {"chuangmi.radio.v1", "ddd_Chuangmi_radio.xml"},

            {"philips.light.mono1", "ddd_philips_light.xml"},

            {"qmi.powerstrip.v1", "ddd_Qmi_Powerstrip.xml"},

            {"zimi.powerstrip.v2", "ddd_Qmi_Powerstrip.xml"},

            {"zhimi.fan.v2", "ddd_zhimi_fan_v2.xml"},

    };

    private class MiotOpenTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {

            for (int i = 0; i < sDevModels.length; i++) {
                Log.d(TAG, "add device model: " + sDevModels[i][0] + " " + sDevModels[i][1]);
                try {
                    DeviceModel deviceModel = DeviceModelFactory.createDeviceModel(getApplicationContext(),
                            sDevModels[i][0], sDevModels[i][1]);
                    MiotManager.getInstance().addModel(deviceModel);

                } catch (DeviceModelException e) {
                    Log.e(TAG, e.toString());
                }
            }
            return MiotManager.getInstance().open();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            int result = integer;
            Log.d(TAG, "MiotOpen result: " + result);
            onMiotOpenResult(result == ReturnCode.OK);
        }
    }

    private class MiotCloseTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            return MiotManager.getInstance().close();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            int result = integer;
            Log.d(TAG, "MiotClose result: " + result);
        }
    }
}
