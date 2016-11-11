package com.mi.activity.universal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mi.adapter.ServiceAdapter;
import com.mi.device.ChuangmiPlugM1;
import com.mi.device.PlugBaseService;
import com.mi.test.R;
import com.mi.utils.BaseActivity;
import com.mi.utils.TestConstants;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import miot.api.CommonHandler;
import miot.api.CompletionHandler;
import miot.api.DeviceManager;
import miot.api.MiotManager;
import miot.api.device.AbstractDevice;
import miot.typedef.device.Action;
import miot.typedef.device.Device.Ownership;
import miot.typedef.device.Service;
import miot.typedef.device.firmware.MiotFirmware;
import miot.typedef.exception.MiotException;
import miot.typedef.property.Property;
import miot.typedef.share.SharedUser;
import miot.typedef.timer.CrontabTime;
import miot.typedef.timer.DayOfWeek;
import miot.typedef.timer.Timer;

public class UniversalDeviceActivity extends BaseActivity {
    private static String TAG = UniversalDeviceActivity.class.getSimpleName();
    @InjectView(R.id.tv_log)
    TextView tvLog;
    @InjectView(R.id.tv_service_title)
    TextView tvServiceTitle;
    @InjectView(R.id.lv_services)
    ListView lvServices;
    @InjectView(R.id.btn_device_manager)
    Button btnDeviceManager;

