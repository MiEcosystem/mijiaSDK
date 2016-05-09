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

    private AbstractDevice mDevice = null;

    public SmartSocketBaseService(AbstractDevice device) {
        mDevice = device;
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
        void onSucceed(Boolean usbStatus, Boolean powerStatus);

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
        propertyInfo.addProperty(getService().getProperty(PROPERTY_UsbStatus));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_PowerStatus));

        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
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
        });
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
    public void getUsbStatus(final GetUsbStatusCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_UsbStatus);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Boolean usbStatus = (Boolean) info.getValue(PROPERTY_UsbStatus);
                handler.onSucceed(usbStatus);
            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getPowerStatus(final GetPowerStatusCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_PowerStatus);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Boolean powerStatus = (Boolean) info.getValue(PROPERTY_PowerStatus);
                handler.onSucceed(powerStatus);
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
     * 打开USB开关
     */
    public void setUsbPlugOn(final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setUsbPlugOn);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
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
     * 关闭USB开关
     */
    public void setUsbPlugOff(final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setUsbPlugOff);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
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
     * 关闭开关
     */
    public void setPlugOff(final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setPlugOff);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
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
     * 打开开关
     */
    public void setPlugOn(final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setPlugOn);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
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

