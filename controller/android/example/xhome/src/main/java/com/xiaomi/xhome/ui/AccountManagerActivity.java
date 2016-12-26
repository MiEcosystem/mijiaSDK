package com.xiaomi.xhome.ui;

import android.accounts.OperationCanceledException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miot.api.MiotManager;
import com.miot.common.exception.MiotException;
import com.xiaomi.account.openauth.XMAuthericationException;
import com.xiaomi.account.openauth.XiaomiOAuthFuture;
import com.xiaomi.account.openauth.XiaomiOAuthResults;
import com.xiaomi.account.openauth.XiaomiOAuthorize;
import com.xiaomi.xhome.AppConfig;
import com.xiaomi.xhome.R;
import com.xiaomi.xhome.XConfig;
import com.xiaomi.xhome.XHomeApplication;
import com.xiaomi.xhome.util.XiaomiAccountHelper;

import java.io.IOException;

public class AccountManagerActivity extends ActionBarActivity {
    private static final String TAG = AccountManagerActivity.class.getSimpleName();

    private TextView tvAccountInfo;
    private Button btnLogin;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvAccountInfo = (TextView) findViewById(R.id.tv_account_info);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogout = (Button) findViewById(R.id.btn_logout);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountLogin();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountLogout())
                    finish();
            }
        });

        updateUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setButtonVisibility();
    }

    private void setButtonVisibility() {
        boolean loggedIn = MiotManager.getPeopleManager().isLogin();
        btnLogin.setVisibility(loggedIn ? View.GONE : View.VISIBLE);
        btnLogout.setVisibility(loggedIn ? View.VISIBLE : View.GONE);
    }

    private void updateUserInfo() {
        String userId = getString(R.string.account_not_login);
//
//        People people = MiotManager.getPeople();
//        if (people != null) {
//            userId = people.getUserId();
//        }

        userId = XHomeApplication.getInstance().getConfig().getString(XConfig.CONFIG_USER_ID, null);
        String name = XHomeApplication.getInstance().getConfig().getString(XConfig.CONFIG_USER_NAME, null);

        tvAccountInfo.setText(name != null ? name + "  " + userId : userId);
        setButtonVisibility();
    }

    private boolean accountLogin() {
        if (!MiotManager.getInstance().isBound())
            return false;

        if (MiotManager.getPeopleManager().isLogin()) {
            Log.d(TAG, "people already logged in");

            return true;
        }

        XiaomiOAuthFuture<XiaomiOAuthResults> future = new XiaomiOAuthorize()
                .setAppId(AppConfig.OAUTH_APP_ID)
                .setRedirectUrl(AppConfig.OAUTH_REDIRECT_URI)
                .setScope(null)
                .startGetAccessToken(this);
        waitFutureResult(future);

        return true;
    }

    private <V> void waitFutureResult(final XiaomiOAuthFuture<V> future) {
        new AsyncTask<Void, Void, V>() {
            Exception e;

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected V doInBackground(Void... params) {
                V v = null;
                try {
                    v = future.getResult();
                } catch (IOException e1) {
                    this.e = e1;
                } catch (OperationCanceledException e1) {
                    this.e = e1;
                } catch (XMAuthericationException e1) {
                    this.e = e1;
                }
                return v;
            }

            @Override
            protected void onPostExecute(V v) {
                if (v != null) {
                    if (v instanceof XiaomiOAuthResults) {
                        processAuthResult((XiaomiOAuthResults) v);
                    }
                } else if (e != null) {
                    e.printStackTrace();
                } else {
                    Log.e(TAG, "login failed");
                }
            }
        }.execute();
    }

    private void processAuthResult(XiaomiOAuthResults results) {
        String accessToken = results.getAccessToken();
        String expiresIn = results.getExpiresIn();
        String scope = results.getScopes();
        String state = results.getState();
        String tokenType = results.getTokenType();
        String macKey = results.getMacKey();
        String macAlgorithm = results.getMacAlgorithm();

        Log.d(TAG, "accessToken = " + accessToken);
        Log.d(TAG, "expiresIn = " + expiresIn);
        Log.d(TAG, "scope = " + scope);
        Log.d(TAG, "state = " + state);
        Log.d(TAG, "tokenType = " + tokenType);
        Log.d(TAG, "macKey = " + macKey);
        Log.d(TAG, "macAlgorithm = " + macAlgorithm);

        XiaomiAccountHelper.getUserInfoAsync(accessToken, expiresIn, macKey, macAlgorithm, new XiaomiAccountHelper.ResultListener() {
            @Override
            public void onResult(XiaomiAccountHelper.UserInfo userInfo) {
                if (userInfo != null) {
                    try {
                        MiotManager.getPeopleManager().savePeople(userInfo.people);
                        SharedPreferences.Editor edit = XHomeApplication.getInstance().getConfig().edit();
                        edit.putString(XConfig.CONFIG_USER_NAME, userInfo.name);
                        edit.putString(XConfig.CONFIG_USER_ID, userInfo.id);
                        edit.apply();
                        updateUserInfo();
                    } catch (MiotException e) {
                    }
                } else {
                    Log.e(TAG, "fail to get user info");
                }
            }
        });
    }

    private boolean accountLogout() {
        try {
            MiotManager.getPeopleManager().deletePeople();
            Log.d(TAG, "accountLogout");
            SharedPreferences.Editor edit = XHomeApplication.getInstance().getConfig().edit();
            edit.putString(XConfig.CONFIG_USER_NAME, null).putString(XConfig.CONFIG_USER_ID, null).apply();

            updateUserInfo();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}