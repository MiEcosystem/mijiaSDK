/* This file is auto-generated.*/

package com.mi.device;

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

    private AbstractDevice device = null;

    public AirConditionBaseService(AbstractDevice device) {
        this.device = device;
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
    public void subscribeNotifications(final CompletionHandler handler, final PropertyNotificationListener listener) {
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
    public void unsubscribeNotifications(final CompletionHandler handler) {
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
        op.removePropertyChangedListener(propertyInfo,
                new DeviceManipulator.CompletionHandler() {
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
    public int getProperties(final GetPropertiesCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService());
            propertyInfo.addProperty(getService().getProperty(PROPERTY_Temp));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_Mode));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_WindSpeed));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_IndoorTemp));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_Power));

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
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
                    }
            );

        } while (false);

        return ret;
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
    public int getTemp(final GetTempCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Temp);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Double temp = (Double) info.getValue(PROPERTY_Temp);
                            handler.onSucceed(temp);
                        }

                        @Override
                        public void onFailed(int errCode, String description) {
                            handler.onFailed(errCode, description);
                        }
                    }
            );

        } while (false);

        return ret;
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
    public int getMode(final GetModeCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Mode);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Mode mode = Mode.valueOf((String) info.getValue(PROPERTY_Mode));
                            handler.onSucceed(mode);
                        }

                        @Override
                        public void onFailed(int errCode, String description) {
                            handler.onFailed(errCode, description);
                        }
                    }
            );

        } while (false);

        return ret;
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
    public int getWindSpeed(final GetWindSpeedCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_WindSpeed);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Long windSpeed = (Long) info.getValue(PROPERTY_WindSpeed);
                            handler.onSucceed(windSpeed);
                        }

                        @Override
                        public void onFailed(int errCode, String description) {
                            handler.onFailed(errCode, description);
                        }
                    }
            );

        } while (false);

        return ret;
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
    public int getIndoorTemp(final GetIndoorTempCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_IndoorTemp);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Double indoorTemp = (Double) info.getValue(PROPERTY_IndoorTemp);
                            handler.onSucceed(indoorTemp);
                        }

                        @Override
                        public void onFailed(int errCode, String description) {
                            handler.onFailed(errCode, description);
                        }
                    }
            );

        } while (false);

        return ret;
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
    public int getPower(final GetPowerCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Power);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Power power = Power.valueOf((String) info.getValue(PROPERTY_Power));
                            handler.onSucceed(power);
                        }

                        @Override
                        public void onFailed(int errCode, String description) {
                            handler.onFailed(errCode, description);
                        }
                    }
            );

        } while (false);

        return ret;
    }

    //-------------------------------------------------------
    // Actions
    //-------------------------------------------------------

    /**
     * 设置温度
     */
    public int setTemp(Double temp, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setTemp);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_Temp, temp)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.invoke(actionInfo, new DeviceManipulator.InvokeCompletionHandler() {
                @Override
                public void onSucceed(ActionInfo info) {
                    handler.onSucceed();
                }

                @Override
                public void onFailed(final int errCode, final String description) {
                    handler.onFailed(errCode, description);
                }
            });
        } while (false);

        return ret;
    }

    /**
     * 设置云端定时开
     */
    public int setPowerModeTempSpeed(Power power, Mode mode, Double temp, Long windSpeed, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setPowerModeTempSpeed);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_Power, power.toString())) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_Mode, mode.toString())) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_Temp, temp)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_WindSpeed, windSpeed)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.invoke(actionInfo, new DeviceManipulator.InvokeCompletionHandler() {
                @Override
                public void onSucceed(ActionInfo info) {
                    handler.onSucceed();
                }

                @Override
                public void onFailed(final int errCode, final String description) {
                    handler.onFailed(errCode, description);
                }
            });
        } while (false);

        return ret;
    }

    /**
     * 设置模式
     */
    public int setMode(Mode mode, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setMode);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_Mode, mode.toString())) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.invoke(actionInfo, new DeviceManipulator.InvokeCompletionHandler() {
                @Override
                public void onSucceed(ActionInfo info) {
                    handler.onSucceed();
                }

                @Override
                public void onFailed(final int errCode, final String description) {
                    handler.onFailed(errCode, description);
                }
            });
        } while (false);

        return ret;
    }

    /**
     * 开关
     */
    public int setPower(Power power, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setPower);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_Power, power.toString())) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.invoke(actionInfo, new DeviceManipulator.InvokeCompletionHandler() {
                @Override
                public void onSucceed(ActionInfo info) {
                    handler.onSucceed();
                }

                @Override
                public void onFailed(final int errCode, final String description) {
                    handler.onFailed(errCode, description);
                }
            });
        } while (false);

        return ret;
    }

    /**
     * 设置风速
     */
    public int setWindSpeed(Long windSpeed, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setWindSpeed);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_WindSpeed, windSpeed)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.invoke(actionInfo, new DeviceManipulator.InvokeCompletionHandler() {
                @Override
                public void onSucceed(ActionInfo info) {
                    handler.onSucceed();
                }

                @Override
                public void onFailed(final int errCode, final String description) {
                    handler.onFailed(errCode, description);
                }
            });
        } while (false);

        return ret;
    }

}

