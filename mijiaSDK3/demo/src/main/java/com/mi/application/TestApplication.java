package com.mi.application;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.mi.device.ChuangmiPlugM1;
import com.mi.setting.AppConfig;
import com.mi.utils.CrashHandler;
import com.mi.utils.TestConstants;
import com.miot.common.ReturnCode;
import com.miot.common.config.AppConfiguration;
import com.miot.common.model.DeviceModel;
import com.miot.common.model.DeviceModelException;
import com.miot.common.model.DeviceModelFactory;
import com.miot.common.utils.Logger;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;

import com.miot.api.MiotManager;

public class TestApplication extends Application {
    private static final String TAG = TestApplication.class.getSimpleName();
    private LocalBroadcastManager mBindBroadcastManager;

    private static TestApplication sInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.enableLog(true);

        sInstance = this;
        mBindBroadcastManager = LocalBroadcastManager.getInstance(this);

        if (isMainProcess()) {

            MiotManager.getInstance().initialize(this);
            registerPush();
            new MiotOpenTask().execute();
        }
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
    }

    public static final String PUSH_MESSAGE = "com.xiaomi.push.message";
    public static final String PUSH_COMMAND = "com.xiaomi.push.command";

    public void registerPush() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PUSH_COMMAND);
        filter.addAction(PUSH_MESSAGE);
        registerReceiver(mReceiver, filter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case PUSH_COMMAND:
                    MiPushCommandMessage command = (MiPushCommandMessage) intent.getSerializableExtra("command");
                    Logger.d(TAG, "command: " + command.toString());
                    break;
                case PUSH_MESSAGE:
                    MiPushMessage message = (MiPushMessage) intent.getSerializableExtra("message");
                    Logger.d(TAG, "message: " + message.toString());
                    break;
            }
        }
    };

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
                DeviceModel plug = DeviceModelFactory.createDeviceModel(
                        TestApplication.this,
                        TestConstants.CHUANGMI_PLUG_M1,
                        TestConstants.CHUANGMI_PLUG_M1_URL,
                        ChuangmiPlugM1.class);
                MiotManager.getInstance().addModel(plug);

                DeviceModel toothBrush = DeviceModelFactory.createDeviceModel(
                        TestApplication.this,
                        "soocare.toothbrush.x3",
                        "soocare.toothbrush.x3.xml");
                MiotManager.getInstance().addModel(toothBrush);

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
