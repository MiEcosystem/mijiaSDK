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
    public static final String PROPERTY_Sleep = "Sleep";
    public static final String PROPERTY_WindUpDown = "WindUpDown";
    public static final String PROPERTY_WindLeftRight = "WindLeftRight";
    public static final String PROPERTY_ElecHeat = "ElecHeat";
    public static final String PROPERTY_ECO = "ECO";
    public static final String PROPERTY_Clean = "Clean";
    public static final String PROPERTY_ElecLock = "ElecLock";
    public static final String PROPERTY_Health = "Health";
    public static final String PROPERTY_ScreenDisplay = "ScreenDisplay";
    public static final String PROPERTY_MildewProof = "MildewProof";
    public static final String PROPERTY_Strong = "Strong";
    public static final String PROPERTY_Silent = "Silent";
    public static final String PROPERTY_SleepWave = "SleepWave";
    public static final String PROPERTY_RestrictPower = "RestrictPower";
    public static final String PROPERTY_RestrictPowerPercent = "RestrictPowerPercent";

    private AbstractDevice mDevice = null;

    public AirConditionAdditionalService(AbstractDevice device) {
        mDevice = device;
    }

    //-------------------------------------------------------
    // Property value defined
    //-------------------------------------------------------
    /**
    * 睡眠开关（on|off）
    */
    public enum Sleep {
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
    * 左右摆风开关（on|off）
    */
    public enum WindLeftRight {
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
    * ECO开关（on|off）
    */
    public enum ECO {
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
    * 童锁开关（on|off）
    */
    public enum ElecLock {
        undefined,
        on,
        off,
    }

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
    * 防霉（on|off）
    */
    public enum MildewProof {
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
    * 静音（on|off）
    */
    public enum Silent {
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


    //-------------------------------------------------------
    // Property: Notifications
    //-------------------------------------------------------
    public interface PropertyNotificationListener {

        /**
         * 睡眠开关（on|off） 发生改变
         */
        void onSleepChanged(Sleep sleep);
        /**
         * 上下摆风开关（on|off） 发生改变
         */
        void onWindUpDownChanged(WindUpDown windUpDown);
        /**
         * 左右摆风开关（on|off） 发生改变
         */
        void onWindLeftRightChanged(WindLeftRight windLeftRight);
        /**
         * 辅热开关（on|off） 发生改变
         */
        void onElecHeatChanged(ElecHeat elecHeat);
        /**
         * ECO开关（on|off） 发生改变
         */
        void onECOChanged(ECO eCO);
        /**
         * 清洁（on|off） 发生改变
         */
        void onCleanChanged(Clean clean);
        /**
         * 童锁开关（on|off） 发生改变
         */
        void onElecLockChanged(ElecLock elecLock);
        /**
         * 健康（on|off） 发生改变
         */
        void onHealthChanged(Health health);
        /**
         * 屏显（on|off） 发生改变
         */
        void onScreenDisplayChanged(ScreenDisplay screenDisplay);
        /**
         * 防霉（on|off） 发生改变
         */
        void onMildewProofChanged(MildewProof mildewProof);
        /**
         * 强力（on|off） 发生改变
         */
        void onStrongChanged(Strong strong);
        /**
         * 静音（on|off） 发生改变
         */
        void onSilentChanged(Silent silent);
        /**
         * 睡眠曲线 发生改变
         */
        void onSleepWaveChanged(String sleepWave);
        /**
         * 限电开启 发生改变
         */
        void onRestrictPowerChanged(RestrictPower restrictPower);
        /**
         * 限电百分比 发生改变
         */
        void onRestrictPowerPercentChanged(Integer restrictPowerPercent);
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
                            case PROPERTY_Sleep:
                                Sleep sleep = Sleep.valueOf((String) info.getValue(PROPERTY_Sleep));
                                listener.onSleepChanged(sleep);
                                break;
                            case PROPERTY_WindUpDown:
                                WindUpDown windUpDown = WindUpDown.valueOf((String) info.getValue(PROPERTY_WindUpDown));
                                listener.onWindUpDownChanged(windUpDown);
                                break;
                            case PROPERTY_WindLeftRight:
                                WindLeftRight windLeftRight = WindLeftRight.valueOf((String) info.getValue(PROPERTY_WindLeftRight));
                                listener.onWindLeftRightChanged(windLeftRight);
                                break;
                            case PROPERTY_ElecHeat:
                                ElecHeat elecHeat = ElecHeat.valueOf((String) info.getValue(PROPERTY_ElecHeat));
                                listener.onElecHeatChanged(elecHeat);
                                break;
                            case PROPERTY_ECO:
                                ECO eCO = ECO.valueOf((String) info.getValue(PROPERTY_ECO));
                                listener.onECOChanged(eCO);
                                break;
                            case PROPERTY_Clean:
                                Clean clean = Clean.valueOf((String) info.getValue(PROPERTY_Clean));
                                listener.onCleanChanged(clean);
                                break;
                            case PROPERTY_ElecLock:
                                ElecLock elecLock = ElecLock.valueOf((String) info.getValue(PROPERTY_ElecLock));
                                listener.onElecLockChanged(elecLock);
                                break;
                            case PROPERTY_Health:
                                Health health = Health.valueOf((String) info.getValue(PROPERTY_Health));
                                listener.onHealthChanged(health);
                                break;
                            case PROPERTY_ScreenDisplay:
                                ScreenDisplay screenDisplay = ScreenDisplay.valueOf((String) info.getValue(PROPERTY_ScreenDisplay));
                                listener.onScreenDisplayChanged(screenDisplay);
                                break;
                            case PROPERTY_MildewProof:
                                MildewProof mildewProof = MildewProof.valueOf((String) info.getValue(PROPERTY_MildewProof));
                                listener.onMildewProofChanged(mildewProof);
                                break;
                            case PROPERTY_Strong:
                                Strong strong = Strong.valueOf((String) info.getValue(PROPERTY_Strong));
                                listener.onStrongChanged(strong);
                                break;
                            case PROPERTY_Silent:
                                Silent silent = Silent.valueOf((String) info.getValue(PROPERTY_Silent));
                                listener.onSilentChanged(silent);
                                break;
                            case PROPERTY_SleepWave:
                                String sleepWave = (String) info.getValue(PROPERTY_SleepWave);
                                listener.onSleepWaveChanged(sleepWave);
                                break;
                            case PROPERTY_RestrictPower:
                                RestrictPower restrictPower = RestrictPower.valueOf((String) info.getValue(PROPERTY_RestrictPower));
                                listener.onRestrictPowerChanged(restrictPower);
                                break;
                            case PROPERTY_RestrictPowerPercent:
                                Integer restrictPowerPercent = (Integer) info.getValue(PROPERTY_RestrictPowerPercent);
                                listener.onRestrictPowerPercentChanged(restrictPowerPercent);
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
        void onSleepGetted(Sleep sleep);
        void onWindUpDownGetted(WindUpDown windUpDown);
        void onWindLeftRightGetted(WindLeftRight windLeftRight);
        void onElecHeatGetted(ElecHeat elecHeat);
        void onECOGetted(ECO eCO);
        void onCleanGetted(Clean clean);
        void onElecLockGetted(ElecLock elecLock);
        void onHealthGetted(Health health);
        void onScreenDisplayGetted(ScreenDisplay screenDisplay);
        void onMildewProofGetted(MildewProof mildewProof);
        void onStrongGetted(Strong strong);
        void onSilentGetted(Silent silent);
        void onRestrictPowerGetted(RestrictPower restrictPower);
        void onRestrictPowerPercentGetted(Integer restrictPowerPercent);
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
        propertyInfo.addProperty(getService().getProperty(PROPERTY_Sleep));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_WindUpDown));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_WindLeftRight));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_ElecHeat));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_ECO));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_Clean));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_ElecLock));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_Health));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_ScreenDisplay));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_MildewProof));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_Strong));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_Silent));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_RestrictPower));
        propertyInfo.addProperty(getService().getProperty(PROPERTY_RestrictPowerPercent));

        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property sleep = info.getProperty(PROPERTY_Sleep);
                if(sleep.isValueValid()) {
                    handler.onSleepGetted(Sleep.valueOf((String) sleep.getValue()));
                }
                Property windUpDown = info.getProperty(PROPERTY_WindUpDown);
                if(windUpDown.isValueValid()) {
                    handler.onWindUpDownGetted(WindUpDown.valueOf((String) windUpDown.getValue()));
                }
                Property windLeftRight = info.getProperty(PROPERTY_WindLeftRight);
                if(windLeftRight.isValueValid()) {
                    handler.onWindLeftRightGetted(WindLeftRight.valueOf((String) windLeftRight.getValue()));
                }
                Property elecHeat = info.getProperty(PROPERTY_ElecHeat);
                if(elecHeat.isValueValid()) {
                    handler.onElecHeatGetted(ElecHeat.valueOf((String) elecHeat.getValue()));
                }
                Property eCO = info.getProperty(PROPERTY_ECO);
                if(eCO.isValueValid()) {
                    handler.onECOGetted(ECO.valueOf((String) eCO.getValue()));
                }
                Property clean = info.getProperty(PROPERTY_Clean);
                if(clean.isValueValid()) {
                    handler.onCleanGetted(Clean.valueOf((String) clean.getValue()));
                }
                Property elecLock = info.getProperty(PROPERTY_ElecLock);
                if(elecLock.isValueValid()) {
                    handler.onElecLockGetted(ElecLock.valueOf((String) elecLock.getValue()));
                }
                Property health = info.getProperty(PROPERTY_Health);
                if(health.isValueValid()) {
                    handler.onHealthGetted(Health.valueOf((String) health.getValue()));
                }
                Property screenDisplay = info.getProperty(PROPERTY_ScreenDisplay);
                if(screenDisplay.isValueValid()) {
                    handler.onScreenDisplayGetted(ScreenDisplay.valueOf((String) screenDisplay.getValue()));
                }
                Property mildewProof = info.getProperty(PROPERTY_MildewProof);
                if(mildewProof.isValueValid()) {
                    handler.onMildewProofGetted(MildewProof.valueOf((String) mildewProof.getValue()));
                }
                Property strong = info.getProperty(PROPERTY_Strong);
                if(strong.isValueValid()) {
                    handler.onStrongGetted(Strong.valueOf((String) strong.getValue()));
                }
                Property silent = info.getProperty(PROPERTY_Silent);
                if(silent.isValueValid()) {
                    handler.onSilentGetted(Silent.valueOf((String) silent.getValue()));
                }
                Property restrictPower = info.getProperty(PROPERTY_RestrictPower);
                if(restrictPower.isValueValid()) {
                    handler.onRestrictPowerGetted(RestrictPower.valueOf((String) restrictPower.getValue()));
                }
                Property restrictPowerPercent = info.getProperty(PROPERTY_RestrictPowerPercent);
                if(restrictPowerPercent.isValueValid()) {
                    handler.onRestrictPowerPercentGetted((Integer) restrictPowerPercent.getValue());
                }
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
     * 回调接口： 读取Sleep
     */
    public interface GetSleepCompletionHandler {
        void onSucceed(Sleep sleep);

        void onFailed(int errCode, String description);
    }

    /**
     * 读取睡眠开关（on|off）
     */
    public void getSleep(final GetSleepCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Sleep);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property sleep = info.getProperty(PROPERTY_Sleep);
                if(sleep.isValueValid()) {
                    handler.onSucceed(Sleep.valueOf((String) info.getValue(PROPERTY_Sleep)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + sleep.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getWindUpDown(final GetWindUpDownCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_WindUpDown);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property windUpDown = info.getProperty(PROPERTY_WindUpDown);
                if(windUpDown.isValueValid()) {
                    handler.onSucceed(WindUpDown.valueOf((String) info.getValue(PROPERTY_WindUpDown)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + windUpDown.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getWindLeftRight(final GetWindLeftRightCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_WindLeftRight);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property windLeftRight = info.getProperty(PROPERTY_WindLeftRight);
                if(windLeftRight.isValueValid()) {
                    handler.onSucceed(WindLeftRight.valueOf((String) info.getValue(PROPERTY_WindLeftRight)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + windLeftRight.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getElecHeat(final GetElecHeatCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_ElecHeat);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property elecHeat = info.getProperty(PROPERTY_ElecHeat);
                if(elecHeat.isValueValid()) {
                    handler.onSucceed(ElecHeat.valueOf((String) info.getValue(PROPERTY_ElecHeat)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + elecHeat.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getECO(final GetECOCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_ECO);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property eCO = info.getProperty(PROPERTY_ECO);
                if(eCO.isValueValid()) {
                    handler.onSucceed(ECO.valueOf((String) info.getValue(PROPERTY_ECO)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + eCO.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getClean(final GetCleanCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Clean);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property clean = info.getProperty(PROPERTY_Clean);
                if(clean.isValueValid()) {
                    handler.onSucceed(Clean.valueOf((String) info.getValue(PROPERTY_Clean)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + clean.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getElecLock(final GetElecLockCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_ElecLock);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property elecLock = info.getProperty(PROPERTY_ElecLock);
                if(elecLock.isValueValid()) {
                    handler.onSucceed(ElecLock.valueOf((String) info.getValue(PROPERTY_ElecLock)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + elecLock.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getHealth(final GetHealthCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Health);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property health = info.getProperty(PROPERTY_Health);
                if(health.isValueValid()) {
                    handler.onSucceed(Health.valueOf((String) info.getValue(PROPERTY_Health)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + health.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getScreenDisplay(final GetScreenDisplayCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_ScreenDisplay);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property screenDisplay = info.getProperty(PROPERTY_ScreenDisplay);
                if(screenDisplay.isValueValid()) {
                    handler.onSucceed(ScreenDisplay.valueOf((String) info.getValue(PROPERTY_ScreenDisplay)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + screenDisplay.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getMildewProof(final GetMildewProofCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_MildewProof);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property mildewProof = info.getProperty(PROPERTY_MildewProof);
                if(mildewProof.isValueValid()) {
                    handler.onSucceed(MildewProof.valueOf((String) info.getValue(PROPERTY_MildewProof)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + mildewProof.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getStrong(final GetStrongCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Strong);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property strong = info.getProperty(PROPERTY_Strong);
                if(strong.isValueValid()) {
                    handler.onSucceed(Strong.valueOf((String) info.getValue(PROPERTY_Strong)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + strong.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getSilent(final GetSilentCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_Silent);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property silent = info.getProperty(PROPERTY_Silent);
                if(silent.isValueValid()) {
                    handler.onSucceed(Silent.valueOf((String) info.getValue(PROPERTY_Silent)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + silent.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
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
    public void getRestrictPower(final GetRestrictPowerCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_RestrictPower);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property restrictPower = info.getProperty(PROPERTY_RestrictPower);
                if(restrictPower.isValueValid()) {
                    handler.onSucceed(RestrictPower.valueOf((String) info.getValue(PROPERTY_RestrictPower)));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + restrictPower.getValue());
                }

            }

            @Override
            public void onFailed(int errCode, String description) {
                handler.onFailed(errCode, description);
            }
        });
    }
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
    public void getRestrictPowerPercent(final GetRestrictPowerPercentCompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        PropertyInfo propertyInfo = PropertyInfoFactory.create(getService(), PROPERTY_RestrictPowerPercent);
        DeviceManipulator op = MiotManager.getDeviceManipulator();
        op.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
            @Override
            public void onSucceed(PropertyInfo info) {
                Property restrictPowerPercent = info.getProperty(PROPERTY_RestrictPowerPercent);
                if(restrictPowerPercent.isValueValid()) {
                    handler.onSucceed((Integer) info.getValue(PROPERTY_RestrictPowerPercent));
                } else {
                    handler.onFailed(ReturnCode.E_INVALID_DATA, "device response valid: " + restrictPowerPercent.getValue());
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
     * 设置屏显
     */
    public void setScreenDisplay(ScreenDisplay screenDisplay, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setScreenDisplay);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_ScreenDisplay, screenDisplay.toString())) {
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
     * 上下摆风
     */
    public void setWindUpDown(WindUpDown windUpDown, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setWindUpDown);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_WindUpDown, windUpDown.toString())) {
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
     * 设置限电百分比
     */
    public void setRestrictPowerPercent(Integer restrictPowerPercent, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setRestrictPowerPercent);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_RestrictPowerPercent, restrictPowerPercent)) {
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
     * 睡眠
     */
    public void setSleep(Sleep sleep, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setSleep);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_Sleep, sleep.toString())) {
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
     * 电子锁
     */
    public void setElecLock(ElecLock elecLock, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setElecLock);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_ElecLock, elecLock.toString())) {
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
     * 节能
     */
    public void setECO(ECO eCO, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setECO);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_ECO, eCO.toString())) {
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
     * 辅热
     */
    public void setElecHeat(ElecHeat elecHeat, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setElecHeat);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_ElecHeat, elecHeat.toString())) {
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
     * 设置防霉
     */
    public void setMildewProof(MildewProof mildewProof, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setMildewProof);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_MildewProof, mildewProof.toString())) {
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
     * 设置睡眠曲线
     */
    public void setSleepWave(String sleepWave, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setSleepWave);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_SleepWave, sleepWave)) {
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
     * 左右摆风
     */
    public void setWindLeftRight(WindLeftRight windLeftRight, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setWindLeftRight);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_WindLeftRight, windLeftRight.toString())) {
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
     * 设置限电开启
     */
    public void setRestrictPower(RestrictPower restrictPower, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setRestrictPower);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_RestrictPower, restrictPower.toString())) {
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
     * 设置静音
     */
    public void setSilent(Silent silent, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setSilent);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_Silent, silent.toString())) {
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
     * 清洁
     */
    public void setClean(Clean clean, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setClean);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_Clean, clean.toString())) {
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
     * 设置强力
     */
    public void setStrong(Strong strong, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setStrong);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_Strong, strong.toString())) {
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
     * 设置健康
     */
    public void setHealth(Health health, final CompletionHandler handler) throws MiotException {
        if (!mDevice.isConnectionEstablished()) {
            throw new MiotException("device not configurated connection");
        }

        final ActionInfo actionInfo = ActionInfoFactory.create(getService(), ACTION_setHealth);
        if(actionInfo == null) {
            throw new MiotException("actionInfo is null");
        }

        if (!actionInfo.setArgumentValue(PROPERTY_Health, health.toString())) {
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

