package com.xiaomi.xhome.util;

import android.os.AsyncTask;
import android.util.Log;

import com.miot.api.MiotManager;
import com.miot.common.config.AppConfiguration;
import com.miot.common.people.People;
import com.miot.common.people.PeopleFactory;
import com.miot.common.utils.Logger;
import com.xiaomi.account.openauth.AuthorizeApi;
import com.xiaomi.account.openauth.XMAuthericationException;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class used to obtain user xiaomi account detail information.
 */
public class XiaomiAccountHelper {
    private static final String TAG = "XiaomiAccountHelper";

    public static class UserInfo {
        public People people;
        public String name;
        public String icon;
        public String id;
    }

    public interface ResultListener {
        void onResult(UserInfo userInfo);
    }

    private static class GetPeopleInfoTask extends AsyncTask<Void, Void, JSONObject> {
        private String mAccessToken;
        private long mExpiresIn;
        private String mMacKey;
        private String mMacAlgorithm;

        private ResultListener mListener;

        public GetPeopleInfoTask(String accessToken, String expiresIn, String macKey, String macAlgorithm, ResultListener listener) {
            mAccessToken = accessToken;
            mExpiresIn = Long.valueOf(expiresIn);
            mMacKey = macKey;
            mMacAlgorithm = macAlgorithm;
            mListener = listener;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return getPeopleInfo(mAccessToken, mMacKey, mMacAlgorithm);
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            UserInfo userInfo = new UserInfo();
            if (data != null) {
                Logger.d(TAG, "data: " + data.toString());
                userInfo.id = data.optString("userId");
                userInfo.name = data.optString("miliaoNick");
                userInfo.icon = data.optString("miliaoIcon_75");
                userInfo.people = PeopleFactory.createOauthPeople(mAccessToken, userInfo.id, mExpiresIn, mMacKey, mMacAlgorithm);
            }

            mListener.onResult(userInfo.people != null ? userInfo : null);
        }

    }

    public static void getUserInfoAsync(String accessToken, String expiresIn, String macKey, String macAlgorithm, ResultListener listener) {
        new GetPeopleInfoTask(accessToken, expiresIn, macKey, macAlgorithm, listener).execute();
    }

    private static JSONObject getPeopleInfo(String accessToken, String macKey, String macAlgorithm) {
        String response;
        AppConfiguration appConfig = MiotManager.getInstance().getAppConfig();

        try {
            response = AuthorizeApi.doHttpGet("/user/profile",
                    appConfig.getAppId(),
                    accessToken,
                    macKey,
                    macAlgorithm);
        } catch (XMAuthericationException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
            return null;
        }

        Log.d(TAG, response);
        JSONObject jobject;
        try {
            jobject = new JSONObject(response);
        } catch (JSONException e) {
            return null;
        }


        return jobject.optJSONObject("data");

    }
}