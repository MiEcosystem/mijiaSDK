package com.mi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mi.test.R;


/**
 * Created by zhangpengfei on 14-1-21.
 */
public class SingleCheckLinearLayout extends LinearLayout implements Checkable{

    CheckBox mCheckBox;
    Context mContext;
    TextView mTextView;

    public SingleCheckLinearLayout(Context context) {
        super(context);
    }

    public SingleCheckLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCheckBox = (CheckBox) findViewById(R.id.checkbox);
        mTextView = (TextView)findViewById(R.id.text1);
    }

    @Override
    public void setChecked(boolean checked) {
        mCheckBox.setChecked(checked);
        if(mTextView!= null)mTextView.setSelected(checked);
    }

    @Override
    public boolean isChecked() {
        return mCheckBox.isChecked();
    }

    @Override
    public void toggle() {

    }
}
