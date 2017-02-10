package com.mi.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mi.test.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by vww on 17-2-6.
 */

public class DeviceDescAdapter extends LBaseAdapter<Pair<String,String>> {
    public DeviceDescAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pair<String, String> item = mItems.get(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_deivce_desc, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.descName.setText(item.first);
        viewHolder.descValue.setText(item.second);

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.desc_name)
        TextView descName;
        @InjectView(R.id.desc_value)
        TextView descValue;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
