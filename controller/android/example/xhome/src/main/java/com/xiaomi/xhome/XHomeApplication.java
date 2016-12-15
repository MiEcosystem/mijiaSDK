package com.xiaomi.xhome;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.xiaomi.mistatistic.sdk.MiStatInterface;
import com.xiaomi.xhome.data.Dashboard;
import com.xiaomi.xhome.mamlplugin.XHomeActionCommandFactory;
import com.xiaomi.xhome.util.CalendarFormatSymbols;
import com.xiaomi.xhome.util.Utils;

import java.io.File;
import java.io.IOException;

import miot.api.MiotManager;
import miot.typedef.config.AppConfiguration;

public class XHomeApplication extends Application {
    private static final String TAG = "XHomeApplication";
    public static final String DIR_DASHBOARD = "dashboard";
    public static final String DIR_MODELS = "models";
    public static final int STAT_UPLOAD_INTERVAL = 4 * 60 * 60000; // 4h
    public static final String SHARED_PREF_NAME = "XHomeConfig";

    private static XHomeApplication sInstance;
    private Dashboard mDashboard;
    private XHomeActionCommandFactory mActionCommandFactory;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        CalendarFormatSymbols.init(getResources());

        MiStatInterface.initialize(this, AppConfig.APP_ID, AppConfig.OAUTH_APP_KEY, "default channel");
        MiStatInterface.setUploadPolicy(MiStatInterface.UPLOAD_POLICY_REALTIME, 0);
        Log.d(TAG, "MiStat, DeviceID: " + MiStatInterface.getDeviceID(getApplicationContext()));

        sInstance = this;
        MiotManager.getInstance().initialize(getApplicationContext());

        AppConfiguration appConfig = new AppConfiguration();
        appConfig.setAppId(AppConfig.OAUTH_APP_ID);
        appConfig.setAppKey(AppConfig.OAUTH_APP_KEY);
        MiotManager.getInstance().setAppConfig(appConfig);

        mSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String[] builtinThemes = {"default", "home"};
        for (String theme : builtinThemes) {
            setupBuiltInTheme(theme, versionChanged());
        }

        // update version config
        try {
            String currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mSharedPreferences.edit().putString(XConfig.CONFIG_VERSION, currentVersion).apply();
        } catch (PackageManager.NameNotFoundException e) {
        }

        loadDashboard(mSharedPreferences.getString(XConfig.CONFIG_SPACE, null));
        mActionCommandFactory = new XHomeActionCommandFactory();
    }

    private void loadDashboard(String space) {
        mDashboard = new Dashboard(space);
        mDashboard.load();
    }

    private boolean versionChanged() {
        String currentVersion = null;
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }

        if (TextUtils.equals(currentVersion, mSharedPreferences.getString(XConfig.CONFIG_VERSION, ""))) {
            return false;
        }

        return true;
    }

    public SharedPreferences getConfig() {
        return mSharedPreferences;
    }

    public static XHomeApplication getInstance() {
        return sInstance;
    }

    public Dashboard getDashboard() {
        return mDashboard;
    }

    public XHomeActionCommandFactory getXHomeActionCommandFactory() {
        return mActionCommandFactory;
    }

    private void setupBuiltInTheme(String theme, boolean overwrite) {
        String path = getThemePath(theme);
        File themeDir = new File(path);
        if (overwrite || !themeDir.exists() || themeDir.list() == null) {
            Log.d(TAG, "copying themes...");
            Utils.delDirContent(themeDir);
            themeDir.mkdirs();
            //extract assets/default to /dashboard/themes/default at first time
            try {
                Utils.unzip(getAssets().open(theme), path);
            } catch (IOException e) {
                Log.e(TAG, "fail to unzip theme asset. " + theme + " " + e.toString());
            }
        }
    }

    public String getDashboardPath() {
        File dir = getDir(DIR_DASHBOARD, Context.MODE_PRIVATE);
        return dir.getPath();
    }

    public String getThemePath(String theme) {
        return getThemesPath() + theme;
    }

    public String getThemesPath() {
        return getDashboardPath() + "/themes/";
    }

    public String getDashboardConfigPath(String space) {
        String cfg = TextUtils.isEmpty(space) ? "/dashboard.xml" : ("/spaces/" + space + ".xml");
        return getDashboardPath() + cfg;
    }

    // myspace_mytheme.xml
    public String getDashboardThemeConfigPath(String space, String theme) {
        String path = getThemeConfigsPath();
        ensurePath(path);

        String cfg = (TextUtils.isEmpty(space) ? "default" : space) + "_" + theme + ".xml";
        return path + "/" + cfg;
    }

    public String getDashboardSpacesPath() {
        return XHomeApplication.getInstance().getDashboardPath() + "/spaces";
    }

    public String getThemeConfigsPath() {
        return XHomeApplication.getInstance().getDashboardPath() + "/theme_configs";
    }

    public void ensurePath(String path) {
        File spacesDir = new File(path);
        if (!spacesDir.exists()) {
            spacesDir.mkdirs();
        }
    }

    public interface OnDashboardUpdateListener {
        void onUpdateDashboard();
    }

    private OnDashboardUpdateListener mOnDashboardUpdateListener;

    public void setOnDashboardUpdateListener(OnDashboardUpdateListener l) {
        mOnDashboardUpdateListener = l;
    }

    public void switchTheme(String theme) {
        if (TextUtils.equals(mDashboard.getTheme(), theme))
            return;

        // save theme name
        mDashboard.saveBoard(theme);
//        mDashboard.saveThemeConfig();

        loadDashboard(mDashboard.getSpace());
        if (mOnDashboardUpdateListener != null) {
            mOnDashboardUpdateListener.onUpdateDashboard();
        }

    }

    public void switchSpace(String space) {
        if (TextUtils.equals(mDashboard.getSpace(), space))
            return;

        loadDashboard(space);
        if (mOnDashboardUpdateListener != null) {
            mOnDashboardUpdateListener.onUpdateDashboard();
        }
        mSharedPreferences.edit().putString(XConfig.CONFIG_SPACE, space).apply();
    }
}
