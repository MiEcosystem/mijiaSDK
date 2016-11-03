# mijiaSDK文档
---
## 尽量不要再继续使用mijiaSDK-2-3-4.aar，这个以后不会再更新。建议使用mijiaSDK3进行替代，关于接口方面的调整参考[mijiaSDK3](3.0/README.md)

## 目的
本文档为接入mijiaSDK的第三方开发者提供开发独立App的指导。

## 前提条件
在进行独立App开发前，您需要
- 了解产品功能描述、产品Model等相关概念 [小米智能硬件开放平台](https://open.home.mi.com/resource.html#/summarize)
- 完成产品登记，并完成固件调试 [产品登记](https://open.home.mi.com/develop.html#/product)
- 了解Android开发、上线等一系列流程 [安卓开发](http://developer.android.com/)
- 完成小米开发者账号的注册和资质认证 [小米开放平台](https://dev.mi.com/console/)

## 基础准备
在您正式进行产品接入前，需要首先完成一下准备工作

### 1. 帐号接入
mijiaSDK目前支持小米帐号登陆，开发者需要到[小米帐号开放平台](http://dev.xiaomi.com/docs/passport/ready/)注册自己的App信息，**并在小米帐号系统Oauth权限管理中，申请并审核通过“智能家庭服务”权限，请确保申请通过此权限，否则不能正常使用**。如果在接入过程中遇到问题，可加QQ群385428920咨询。然后下载[小米帐号最新版SDK](https://github.com/xiaomipassport/oauth-Android-sdk)，并集成到自己应用中。

### 2. 消息推送
mijiaSDK中集成有Mipush，目前主要是用于订阅设备事件。App使用前，开发者需要到[小米消息推送服务](http://dev.xiaomi.com/doc/?page_id=1670)注册自己的App信息。

### 3. 注册AppId，AppKey以及设备信息
将应用注册到小米帐号时，会生成相关的AppId和Appkey，需要将这部分信息注册到智能家居后台。

## 产品接入
### 1. 云端配置产品信息
将应用需要支持的设备信息注册到智能家居后台。

### 2. 撰写设备功能描述文档
首先，这里简述一下MiotDevice的模型，一个Device包含一个或者多个Service，此处的Service代表设备的一种能力，一个具体的设备可能有多种能力。一个Service包含一系列property，action：
* property表示该service当前状态，property具有名称，数据类型，默认值，以及其值改变时是否触发事件
* action表示service可以控制的动作
撰写一份xml格式的设备功能描述文档，用于描述设备具体功能，可以参考demo中附带的ddd_SmartSocket.xml（小米智能插座）以及ddd_AuxAircondition.xml（奥克斯空调）。

### 3. 生成设备代码
撰写完设备功能描述文档后，使用提供的脚本codegenerator.jar，运行如下命令
```
    java -jar codegenerator.jar src/main/assets/ddd_SmartSocket.xml
```
运行时将最后一个变量替换为自己xml文档的相对路径，运行成功后将生成SmartSocketBase.java以及SmartSocketBaseService.java两个文件（生成文件数量与xml中定义的service数量有关）

## 使用SDK开发App
接下来将简要介绍如何配置和使用mijiaSDK开发自己的App，也可以参考其中的demo。
### 1. 配置AndroidManifest.xml
* mijiaSDK支持的最低android版本
```xml
     <uses-sdk
        android:minSdkVersion="16"
        android:targetSdkVersion="18" />
```
* mijiaSDK需要的权限列表
```xml
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.CONNECTIVITY_INTERNAL"/>​
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.USE_CREDENTIALS"/>
    <uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <uses-permission android:name="android.permission.GET_TASKS"/>
    <uses-permission android:name="android.permission.VIBRATE"/>
    <uses-permission android:name="android.permission.BLUETOOTH"/>
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE"/>

    <!--小米帐号SDK需要的权限-->   
    <uses-permission android:name="com.xiaomi.permission.AUTH_SERVICE" />

    <!--Mipush需要的权限，这里的com.mi.test改成app的包名-->   
    <permission
        android:name="com.mi.test.permission.MIPUSH_RECEIVE"
        android:protectionLevel="signature" />
    <uses-permission android:name="com.mi.test.permission.MIPUSH_RECEIVE" />
```

### 2. 初始化SDK
这里简单描述如何初始化mijiaSDK，可以参考demo中TestApplication.java
* 首先初始化MiotManager：
```Java
    MiotManager.getInstance().initialize(getApplicationContext());
```
* 然后将小米帐号处的AppId，AppKey设置到SDK中：
```Java
    AppConfiguration appConfig = new AppConfiguration();
    appConfig.setAppId(AppConfig.APP_ID);
    appConfig.setAppKey(AppConfig.APP_KEY);
    MiotManager.getInstance().setAppConfig(appConfig);
```
* 接着将App需要处理的设备配置到SDK中（**蓝牙设备跳过这一步**）：
```Java
    try {
        DeviceModel plug = DeviceModelFactory.createDeviceModel(TestApplication.this,
                TestConstants.CHUANGMI_PLUG_M1,
                TestConstants.CHUANGMI_PLUG_M1_URL,
                ChuangmiPlugM1.class);
        MiotManager.getInstance().addModel(plug);
    } catch (DeviceModelException e) {
        e.printStackTrace();
    }
```
方法createDeviceModel中的参数依次为：context；model：云端注册的设备model；url：xml格式的profile文档，需要放到项目assets目录下；clazz（Class<?>）：之前生成的设备代码的class
* 最后bindService：
```Java
    MiotManager.getInstance().open();
```
如果返回值为0，则为绑定成功。退出应用时需要unBind：
```Java
    MiotManager.getInstance().close()
```
同上，返回值为0，则为解绑成功

* 将用户信息保存到Service中，可以参考demo中AccountActivity.java
```Java
private void processAuthResult(XiaomiOAuthResults results) {
    String accessToken = results.getAccessToken();
    String expiresIn = results.getExpiresIn();
    String scope = results.getScopes();
    String state = results.getState();
    String tokenType = results.getTokenType();
    String macKey = results.getMacKey();
    String macAlgorithm = results.getMacAlgorithm();

    ...

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
```

### 3. 开发设备相关功能
SDK目前提供了如下功能：
1. **获取设备列表**：获取设备
2. **快连设备**：用于将新设备联网，也就是快连（目前快连默认完成绑定设备）
3. **绑定设备**: 完成设备与用户的帐号绑定关系
4. **解绑设备**：允许用户解除与设备的绑定关系
5. **分享设备**：用户可以将设备分享给其他人（目前仅小米帐号）
6. **控制设备**：包括获取设备状态、下发指令和订阅设备事件

目前这些操作的log信息是默认打开的，可以通过一下接口关闭log信息
```Java
    miot.service.common.utils.Logger.enableLog(true);
```

#### 获取设备列表
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

#### 快连设备
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
                    Log.e(TAG, "on permission to scan device");
                }
        }
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

#### 绑定设备
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

#### 解绑设备
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

#### 分享设备
* 将设备分享给其他人
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

#### 控制设备
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

### 4. Mipush
目前，智能家庭后台的push消息格式是这样：{type:****, body: *****}，其中type有以下几种：device、share、scene和adv，这几类消息SDK会尝试解析，不会传递给客户端，独立APP的push消息格式不要和这个重复。其他Push消息SDK会通过广播的方式将其发送出来，具体可以参考如下代码：

```Java
    public static final String PUSH_MESSAGE = "com.xiaomi.push.message";
    public static final String PUSH_COMMAND = "com.xiaomi.push.command";

    public void registerPush() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PUSH_COMMAND);
        filter.addAction(PUSH_MESSAGE);
        registerReceiver(mReceiver, filter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case PUSH_COMMAND:
                    MiPushCommandMessage command = (MiPushCommandMessage) intent.getSerializableExtra("command");
                    Logger.d(TAG, "command: " + command.toString());
                    break;
                case PUSH_MESSAGE:
                    MiPushMessage message = (MiPushMessage) intent.getSerializableExtra("message");
                    Logger.d(TAG, "message: " + message.toString());
                    break;
            }
        }
    };
```

## 备注
1. 在进行独立App开发时，您可以通过您在[小米智能硬件开放平台](https://open.home.mi.com/)注册的开发者帐号绑定设备并进行调试，相关文档请参阅小米智能硬件开放平台的相关文档
2. 如果您在开发中遇到任何问题，可以联系xuxiaotian@xiaomi.com
