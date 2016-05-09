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

public class AirConditionBaseService extends AbstractService {
    private static final String TAG = "AirConditionBaseService";

    //-------------------------------------------------------
    // Action Name
    //-------------------------------------------------------
    public static final String ACTION_setTemp = "setTemp";
    public static final String ACTION_setPowerModeTempSpeed = "setPowerModeTempSpeed";
    public static final String ACTION_setMode = "setMode";
    public static final String ACTION_setPower = "setPower";
    public static final String ACTION_setWindSpeed = "setWindSpeed";

    //-------------------------------------------------------
    // Property Name
    //-------------------------------------------------------
    public static final String PROPERTY_Temp = "Temp";
    public static final String PROPERTY_Mode = "Mode";
    public static final String PROPERTY_WindSpeed = "WindSpeed";
    public static final String PROPERTY_IndoorTemp = "IndoorTemp";
    public static final String PROPERTY_Power = "Power";

    private AbstractDevice mDevice = null;

    public AirConditionBaseService(AbstractDevice device) {
        mDevice = device;
    }

    //-------------------------------------------------------
    // Property value defined
    //-------------------------------------------------------
    /**
    * 模式（自动|制冷|除湿|送风|制暖）
    */
    public enum Mode {
        undefined,
        auto,
        cold,
        dehumidifier,
        hot,
        wind,
    }

    /**
    * 开关状态（on|off）
    */
    public enum Power {
        undefined,
        on,
        off,
    }


    //-------------------------------------------------------
    // Property: Notifications
    //-------------------------------------------------------
    public interface PropertyNotificationListener {

        /**
         * 温度（16,32） 发生改变
         */
        void onTempChanged(Double temp);
        /**
         * 模式（自动|制冷|除湿|送风|制暖） 发生改变
         */
        void onModeChanged(Mode mode);
        /**
         * 风速（自动|低风|中低风|中风|中高|高风） 发生改变
         */
        void onWindSpeedChanged(Long windSpeed);
        /**
         * 室内温度 发生改变
         */
        void onIndoorTempChanged(Double indoorTemp);
        /**
         * 开关状态（on|off） 发生改变
         */
        void onPowerChanged(Power power);
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
                            case PROPERTY_Temp:
                                Double temp = (Double) info.getValue(PROPERTY_Temp);
                                listener.onTempChanged(temp);
                                break;
                            case PROPERTY_Mode:
                                Mode mode = Mode.valueOf((String) info.getValue(PROPERTY_Mode));
                                listener.onModeChanged(mode);
                                break;
                            case PROPERTY_WindSpeed:
                                Long windSpeed = (Long) info.getValue(PROPERTY_WindSpeed);
                                listener.onWindSpeedChanged(windSpeed);
                                break;
                            case PROPERTY_IndoorTemp:
                                Double indoorTemp = (Double) info.getValue(PROPERTY_IndoorTemp);
                                listener.onIndoorTempChanged(indoorTemp);
                                break;
                            case PROPERTY_Power:
                                Power power = Power.valueOf((String) info.getValue(PROPERTY_Power));
                                listener.onPowerChanged(power);
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
        void onSucceed(Double temp, Mode mode, Long windSpeed, Double indoorTemp, Power power);

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
        propertyInfo.addProperty(getService().getProperty(PROPERTY_Temp));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_Mode));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_WindSpeed));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_IndoorTemp));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_Power));

        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Double temp = (Double) info.getValue(PROPERTY_Temp);
                Mode mode = Mode.valueOf((String) info.getValue(PROPERTY_Mode));
                Long windSpeed = (Long) info.getValue(PROPERTY_WindSpeed);
                Double indoorTemp = (Double) info.getValue(PROPERTY_IndoorTemp);
                Power power = Power.valueOf((String) info.getValue(PROPERTY_Power));
                handler.onSucceed(temp, mode, windSpeed, indoorTemp, power);
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
     * 回调接口： 读取Temp
     */
    public interface GetTempCompletionHandler {
        void onSucceed(Double temp);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取温度（16,32）
     */
    public void getTemp(final GetTempCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Temp);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Double temp = (Double) info.getValue(PROPERTY_Temp);
                handler.onSucceed(temp);
            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
    }
    /**
     * 回调接口： 读取Mode
     */
    public interface GetModeCompletionHandler {
        void onSucceed(Mode mode);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取模式（自动|制冷|除湿|送风|制暖）
     */
    public void getMode(final GetModeCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Mode);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Mode mode = Mode.valueOf((String) info.getValue(PROPERTY_Mode));
                handler.onSucceed(mode);
            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
    }
    /**
     * 回调接口： 读取WindSpeed
     */
    public interface GetWindSpeedCompletionHandler {
        void onSucceed(Long windSpeed);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取风速（自动|低风|中低风|中风|中高|高风）
     */
    public void getWindSpeed(final GetWindSpeedCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_WindSpeed);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Long windSpeed = (Long) info.getValue(PROPERTY_WindSpeed);
                handler.onSucceed(windSpeed);
            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
    }
    /**
     * 回调接口： 读取IndoorTemp
     */
    public interface GetIndoorTempCompletionHandler {
        void onSucceed(Double indoorTemp);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取室内温度
     */
    public void getIndoorTemp(final GetIndoorTempCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_IndoorTemp);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Double indoorTemp = (Double) info.getValue(PROPERTY_IndoorTemp);
                handler.onSucceed(indoorTemp);
            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
    }
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
                Power power = Power.valueOf((String) info.getValue(PROPERTY_Power));
                handler.onSucceed(power);
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
     * 设置温度
     */
    public void setTemp(Double temp, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setTemp);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_Temp, temp)) {
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
     * 设置云端定时开
     */
    public void setPowerModeTempSpeed(Power power, Mode mode, Double temp, Long windSpeed, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setPowerModeTempSpeed);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_Power, power.toString())) {
            throw new MiotException("invalid value");
        }
        if (!actionInfo.setArgumentValue(PROPERTY_Mode, mode.toString())) {
            throw new MiotException("invalid value");
        }
        if (!actionInfo.setArgumentValue(PROPERTY_Temp, temp)) {
            throw new MiotException("invalid value");
        }
        if (!actionInfo.setArgumentValue(PROPERTY_WindSpeed, windSpeed)) {
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
     * 设置模式
     */
    public void setMode(Mode mode, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setMode);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_Mode, mode.toString())) {
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
     * 开关
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
     * 设置风速
     */
    public void setWindSpeed(Long windSpeed, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setWindSpeed);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_WindSpeed, windSpeed)) {
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

