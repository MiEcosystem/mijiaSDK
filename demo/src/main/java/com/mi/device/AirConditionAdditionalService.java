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

public class AirConditionAdditionalService extends AbstractService {
    private static final String TAG = "AirConditionAdditionalService";

    //-------------------------------------------------------
    // Action Name
    //-------------------------------------------------------
    public static final String ACTION_setScreenDisplay = "setScreenDisplay";
    public static final String ACTION_setWindUpDown = "setWindUpDown";
    public static final String ACTION_setRestrictPowerPercent = "setRestrictPowerPercent";
    public static final String ACTION_setSleep = "setSleep";
    public static final String ACTION_setElecLock = "setElecLock";
    public static final String ACTION_setECO = "setECO";
    public static final String ACTION_setElecHeat = "setElecHeat";
    public static final String ACTION_setMildewProof = "setMildewProof";
    public static final String ACTION_setSleepWave = "setSleepWave";
    public static final String ACTION_setWindLeftRight = "setWindLeftRight";
    public static final String ACTION_setRestrictPower = "setRestrictPower";
    public static final String ACTION_setSilent = "setSilent";
    public static final String ACTION_setClean = "setClean";
    public static final String ACTION_setStrong = "setStrong";
    public static final String ACTION_setHealth = "setHealth";

    //-------------------------------------------------------
    // Property Name
    //-------------------------------------------------------
    public static final String PROPERTY_SleepWave = "SleepWave";
    public static final String PROPERTY_RestrictPowerPercent = "RestrictPowerPercent";
    public static final String PROPERTY_Health = "Health";
    public static final String PROPERTY_ScreenDisplay = "ScreenDisplay";
    public static final String PROPERTY_Silent = "Silent";
    public static final String PROPERTY_WindUpDown = "WindUpDown";
    public static final String PROPERTY_Clean = "Clean";
    public static final String PROPERTY_Strong = "Strong";
    public static final String PROPERTY_WindLeftRight = "WindLeftRight";
    public static final String PROPERTY_ECO = "ECO";
    public static final String PROPERTY_ElecLock = "ElecLock";
    public static final String PROPERTY_Sleep = "Sleep";
    public static final String PROPERTY_RestrictPower = "RestrictPower";
    public static final String PROPERTY_ElecHeat = "ElecHeat";
    public static final String PROPERTY_FaultDetail = "FaultDetail";
    public static final String PROPERTY_MildewProof = "MildewProof";

    private AbstractDevice device = null;

    public AirConditionAdditionalService(AbstractDevice device) {
        this.device = device;
    }

    //-------------------------------------------------------
    // Property value defined
    //-------------------------------------------------------

    /**
     * 健康（on|off）
     */
    public enum Health {
        undefined,
        on,
        off,
    }

    /**
     * 屏显（on|off）
     */
    public enum ScreenDisplay {
        undefined,
        on,
        off,
    }

    /**
     * 静音（on|off）
     */
    public enum Silent {
        undefined,
        on,
        off,
    }

    /**
     * 上下摆风开关（on|off）
     */
    public enum WindUpDown {
        undefined,
        on,
        off,
    }

    /**
     * 清洁（on|off）
     */
    public enum Clean {
        undefined,
        on,
        off,
    }

    /**
     * 强力（on|off）
     */
    public enum Strong {
        undefined,
        on,
        off,
    }

    /**
     * 左右摆风开关（on|off）
     */
    public enum WindLeftRight {
        undefined,
        on,
        off,
    }

    /**
     * ECO开关（on|off）
     */
    public enum ECO {
        undefined,
        on,
        off,
    }

    /**
     * 童锁开关（on|off）
     */
    public enum ElecLock {
        undefined,
        on,
        off,
    }

    /**
     * 睡眠开关（on|off）
     */
    public enum Sleep {
        undefined,
        on,
        off,
    }

    /**
     * 限电开启
     */
    public enum RestrictPower {
        undefined,
        on,
        off,
    }

    /**
     * 辅热开关（on|off）
     */
    public enum ElecHeat {
        undefined,
        on,
        off,
    }

    /**
     * 防霉（on|off）
     */
    public enum MildewProof {
        undefined,
        on,
        off,
    }


