package com.mi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mi.test.R;
import com.miot.common.abstractdevice.AbstractDevice;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by stephen on 16-1-18.
 */
public class DeviceAdapter extends LBaseAdapter<AbstractDevice> {
    private static final String TAG = DeviceAdapter.class.getSimpleName();

    public DeviceAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AbstractDevice device = mItems.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_device, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String name = device.getName();
        String id = device.getDeviceId();
        viewHolder.tvDevice.setText(name + "  " + id);
        switch (device.getConnectionType()) {
            case MIOT_WAN:
                viewHolder.tvDeviceStatus.setText(R.string.wan_device);
                break;
            case MIOT_WIFI:
                viewHolder.tvDeviceStatus.setText(R.string.wifi_device);
        }
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.iv_device)
        ImageView ivDevice;
        @InjectView(R.id.tv_device)
        TextView tvDevice;
        @InjectView(R.id.tv_device_status)
        TextView tvDeviceStatus;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
