# MiotService文档
---

## 准备工作

### 帐号接入
MiotService目前支持小米帐号登录，Service中已经集成有小米帐号Sdk，开发者需要到[小米帐号开放平台](http://dev.xiaomi.com/docs/passport/ready/)注册自己的App信息，**并在小米帐号系统Oauth权限管理中，申请并审核通过“智能家庭服务”权限，请确保申请通过此权限，否则不能正常使用**。如果在接入过程中遇到问题，可加QQ群385428920咨询。

### 消息推送
MiotService中集成有Mipush，主要是用于订阅设备事件。如果App中需要使用，开发者需要到[小米消息推送服务](http://dev.xiaomi.com/doc/?page_id=1670)注册自己的App信息。

---

## SDK使用说明

这里简要介绍如何配置和使用MiotService，也可以参考SDK中的demo。
### 配置AndroidManifest.xml
* MiotService支持的最低android版本
```xml
     <uses-sdk
        android:minSdkVersion="16"
        android:targetSdkVersion="18" />
```
* MiotService需要的权限列表
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
* MiotService需要配置的android组件（这些组件已经在SDK中声明过了，在自己的APP中可以不再声明）
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
        <activity
            android:name="miot.service.connection.wifi.DeviceConnectionUap"
            android:configChanges="keyboardHidden|keyboard|orientation"
            android:launchMode="singleTask"
            android:process=":miot"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.NoTitleBar"
            android:windowSoftInputMode="stateHidden|stateAlwaysHidden|adjustPan"/>

        <service
            android:name="miot.service.negotiator.NegotiatorService"
            android:enabled="true"
            android:exported="true"
            android:label="NegotiatorService"
            android:permission="android.permission.INTERNET"
            android:process=":miot">
            <intent-filter>
                <action android:name="miot.aidl.INegotiatorService"/>
            </intent-filter>
        </service>
        <service
            android:name="miot.service.manager.DeviceManagerService"
            android:enabled="true"
            android:exported="true"
            android:label="DeviceManagerService"
            android:permission="android.permission.INTERNET"
            android:process=":miot">
            <intent-filter>
                <action android:name="miot.aidl.IDeviceManagerService"/>
            </intent-filter>
        </service>
        <service
            android:name="miot.service.manipulator.DeviceManipulatorService"
            android:enabled="true"
            android:exported="true"
            android:label="DeviceManipulatorService"
            android:permission="android.permission.INTERNET"
            android:process=":miot">
            <intent-filter>
                <action android:name="miot.aidl.IDeviceManipulatorService"/>
            </intent-filter>
        </service>
        <service
            android:name="miot.service.connection.DeviceConnectionService"
            android:enabled="true"
            android:exported="true"
            android:label="DeviceConnectionService"
            android:permission="android.permission.INTERNET"
            android:process=":miot">
            <intent-filter>
                <action android:name="miot.aidl.IDeviceConnectionService"/>
            </intent-filter>
        </service>
        <service
            android:name="miot.service.people.PeopleManagerService"
            android:enabled="true"
            android:exported="true"
            android:label="PeopleManagerService"
            android:permission="android.permission.INTERNET"
            android:process=":miot">
            <intent-filter>
                <action android:name="miot.aidl.IPeopleManagerService"/>
            </intent-filter>
        </service>
```

### 注册AppId，AppKey以及设备信息
将应用注册到小米帐号时，会生成相关的AppId和Appkey，需要将这部分信息注册到智能家居后台。此外，还需要将应用需要支持的设备信息注册到智能家居后台。

### 生成设备代码
首先，这里简述一下MiotDevice的模型，一个Device包含一个或者多个Service，此处的Service代表设备的一种能力，一个具体的设备可能有多种能力。一个Service包含一系列property，action：
* property表示该service当前状态，property具有名称，数据类型，默认值，以及其值改变时是否触发事件
* action表示service可以控制的动作

撰写一份xml格式的profile文件，具体可以参见demo中附带的ddd_SmartSocket.xml（小米智能插座）以及ddd_AuxAircondition.xml（奥克斯空调）。然后使用提供的脚本codegenerator.jar，运行如下命令
```
    java -jar codegenerator.jar src/main/assets/ddd_SmartSocket.xml
```
运行时将最后一个变量替换为自己xml文档的相对路径，运行成功后将生成SmartSocketBase.java以及SmartSocketBaseService.java两个文件（生成文件数量与xml中定义的service数量有关）

### 使用Service
* 首先初始化MiotManager：
```Java
    MiotManager.getInstance().initialize(getApplicationContext());
```
* 然后将小米帐号处的AppId，AppKey设置到Service中：
```Java
    AppConfiguration appConfig = new AppConfiguration();
    appConfig.setAppId(AppConfig.APP_ID);
    appConfig.setAppKey(AppConfig.APP_KEY);
    MiotManager.getInstance().setAppConfig(appConfig);
```
* 接着将App需要处理的设备配置到Service中：
```Java
    try {
        DeviceModel deviceModel = DeviceModelFactory.createDeviceModel(TestApplication.this, TestConstants.CHUANGMI_PLUG_V1,
                                                  TestConstants.CHUANGMI_PLUG_V1_URL, SmartSocketBase.class);
        MiotManager.getInstance().addModel(deviceModel);
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

* 将用户信息保存到Service中
参见小米帐号SDK使用说明
```Java
    String accessToken = bundle.getString("access_token");
    String expiresIn = bundle.getString("expires_in");
    String scope = bundle.getString("scope");
    String state = bundle.getString("state");
    String tokenType = bundle.getString("token_type");
    String macKey = bundle.getString("mac_key");
    String macAlgorithm = bundle.getString("mac_algorithm");

    new XiaomiAccountGetPeopleInfoTask(accessToken, expiresIn, macKey, macAlgorithm, this,
            new XiaomiAccountGetPeopleInfoTask.Handler() {
                @Override
                public void onSucceed(People people) {
                    Log.d(TAG, "XiaomiAccountGetPeopleInfoTask OK");
                    MiotManager.getPeopleManager().savePeople(people);
                }

                @Override
                public void onFailed() {
                    Log.d(TAG, "XiaomiAccountGetPeopleInfoTask Failed");
                }
            }).execute();
```
* 获取设备列表
调用DeviceManager中的设备发现接口，详见demo以及doc文档
```Java
    List<DiscoveryType> types = new ArrayList<>();
    types.add(DiscoveryType.MIOT_WAN);
    types.add(DiscoveryType.MIOT_WIFI);
    int result = MiotManager.getDeviceManager().startDiscovery(types,
            new CompletionHandler() {
                @Override
                public void onSucceed() {
                    Log.d(TAG, "discovery onSucceed");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    Log.e(TAG, "discovery onFailed " + errCode + description);
                }
            },
            new DeviceManager.DeviceListener() {
                @Override
                public void onDeviceFound(List<AbstractDevice> devices) {
                    //TODO: found devices
                }

                @Override
                public void onDeviceLost(List<AbstractDevice> devices) {
                    //TODO: lost devices
                }

                @Override
                public void onDeviceUpdate(List<AbstractDevice> devices) {
                    //TODO: update devices
                }
            });
```
---

### 快连
1.配置快连图标和文案：将快连图标放置到自己工程的res/drawable-xxhdpi目录下，并命名为kuailian_miio_icon.png。在strings.xml文件添加

    <string name="common_mieda_device">（文案）</string>

2.快连设备
获取到设备列表后，如果设备是待连接的设备，可以调用以下方法连接到云端：

```Java
    int ret = abstractDevice.connectToCloud(new CompletionHandler() {
        @Override
        public void onSucceed() {
            Log.d(TAG, "connectDevice onSucceed");
        }

        @Override
        public void onFailed(int errCode, String description) {
            Log.e(TAG, "connectDevice onFailed: " + errCode + " " + description);
        }
    });
    if(ret != ReturnCode.OK) {
        Log.e(TAG, "connectDevice onFailed: " + ret);
    }
```
---

### 设备操作
操作具体设备之前，首先要绑定该设备：

### 绑定和删除设备
1.绑定设备，只能用于未绑定的广域网设备（目前快连过程中会默认绑定设备）
```Java
    int ret = MiotManager.getDeviceManager().takeOwnership(abstractDevice, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    Log.d(TAG, "takeOwnership onSucceed");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    Log.e(TAG, "takeOwnership onFialed: " + errCode + " " + description);
                }
            });
```
2.删除设备
```Java
    int ret = MiotManager.getDeviceManager().disclaimOwnership(abstractDevice, new CompletionHandler() {
                @Override
                public void onSucceed() {
                    Log.d(TAG, "disclaimOwnership onSucceed");
                }

                @Override
                public void onFailed(int errCode, String description) {
                    Log.d(TAG, "disclaimOwnership onFialed: " + errCode + " " + description);
                }
            });
```

###操作具体设备
这里以小米插座为例，简要说明一下，具体可以参见demo中SmartsocketActivity
* 获取设备状态
```Java
public void getProperties() {
    int ret = mSmartSocketBaseService.getProperties(new SmartSocketBaseService.GetPropertiesCompletionHandler() {
        @Override
        public void onSucceed(final Boolean usbStatus, final Boolean powerStatus) {
            Log.d(TAG， String.format("getProperties usbStatus=%s powerStatus=%s", usbStatus, powerStatus));
            ...
        }

        @Override
        public void onFailed(int errCode, String description) {
            Log.e(TAG， String.format("getProperties Failed, code: %d %s", errCode, description));
        }
    });
    if (ret != ReturnCode.OK) {
        Log.e(TAG， String.format("getProperties failed: %d", ret));
    }
}
```
* 下发命令
```Java
public void setPlugOn() {
    int ret = mSmartSocketBaseService.setPlugOn(new CompletionHandler() {
        @Override
        public void onSucceed() {
            Log.d(TAG, "setPlugOn OK");
            ...
        }

        @Override
        public void onFailed(int errCode, String description) {
            Log.e(TAG， String.format("setPlugOn Failed, code: %d %s", errCode, description));
        }

    });

    if (ret != ReturnCode.OK) {
        Log.e(TAG, String.format("setPlugOn failed: %d", ret));
    }
}
```
* 订阅状态变化
需要注意的是订阅部分使用的是MiPush的服务，其与应用包名绑定。
```Java
public void subscribeNotification() {
    mSmartSocketBaseService.subscribeNotifications(new CompletionHandler() {
        @Override
        public void onSucceed() {
            Log.d(TAG, "subscribe OK");
        }

        @Override
        public void onFailed(int errCode, String description) {
            Log.d(TAG, String.format("subscribe Failed, code: %d %s", errCode, description));
        }
    }, new SmartSocketBaseService.PropertyNotificationListener() {
        @Override
        public void onUsbStatusChanged(final Boolean usbStatus) {
            Log.d("usbStatusChanged: ", String.valueOf(usbStatus));
            ...
        }

        @Override
        public void onPowerStatusChanged(final Boolean powerStatus) {
            Log.d("powerStatusChanged: ", String.valueOf(powerStatus));
            ...
        }
    });
}
```