    //-------------------------------------------------------
    // Property: Notifications
    //-------------------------------------------------------
    public interface PropertyNotificationListener {

        /**
         * 睡眠曲线 发生改变
         */
        void onSleepWaveChanged(String sleepWave);

        /**
         * 限电百分比 发生改变
         */
        void onRestrictPowerPercentChanged(Integer restrictPowerPercent);

        /**
         * 健康（on|off） 发生改变
         */
        void onHealthChanged(Health health);

        /**
         * 屏显（on|off） 发生改变
         */
        void onScreenDisplayChanged(ScreenDisplay screenDisplay);

        /**
         * 静音（on|off） 发生改变
         */
        void onSilentChanged(Silent silent);

        /**
         * 上下摆风开关（on|off） 发生改变
         */
        void onWindUpDownChanged(WindUpDown windUpDown);

        /**
         * 清洁（on|off） 发生改变
         */
        void onCleanChanged(Clean clean);

        /**
         * 强力（on|off） 发生改变
         */
        void onStrongChanged(Strong strong);

        /**
         * 左右摆风开关（on|off） 发生改变
         */
        void onWindLeftRightChanged(WindLeftRight windLeftRight);

        /**
         * ECO开关（on|off） 发生改变
         */
        void onECOChanged(ECO eCO);

        /**
         * 童锁开关（on|off） 发生改变
         */
        void onElecLockChanged(ElecLock elecLock);

        /**
         * 睡眠开关（on|off） 发生改变
         */
        void onSleepChanged(Sleep sleep);

        /**
         * 限电开启 发生改变
         */
        void onRestrictPowerChanged(RestrictPower restrictPower);

        /**
         * 辅热开关（on|off） 发生改变
         */
        void onElecHeatChanged(ElecHeat elecHeat);

        /**
         * 故障代码 发生改变
         */
        void onFaultDetailChanged(Long faultDetail);

