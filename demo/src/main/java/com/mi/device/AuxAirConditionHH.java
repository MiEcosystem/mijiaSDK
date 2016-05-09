/* This file is auto-generated.*/

package com.mi.device;

import android.os.Parcel;
import android.util.Log;

import com.mi.device.AirConditionAdditionalService;
import com.mi.device.AirConditionBaseService;

import miot.api.device.AbstractDevice;
import miot.typedef.device.Device;
import miot.typedef.device.Service;

public class AuxAirConditionHH extends AbstractDevice {

    private static final String TAG = "AuxAirConditionHH";

    private static final String DEVICE_TYPE = "AuxAirConditionHH";

    public static final String SERVICE_AirConditionBaseService = "urn:schemas-mi-com:service:AirCondition:BaseService:1";
    public AirConditionBaseService mAirConditionBaseService = new AirConditionBaseService(this);
    public static final String SERVICE_AirConditionAdditionalService = "urn:schemas-mi-com:service:AirCondition:AdditionalService:1";
    public AirConditionAdditionalService mAirConditionAdditionalService = new AirConditionAdditionalService(this);

    public synchronized static AuxAirConditionHH create(Device device) {
        Log.d(TAG, "create");

        AuxAirConditionHH thiz = null;
        do {
            String deviceType = device.getType().getClassType() + device.getType().getSubType();
            if (!deviceType.equals(DEVICE_TYPE)) {
                break;
            }

            thiz = new AuxAirConditionHH();
            if (!thiz.init(device)) {
                thiz = null;
            }
        } while (false);

        return thiz;
    }

    private boolean init(Device device) {
        boolean ret = false;

        do {
            if (device == null) {
                break;
            }

            Service baseService = device.getService(SERVICE_AirConditionBaseService);
            if (baseService == null) {
                break;
            }
            mAirConditionBaseService.setService(baseService);
            Service additionalService = device.getService(SERVICE_AirConditionAdditionalService);
            if (additionalService == null) {
                break;
            }
            mAirConditionAdditionalService.setService(additionalService);
            this.setDevice(device);

            ret = true;
        } while (false);

        return ret;
    }

    //-------------------------------------------------------
    // Parcelable
    //-------------------------------------------------------
    public static final Creator<AuxAirConditionHH> CREATOR = new Creator<AuxAirConditionHH>() {

        @Override
        public AuxAirConditionHH createFromParcel(Parcel in) {
            return new AuxAirConditionHH(in);
        }

        @Override
        public AuxAirConditionHH[] newArray(int size) {
            return new AuxAirConditionHH[size];
        }
    };

    private AuxAirConditionHH() {
    }

    private AuxAirConditionHH(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        Device device = in.readParcelable(Device.class.getClassLoader());

        if (!this.init(device)) {
            Log.d(TAG, "init from device failed!");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.getDevice(), flags);
    }
}
