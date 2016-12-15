/* This file is auto-generated.*/

package com.mi.device;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import miot.api.CompletionHandler;
import miot.api.DeviceManipulator;
import miot.api.MiotManager;
import miot.api.device.AbstractDevice;
import miot.api.device.AbstractService;
import miot.typedef.ReturnCode;
import miot.typedef.device.invocation.ActionInfo;
import miot.typedef.device.invocation.ActionInfoFactory;
import miot.typedef.device.invocation.PropertyInfo;
import miot.typedef.device.invocation.PropertyInfoFactory;
import miot.typedef.property.Property;
import miot.typedef.property.PropertyDefinition;
import miot.typedef.exception.MiotException;

public class PlugBaseService extends AbstractService {
    private static final String TAG = "PlugBaseService";

    //-------------------------------------------------------
    // Action Name
    //-------------------------------------------------------
    public static final String ACTION_setPower = "setPower";
    public static final String ACTION_setWifiLed = "setWifiLed";

    //-------------------------------------------------------
    // Property Name
    //-------------------------------------------------------
    public static final String PROPERTY_Power = "Power";
    public static final String PROPERTY_WifiLed = "WifiLed";
    public static final String PROPERTY_Temperature = "Temperature";

    private AbstractDevice mDevice = null;

    public PlugBaseService(AbstractDevice device) {
        mDevice = device;
    }

    //-------------------------------------------------------
    // Property value defined
    //-------------------------------------------------------
    /**
    * 开关状态（on|off）
    */
    public enum Power {
        undefined,
        on,
        off,
    }

    /**
    * wifi指示灯开关
    */
    public enum WifiLed {
        undefined,
        on,
        off,
    }


    //-------------------------------------------------------
    // Property: Notifications
    //-------------------------------------------------------
    public interface PropertyNotificationListener {

        /**
         * 开关状态（on|off） 发生改变
         */
        void onPowerChanged(Power power);
        /**
         * wifi指示灯开关 发生改变
         */
        void onWifiLedChanged(WifiLed wifiLed);
        /**
         * 温度 发生改变
         */
        void onTemperatureChanged(Integer temperature);
    }

    //-------------------------------------------------------
    // Property: subscribeNotifications
    //-------------------------------------------------------
    public void subscribeNotifications(final CompletionHandler handler, final PropertyNotificationListener listener) throws MiotException {
        if (handler == null) {
            throw new IllegalArgumentException("handler is null");
        }

        if (listener == null) {
            throw new IllegalArgumentException("listener is null");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService());
        for (Property property : getService().getProperties()) {
            PropertyDefinition definition = property.getDefinition();
            if (definition.isNotifiable()) {
                propertyInfo.addProperty(property);
            }
        }

        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.addPropertyChangedListener(propertyInfo,
                new DeviceManipulator.CompletionHandler() {
                    @Override
                    public void onSucceed() {
                        handler.onSucceed();
                    }

                    @Override
                    public void onFailed(int errCode, String description) {
                        handler.onFailed(errCode, description);
                    }
                },
                new DeviceManipulator.PropertyChangedListener() {
                    @Override
                    public void onPropertyChanged(PropertyInfo info, String propertyName) {
                        switch (propertyName) {
                            case PROPERTY_Power:
                                Power power = Power.valueOf((String) info.getValue(PROPERTY_Power));
                                listener.onPowerChanged(power);
                                break;
                            case PROPERTY_WifiLed:
                                WifiLed wifiLed = WifiLed.valueOf((String) info.getValue(PROPERTY_WifiLed));
                                listener.onWifiLedChanged(wifiLed);
                                break;
                            case PROPERTY_Temperature:
                                Integer temperature = (Integer) info.getValue(PROPERTY_Temperature);
                                listener.onTemperatureChanged(temperature);
                                break;

                            default:
                                break;
                        }
                    }
                });
    }

