package com.xiaomi.xhome.ui;

import android.content.Context;
import android.util.Log;

import com.xiaomi.xhome.data.Dashboard;
import com.xiaomi.xhome.maml.ScreenElementRoot;
import com.xiaomi.xhome.util.Utils;

import miot.api.CommonHandler;
import miot.api.CompletionHandler;
import miot.api.MiotManager;
import miot.typedef.exception.MiotException;
import miot.typedef.scene.SceneBean;

/**
 * Created by ruijun on 3/17/16.
 */
public class SceneView extends TileView {
    private static final String TAG = "SceneView";

    public SceneView(Context context, DashboardView dv, ScreenElementRoot root, Dashboard.DeviceInfo di) {
        super(context, dv, root, di);
    }

    public void runScene() {
        try {
            MiotManager.getDeviceManager().runScene(Utils.transSceneId(mDeviceInfo.id), new CompletionHandler() {
                @Override
                public void onSucceed() {
                }

                @Override
                public void onFailed(int errCode, String description) {
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void enableScene(final boolean enable) {
        try {

            CompletionHandler completionHandler = new CompletionHandler() {
                @Override
                public void onSucceed() {
                    getRoot().getVariables().put("SceneEnable", enable ? 1 : 0);
                    Log.d(TAG, "enable/disable scene: " + mDeviceInfo.name + " " + mDeviceInfo.id);
                }

                @Override
                public void onFailed(int errCode, String description) {
                    Log.e(TAG, "fail to enable/disable scene: " + mDeviceInfo.name + " " + mDeviceInfo.id);
                }
            };
            if (enable) {
                MiotManager.getDeviceManager().enableScene(Utils.transSceneId(mDeviceInfo.id), completionHandler);
            } else {
                MiotManager.getDeviceManager().disableScene(Utils.transSceneId(mDeviceInfo.id), completionHandler);
            }
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPoll() {
//        if (mDeviceInfo.model.equals(Dashboard.MODEL_SCENE)) {
        // scene
        try {
            MiotManager.getDeviceManager().queryScene(Utils.transSceneId(mDeviceInfo.id), new CommonHandler<SceneBean>() {
                @Override
                public void onSucceed(SceneBean result) {
                    if (result != null) {
                        getRoot().getVariables().put("SceneEnable", result.isEnable() ? 1 : 0);
                    }
                }

                @Override
                public void onFailed(int errCode, String description) {
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }
//    }
}
