/* This file is auto-generated.*/

package com.mi.device;

import android.os.Parcel;
import android.util.Log;

import miot.api.device.AbstractDevice;
import miot.typedef.device.Device;
import miot.typedef.device.Service;

public class YeelinkLightMono extends AbstractDevice {

    private static final String TAG = "YeelinkLightMono";

    private static final String DEVICE_TYPE = "YeelinkLightMono";

    public static final String SERVICE_LightMonoService = "urn:schemas-mi-com:service:LightMono:Service:1";
    public LightMonoService mLightMonoService = new LightMonoService(this);

    public synchronized static YeelinkLightMono create(Device device) {
        Log.d(TAG, "create");

        YeelinkLightMono thiz = null;
        do {
            String deviceType = device.getType().getClassType() + device.getType().getSubType();
            if (!deviceType.equals(DEVICE_TYPE)) {
                break;
            }

            thiz = new YeelinkLightMono();
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

            Service service = device.getService(SERVICE_LightMonoService);
            if (service == null) {
                break;
            }
            mLightMonoService.setService(service);
            this.setDevice(device);

            ret = true;
        } while (false);

        return ret;
    }

    //-------------------------------------------------------
    // Parcelable
    //-------------------------------------------------------
    public static final Creator<YeelinkLightMono> CREATOR = new Creator<YeelinkLightMono>() {

        @Override
        public YeelinkLightMono createFromParcel(Parcel in) {
            return new YeelinkLightMono(in);
        }

        @Override
        public YeelinkLightMono[] newArray(int size) {
            return new YeelinkLightMono[size];
        }
    };

    private YeelinkLightMono() {
    }

    private YeelinkLightMono(Parcel in) {
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
