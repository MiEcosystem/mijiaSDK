## mijiaSDK文档
> sdk的详细api使用介绍见项目项目根目录

## mijiaSDK添加

~~3.2.7以前的添加方式如下~~
```
//需要将aar拷贝到libs下
repositories {
    flatDir {
        dirs 'libs'
    }

}
dependencies {
    compile(name: 'mijiaClient-3-2-7', ext: 'aar')
    compile(name: 'mijiaService-3-2-7', ext: 'aar')
    compile(name: 'mijiaBluetooth-3-2-7', ext: 'aar')
}
```
**3.2.7及其以后的添加方式如下**
```
repositories {
    maven {
        url 'http://mijia.sdk.westm.cn'
    }
}

dependencies {
    compile "com.xiaomi.miot:mijia-controller-client:3.2.+"
    compile "com.xiaomi.miot:mijia-controller-service:3.2.+"
    compile "com.xiaomi.miot:mijia-controller-bluetooth:3.2.+"
    compile files('libs/oauth-xiaomiopenauth.jar')
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