/* This file is auto-generated.*/

package com.mi.device;

import android.os.Parcel;
import android.util.Log;

import miot.api.device.AbstractDevice;
import miot.typedef.device.Device;
import miot.typedef.device.Service;

public class ChuangmiPlugM1 extends AbstractDevice {

    private static final String TAG = "ChuangmiPlugM1";

    private static final String DEVICE_TYPE = "ChuangmiPlugM1";

    public static final String SERVICE_PlugBaseService = "urn:schemas-mi-com:service:Plug:BaseService:1";
    public PlugBaseService mPlugBaseService = new PlugBaseService(this);

    public synchronized static ChuangmiPlugM1 create(Device device) {
        Log.d(TAG, "create");

        ChuangmiPlugM1 thiz = null;
        do {
            String deviceType = device.getType().getClassType() + device.getType().getSubType();
            if (!deviceType.equals(DEVICE_TYPE)) {
                break;
            }

            thiz = new ChuangmiPlugM1();
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

            Service baseService = device.getService(SERVICE_PlugBaseService);
            if (baseService == null) {
                break;
            }
            mPlugBaseService.setService(baseService);
            this.setDevice(device);

            ret = true;
        } while (false);

        return ret;
    }

    //-------------------------------------------------------
    // Parcelable
    //-------------------------------------------------------
    public static final Creator<ChuangmiPlugM1> CREATOR = new Creator<ChuangmiPlugM1>() {

        @Override
        public ChuangmiPlugM1 createFromParcel(Parcel in) {
            return new ChuangmiPlugM1(in);
        }

        @Override
        public ChuangmiPlugM1[] newArray(int size) {
            return new ChuangmiPlugM1[size];
        }
    };

    private ChuangmiPlugM1() {
    }

    private ChuangmiPlugM1(Parcel in) {
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
