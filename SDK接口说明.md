# SDK接口说明

## SDK目前提供了如下功能：
1. **获取设备列表**：获取设备
2. **快连设备**：用于将新设备联网，也就是快连（目前快连默认完成绑定设备）
3. **绑定设备**: 完成设备与用户的帐号绑定关系
4. **解绑设备**：允许用户解除与设备的绑定关系
5. **分享设备**：用户可以将设备分享给其他人（目前仅小米帐号）
6. **控制设备**：包括获取设备状态、下发指令和订阅设备事件
7. **固件更新**：查询最新固件、下发新的固件
8. **蓝牙操作**：包含蓝牙各种操作接口
9. **蓝牙双模快连**: 支持双模设备通过蓝牙传递配网信息

目前这些操作的log信息是默认打开的，可以通过一下接口关闭log信息

```Java
    com.miot.common.utils.Logger.enableLog(true);
```

### 获取设备列表
调用DeviceManager中的设备发现接口，可以参考demo中MiDeviceManager.java相关代码

```Java
    try {
        MiotManager.getDeviceManager().getRemoteDeviceList(new DeviceManager.DeviceHandler() {
            @Override
            public void onSucceed(List<AbstractDevice> devices) {
                //TODO
            }

            @Override
            public void onFailed(int errCode, String description) {
                //TODO
            }
        });
    } catch (MiotException e) {
        e.printStackTrace();
    }
```

### 快连设备
* 发现待连接设备
调用DeviceManager中的设备扫描接口，可以参考demo中MiDeviceManager.java相关代码

```Java
    List<DiscoveryType> types = new ArrayList<>();
    types.add(DiscoveryType.MIOT_WIFI);
    try {
        MiotManager.getDeviceManager().startScan(types, new DeviceManager.DeviceHandler() {
            @Override
            public void onSucceed(List<AbstractDevice> devices) {
                //TODO
            }

            @Override
            public void onFailed(int errCode, String description) {
                //TODO
            });
    } catch (MiotException e) {
        e.printStackTrace();
    }
```

