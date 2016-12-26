/* This file is auto-generated.*/

package com.mi.device;

import android.os.Parcel;
import android.util.Log;

import com.miot.common.abstractdevice.AbstractDevice;
import com.miot.common.device.Device;
import com.miot.common.device.Service;

public class SoocareToothbrushx3 extends AbstractDevice {

    private static final String TAG = "SoocareToothbrushx3";

    private static final String DEVICE_TYPE = "SoocareToothbrushx3";

    public synchronized static SoocareToothbrushx3 create(Device device) {
        Log.d(TAG, "create");

        SoocareToothbrushx3 thiz = null;
        do {
            String deviceType = device.getType().getName();
            if (!deviceType.equals(DEVICE_TYPE)) {
                break;
            }

            thiz = new SoocareToothbrushx3();
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

            this.setDevice(device);

            ret = true;
        } while (false);

        return ret;
    }

    //-------------------------------------------------------
    // Parcelable
    //-------------------------------------------------------
    public static final Creator<SoocareToothbrushx3> CREATOR = new Creator<SoocareToothbrushx3>() {

        @Override
        public SoocareToothbrushx3 createFromParcel(Parcel in) {
            return new SoocareToothbrushx3(in);
        }

        @Override
        public SoocareToothbrushx3[] newArray(int size) {
            return new SoocareToothbrushx3[size];
        }
    };

    private SoocareToothbrushx3() {
    }

    private SoocareToothbrushx3(Parcel in) {
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