        /**
         * 防霉（on|off） 发生改变
         */
        void onMildewProofChanged(MildewProof mildewProof);
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
                            case PROPERTY_SleepWave:
                                String sleepWave = (String) info.getValue(PROPERTY_SleepWave);
                                listener.onSleepWaveChanged(sleepWave);
                                break;
                            case PROPERTY_RestrictPowerPercent:
                                Integer restrictPowerPercent = (Integer) info.getValue(PROPERTY_RestrictPowerPercent);
                                listener.onRestrictPowerPercentChanged(restrictPowerPercent);
                                break;
                            case PROPERTY_Health:
                                Health health = Health.valueOf((String) info.getValue(PROPERTY_Health));
                                listener.onHealthChanged(health);
                                break;
                            case PROPERTY_ScreenDisplay:
                                ScreenDisplay screenDisplay = ScreenDisplay.valueOf((String) info.getValue(PROPERTY_ScreenDisplay));
                                listener.onScreenDisplayChanged(screenDisplay);
                                break;
                            case PROPERTY_Silent:
                                Silent silent = Silent.valueOf((String) info.getValue(PROPERTY_Silent));
                                listener.onSilentChanged(silent);
                                break;
                            case PROPERTY_WindUpDown:
                                WindUpDown windUpDown = WindUpDown.valueOf((String) info.getValue(PROPERTY_WindUpDown));
                                listener.onWindUpDownChanged(windUpDown);
                                break;
                            case PROPERTY_Clean:
                                Clean clean = Clean.valueOf((String) info.getValue(PROPERTY_Clean));
                                listener.onCleanChanged(clean);
                                break;
                            case PROPERTY_Strong:
                                Strong strong = Strong.valueOf((String) info.getValue(PROPERTY_Strong));
                                listener.onStrongChanged(strong);
                                break;
                            case PROPERTY_WindLeftRight:
                                WindLeftRight windLeftRight = WindLeftRight.valueOf((String) info.getValue(PROPERTY_WindLeftRight));
                                listener.onWindLeftRightChanged(windLeftRight);
                                break;
                            case PROPERTY_ECO:
                                ECO eCO = ECO.valueOf((String) info.getValue(PROPERTY_ECO));
                                listener.onECOChanged(eCO);
                                break;
                            case PROPERTY_ElecLock:
                                ElecLock elecLock = ElecLock.valueOf((String) info.getValue(PROPERTY_ElecLock));
                                listener.onElecLockChanged(elecLock);
                                break;
                            case PROPERTY_Sleep:
                                Sleep sleep = Sleep.valueOf((String) info.getValue(PROPERTY_Sleep));
                                listener.onSleepChanged(sleep);
                                break;
                            case PROPERTY_RestrictPower:
                                RestrictPower restrictPower = RestrictPower.valueOf((String) info.getValue(PROPERTY_RestrictPower));
                                listener.onRestrictPowerChanged(restrictPower);
                                break;
                            case PROPERTY_ElecHeat:
                                ElecHeat elecHeat = ElecHeat.valueOf((String) info.getValue(PROPERTY_ElecHeat));
                                listener.onElecHeatChanged(elecHeat);
                                break;
                            case PROPERTY_FaultDetail:
                                Long faultDetail = (Long) info.getValue(PROPERTY_FaultDetail);
                                listener.onFaultDetailChanged(faultDetail);
                                break;
                            case PROPERTY_MildewProof:
                                MildewProof mildewProof = MildewProof.valueOf((String) info.getValue(PROPERTY_MildewProof));
                                listener.onMildewProofChanged(mildewProof);
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
        void onSucceed(Integer restrictPowerPercent, Health health, ScreenDisplay screenDisplay, Silent silent, WindUpDown windUpDown, Clean clean, Strong strong, WindLeftRight windLeftRight, ECO eCO, ElecLock elecLock, Sleep sleep, RestrictPower restrictPower, ElecHeat elecHeat, MildewProof mildewProof);

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
            propertyInfo.addProperty(getService().getProperty(PROPERTY_RestrictPowerPercent));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_Health));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_ScreenDisplay));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_Silent));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_WindUpDown));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_Clean));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_Strong));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_WindLeftRight));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_ECO));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_ElecLock));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_Sleep));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_RestrictPower));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_ElecHeat));
            propertyInfo.addProperty(getService().getProperty(PROPERTY_MildewProof));

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Integer restrictPowerPercent = (Integer) info.getValue(PROPERTY_RestrictPowerPercent);
                            Health health = Health.valueOf((String) info.getValue(PROPERTY_Health));
                            ScreenDisplay screenDisplay = ScreenDisplay.valueOf((String) info.getValue(PROPERTY_ScreenDisplay));
                            Silent silent = Silent.valueOf((String) info.getValue(PROPERTY_Silent));
                            WindUpDown windUpDown = WindUpDown.valueOf((String) info.getValue(PROPERTY_WindUpDown));
                            Clean clean = Clean.valueOf((String) info.getValue(PROPERTY_Clean));
                            Strong strong = Strong.valueOf((String) info.getValue(PROPERTY_Strong));
                            WindLeftRight windLeftRight = WindLeftRight.valueOf((String) info.getValue(PROPERTY_WindLeftRight));
                            ECO eCO = ECO.valueOf((String) info.getValue(PROPERTY_ECO));
                            ElecLock elecLock = ElecLock.valueOf((String) info.getValue(PROPERTY_ElecLock));
                            Sleep sleep = Sleep.valueOf((String) info.getValue(PROPERTY_Sleep));
                            RestrictPower restrictPower = RestrictPower.valueOf((String) info.getValue(PROPERTY_RestrictPower));
                            ElecHeat elecHeat = ElecHeat.valueOf((String) info.getValue(PROPERTY_ElecHeat));
                            MildewProof mildewProof = MildewProof.valueOf((String) info.getValue(PROPERTY_MildewProof));
                            handler.onSucceed(restrictPowerPercent, health, screenDisplay, silent, windUpDown, clean, strong, windLeftRight, eCO, elecLock, sleep, restrictPower, elecHeat, mildewProof);
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
     * 回调接口： 读取RestrictPowerPercent
     */
    public interface GetRestrictPowerPercentCompletionHandler {
        void onSucceed(Integer restrictPowerPercent);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取限电百分比
     */
    public int getRestrictPowerPercent(final GetRestrictPowerPercentCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_RestrictPowerPercent);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Integer restrictPowerPercent = (Integer) info.getValue(PROPERTY_RestrictPowerPercent);
                            handler.onSucceed(restrictPowerPercent);
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
     * 回调接口： 读取Health
     */
    public interface GetHealthCompletionHandler {
        void onSucceed(Health health);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取健康（on|off）
     */
    public int getHealth(final GetHealthCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Health);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Health health = Health.valueOf((String) info.getValue(PROPERTY_Health));
                            handler.onSucceed(health);
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
     * 回调接口： 读取ScreenDisplay
     */
    public interface GetScreenDisplayCompletionHandler {
        void onSucceed(ScreenDisplay screenDisplay);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取屏显（on|off）
     */
    public int getScreenDisplay(final GetScreenDisplayCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_ScreenDisplay);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            ScreenDisplay screenDisplay = ScreenDisplay.valueOf((String) info.getValue(PROPERTY_ScreenDisplay));
                            handler.onSucceed(screenDisplay);
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
     * 回调接口： 读取Silent
     */
    public interface GetSilentCompletionHandler {
        void onSucceed(Silent silent);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取静音（on|off）
     */
    public int getSilent(final GetSilentCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Silent);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Silent silent = Silent.valueOf((String) info.getValue(PROPERTY_Silent));
                            handler.onSucceed(silent);
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
     * 回调接口： 读取WindUpDown
     */
    public interface GetWindUpDownCompletionHandler {
        void onSucceed(WindUpDown windUpDown);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取上下摆风开关（on|off）
     */
    public int getWindUpDown(final GetWindUpDownCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_WindUpDown);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            WindUpDown windUpDown = WindUpDown.valueOf((String) info.getValue(PROPERTY_WindUpDown));
                            handler.onSucceed(windUpDown);
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
     * 回调接口： 读取Clean
     */
    public interface GetCleanCompletionHandler {
        void onSucceed(Clean clean);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取清洁（on|off）
     */
    public int getClean(final GetCleanCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Clean);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Clean clean = Clean.valueOf((String) info.getValue(PROPERTY_Clean));
                            handler.onSucceed(clean);
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
     * 回调接口： 读取Strong
     */
    public interface GetStrongCompletionHandler {
        void onSucceed(Strong strong);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取强力（on|off）
     */
    public int getStrong(final GetStrongCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Strong);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Strong strong = Strong.valueOf((String) info.getValue(PROPERTY_Strong));
                            handler.onSucceed(strong);
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
     * 回调接口： 读取WindLeftRight
     */
    public interface GetWindLeftRightCompletionHandler {
        void onSucceed(WindLeftRight windLeftRight);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取左右摆风开关（on|off）
     */
    public int getWindLeftRight(final GetWindLeftRightCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_WindLeftRight);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            WindLeftRight windLeftRight = WindLeftRight.valueOf((String) info.getValue(PROPERTY_WindLeftRight));
                            handler.onSucceed(windLeftRight);
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
     * 回调接口： 读取ECO
     */
    public interface GetECOCompletionHandler {
        void onSucceed(ECO eCO);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取ECO开关（on|off）
     */
    public int getECO(final GetECOCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_ECO);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            ECO eCO = ECO.valueOf((String) info.getValue(PROPERTY_ECO));
                            handler.onSucceed(eCO);
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
     * 回调接口： 读取ElecLock
     */
    public interface GetElecLockCompletionHandler {
        void onSucceed(ElecLock elecLock);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取童锁开关（on|off）
     */
    public int getElecLock(final GetElecLockCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_ElecLock);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            ElecLock elecLock = ElecLock.valueOf((String) info.getValue(PROPERTY_ElecLock));
                            handler.onSucceed(elecLock);
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
     * 回调接口： 读取Sleep
     */
    public interface GetSleepCompletionHandler {
        void onSucceed(Sleep sleep);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取睡眠开关（on|off）
     */
    public int getSleep(final GetSleepCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Sleep);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            Sleep sleep = Sleep.valueOf((String) info.getValue(PROPERTY_Sleep));
                            handler.onSucceed(sleep);
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
     * 回调接口： 读取RestrictPower
     */
    public interface GetRestrictPowerCompletionHandler {
        void onSucceed(RestrictPower restrictPower);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取限电开启
     */
    public int getRestrictPower(final GetRestrictPowerCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_RestrictPower);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            RestrictPower restrictPower = RestrictPower.valueOf((String) info.getValue(PROPERTY_RestrictPower));
                            handler.onSucceed(restrictPower);
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
     * 回调接口： 读取ElecHeat
     */
    public interface GetElecHeatCompletionHandler {
        void onSucceed(ElecHeat elecHeat);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取辅热开关（on|off）
     */
    public int getElecHeat(final GetElecHeatCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_ElecHeat);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            ElecHeat elecHeat = ElecHeat.valueOf((String) info.getValue(PROPERTY_ElecHeat));
                            handler.onSucceed(elecHeat);
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
     * 回调接口： 读取MildewProof
     */
    public interface GetMildewProofCompletionHandler {
        void onSucceed(MildewProof mildewProof);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取防霉（on|off）
     */
    public int getMildewProof(final GetMildewProofCompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_MildewProof);
            if (propertyInfo == null) {
                ret = ReturnCode.E_PROPERTY_INVALID;
                break;
            }

            DeviceManipulator op = MiotManager.getDeviceManipulator();
            ret = op.readProperty(propertyInfo,
                    new DeviceManipulator.ReadPropertyCompletionHandler() {
                        @Override
                        public void onSucceed(PropertyInfo info) {
                            MildewProof mildewProof = MildewProof.valueOf((String) info.getValue(PROPERTY_MildewProof));
                            handler.onSucceed(mildewProof);
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
     * 设置屏显
     */
    public int setScreenDisplay(ScreenDisplay screenDisplay, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setScreenDisplay);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_ScreenDisplay, screenDisplay.toString())) {
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
     * 上下摆风
     */
    public int setWindUpDown(WindUpDown windUpDown, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setWindUpDown);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_WindUpDown, windUpDown.toString())) {
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
     * 设置限电百分比
     */
    public int setRestrictPowerPercent(Integer restrictPowerPercent, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setRestrictPowerPercent);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_RestrictPowerPercent, restrictPowerPercent)) {
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
     * 睡眠
     */
    public int setSleep(Sleep sleep, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setSleep);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_Sleep, sleep.toString())) {
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
     * 电子锁
     */
    public int setElecLock(ElecLock elecLock, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setElecLock);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_ElecLock, elecLock.toString())) {
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
     * 节能
     */
    public int setECO(ECO eCO, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setECO);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_ECO, eCO.toString())) {
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
     * 辅热
     */
    public int setElecHeat(ElecHeat elecHeat, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setElecHeat);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_ElecHeat, elecHeat.toString())) {
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
     * 设置防霉
     */
    public int setMildewProof(MildewProof mildewProof, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setMildewProof);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_MildewProof, mildewProof.toString())) {
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
     * 设置睡眠曲线
     */
    public int setSleepWave(String sleepWave, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setSleepWave);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_SleepWave, sleepWave)) {
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
     * 左右摆风
     */
    public int setWindLeftRight(WindLeftRight windLeftRight, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setWindLeftRight);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_WindLeftRight, windLeftRight.toString())) {
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
     * 设置限电开启
     */
    public int setRestrictPower(RestrictPower restrictPower, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setRestrictPower);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_RestrictPower, restrictPower.toString())) {
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
     * 设置静音
     */
    public int setSilent(Silent silent, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setSilent);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_Silent, silent.toString())) {
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
     * 清洁
     */
    public int setClean(Clean clean, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setClean);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_Clean, clean.toString())) {
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
     * 设置强力
     */
    public int setStrong(Strong strong, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setStrong);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_Strong, strong.toString())) {
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
     * 设置健康
     */
    public int setHealth(Health health, final CompletionHandler handler) {
        int ret = 0;

        do {
            if (!this.device.isConnectionEstablished()) {
                ret = ReturnCode.E_DEVICE_NOT_CONFIGURATE_CONNECTION;
                break;
            }

            final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setHealth);
            if (actionInfo == null) {
                ret = ReturnCode.E_ACTION_NOT_SUPPORT;
                break;
            }

            if (!actionInfo.setArgumentValue(PROPERTY_Health, health.toString())) {
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

