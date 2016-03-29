/* This file is auto-generated.*/

package com.mi.device;

import android.os.Parcel;
import android.util.Log;

import miot.api.device.AbstractDevice;
import miot.typedef.device.Device;
import miot.typedef.device.Service;

public class SmartSocketBase extends AbstractDevice {

    private static final String TAG = "SmartSocketBase";

    private static final String DEVICE_TYPE = "SmartSocketBase";

    public static final String SERVICE_SmartSocketBaseService = "urn:schemas-mi-com:service:SmartSocket:BaseService:1";
    public SmartSocketBaseService mSmartSocketBaseService = new SmartSocketBaseService(this);

    public synchronized static SmartSocketBase create(Device device) {
        Log.d(TAG, "create");

        SmartSocketBase thiz = null;
        do {
            String deviceType = device.getType().getClassType() + device.getType().getSubType();
            if (!deviceType.equals(DEVICE_TYPE)) {
                break;
            }

            thiz = new SmartSocketBase();
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

            Service baseService = device.getService(SERVICE_SmartSocketBaseService);
            if (baseService == null) {
                break;
            }
            mSmartSocketBaseService.setService(baseService);
            this.setDevice(device);

            ret = true;
        } while (false);

        return ret;
    }

    //-------------------------------------------------------
    // Parcelable
    //-------------------------------------------------------
    public static final Creator<SmartSocketBase> CREATOR = new Creator<SmartSocketBase>() {

        @Override
        public SmartSocketBase createFromParcel(Parcel in) {
            return new SmartSocketBase(in);
        }

        @Override
        public SmartSocketBase[] newArray(int size) {
            return new SmartSocketBase[size];
        }
    };

    private SmartSocketBase() {
    }

    private SmartSocketBase(Parcel in) {
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
