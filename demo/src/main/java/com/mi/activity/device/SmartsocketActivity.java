package com.mi.activity.device;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mi.device.SmartSocketBase;
import com.mi.device.SmartSocketBaseService;
import com.mi.test.R;
import com.mi.utils.BaseActivity;
import com.mi.utils.TestConstants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import miot.api.CompletionHandler;
import miot.api.MiotManager;
import miot.api.device.AbstractDevice;
import miot.typedef.ReturnCode;

public class SmartsocketActivity extends BaseActivity {
    private static String TAG = SmartsocketActivity.class.getSimpleName();

    @InjectView(R.id.btn_bind)
    Button btnBind;
    @InjectView(R.id.btn_unbind)
    Button btnUnbind;
    @InjectView(R.id.btn_get_properties)
    Button btnGetProperties;
    @InjectView(R.id.btn_subscribe)
    Button btnSubscribe;
    @InjectView(R.id.btn_unsubscribe)
    Button btnUnsubscribe;
    @InjectView(R.id.btn_set_plug_on)
    Button btnSetPlugOn;
    @InjectView(R.id.btn_set_plug_off)
    Button btnSetPlugOff;
    @InjectView(R.id.btn_set_usb_on)
    Button btnSetUsbOn;
    @InjectView(R.id.btn_set_usb_off)
    Button btnSetUsbOff;
    @InjectView(R.id.tv_power)
    TextView tvPower;
    @InjectView(R.id.tv_usb)
    TextView tvUsb;

    private SmartSocketBase mSmartSocket;
    private SmartSocketBaseService mSmartSocketBaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartsocket);
        ButterKnife.inject(this);

        AbstractDevice abstractDevice = getIntent().getParcelableExtra(TestConstants.EXTRA_DEVICE);
        if (abstractDevice != null && abstractDevice instanceof SmartSocketBase) {
            mSmartSocket = (SmartSocketBase) abstractDevice;
        } else {
            Log.e(TAG, "abstractDevice error");
            finish();
            return;
        }
        mSmartSocketBaseService = mSmartSocket.mSmartSocketBaseService;

        setTitle(mSmartSocket.getName());
        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bind();
            }
        });
        btnUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unBind();
            }
        });
        btnGetProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProperties();
            }
        });
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeNotification();
            }
        });
        btnUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSubscribeNotification();
            }
        });
        btnSetPlugOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlugOn();
            }
        });
        btnSetPlugOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlugOff();
            }
        });
        btnSetUsbOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsbOn();
            }
        });
        btnSetUsbOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsbOff();
            }
        });
    }

    private void show(final String name, final String result) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(String.format("%s -> %s", name, result));
            }
        });
    }

    public void bind() {
        MiotManager.getDeviceManager().takeOwnership(this.mSmartSocket, new CompletionHandler() {
            @Override
            public void onSucceed() {
                show("takeOwnership", "OK");
            }

            @Override
            public void onFailed(int errCode, String description) {
                show("takeOwnership",
                        String.format("Failed, code: %d %s", errCode, description));
            }
        });
    }

    public void unBind() {
        MiotManager.getDeviceManager().disclaimOwnership(this.mSmartSocket, new CompletionHandler() {
            @Override
            public void onSucceed() {
                show("disclaimOwnership", "OK");
            }

            @Override
            public void onFailed(int errCode, String description) {
                show("disclaimOwnership",
                        String.format("Failed, code: %d %s", errCode, description));
            }
        });
    }

    public void getProperties() {
        int ret = mSmartSocketBaseService.getProperties(new SmartSocketBaseService.GetPropertiesCompletionHandler() {
            @Override
            public void onSucceed(final Boolean usbStatus, final Boolean powerStatus) {
                show("getProperties", String.format("usbStatus=%s powerStatus=%s", usbStatus, powerStatus));
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvPower.setText(String.valueOf(powerStatus));
                        tvUsb.setText(String.valueOf(usbStatus));
                    }
                });
            }

            @Override
            public void onFailed(int errCode, String description) {
                show("getProperties", String.format("Failed, code: %d %s", errCode, description));
            }
        });
        if (ret != ReturnCode.OK) {
            show("getProperties", String.format("invoke failed: %d", ret));
        }
    }

    public void subscribeNotification() {
        mSmartSocketBaseService.subscribeNotifications(new CompletionHandler() {
            @Override
            public void onSucceed() {
                show("subscribe", "OK");
            }

            @Override
            public void onFailed(int errCode, String description) {
                show("subscribe", String.format("Failed, code: %d %s", errCode, description));
            }
        }, new SmartSocketBaseService.PropertyNotificationListener() {
            @Override
            public void onUsbStatusChanged(final Boolean usbStatus) {
                show("usbStatusChanged: ", String.valueOf(usbStatus));
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvUsb.setText(String.valueOf(usbStatus));
                    }
                });
            }

            @Override
            public void onPowerStatusChanged(final Boolean powerStatus) {
                show("powerStatusChanged: ", String.valueOf(powerStatus));
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvPower.setText(String.valueOf(powerStatus));
                    }
                });
            }
        });
    }

    public void unSubscribeNotification() {
        mSmartSocketBaseService.unsubscribeNotifications(new CompletionHandler() {
            @Override
            public void onSucceed() {
                show("unSubscribe", "OK");
            }

            @Override
            public void onFailed(int errCode, String description) {
                show("unSubscribe", String.format("Failed, code: %d %s", errCode, description));
            }
        });
    }

    public void setPlugOn() {
        int ret = mSmartSocketBaseService.setPlugOn(new CompletionHandler() {
            @Override
            public void onSucceed() {
                show("setPlugOn", "OK");
            }

            @Override
            public void onFailed(int errCode, String description) {
                show("setPlugOn", String.format("Failed, code: %d %s", errCode, description));
            }

        });

        if (ret != ReturnCode.OK) {
            show("setPlugOn", String.format("invoke failed: %d", ret));
        }
    }

    public void setPlugOff() {
        Log.d(TAG, "setPlugOff");

        int ret = mSmartSocketBaseService.setPlugOff(new CompletionHandler() {

            @Override
            public void onSucceed() {
                show("setPlugOff", "OK");
            }

            @Override
            public void onFailed(int errCode, String description) {
                show("setPlugOff", String.format("Failed, code: %d %s", errCode, description));
            }

        });

        if (ret != ReturnCode.OK) {
            show("setPlugOff", String.format("invoke failed: %d", ret));
        }
    }

    public void setUsbOn() {
        int ret = mSmartSocketBaseService.setUsbPlugOn(new CompletionHandler() {
            @Override
            public void onSucceed() {
                show("setUsbPlugOn", "OK");
            }

            @Override
            public void onFailed(int errCode, String description) {
                show("setUsbPlugOn", String.format("Failed, code: %d %s", errCode, description));
            }
        });

        if (ret != ReturnCode.OK) {
            show("setUsbPlugOn", String.format("invoke failed: %d", ret));
        }
    }

    public void setUsbOff() {
        int ret = mSmartSocketBaseService.setUsbPlugOff(new CompletionHandler() {

            @Override
            public void onSucceed() {
                show("setUsbPlugOff", "OK");
            }

            @Override
            public void onFailed(int errCode, String description) {
                show("setUsbPlugOff", String.format("Failed, code: %d %s", errCode, description));
            }

        });

        if (ret != ReturnCode.OK) {
            show("setUsbPlugOff", String.format("invoke failed: %d", ret));
        }
    }
}
