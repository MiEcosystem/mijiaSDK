package com.mi.activity.device;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mi.device.ChuangmiPlugM1;
import com.mi.device.PlugBaseService;
import com.mi.test.R;
import com.mi.utils.BaseActivity;
import com.mi.utils.TestConstants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import miot.api.CompletionHandler;
import miot.api.MiotManager;
import miot.api.device.AbstractDevice;
import miot.typedef.exception.MiotException;

public class PlugActivity extends BaseActivity {
    private static String TAG = PlugActivity.class.getSimpleName();

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
    @InjectView(R.id.btn_set_power)
    Button btnSetPower;
    @InjectView(R.id.btn_set_wifiled)
    Button btnSetWifiLed;
    @InjectView(R.id.tv_power)
    TextView tvPower;
    @InjectView(R.id.tv_wifiled)
    TextView tvWifiLed;
    @InjectView(R.id.tv_temperature)
    TextView tvTemperature;

    private ChuangmiPlugM1 mPlugM1;
    private PlugBaseService mBaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug);
        ButterKnife.inject(this);

        AbstractDevice abstractDevice = getIntent().getParcelableExtra(TestConstants.EXTRA_DEVICE);
        if (abstractDevice != null && abstractDevice instanceof ChuangmiPlugM1) {
            mPlugM1 = (ChuangmiPlugM1) abstractDevice;
        } else {
            Log.e(TAG, "abstractDevice error");
            finish();
            return;
        }
        mBaseService = mPlugM1.mPlugBaseService;

        setTitle(mPlugM1.getName());
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
        btnSetPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPower();
            }
        });
        btnSetWifiLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWifiLed();
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
        try {
            MiotManager.getDeviceManager().takeOwnership(mPlugM1, new CompletionHandler() {
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
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void unBind() {
        try {
            MiotManager.getDeviceManager().disclaimOwnership(mPlugM1, new CompletionHandler() {
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
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void getProperties() {
        try {
            mBaseService.getProperties(new PlugBaseService.GetPropertiesCompletionHandler() {
                @Override
                public void onSucceed(final PlugBaseService.Power power, final PlugBaseService.WifiLed wifiLed, final Integer temperature) {
                    show("getProperties", String.format("Power=%s WifiLed=%s Temperature=%s", power, wifiLed, temperature));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvPower.setText(String.valueOf(power));
                            tvWifiLed.setText(String.valueOf(wifiLed));
                            tvTemperature.setText(String.valueOf(temperature));
                        }
                    });
                }

                @Override
                public void onFailed(int errCode, String description) {
                    show("getProperties", String.format("Failed, code: %d %s", errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void subscribeNotification() {
        try {
            mBaseService.subscribeNotifications(new CompletionHandler() {
                @Override
                public void onSucceed() {
                    show("subscribe", "OK");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    show("subscribe", String.format("Failed, code: %d %s", errCode, description));
                }
            }, new PlugBaseService.PropertyNotificationListener() {
                @Override
                public void onPowerChanged(final PlugBaseService.Power power) {
                    show("onPowerChanged: ", String.valueOf(power));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvPower.setText(String.valueOf(power));
                        }
                    });
                }

                @Override
                public void onWifiLedChanged(final PlugBaseService.WifiLed wifiLed) {
                    show("onWifiLedChanged: ", String.valueOf(wifiLed));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvWifiLed.setText(String.valueOf(wifiLed));
                        }
                    });
                }

                @Override
                public void onTemperatureChanged(final Integer temperature) {
                    show("onWifiLedChanged: ", String.valueOf(temperature));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvTemperature.setText(String.valueOf(temperature));
                        }
                    });
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void unSubscribeNotification() {
        try {
            mBaseService.unsubscribeNotifications(new CompletionHandler() {
                @Override
                public void onSucceed() {
                    show("unSubscribe", "OK");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    show("unSubscribe", String.format("Failed, code: %d %s", errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void setPower() {
        try {
            PlugBaseService.Power power = PlugBaseService.Power.on;
            mBaseService.setPower(power, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    show("setPower", "OK");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    show("setPower", String.format("Failed, code: %d %s", errCode, description));
                }

            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void setWifiLed() {
        try {
            PlugBaseService.WifiLed wifiLed = PlugBaseService.WifiLed.on;
            mBaseService.setWifiLed(wifiLed, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    show("setWifiLed", "OK");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    show("setWifiLed", String.format("Failed, code: %d %s", errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }
}
