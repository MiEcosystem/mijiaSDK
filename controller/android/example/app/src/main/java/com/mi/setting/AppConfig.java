package com.mi.setting;

import com.mi.application.TestApplication;

public class AppConfig {
    //TODO: replace it to your own appId and appKey
    public static long OAUTH_APP_ID;
    public static String OAUTH_APP_KEY;
    public static String OAUTH_REDIRECT_URI;

    static {
        String packageName = TestApplication.getAppContext().getPackageName();
        if ("com.mijiasdk.demo".equals(packageName)) {
            OAUTH_APP_ID = 12345L;
            OAUTH_APP_KEY = "123";
            OAUTH_REDIRECT_URI = "http://www.xiaomi.com/abc";
        }
    }
}
