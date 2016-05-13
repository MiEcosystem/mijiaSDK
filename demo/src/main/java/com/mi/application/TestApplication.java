package com.mi.application;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.mi.device.AuxAirConditionHH;
import com.mi.device.SmartSocketBase;
import com.mi.setting.AppConfig;
import com.mi.utils.CrashHandler;
import com.mi.utils.TestConstants;

import miot.api.MiotManager;
import miot.service.common.utils.Logger;
import miot.typedef.ReturnCode;
import miot.typedef.config.AppConfiguration;
import miot.typedef.model.DeviceModel;
import miot.typedef.model.DeviceModelException;
import miot.typedef.model.DeviceModelFactory;

public class TestApplication extends Application {
    private static final String TAG = TestApplication.class.getSimpleName();
    private LocalBroadcastManager mBindBroadcastManager;

    private static TestApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.enableLog(true);

        sInstance = this;
        mBindBroadcastManager = LocalBroadcastManager.getInstance(this);

        if (isMainProcess()) {
            MiotManager.getInstance().initialize(this);
            new MiotOpenTask().execute();
        }

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
    }

    public static Context getAppContext() {
        return sInstance;
    }

    private boolean isMainProcess() {
        String mainProcessName = getPackageName();
        String processName = getProcessName();
        return TextUtils.equals(processName, mainProcessName);
    }

    private String getProcessName() {
        int pid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }

    @Override
    public void onTerminate() {
        new MiotCloseTask().execute();
        super.onTerminate();
    }

    private class MiotOpenTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            AppConfiguration appConfig = new AppConfiguration();
            appConfig.setAppId(AppConfig.OAUTH_APP_ID);
            appConfig.setAppKey(AppConfig.OAUTH_APP_KEY);
            MiotManager.getInstance().setAppConfig(appConfig);

            try {
                DeviceModel smartSocket = DeviceModelFactory.createDeviceModel(
                        TestApplication.this,
                        TestConstants.CHUANGMI_PLUG_V1,
                        TestConstants.CHUANGMI_PLUG_V1_URL,
                        SmartSocketBase.class);
                MiotManager.getInstance().addModel(smartSocket);

                DeviceModel aircon = DeviceModelFactory.createDeviceModel(
                        TestApplication.this,
                        TestConstants.AUX_AIRCONDITION_V1,
                        TestConstants.AUX_AIRCONDITION_V1_URL,
                        AuxAirConditionHH.class);
                MiotManager.getInstance().addModel(aircon);
            } catch (DeviceModelException e) {
                e.printStackTrace();
            }
            return MiotManager.getInstance().open();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            do {
                int result = integer;
                Log.d(TAG, "MiotOpen result: " + result);
                Intent intent = new Intent(TestConstants.ACTION_BIND_SERVICE_FAILED);
                if (result == ReturnCode.OK) {
                    intent = new Intent(TestConstants.ACTION_BIND_SERVICE_SUCCEED);
                }
                mBindBroadcastManager.sendBroadcast(intent);
            }
            while (false);
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

            do {
                int result = integer;
                Log.d(TAG, "MiotClose result: " + result);
            }
            while (false);
        }
    }
}
