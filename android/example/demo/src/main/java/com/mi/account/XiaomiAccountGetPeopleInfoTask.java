package com.mi.account;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.xiaomi.account.openauth.AuthorizeApi;
import com.xiaomi.account.openauth.XMAuthericationException;

import org.json.JSONException;
import org.json.JSONObject;

import miot.api.MiotManager;
import miot.service.common.utils.Logger;
import miot.typedef.config.AppConfiguration;
import miot.typedef.people.People;
import miot.typedef.people.PeopleFactory;

/**
 * This class used to obtain user xiaomi account detail information.
 */
public class XiaomiAccountGetPeopleInfoTask extends AsyncTask<Void, Void, People> {

    private static final String TAG = XiaomiAccountGetPeopleInfoTask.class.getSimpleName();

    private String mAccessToken;
    private long mExpiresIn;
    private String mMacKey;
    private String mMacAlgorithm;
    private Handler mHandler;

    public interface Handler {
        void onSucceed(People people);

        void onFailed();
    }

    public XiaomiAccountGetPeopleInfoTask(String accessToken, String expiresIn, String macKey, String macAlgorithm, Handler handler) {
        mAccessToken = accessToken;
        mExpiresIn = Long.valueOf(expiresIn);
        mMacKey = macKey;
        mMacAlgorithm = macAlgorithm;
        mHandler = handler;
    }

    @Override
    protected People doInBackground(Void... params) {
        return getPeopleInfo(mAccessToken, mMacKey, mMacAlgorithm);
    }

    @Override
    protected void onPostExecute(People people) {
        if (people != null) {
            mHandler.onSucceed(people);
        } else {
            mHandler.onFailed();
        }
    }

    private People getPeopleInfo(String accessToken, String macKey, String macAlgorithm) {
        People people = null;

        do {
            String response = null;
            AppConfiguration appConfig = MiotManager.getInstance().getAppConfig();

            try {
                response = AuthorizeApi.doHttpGet("/user/profile",
                        appConfig.getAppId(),
                        accessToken,
                        macKey,
                        macAlgorithm);
            } catch (XMAuthericationException e) {
                e.printStackTrace();
                break;
            }

            JSONObject jobject = null;

            try {
                jobject = new JSONObject(response);
            } catch (JSONException e) {
                break;
            }

            Log.e(TAG, response);

            JSONObject data = jobject.optJSONObject("data");

            if (data != null) {
                //TODO: get account info, such as userId, name and icon...
                Logger.d(TAG, "data: " + data.toString());
                String userId = data.optString("userId");
                String name = data.optString("miliaoNick");
                String icon = data.optString("miliaoIcon");
                String icon75 = data.optString("miliaoIcon_75");
                String icon90 = data.optString("miliaoIcon_90");
                String icon120 = data.optString("miliaoIcon_120");
                String icon320 = data.optString("miliaoIcon_320");
                people = PeopleFactory.createOauthPeople(accessToken, userId, mExpiresIn, macKey, macAlgorithm);
                people.setUserName(name);
                people.setIcon(icon);
                people.setIcon75(icon75);
                people.setIcon90(icon90);
                people.setIcon120(icon120);
                people.setIcon320(icon320);
            }
        } while (false);

        return people;
    }
}