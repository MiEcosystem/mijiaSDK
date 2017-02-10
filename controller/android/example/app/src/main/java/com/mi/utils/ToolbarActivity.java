package com.mi.utils;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.TextView;

import com.mi.test.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by vww on 16-12-20.
 */

public class ToolbarActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);

        Pair<Integer, Boolean> params = getCustomTitle();
        if (params.second) {
            setShowCustomTileAndBackArrow(params.first);
        } else {
            setShowCustomTile(params.first);
        }
    }

    private void setShowCustomTile(int resid){
        Log.i("ToolbarActivity", mToolbar+"");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbarTitle.setText(resid);
        }

    }

    private void setShowCustomTileAndBackArrow(int resid){
        setShowCustomTile(resid);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    protected Pair<Integer, Boolean> getCustomTitle(){
        return new Pair<>(0, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
