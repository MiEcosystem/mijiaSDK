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

public class LightMonoService extends AbstractService {
    private static final String TAG = "LightMonoService";

    //-------------------------------------------------------
    // Action Name
    //-------------------------------------------------------
    public static final String ACTION_setSceneSingle = "setSceneSingle";
    public static final String ACTION_setParamFinish = "setParamFinish";
    public static final String ACTION_setBright = "setBright";
    public static final String ACTION_delCron = "delCron";
    public static final String ACTION_restore = "restore";
    public static final String ACTION_setScene = "setScene";
    public static final String ACTION_setPower = "setPower";
    public static final String ACTION_toggle = "toggle";
    public static final String ACTION_addCron = "addCron";
    public static final String ACTION_setDefault = "setDefault";
    public static final String ACTION_setParamColors = "setParamColors";
    public static final String ACTION_setParamModel = "setParamModel";
    public static final String ACTION_setCt = "setCt";
    public static final String ACTION_getCron = "getCron";
    public static final String ACTION_setParamCount = "setParamCount";

    //-------------------------------------------------------
    // Property Name
    //-------------------------------------------------------
    public static final String PROPERTY_Ct = "Ct";
    public static final String PROPERTY_DelayOff = "DelayOff";
    public static final String PROPERTY_ParamModel = "ParamModel";
    public static final String PROPERTY_Effect = "Effect";
    public static final String PROPERTY_Duration = "Duration";
    public static final String PROPERTY_ParamCount = "ParamCount";
    public static final String PROPERTY_ParamFinish = "ParamFinish";
    public static final String PROPERTY_Bright = "Bright";
    public static final String PROPERTY_CronType = "CronType";
    public static final String PROPERTY_Power = "Power";
    public static final String PROPERTY_ParamColors = "ParamColors";

    private AbstractDevice device = null;

    public LightMonoService(AbstractDevice device) {
        this.device = device;
    }

    //-------------------------------------------------------
    // Property value defined
    //-------------------------------------------------------
    /**
    * 变化效果
    */
    public enum Effect {
        undefined,
        smooth,
        sudden,
    }

    /**
    * 开关状态
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
         * 延时关灯时间 发生改变
         */
        void onDelayOffChanged(Long delayOff);
        /**
         * 灯亮度 发生改变
         */
        void onBrightChanged(Long bright);
        /**
         * 开关状态 发生改变
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
                            case PROPERTY_DelayOff:
                                Long delayOff = (Long) info.getValue(PROPERTY_DelayOff);
                                listener.onDelayOffChanged(delayOff);
                                break;
                            case PROPERTY_Bright:
                                Long bright = (Long) info.getValue(PROPERTY_Bright);
                                listener.onBrightChanged(bright);
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
        void onSucceed(Long delayOff, Long bright, Power power);

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
            propertyInfo.addProperty(getService().getProperty(PROPERTY_DelayOff));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_Bright));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_Power));

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Long delayOff = (Long) info.getValue(PROPERTY_DelayOff);
                            Long bright = (Long) info.getValue(PROPERTY_Bright);
                            Power power = Power.valueOf((String) info.getValue(PROPERTY_Power));
                            handler.onSucceed(delayOff, bright, power);
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
     * 回调接口： 读取DelayOff
     */
    public interface GetDelayOffCompletionHandler {
        void onSucceed(Long delayOff);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取延时关灯时间
     */
    public int getDelayOff(final GetDelayOffCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_DelayOff);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Long delayOff = (Long) info.getValue(PROPERTY_DelayOff);
                            handler.onSucceed(delayOff);
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
     * 回调接口： 读取Bright
     */
    public interface GetBrightCompletionHandler {
        void onSucceed(Long bright);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取灯亮度
     */
    public int getBright(final GetBrightCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Bright);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Long bright = (Long) info.getValue(PROPERTY_Bright);
                            handler.onSucceed(bright);
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
     * 读取开关状态
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
     * 设置灯光
     */
    public int setSceneSingle(String paramModel, Long paramCount, Long paramFinish, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setSceneSingle);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_ParamModel, paramModel)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_ParamCount, paramCount)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_ParamFinish, paramFinish)) {
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
     * 设置流光结束？
     */
    public int setParamFinish(Long paramFinish, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setParamFinish);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_ParamFinish, paramFinish)) {
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
     * 设置亮度
     */
    public int setBright(Long bright, Effect effect, Long duration, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setBright);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_Bright, bright)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_Effect, effect)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_Duration, duration)) {
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
     * 删除计划任务
     */
    public int delCron(Long cronType, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_delCron);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_CronType, cronType)) {
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
     * 重置
     */
    public int restore(final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_restore);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
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
     * 设置灯光
     */
    public int setScene(String paramModel, Long paramCount, Long paramFinish, String paramColors, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setScene);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_ParamModel, paramModel)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_ParamCount, paramCount)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_ParamFinish, paramFinish)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_ParamColors, paramColors)) {
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
     * 开关灯
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

            if (!actionInfo.setArgumentValue(PROPERTY_Power, power)) {
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
     * 开关灯切换
     */
    public int toggle(final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_toggle);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
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
     * 计划任务(如延时关灯)
     */
    public int addCron(Long cronType, Long delayOff, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_addCron);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_CronType, cronType)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_DelayOff, delayOff)) {
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
     * 设置当前状态为默认开启
     */
    public int setDefault(final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setDefault);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
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
     * 设置流光颜色
     */
    public int setParamColors(String paramColors, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setParamColors);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_ParamColors, paramColors)) {
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
     * 设置流光模式
     */
    public int setParamModel(String paramModel, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setParamModel);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_ParamModel, paramModel)) {
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
     * 设置色温
     */
    public int setCt(Long ct, Effect effect, Long duration, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setCt);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_Ct, ct)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_Effect, effect)) {
                ret = ReturnCode.E_ACTION_ARGUMENT_INVALID;
                break;
            }
            if (!actionInfo.setArgumentValue(PROPERTY_Duration, duration)) {
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
     * 获得计划任务
     */
    public int getCron(Long cronType, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_getCron);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_CronType, cronType)) {
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
     * 设置流光次数
     */
    public int setParamCount(Long paramCount, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setParamCount);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_ParamCount, paramCount)) {
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

