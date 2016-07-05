package com.xiaomi.xhome;

import android.os.Parcel;

import miot.api.device.AbstractDevice;

/**
 * Created by ruijun on 4/1/16.
 */
public class VirtualDevice extends AbstractDevice {
    public static final Creator<AbstractDevice> CREATOR = new Creator() {
        public VirtualDevice createFromParcel(Parcel var1) {
            return new VirtualDevice(var1.readString(), var1.readString(), var1.readString());
        }

        public VirtualDevice[] newArray(int var1) {
            return new VirtualDevice[var1];
        }
    };
    private final String mName;
    private final String mModel;
    private final String mId;

    public VirtualDevice(String name, String model, String id) {
        mName = name;
        mModel = model;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public String getDeviceId() {
        return mId;
    }

    public String getDeviceModel() {
        return mModel;
    }

}
