package com.mi.activity.main;

import android.accounts.OperationCanceledException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mi.account.XiaomiAccountGetPeopleInfoTask;
import com.mi.setting.AppConfig;
import com.mi.test.R;
import com.mi.utils.ToolbarActivity;
import com.miot.api.MiotManager;
import com.miot.common.ReturnCode;
import com.miot.common.exception.MiotException;
import com.miot.common.people.People;
import com.xiaomi.account.openauth.XMAuthericationException;
import com.xiaomi.account.openauth.XiaomiOAuthFuture;
import com.xiaomi.account.openauth.XiaomiOAuthResults;
import com.xiaomi.account.openauth.XiaomiOAuthorize;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AccountActivity extends ToolbarActivity {
    private static final String TAG = "AccountActivity";

    @InjectView(R.id.tv_account_info)
    TextView tvAccountInfo;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.btn_logout)
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.inject(this);

        initUserInfo();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountLogin();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountLogout();
            }
        });
    }

    @Override
    protected Pair<Integer, Boolean> getCustomTitle() {
        return new Pair<>(R.string.title_toolbar_accountmanager, true);
    }

    private void login(String userInfo){
        if (TextUtils.isEmpty(userInfo)) {
            logout();
        } else {
            tvAccountInfo.setText(userInfo);
            tvAccountInfo.setTextColor(getResources().getColor(R.color.green));
        }
    }

    private void logout(){
        tvAccountInfo.setText(R.string.account_not_login);
        tvAccountInfo.setTextColor(Color.RED);
    }

    private void initUserInfo() {
        String userInfo = "";

        do {
            People people = MiotManager.getPeople();
            if (people == null) {
                break;
            }

            userInfo = getString(R.string.account_logined, people.getUserName(), people.getUserId());
        } while (false);

        login(userInfo);
    }

    private int accountLogin() {
        if (MiotManager.getPeopleManager().isLogin()) {
            Log.d(TAG, "people already login");
            showToast(R.string.already_login);
            return ReturnCode.OK;
        }

        int[] scope = new int[]{1, 3, 6000};
        XiaomiOAuthFuture<XiaomiOAuthResults> future = new XiaomiOAuthorize()
                .setAppId(AppConfig.OAUTH_APP_ID)
                .setRedirectUrl(AppConfig.OAUTH_REDIRECT_URI)
                .setScope(scope)
                .startGetAccessToken(this);
        waitFutureResult(future);

        return ReturnCode.OK;
    }

    private void accountLogout() {
        try {
            MiotManager.getPeopleManager().deletePeople();
            logout();
        } catch (MiotException e) {
            e.printStackTrace();
        }
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

        new XiaomiAccountGetPeopleInfoTask(accessToken, expiresIn, macKey, macAlgorithm,
                new XiaomiAccountGetPeopleInfoTask.Handler() {
                    @Override
                    public void onSucceed(People people) {
                        Log.d(TAG, "XiaomiAccountGetPeopleInfoTask OK");
                        try {
                            MiotManager.getPeopleManager().savePeople(people);
                            initUserInfo();
                        } catch (MiotException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed() {
                        Log.d(TAG, "XiaomiAccountGetPeopleInfoTask Failed");
                    }
                }).execute();
    }

}