需要注意的是，Android6.0加入运行时权限，扫描Wifi设备需要运行时权限的支持，关于运行时权限，详见[google文档](https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html?hl=zh-cn#behavior-runtime-permissions)，demo中有一个简单的示例，代码如下所示：

```Java
    private static final int REQUEST_LOCATION_PERMISSION = 10000;

    @TargetApi(23)
    private boolean hasPermission() {
        return Build.VERSION.SDK_INT < 23 ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(23)
    private void requestPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MiDeviceManager.getInstance().startScan();
                } else {
                    Log.e(TAG, "no permission to scan device");
                }
        }
    }
```

* 停止扫描

```Java
    try {
        MiotManager.getDeviceManager().stopScan();
    } catch (MiotException e) {
        e.printStackTrace();
    }
```

* 配置快连图标和文案：将快连图标放置到自己工程的res/drawable-xxhdpi目录下，并命名为kuailian_miio_icon.png。在strings.xml文件添加

```xml
    <string name="common_mieda_device">（文案）</string>
```

* 快连设备，获取到设备列表后，如果设备是待连接的设备，可以调用以下方法连接到云端，具体可以参考demo中DeviceActivity.class：

```Java
    private void connectDevice(AbstractDevice device) {
        try {
            MiotManager.getDeviceConnector().connectToCloud(device, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    Logger.d(TAG, "connect device onSucceed");
                }
    
                @Override
                public void onFailed(int errCode, String description) {
                    Logger.e(TAG, "connect device onFailed: " + errCode + description);
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }
```

### 绑定设备
绑定设备，即为将设备绑定到一个用户帐号下，只能用于未绑定的广域网设备（目前快连过程中会默认绑定设备）

```Java
    try {
        MiotManager.getDeviceManager().takeOwnership(abstractDevice, new CompletionHandler() {
            @Override
            public void onSucceed() {
                Logger.d(TAG, "takeOwnership onSucceed");
            }

            @Override
            public void onFailed(int errCode, String description) {
                Logger.e(TAG, "takeOwnership onFialed: " + errCode + " " + description);
            }
        });      
    } catch (MiotException e) {
        e.printStackTrace();
    }
```

#### 通过扫描绑定

可通过扫描设备显示的二维码，然后把信息传递给服务端进行绑定

```Java
    try {
        deviceManager.bindWithBindKey(bindKey, new CommonHandler<String>() {
            @Override
            public void onSucceed(String result) {
                Log.d(TAG, "bindWithBindKey onSuccess result = " + result);
            }

            @Override
            public void onFailed(int errCode, String description) {
                Log.d(TAG, "bindWithBindKey onFailed errCode = " + errCode + ", description = " + description);
            }
        });
    } catch (MiotException e) {
        e.printStackTrace();
    }
```

### 解绑设备
解绑设备，即为从用户名下删除该设备，并重置该设备

```Java
    try {
        MiotManager.getDeviceManager().disclaimOwnership(abstractDevice, new CompletionHandler() {
            @Override
            public void onSucceed() {
                Logger.d(TAG, "disclaimOwnership onSucceed");
            }

            @Override
            public void onFailed(int errCode, String description) {
                Logger.e(TAG, "disclaimOwnership onFialed: " + errCode + " " + description);
            }
        });      
    } catch (MiotException e) {
        e.printStackTrace();
    }
```

### 分享设备
* 将设备分享给其他人

说明：只能分享给小米账号ID，如果要分享的是手机号/邮箱，需要先通过getUserProfile接口获取对应小米账号ID信息。

```Java
    try {
        MiotManager.getDeviceManager().shareDevice(mAbstractDevice, userId, new CompletionHandler() {
            @Override
            public void onSucceed() {
                Logger.d(TAG, "shareDevice onSucceed");
            }
    
            @Override
            public void onFailed(int errCode, String description) {
                Logger.e(TAG, "shareDevice onFailed: " + errCode + " " + description);
            }
        });
    } catch (MiotException e) {
        e.printStackTrace();
    }
```
其中userId为用户小米帐号ID。

* 获取小米账号ID信息

可以获取到指定用户的小米账号ID、昵称、头像信息。

```Java
    try {
        MiotManager.getDeviceManager().getUserProfile(userId, new CommonHandler<UserInfo>() {
            @Override
            public void onSucceed(UserInfo result) {
                
            }
        
            @Override
            public void onFailed(int errCode, String description) {
        
            }
        });
    } catch (MiotException e) {
        e.printStackTrace();
    }
```

* 取消分享

```Java
    try {
        MiotManager.getDeviceManager().cancelShare(mAbstractDevice, userId, new CompletionHandler() {
            @Override
            public void onSucceed() {
                Logger.d(TAG, "cancelShare onSucceed");
            }

            @Override
            public void onFailed(int errCode, String description) {
                Logger.e(TAG, "cancelShare onFailed: " + errCode + " " + description);
            }
        });
    } catch (MiotException e) {
        e.printStackTrace();
    }
```

* 查看设备被分享用户列表

```Java
    try {
        MiotManager.getDeviceManager().querySharedUsers(mAbstractDevice, new CommonHandler<List<SharedUser>>() {
            @Override
            public void onSucceed(List<SharedUser> result) {
                Logger.d(TAG, "querySharedUsers onSucceed");
                ...
            }

            @Override
            public void onFailed(int errCode, String description) {
                Logger.e(TAG, "querySharedUsers onFailed: " + errCode + " " + description);
            }
        });
    } catch (MiotException e) {
        e.printStackTrace();
    }
```

* 被分享者查看设备分享邀请

```Java
    try {
        MiotManager.getDeviceManager().querySharedRequests(new CommonHandler<List<SharedRequest>>() {
            @Override
            public void onSucceed(List<SharedRequest> result) {
                Logger.d(TAG, "querySharedRequests onSucceed");
                ...
            }

            @Override
            public void onFailed(int errCode, String description) {
                Logger.e(TAG, "querySharedRequests: " + errCode + description);
            }
        });
    } catch (MiotException e) {
        e.printStackTrace();
    }
```

* 接受或者拒绝设备分享邀请

```Java
    sharedRequest.setShareStatus(ShareStatus.accept);
    try {
        MiotManager.getDeviceManager().replySharedRequest(sharedRequest, new CompletionHandler() {
            @Override
            public void onSucceed() {
                Logger.d(TAG, "replySharedRequest onSucceed");
            }

            @Override
            public void onFailed(int errCode, String description) {
                Logger.e(TAG, "replySharedRequest: " + errCode + description);
            }
        });
    } catch (MiotException e) {
        e.printStackTrace();
    }
```
其中ShareStatus中accept为接受邀请，reject为拒绝邀请。

### 控制设备
这里以小米插座为例，简要说明一下，具体可以参见demo中PlugActivity

* 获取设备状态

```Java
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
```

* 下发命令

```Java
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
```

* 订阅状态变化
需要注意的是订阅部分使用的是MiPush的服务，其与应用包名绑定。
```Java
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
```

* 局域网控制
局域网控制是默认打开的，可以通过通过下面接口进行关闭

```Java
    MiotManager.getDeviceManipulator().enableLanCtrl(false);
```

* 修改服务端记录的设备名称

```Java
    try {
        MiotManager.getDeviceManager().renameDevice(mAbstractDevice, name, new CompletionHandler() {
            @Override
            public void onSucceed() {
                Log.d(TAG, "renameDevice onSucceed");
            }

            @Override
            public void onFailed(int errCode, String description) {
                Log.d(TAG, "renameDevice onFailed: " + errCode + description);
            }
        });
    } catch (MiotException e) {
        e.printStackTrace();
    }
```

### 固件更新
* 查询设备的固件版本信息

检查是否有新的固件可以更新。

```Java
    try {
            MiotManager.getDeviceManager().queryFirmwareInfo(mAbstractDevice, new DeviceManager.QueryFirmwareHandler() {
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
```

* 通知服务端给设备下发升级新的固件

```Java
    try {
        MiotManager.getDeviceManager().startUpgradeFirmware(mAbstractDevice, new CompletionHandler() {
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
```

* 检查当前的固件升级进度

```Java
    try {
        MiotManager.getDeviceManager().queryFirmwareUpgradeInfo(mAbstractDevice, new DeviceManager.QueryFirmwareHandler() {
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
```

### 蓝牙操作

参考Demo中的ConnActivity.java类的实现。

* 设备蓝牙设备基本信息

设置要操作的蓝牙设备的model、productId、绑定类型（强绑定/弱绑定）信息。

```Java
    XmBluetoothManager.getInstance().setDeviceConfig(config);
```

* 扫描周围蓝牙设备

```Java
    SearchRequest searchRequest = new SearchRequest.Builder()
                .searchBluetoothLeDevice(20000, 2)
                .build();
    XmBluetoothManager.getInstance().search(searchRequest, new SearchResponse() {
        @Override
        public void onSearchStarted() {
            
        }

        @Override
        public void onDeviceFounded(SearchResult device) {

        }

        @Override
        public void onSearchStopped() {

        }

        @Override
        public void onSearchCanceled() {

        }
    });
```

* 停止扫描

```Java
    XmBluetoothManager.getInstance().stopSearch(mac, response);
```

* 连接设备

只是单独的通过系统接口连接到了设备，没有做登录认证。

```Java
    XmBluetoothManager.getInstance().connect(mac, response);
```

* 安全连接

绑定并连接设备，如果设备是强绑定类型，一次只能被一个账号绑定，必须解绑后才能被其他账号绑定。弱绑定可以不用解绑，被其他账号绑定。

```Java
    XmBluetoothManager.getInstance().secureConnect(mac, response);
```

* 清除本地保存的绑定token信息

弱绑定设备，绑定的时候可能出现“token not match”错误，需要清除本地绑定的token后重新绑定。

```Java
    XmBluetoothManager.getInstance().clearLocalToken(mac);
```

* 断开连接

```Java
    XmBluetoothManager.getInstance().disconnect(mac);
```

* 读设备信息

```Java
    XmBluetoothManager.getInstance().read(String mac, UUID serviceId, UUID characterId, BleReadResponse response);
```

* 写设备信息

```Java
    XmBluetoothManager.getInstance().write(String mac, UUID serviceId, UUID characterId, byte[] bytes, BleWriteResponse response);
```

或者

```Java
    XmBluetoothManager.getInstance().writeNoRsp(String mac, UUID serviceId, UUID characterId, byte[] bytes, BleWriteResponse response);
```

* 读设备rssi

```Java
    XmBluetoothManager.getInstance().readRemoteRssi(String mac, BleReadRssiResponse response);
```

* 设置notify

```Java
    XmBluetoothManager.getInstance().notify(String mac, UUID serviceId, UUID characterId, BleNotifyResponse response);
```

* 取消notify

```Java
    XmBluetoothManager.getInstance().unnotify(String mac, UUID serviceId, UUID characterId, BleNotifyResponse response);
```

* 监听设备发送的notify信息

监听设备连接状态变化、notify数据通知。

```Java
    IntentFilter filter = new IntentFilter();
    filter.addAction(Constants.ACTION_CONNECT_STATUS_CHANGED);
    filter.addAction(Constants.ACTION_CHARACTER_CHANGED);
    registerReceiver(mReceiver, filter);
    
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }

            String action = intent.getAction();
            String mac = intent.getStringExtra(Constants.EXTRA_MAC);
            // 收到数据后需要先判断下是不是自己设备发送过来的
            if (TextUtils.equals(mac, mDevice.getMac())) {
                if (Constants.ACTION_CHARACTER_CHANGED.equalsIgnoreCase(action)) {
                    // 接收设备发送来的数据
                    UUID service = (UUID) intent.getSerializableExtra(Constants.EXTRA_SERVICE_UUID);
                    UUID character = (UUID) intent.getSerializableExtra(Constants.EXTRA_CHARACTER_UUID);
                    byte[] value = intent.getByteArrayExtra(Constants.EXTRA_BYTE_VALUE);
                    processNotify(service, character, value);
                } else if (Constants.ACTION_CONNECT_STATUS_CHANGED.equalsIgnoreCase(action)) {
                    // 接收设备连接状态变化
                    int status = intent.getIntExtra(Constants.EXTRA_STATUS, 0);
                    if (status == Constants.STATUS_CONNECTED) {

                    } else if (status == Constants.STATUS_DISCONNECTED) {

                    }
                    processConnectStatusChanged(status);
                }
            }
        }
    };
```
    
* 获取服务端最新的设备固件更新信息

```Java
    XmBluetoothManager.getInstance().getBluetoothFirmwareUpdateInfo(String model,GetFirmwareUpdateInfoResponse response);
```

### 蓝牙双模快连

对于WiFi蓝牙双模设备，支持通过蓝牙传递配网信息。

* 在Application里添加设备Model信息

例如：

```Java
    DeviceModel testModel = DeviceModelFactory.createDeviceModel(
            TestApplication.this,
            "com.xiaomi.test",
            "com.xiaomi.test.xml"
    );
    MiotManager.getInstance().addModel(testModel);
```

* 配置蓝牙基本信息

例如：

```Java
    private void config() {
        if (MiotManager.getBluetoothManager() == null) {
            IntentFilter intentFilter = new IntentFilter(TestConstants.ACTION_BIND_SERVICE_SUCCEED);
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
        } else {
            setDeviceConfig();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setDeviceConfig();
        }
    };

    private void setDeviceConfig() {
        BluetoothDeviceConfig config = new BluetoothDeviceConfig();
        config.bindStyle = BindStyle.STRONG;
        config.model = "com.xiaomi.test";
        config.productId = 0;
        XmBluetoothManager.getInstance().setDeviceConfig(config);
    }
```

* 设备要开启ap热点

ap热点名称要符合米家定义的规范，最末尾四个字节是ComboKey，用于找到对应的蓝牙设备。

* 设备要广播蓝牙beacon信息

参考米家规范，beacon要广播ComboKey字段，这个字段与上述ap热点里面的ComboKey要一致。

* 扫描周围的ap设备，然后点击连接设备

点击连接的时候会优先使用蓝牙传输配网信息，如果蓝牙传输失败，会继续通过ap来传输配网信息。

```Java
    List<DiscoveryType> types = new ArrayList<>();
    types.add(DiscoveryType.MIOT_WIFI);
    try {
        MiotManager.getDeviceManager().startScan(types, new DeviceManager.DeviceHandler() {
            @Override
            public void onSucceed(List<AbstractDevice> devices) {
                //TODO
            }

            @Override
            public void onFailed(int errCode, String description) {
                //TODO
            });
    } catch (MiotException e) {
        e.printStackTrace();
    }
```
