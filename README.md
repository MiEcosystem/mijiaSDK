# mijiaSDK文档
---
## 目的
本文档为接入mijiaSDK的第三方开发者提供开发独立App的指导。

## 前提条件
在进行独立App开发前，您需要
- 了解产品功能描述、产品Model等相关概念 [小米智能硬件开放平台](https://open.home.mi.com/resource.html#/summarize)
- 完成产品登记，并完成固件调试 [产品登记](https://open.home.mi.com/develop.html#/product)
- 了解Android开发、上线等一系列流程 [安卓开发](http://developer.android.com/)
- 完成小米开发者账号的注册和资质认证 [小米开放平台](dev.xiaomi.com)

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
* mijiaSDK需要配置的android组件（这些组件已经在SDK中声明过了，在自己的APP中可以不再声明）
```xml
        <!--小米帐号SDK的组件-->
        <activity
            android:name="com.xiaomi.account.openauth.AuthorizeActivity"
            android:configChanges="orientation"
            android:windowSoftInputMode="stateHidden">
            <intent-filter>
                <action android:name="com.xiaomi.account.openauth.action.AUTH"/>

                <category android:name="android.intent.category.DEFAULT"/>
            </intent-filter>
        </activity>
        <!--Mipush的组件-->    
        <service
            android:name="com.xiaomi.push.service.XMPushService"
            android:enabled="true"
            android:process=":miot"/>
        <service
            android:name="com.xiaomi.mipush.sdk.PushMessageHandler"
            android:enabled="true"
            android:exported="true"
            android:process=":miot"/>
        <service
            android:name="com.xiaomi.mipush.sdk.MessageHandleService"
            android:enabled="true"
            android:process=":miot"/>
        <receiver
            android:name="com.xiaomi.push.service.receivers.NetworkStatusReceiver"
            android:exported="true"
            android:process=":miot">
            <intent-filter>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>

                <category android:name="android.intent.category.DEFAULT"/>
            </intent-filter>
        </receiver>
        <receiver
            android:name="com.xiaomi.push.service.receivers.PingReceiver"
            android:exported="false"
            android:process=":miot">
            <intent-filter>
                <action android:name="com.xiaomi.push.PING_TIMER"/>
            </intent-filter>
        </receiver>
        <receiver
            android:name="miot.service.common.miotpush.MiotpnReceiver"
            android:exported="true"
            android:process=":miot">
            <intent-filter>
                <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE"/>
            </intent-filter>
            <intent-filter>
                <action android:name="com.xiaomi.mipush.ERROR"/>
            </intent-filter>
        </receiver>
        <!--mijiaSDK的组件-->    
        <activity
            android:name="miot.service.connection.wifi.DeviceConnectionUap"
            android:configChanges="keyboardHidden|keyboard|orientation"
            android:launchMode="singleTask"
            android:process=":miot"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.NoTitleBar"
            android:windowSoftInputMode="stateHidden|stateAlwaysHidden|adjustPan"/>
        <service
            android:name="miot.service.MiotService"
            android:enabled="true"
            android:exported="true"
            android:label="NegotiatorService"
            android:permission="android.permission.INTERNET"
            android:process=":miot">
            <intent-filter>
                <action android:name="miot.aidl.IBinderPool"/>
            </intent-filter>
        </service>
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
* 接着将App需要处理的设备配置到SDK中：
```Java
    try {
        DeviceModel smartSocket = DeviceModelFactory.createDeviceModel(TestApplication.this,
                TestConstants.CHUANGMI_PLUG_V1,
                TestConstants.CHUANGMI_PLUG_V1_URL,
                SmartSocketBase.class);
        MiotManager.getInstance().addModel(smartSocket);
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
5. **控制设备**：包括获取设备状态、下发指令和订阅设备事件

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

#### 控制设备
这里以小米插座为例，简要说明一下，具体可以参见demo中SmartsocketActivity
* 获取设备状态
```Java
public void getProperties() {
    try {
        mSmartSocketBaseService.getProperties(new SmartSocketBaseService.GetPropertiesCompletionHandler() {
            @Override
            public void onSucceed(final Boolean usbStatus, final Boolean powerStatus) {
                Logger.d(TAG， String.format("getProperties usbStatus=%s powerStatus=%s", usbStatus, powerStatus));
                ...
            }

            @Override
            public void onFailed(int errCode, String description) {
                Logger.e(TAG， String.format("getProperties Failed, code: %d %s", errCode, description));
            }
        });
    } catch(MiotException e) {
        e.printStackTrace();
    }
}
```
* 下发命令
```Java
public void setPlugOn() {
    try {
        mSmartSocketBaseService.setPlugOn(new CompletionHandler() {
            @Override
            public void onSucceed() {
                Logger.d(TAG, "setPlugOn OK");
                ...
            }

            @Override
            public void onFailed(int errCode, String description) {
                Logger.e(TAG， String.format("setPlugOn Failed, code: %d %s", errCode, description));
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
        mSmartSocketBaseService.subscribeNotifications(new CompletionHandler() {
            @Override
            public void onSucceed() {
                Logger.d(TAG, "subscribe OK");
            }

            @Override
            public void onFailed(int errCode, String description) {
                Logger.d(TAG, String.format("subscribe Failed, code: %d %s", errCode, description));
            }
        }, new SmartSocketBaseService.PropertyNotificationListener() {
            @Override
            public void onUsbStatusChanged(final Boolean usbStatus) {
                Logger.d("usbStatusChanged: ", String.valueOf(usbStatus));
                ...
            }

            @Override
            public void onPowerStatusChanged(final Boolean powerStatus) {
                Logger.d("powerStatusChanged: ", String.valueOf(powerStatus));
                ...
            }
        });
    } catch (MiotException e) {
        e.printStackTrace();
    }
}
```

## 备注
1. 在进行独立App开发时，您可以通过您在[小米智能硬件开放平台](https://open.home.mi.com/)注册的开发者帐号绑定设备并进行调试，相关文档请参阅小米智能硬件开放平台的相关文档
2. 如果您在开发中遇到任何问题，可以联系xuxiaotian@xiaomi.com
