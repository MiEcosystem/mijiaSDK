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

public class SmartSocketBaseService extends AbstractService {
    private static final String TAG = "SmartSocketBaseService";

    //-------------------------------------------------------
    // Action Name
    //-------------------------------------------------------
    public static final String ACTION_setUsbPlugOn = "setUsbPlugOn";
    public static final String ACTION_setUsbPlugOff = "setUsbPlugOff";
    public static final String ACTION_setPlugOff = "setPlugOff";
    public static final String ACTION_setPlugOn = "setPlugOn";

    //-------------------------------------------------------
    // Property Name
    //-------------------------------------------------------
    public static final String PROPERTY_UsbStatus = "UsbStatus";
    public static final String PROPERTY_PowerStatus = "PowerStatus";

    private AbstractDevice device = null;

    public SmartSocketBaseService(AbstractDevice device) {
        this.device = device;
    }

    //-------------------------------------------------------
    // Property value defined
    //-------------------------------------------------------

    //-------------------------------------------------------
    // Property: Notifications
    //-------------------------------------------------------
    public interface PropertyNotificationListener {

        /**
         * USB插口状态 发生改变
         */
        void onUsbStatusChanged(Boolean usbStatus);
        /**
         * 电力插口状态 发生改变
         */
        void onPowerStatusChanged(Boolean powerStatus);
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
                            case PROPERTY_UsbStatus:
                                Boolean usbStatus = (Boolean) info.getValue(PROPERTY_UsbStatus);
                                listener.onUsbStatusChanged(usbStatus);
                                break;
                            case PROPERTY_PowerStatus:
                                Boolean powerStatus = (Boolean) info.getValue(PROPERTY_PowerStatus);
                                listener.onPowerStatusChanged(powerStatus);
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
        void onSucceed(Boolean usbStatus, Boolean powerStatus);

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
            propertyInfo.addProperty(getService().getProperty(PROPERTY_UsbStatus));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_PowerStatus));

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Boolean usbStatus = (Boolean) info.getValue(PROPERTY_UsbStatus);
                            Boolean powerStatus = (Boolean) info.getValue(PROPERTY_PowerStatus);
                            handler.onSucceed(usbStatus, powerStatus);
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
     * 回调接口： 读取UsbStatus
     */
    public interface GetUsbStatusCompletionHandler {
        void onSucceed(Boolean usbStatus);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取USB插口状态
     */
    public int getUsbStatus(final GetUsbStatusCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_UsbStatus);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Boolean usbStatus = (Boolean) info.getValue(PROPERTY_UsbStatus);
                            handler.onSucceed(usbStatus);
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
     * 回调接口： 读取PowerStatus
     */
    public interface GetPowerStatusCompletionHandler {
        void onSucceed(Boolean powerStatus);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取电力插口状态
     */
    public int getPowerStatus(final GetPowerStatusCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_PowerStatus);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Boolean powerStatus = (Boolean) info.getValue(PROPERTY_PowerStatus);
                            handler.onSucceed(powerStatus);
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
     * 打开USB开关
     */
    public int setUsbPlugOn(final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setUsbPlugOn);
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
     * 关闭USB开关
     */
    public int setUsbPlugOff(final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setUsbPlugOff);
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
     * 关闭开关
     */
    public int setPlugOff(final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setPlugOff);
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
     * 打开开关
     */
    public int setPlugOn(final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setPlugOn);
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

}

