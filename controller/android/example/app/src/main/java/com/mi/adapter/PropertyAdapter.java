package com.mi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mi.test.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.miot.common.property.Property;

public class PropertyAdapter extends LBaseAdapter<Property> {
    public PropertyAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Property property = mItems.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_property, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String title = property.getDefinition().getFriendlyName();
        if (property.getDefinition().isGettable()) {
            title = String.format("%s (可读)", title);
        }
        if (property.getDefinition().isNotifiable()) {
            title = String.format("%s (可订阅)", title);
        }
        viewHolder.tvName.setText(title);

        viewHolder.tvDesp.setText(property.getDefinition().getDescription());

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.tv_desp)
        TextView tvDesp;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
