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

version = '3.2.9'

dependencies {
    compile "com.xiaomi.miot:mijia-controller-client:$version"
    compile "com.xiaomi.miot:mijia-controller-service:$version"
    compile "com.xiaomi.miot:mijia-controller-bluetooth:$version"
    compile files('libs/oauth-xiaomiopenauth.jar')
}
```

## 版本更新说明

### 3.2.8

- 没登录调用MiotCloudAPI直接返回错误的Response
- 增加接口MiotManager.getDeviceConnector().setHttpUserAgent(mijia-sdk-demo);修改MiotCloudAPI的user agent

### 3.2.9
- 添加us服务器