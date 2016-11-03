package com.xiaomi.xhome.mamlplugin;

import android.view.View;

import com.xiaomi.xhome.maml.ScreenContext;
import com.xiaomi.xhome.maml.ScreenElementRoot;
import com.xiaomi.xhome.maml.elements.ScreenElement;

/**
 * Created by ruijun on 3/31/16.
 */
public class DeviceViewScreenElementRoot extends ScreenElementRoot {
    private View mDeviceView;

    public DeviceViewScreenElementRoot(ScreenContext c) {
        super(c);
    }

    public void setDeviceView(View dv) {
        mDeviceView = dv;
    }

    @Override
    public void haptic(int effectId) {
        super.haptic(effectId);
//        if (mDeviceView != null) {
//            mDeviceView.performHapticFeedback(effectId);
//        }
    }

    @Override
    public void onUIInteractive(ScreenElement e, String action) {
        super.onUIInteractive(e, action);
        if (mDeviceView != null) {
            if ("up".equals(action)) {
//                mDeviceView.playSoundEffect(SoundEffectConstants.CLICK);
            }
//            if ("up".equals(action) && "down".equals(action)) {
//                mDeviceView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//            }
        }
    }
}