    //-------------------------------------------------------
    // Property: unsubscribeNotifications
    //-------------------------------------------------------
    public void unsubscribeNotifications(final CompletionHandler handler) throws MiotException {
        if (handler == null) {
            throw new IllegalArgumentException("handler is null");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService());
        for (Property property : getService().getProperties()) {
            PropertyDefinition definition = property.getDefinition();
            if (definition.isNotifiable()) {
                propertyInfo.addProperty(property);
            }
        }

        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.removePropertyChangedListener(propertyInfo, new DeviceManipulator.CompletionHandler() {
            @Override
            public void onSucceed() {
                handler.onSucceed();
            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
    }

    //-------------------------------------------------------
    // Properties Getter
    //-------------------------------------------------------
    /**
     * 回调接口： 读取所有可读属性
     */

    public interface GetPropertiesCompletionHandler {
        void onSucceed(Power power, WifiLed wifiLed, Integer temperature);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取所有可读属性
     */
    public void getProperties(final GetPropertiesCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService());
        propertyInfo.addProperty(getService().getProperty(PROPERTY_Power));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_WifiLed));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_Temperature));

        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property powerProp = info.getProperty(PROPERTY_Power);
                Power power = null;
                if(powerProp.isValueValid()) {
                    power = Power.valueOf((String) powerProp.getValue());
                }

                Property wifiLedProp = info.getProperty(PROPERTY_WifiLed);
                WifiLed wifiLed = null;
                if(wifiLedProp.isValueValid()) {
                    wifiLed = WifiLed.valueOf((String) wifiLedProp.getValue());
                }

                Property temperatureProp = info.getProperty(PROPERTY_Temperature);
                Integer temperature = null;
                if(temperatureProp.isValueValid()) {
                    temperature = (Integer) temperatureProp.getValue();
                }

                handler.onSucceed(power, wifiLed, temperature);
            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
    }

    //-------------------------------------------------------
    // Property Getters
    //-------------------------------------------------------
    /**
     * 回调接口： 读取Power
     */
    public interface GetPowerCompletionHandler {
        void onSucceed(Power power);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取开关状态（on|off）
     */
    public void getPower(final GetPowerCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Power);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property power = info.getProperty(PROPERTY_Power);
                if(power.isValueValid()) {
                    handler.onSucceed(Power.valueOf((String) info.getValue(PROPERTY_Power)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + power.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
    }
    /**
     * 回调接口： 读取WifiLed
     */
    public interface GetWifiLedCompletionHandler {
        void onSucceed(WifiLed wifiLed);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取wifi指示灯开关
     */
    public void getWifiLed(final GetWifiLedCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_WifiLed);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property wifiLed = info.getProperty(PROPERTY_WifiLed);
                if(wifiLed.isValueValid()) {
                    handler.onSucceed(WifiLed.valueOf((String) info.getValue(PROPERTY_WifiLed)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + wifiLed.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
    }
    /**
     * 回调接口： 读取Temperature
     */
    public interface GetTemperatureCompletionHandler {
        void onSucceed(Integer temperature);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取温度
     */
    public void getTemperature(final GetTemperatureCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Temperature);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property temperature = info.getProperty(PROPERTY_Temperature);
                if(temperature.isValueValid()) {
                    handler.onSucceed((Integer) info.getValue(PROPERTY_Temperature));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + temperature.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
    }

    //-------------------------------------------------------
    // Actions
    //-------------------------------------------------------
    /**
     * 电源开关
     */
    public void setPower(Power power, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setPower);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_Power, power.toString())) {
            throw new MiotException("invalid value");
        }
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.invoke(actionInfo, new DeviceManipulator.InvokeCompletionHandler() {
            @Override
            public void onSucceed(ActionInfo info) {
                 handler.onSucceed();
            }

            @Override
            public void onFailed(final int errCode, final String description) {
                handler.onFailed(errCode, description);
            }
        });
    }

    /**
     * 设置wifi指示灯
     */
    public void setWifiLed(WifiLed wifiLed, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setWifiLed);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_WifiLed, wifiLed.toString())) {
            throw new MiotException("invalid value");
        }
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.invoke(actionInfo, new DeviceManipulator.InvokeCompletionHandler() {
            @Override
            public void onSucceed(ActionInfo info) {
                 handler.onSucceed();
            }

            @Override
            public void onFailed(final int errCode, final String description) {
                handler.onFailed(errCode, description);
            }
        });
    }

}

