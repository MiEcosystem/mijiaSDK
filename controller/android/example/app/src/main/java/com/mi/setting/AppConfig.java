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
            OAUTH_APP_ID = 2882303761517532465L;
            OAUTH_APP_KEY = "5161753214465";
            OAUTH_REDIRECT_URI = "http://www.xiaomi.com/mijiasdk";
        } else if("com.xiaomi.xhome".equals(packageName)){
            OAUTH_APP_ID = 2882303761517461087L;
            OAUTH_APP_KEY = "5901746197087";
            OAUTH_REDIRECT_URI = "http://www.xiaomi.com/xhome";
        }
    }
}
