package com.mi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mi.test.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import miot.typedef.device.Service;

public class ServiceAdapter extends LBaseAdapter<Service> {

    public ServiceAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Service service = mItems.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_service, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(service.getType().getName());
        return convertView;
    }


    static class ViewHolder {
        @InjectView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