    private AbstractDevice mAbstractDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_device);
        ButterKnife.inject(this);

        mAbstractDevice = getIntent().getParcelableExtra(TestConstants.EXTRA_DEVICE);
        if (mAbstractDevice == null) {
            Log.e(TAG, "mAbstractDevice is null");
            finish();
            return;
        }

        initTitle();
        initLog();
        initServiceList();
        btnDeviceManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageDevice();
            }
        });
    }

    public void manageDevice() {
        String menu[] = new String[]{
                "0: 绑定",
                "1: 解除绑定",
                "2: 检查固件版本",
                "3: 开始升级固件",
                "4: 读取固件升级进度",
                "5: 读取定时器列表",
                "6: 添加定时器",
                "7: 删除定时器",
                "8: 分享设备",
                "9: 取消分享",
                "10: 获取被分享者",
        };

        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("管理设备")
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: " + which);

                        switch (which) {
                            case 0:
                                takeOwnership();
                                break;

                            case 1:
                                disclaimOwnership();
                                break;

                            case 2:
                                queryFirmware();
                                break;

                            case 3:
                                upgradeFirmware();
                                break;

                            case 4:
                                queryFirmwareUpgradingInfo();
                                break;

                            case 5:
                                getTimerList();
                                break;

                            case 6:
                                addTimer();
                                break;

                            case 7:
                                removeTimer();
                                break;

                            case 8:
                                shareDevice();
                                break;

                            case 9:
                                cancelShare();
                                break;

                            case 10:
                                queryShareUsers();
                                break;
                        }
                    }
                })
                .setNeutralButton("取消", null)
                .create();
        alert.show();
    }

    private void initTitle() {
        String title = String.format("%s(%s)",
                this.getString(R.string.title_activity_universaldevice),
                mAbstractDevice.getName());
        this.setTitle(title);
    }

    private void initLog() {
        String log = String.format("设备ID: %s\r\n状态: %s\r\n设备类型: %s\r\n设备主人: %s\r\n",
                mAbstractDevice.getDeviceId(),
                mAbstractDevice.getDevice().isOnline() ? "在线" : "离线",
                mAbstractDevice.getDevice().getConnectionType().toString(),
                mAbstractDevice.getDevice().getOwnerInfo().getUserId());
        tvLog.setText(log);
        tvLog.setMovementMethod(new ScrollingMovementMethod());
    }

    private void initServiceList() {
        String title = String.format("服务列表(%d)", mAbstractDevice.getDevice().getServices().size());
        tvServiceTitle.setText(title);

        ServiceAdapter serviceAdapter = new ServiceAdapter(this);
        serviceAdapter.addItems(mAbstractDevice.getDevice().getServices());

        lvServices.setAdapter(serviceAdapter);
        lvServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Service service = (Service) adapterView.getItemAtPosition(position);
                if (service == null) {
                    return;
                }

                Intent intent = new Intent(UniversalDeviceActivity.this, UniversalServiceActivity.class);
                intent.putExtra(TestConstants.EXTRA_SERVICE, service);
                startActivity(intent);
            }
        });
    }

    private void showLog(String info) {
        final String newLog;

        CharSequence oldLog = tvLog.getText();
        if (oldLog.length() > 1024 * 10) {
            newLog = String.format("%s\r\n", info);
        } else {
            newLog = String.format("%s%s\r\n", oldLog, info);
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tvLog.setText(newLog);
            }
        });
    }

    private void takeOwnership() {
        Log.d(TAG, "takeOwnership");

        Ownership owner = mAbstractDevice.getOwnership();
        if (owner == Ownership.MINE) {
            showLog("绑定失败，因为这是你自己的设备！");
            return;
        }

        try {
            MiotManager.getDeviceManager().takeOwnership(mAbstractDevice, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    showLog("takeOwnership: OK");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog(String.format("takeOwnership Failed, code: %d %s", errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private void disclaimOwnership() {
        Log.d(TAG, "disclaimOwnership");

        Ownership owner = mAbstractDevice.getOwnership();
        if (owner != Ownership.MINE) {
            showLog("解绑失败，因为这不是自己的设备！");
            return;
        }

        try {
            MiotManager.getDeviceManager().disclaimOwnership(mAbstractDevice, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    showLog("disclaimOwnership: OK");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog(String.format("disclaimOwnership Failed, code: %d %s", errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private void queryFirmware() {
        MiotFirmware firmware = mAbstractDevice.getMiotFirmware();
        if (firmware != null) {
            logFirmware(firmware);
            return;
        }

        try {
            mAbstractDevice.queryFirmwareInfo(new DeviceManager.QueryFirmwareHandler() {
                @Override
                public void onSucceed(MiotFirmware firmware) {
                    showLog("queryFirmwareUpgradeInfo: OK");
                    logFirmware(firmware);
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog(String.format("queryFirmwareInfo Failed, code: %d %s", errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private void upgradeFirmware() {
        try {
            mAbstractDevice.startUpgradeFirmware(new CompletionHandler() {
                @Override
                public void onSucceed() {
                    showLog("upgradeFirmware: OK");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog(String.format("upgradeFirmware Failed, code: %d %s", errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private void queryFirmwareUpgradingInfo() {
        try {
            mAbstractDevice.queryFirmwareUpgradeInfo(new DeviceManager.QueryFirmwareHandler() {
                @Override
                public void onSucceed(MiotFirmware firmware) {
                    showLog("queryFirmwareUpgradeInfo: OK");
                    logFirmware(firmware);
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog(String.format("queryFirmwareUpgradeInfo Failed, code: %d %s", errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private void logFirmware(MiotFirmware firmware) {
        String log = String.format("isUpgrading: %s\r\n", Boolean.toString(firmware.isUpgrading()))
                + String.format("currentVersion: %s\r\n", firmware.getCurrentVersion())
                + String.format("latestVersion: %s\r\n", firmware.getLatestVersion())
                + String.format("isLatestVersion: %s\r\n", firmware.isLatestVersion())
                + String.format("ota_progress: %d\r\n", firmware.getOtaProgress())
                + String.format("ota_status: %s\r\n", firmware.getOtaStatus())
                + String.format("description: %s\r\n", firmware.getDescription());
        showLog(log);
    }


    private void getTimerList() {
        try {
            MiotManager.getDeviceManager().queryTimerList(mAbstractDevice.getDeviceId(), new DeviceManager.TimerListener() {
                @Override
                public void onSucceed(List<Timer> timers) {
                    showLog("queryTimerList: OK: " + timers.size());
                    for (Timer timer : timers) {
                        logTimer(timer);
                    }
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog("queryTimerList: failed: " + errCode + " - " + description);
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private void logTimer(Timer timer) {
        if (timer == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("timerId: ");
        sb.append(timer.getTimerId());
        sb.append(" name: ");
        sb.append(timer.getName());


        sb.append(" startTime: ");
        CrontabTime startTime = timer.getStartTime();
        sb.append(startTime.getHour());
        sb.append(":");
        sb.append(startTime.getMinute());
        sb.append(" repeat: ");
        sb.append(startTime.getDayOfWeeks());
        sb.append(" startAction: ");
        Action startAction = timer.getStartAction();
        for (Property p : startAction.getInArguments()) {
            sb.append(p.getDefinition().getFriendlyName());
            sb.append(" ");
            sb.append(p.getValue().toString());
            sb.append("  ");
        }
        Action endAction = timer.getEndAction();
        for (Property p : endAction.getInArguments()) {
            sb.append(p.getDefinition().getFriendlyName());
            sb.append(" ");
            sb.append(p.getValue().toString());
            sb.append("  ");
        }
        Log.d(TAG, sb.toString());
    }

    //TODO: 以下代码仅供参考，这里用空调作一个例子，具体使用使用自己设备代替
    private void addTimer() {
        Timer timer = new Timer();
        timer.setName("星期一到星期三，晚上８点半开始打开灯泡，５个小时后再关闭灯泡");
        timer.setDeviceId(mAbstractDevice.getDeviceId());
        timer.setPushEnabled(false);
        //定时的开关
        timer.setTimerEnabled(true);
        //定时开始时的开关
        timer.setTimerStartEnabled(true);
        //定时结束时的开关
        timer.setTimerEndEnabled(true);

        CrontabTime startTime = new CrontabTime();
        startTime.setHour(15);
        startTime.setMinute(20);
        startTime.addDayOfWeek(DayOfWeek.MONDAY);
        startTime.addDayOfWeek(DayOfWeek.TUESDAY);
        startTime.addDayOfWeek(DayOfWeek.WEDNESDAY);
        startTime.addDayOfWeek(DayOfWeek.THURSDAY);
        startTime.addDayOfWeek(DayOfWeek.FRIDAY);
        timer.setStartTime(startTime);

        //TODO: action是和具体的设备关联的，这里仅供参考
        if (mAbstractDevice instanceof ChuangmiPlugM1) {
            PlugBaseService service = ((ChuangmiPlugM1) mAbstractDevice).mPlugBaseService;

            Action actionOn = service.newAction(PlugBaseService.ACTION_setPower);
            actionOn.setArgumentValue(PlugBaseService.PROPERTY_Power, PlugBaseService.Power.on.toString());
            actionOn.setDescription("打开开关");
            timer.setStartAction(actionOn);

            Action actionOff = service.newAction(PlugBaseService.ACTION_setPower);
            actionOff.setArgumentValue(PlugBaseService.PROPERTY_Power, PlugBaseService.Power.off.toString());
            actionOff.setDescription("关闭开关");
            timer.setEndAction(actionOff);
        }

        CrontabTime endTime = new CrontabTime();
        endTime.setHour(15);
        endTime.setMinute(40);
        endTime.addDayOfWeek(DayOfWeek.MONDAY);
        endTime.addDayOfWeek(DayOfWeek.TUESDAY);
        endTime.addDayOfWeek(DayOfWeek.WEDNESDAY);
        endTime.addDayOfWeek(DayOfWeek.THURSDAY);
        endTime.addDayOfWeek(DayOfWeek.FRIDAY);
        timer.setEndTime(endTime);
        try {
            MiotManager.getDeviceManager().addTimer(timer, new DeviceManager.AddTimerCompletionHandler() {
                @Override
                public void onSucceed(int timerId) {
                    showLog("addTimer: onSucceed: " + timerId);
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog("addTimer: failed: " + errCode + " - " + description);
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private void editTimer() {
        //TODO：具体逻辑和addTimer相似：区别在于，addTimer是没有timerId的，而从云端获取的定时是有timerId的，
        //TODO：具体使用时不要修改timerId
    }

    private void removeTimer() {
        int timerId = 15825126;
        try {
            MiotManager.getDeviceManager().removeTimer(timerId, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    showLog("removeTimer: onSucceed");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog("removeTimer: failed: " + errCode + " - " + description);
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private String mUserId = ".....";

    public void shareDevice() {
        try {
            MiotManager.getDeviceManager().shareDevice(mAbstractDevice, mUserId, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    showLog("shareDevice: onSucceed");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog("shareDevice: failed: " + errCode + " - " + description);
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void cancelShare() {
        try {
            MiotManager.getDeviceManager().cancelShare(mAbstractDevice, mUserId, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    showLog("cancelShare: onSucceed");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog("cancelShare: failed: " + errCode + " - " + description);
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void queryShareUsers() {
        try {
            MiotManager.getDeviceManager().querySharedUsers(mAbstractDevice, new CommonHandler<List<SharedUser>>() {
                @Override
                public void onSucceed(List<SharedUser> result) {
                    for (SharedUser sharedUser : result) {
                        Log.d(TAG, sharedUser.getUserId() + "  " + sharedUser.getUserName() + " " + sharedUser.getStatus());
                    }
                    showLog("queryShareUsers: onSucceed");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog("queryShareUsers: failed: " + errCode + " - " + description);
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }
}