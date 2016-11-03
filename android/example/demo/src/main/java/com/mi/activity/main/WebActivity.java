package com.mi.activity.main;

import android.os.Bundle;
import android.webkit.WebView;

import com.mi.test.R;
import com.mi.utils.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by stephen on 9/21/16.
 */
public class WebActivity extends BaseActivity {
    @InjectView(R.id.wv_client)
    WebView mWvClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.inject(this);

        mWvClient.getSettings().setJavaScriptEnabled(true);
        mWvClient.loadUrl("http://localhost:8081/web/ChuangmiPlugM1.html?did=46131192");
    }
}
