## mijiaSDK文档
> sdk的详细api使用介绍见项目根目录

## mijiaSDK添加

方式一，下载aar添加：
```
//需要将aar拷贝到libs下
repositories {
    flatDir {
        dirs 'libs'
    }

}
dependencies {
    compile(name: 'mijiaClient-3-3-13', ext: 'aar')
    compile(name: 'mijiaService-3-3-13', ext: 'aar')
    compile(name: 'mijiaBluetooth-3-3-13', ext: 'aar')
    compile 'com.google.code.gson:gson:2.4'
    compile 'commons-io:commons-io:2.4'
    compile 'com.squareup.okhttp:okhttp:2.5.0'
    compile 'javax.jmdns:jmdns:3.4.1'
}
``` 

方式二，maven添加：  
```
//在build.gradle中添加
repositories {
    maven {url "https://raw.githubusercontent.com/MiEcosystem/mijiaSDK/stable3.0/repository"}
}
dependencies {
    compile 'com.miot.bluetooth:mijia:3.3.13@aar'
    compile 'com.miot.api:mijia:3.3.13@aar'
    compile 'com.miot.service:mijia:3.3.13@aar'
    compile 'com.google.code.gson:gson:2.4'
    compile 'commons-io:commons-io:2.4'
    compile 'com.squareup.okhttp:okhttp:2.5.0'
    compile 'javax.jmdns:jmdns:3.4.1'
}
```
注意：请不要对mijia SDK进行混淆处理，如果在app中有做混淆，需声明：
```
-keep class com.miot.** { *; }
```

## 版本更新说明

### 3.3.13
- 解决登录后mipush不能收到订阅消息的问题

### 3.3.12
- 解决设备配置过程中切换到网络wifi没有网络的问题

### 3.3.11
- 修复ota升级前后混淆不一致必现crash的问题
- 修复QR方式wifi界面退出重进crash问题

### 3.3.10
- 增加接口addModels
- 解决Device加密属性始终为false问题

### 3.3.9
- 修改ap直连界面沉浸式效果
- 增加对印度服务器的支持

### 3.3.8
- 升级wifi连接到V2版;
- 解决设备连接小概率超时问题
- 修改线程不安全引起的空指针问题

### 3.3.7
- fix rpc timeout 修改RPC请求超时
- bugfix：请求单个Property时value为数组时返回只有数组第一项

### 3.3.6
- 给蓝牙设备提供直接登录的接口

### 3.3.5
- 优化wifi热点ssid格式解析

### 3.3.4
- 优化蓝牙双模快连

### 3.3.3
- fix bug

### 3.3.2
- 集成mipush 3.6.9版本

### 3.3.1
- 封装获取用户基本信息接口
- 设备属性订阅最长时间为60分钟，超时需要APP再重新调用订阅

### 3.2.23
- 封装MiPush的setAlias和unsetAlias接口

### 3.2.22
- 添加清除本地蓝牙绑定信息接口
- 添加俄罗斯服务器支持

### 3.2.21
- 更新MiPush SDK到3.6.2版本，优化功耗，适配google play审核规范

### 3.2.20
- 更新MiPush SDK到3.6.1版本
- 修复Android O上无法收到push消息的问题
- 修复Push唤醒过于频繁的问题

### 3.2.19
- 回滚MiPush SDK到3.6.0版本
- 添加欧洲服务器支持
- 添加扫描绑定方式支持

### 3.2.18
- 更新MiPush SDK到3.6.6版本

### 3.2.17
- 更新MiPush SDK

### 3.2.16
- bugfix: 修复异常crash

### 3.2.15
- bugfix:更新了快连string
- bugfix：修改蓝牙接口访问从http改为https

### 3.2.14
- 之后正式版本去掉这个功能，只保留在3.2.12-tv ~~MiotManager.getDeviceManipulator().addPropertyChangedListener同一个设备的同一个service可以订阅多次~~
- 修复bug,杀死app进程，之后在个别手机重新订阅属性，不能收到属性变化回调

### 3.2.14
- 之后正式版本去掉这个功能，只保留在3.2.12-tv ~~MiotManager.getDeviceManipulator().addPropertyChangedListener同一个设备的同一个service可以订阅多次~~
- 修复bug,杀死app进程，之后在个别手机重新订阅属性，不能收到属性变化回调

### 3.2.12-tv
- MiotManager.getDeviceManipulator().addPropertyChangedListener同一个设备的同一个service可以订阅多次

### 3.2.12
- DataType不能转行类型错误不再打印污染日志
- SharedRequest添加sender,senderName属性

### 3.2.11
- 更新小米推送3.4.0版本
- MiotManager.getDeviceManipulator().addPropertyChangedListener同一个设备的同一个service可以订阅多次

### 3.2.10
- 兼容老版本接口MiotManager.getVoiceAssistant().startSession([handler])

### 3.2.9
- 添加us服务器
- MiotManager.getDeviceConnector().enableHttpLog();添加okhttplog拦截器,拦截详细日志,适用于调试.
- MiotManager.getVoiceAssistant().startSession([source],[did],[handler])接口修改

### 3.2.8
- 没登录调用MiotCloudAPI直接返回错误的Response
- 增加接口MiotManager.getDeviceConnector().setHttpUserAgent("mijia-sdk-demo");修改MiotCloudAPI的user agent
