# mijiaSDK文档
## 目的
本文档为接入mijiaSDK的第三方开发者提供开发独立App的指导。

[Github项目主页](https://github.com/MiEcosystem/mijiaSDK)

## 前提条件
在进行独立App开发前，您需要:

- 了解产品功能描述、产品Model等相关概念 [小米IoT开发者平台](https://iot.mi.com/new/guide.html#/)

- 完成产品登记，并完成固件调试 [智能硬件接入指南](https://iot.mi.com/guide.html#id=20)

- 了解Android开发、上线等一系列流程 [安卓开发](http://developer.android.com/)

- 完成小米开发者账号的注册和资质认证 [小米开放平台](https://dev.mi.com/console/)

## 基础准备
在您正式进行产品接入前，需要首先完成一下准备工作

### 1. 帐号接入
mijiaSDK目前支持小米帐号登陆，开发者需要到[小米帐号开放平台](http://dev.xiaomi.com/)注册自己的App信息，**并在小米帐号系统Oauth权限管理中，申请并审核通过“智能家庭服务”权限，请确保申请通过此权限，否则不能正常使用**。如果在接入过程中遇到问题，可加QQ群385428920咨询。然后下载[小米帐号最新版SDK](https://github.com/xiaomipassport/oauth-Android-sdk)，并集成到自己应用中。

### 2. 消息推送
mijiaSDK中集成有MiPush，目前主要是用于订阅设备事件。App使用前，开发者需要到[小米消息推送服务](http://dev.xiaomi.com/doc/?page_id=1670)注册自己的App信息。
还需将信息注册到小米IoT开发者平台。
注册信息示例：

```
IOS App 信息：
Bundle ID：xxx
AppID： xxx
AppKey： xxx
AppSecret：xxx

Android App信息：
packageName：xxx
AppID： xxx
AppKey： xxx
AppSecret： xxx
```


### 3. 注册AppId，AppKey以及设备信息
将应用注册到小米帐号时，会生成相关的AppId和Appkey，需要将AppId和公司名称注册到小米IoT开发者平台，否则会返回错误"app_id is null"。

## 产品接入
### 1. 云端配置产品信息
将应用需要支持的设备信息注册到小米IoT开发者平台。

### 2. 撰写设备功能描述文档
首先，这里简述一下MiotDevice的模型，一个Device包含一个或者多个Service，此处的Service代表设备的一种能力，一个具体的设备可能有多种能力。一个Service包含一系列property，action：

* property表示该service当前状态，property具有名称，数据类型，默认值，以及其值改变时是否触发事件
* action表示service可以控制的动作

撰写一份xml格式的设备功能描述文档，用于描述设备具体功能，可以参考demo中附带的ddd_SmartSocket.xml（小米智能插座）以及ddd_AuxAircondition.xml（奥克斯空调）。

### 3. 生成设备代码
撰写完设备功能描述文档后，使用提供的脚本codegenerator.jar，运行如下命令:

```
java -jar codegenerator.jar src/main/assets/ddd_SmartSocket.xml
```
运行时将最后一个变量替换为自己xml文档的相对路径，运行成功后将生成SmartSocketBase.java以及SmartSocketBaseService.java两个文件（生成文件数量与xml中定义的service数量有关）。

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
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.GET_ACCOUNTS"/>
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
    <uses-permission android:name="android.permission.MANAGE_ACCOUNTS"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

    <!--小米帐号SDK需要的权限-->   
    <uses-permission android:name="com.xiaomi.permission.AUTH_SERVICE" />
    
    <!--MiPush需要的权限，这里的com.mi.test改成app的包名-->   
    <permission
        android:name="com.mi.test.permission.MIPUSH_RECEIVE"
        android:protectionLevel="signature" />
    <!--MiPush需要的权限，这里的com.mi.test改成app的包名-->
    <uses-permission android:name="com.mi.test.permission.MIPUSH_RECEIVE" />
```

### 2. 初始化SDK
这里简单描述如何初始化mijiaSDK，可以参考demo中TestApplication.java。

* 首先初始化MiotManager：

```Java
    MiotManager.getInstance().initialize(getApplicationContext());
```

* 然后将小米帐号处的AppId，AppKey设置到SDK中：

```Java
    AppConfiguration appConfig = new AppConfiguration();
    appConfig.setAppId(AppConfig.APP_ID);
    appConfig.setAppKey(AppConfig.APP_KEY);
    // 配置SDK访问的服务器地址，可选，默认访问大陆服务器
    // appConfig.setLocale(AppConfiguration.Locale.sg);
    MiotManager.getInstance().setAppConfig(appConfig);
```

* 接着将App需要处理的设备配置到SDK中：

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

方法createDeviceModel中的参数依次为：context；model：云端注册的设备model；url：xml格式的profile文档，需要放到项目assets目录下；clazz（Class<?>）：之前生成的设备代码的class。

* 最后bindService：

```Java
    // 注意：只能在非UI线程调用open，open会阻塞线程
    MiotManager.getInstance().open();
```

如果返回值为0，则为绑定成功。退出应用时需要unBind：

```Java
    MiotManager.getInstance().close()
```
同上，返回值为0，则为解绑成功。

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

注意：只有用户登录米家账号，并且把登录后的用户信息传递给SDK后，才能调用SDK接口访问米家后台。

### 3. 开发设备相关功能
参考[SDK接口说明](SDK接口说明.md)

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
1. 在进行独立App开发时，您可以通过您在[小米IoT开发者平台](https://iot.mi.com/new/index.html/)注册的开发者帐号绑定设备并进行调试，相关文档请参阅小米IoT开发者平台的相关文档。
