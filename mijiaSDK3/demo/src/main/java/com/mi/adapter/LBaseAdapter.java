package com.mi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class LBaseAdapter<T> extends BaseAdapter {
    private static final String TAG = LBaseAdapter.class.getSimpleName();

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mItems = new ArrayList<>();

    public LBaseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    public void addItems(List<T> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void setItems(List<T> items) {
        mItems.clear();
        mItems = items;
        notifyDataSetChanged();
    }

    public void clearItems() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);
}
