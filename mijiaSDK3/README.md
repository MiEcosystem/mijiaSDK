# mijiaSDK3文档

mijiaSDKV3使用上基本上和之前的一样，主要是做了以下调整：

1. 主要拆分成mijiaClient和mijiaService两部分（应用需要将这两个aar都集成进来），如果需要使用蓝牙，需要将mijiaBluetooth也集成进来，
2. 类和接口的包名调整，具体调整如下所示：
miot.api.* --> com.miot.api.*
miot.typedef.* --> com.miot.common.*
miot.api.device.* --> com.miot.common.abstractdevice.*
3. 移除了Abstractdevice中的重复方法：queryFirmwareInfo方法、queryFirmwareUpgradeInfo方法、startUpgradeFirmware方法和connectToCloud方法，其中前三个方法中可以在DeviceManager中找到，最后一个方法可以在DeviceConnector中找到。
4. 需要使用codegenerator3重新生成一下设备代码。
