## mijiaSDK文档
> sdk的详细api使用介绍见项目项目根目录

## mijiaSDK添加

```
//需要将aar拷贝到libs下
repositories {
    flatDir {
        dirs 'libs'
    }

}
dependencies {
    compile(name: 'mijiaClient-3-2-18', ext: 'aar')
    compile(name: 'mijiaService-3-2-18', ext: 'aar')
    compile(name: 'mijiaBluetooth-3-2-18', ext: 'aar')
}
```

## 版本更新说明

### 3.2.8

- 没登录调用MiotCloudAPI直接返回错误的Response
- 增加接口MiotManager.getDeviceConnector().setHttpUserAgent("mijia-sdk-demo");修改MiotCloudAPI的user agent

### 3.2.9
- 添加us服务器
- MiotManager.getDeviceConnector().enableHttpLog();添加okhttplog拦截器,拦截详细日志,适用于调试.
- MiotManager.getVoiceAssistant().startSession([source],[did],[handler])接口修改

### 3.2.10
- 兼容老版本接口MiotManager.getVoiceAssistant().startSession([handler])

### 3.2.11
- 更新小米推送3.4.0版本
- MiotManager.getDeviceManipulator().addPropertyChangedListener同一个设备的同一个service可以订阅多次

### 3.2.12
- DataType不能转行类型错误不再打印污染日志
- SharedRequest添加sender,senderName属性

### 3.2.12-tv
- MiotManager.getDeviceManipulator().addPropertyChangedListener同一个设备的同一个service可以订阅多次

### 3.2.14
- 之后正式版本去掉这个功能，只保留在3.2.12-tv ~~MiotManager.getDeviceManipulator().addPropertyChangedListener同一个设备的同一个service可以订阅多次~~
- 修复bug,杀死app进程，之后在个别手机重新订阅属性，不能收到属性变化回调

### 3.2.15
- bugfix:更新了快连string
- bugfix：修改蓝牙接口访问从http改为https

### 3.2.16
- bugfix: 修复异常crash

### 3.2.17
- 更新MiPush SDK

### 3.2.18
- 更新MiPush SDK到3.6.6版本