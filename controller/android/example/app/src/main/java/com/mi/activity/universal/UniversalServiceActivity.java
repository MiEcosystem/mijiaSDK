package com.mi.activity.universal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mi.adapter.DeviceDescAdapter;
import com.mi.adapter.PropertyAdapter;
import com.mi.test.R;
import com.mi.utils.TestConstants;
import com.mi.utils.ToolbarActivity;
import com.miot.api.DeviceManipulator;
import com.miot.api.MiotManager;
import com.miot.common.device.Action;
import com.miot.common.device.Service;
import com.miot.common.device.invocation.ActionInfo;
import com.miot.common.device.invocation.ActionInfoFactory;
import com.miot.common.device.invocation.PropertyInfo;
import com.miot.common.device.invocation.PropertyInfoFactory;
import com.miot.common.exception.MiotException;
import com.miot.common.property.AllowedValue;
import com.miot.common.property.AllowedValueList;
import com.miot.common.property.Property;
import com.miot.common.property.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class UniversalServiceActivity extends ToolbarActivity {
    private static String TAG = UniversalServiceActivity.class.getSimpleName();
    @InjectView(R.id.lv_desc)
    ListView lvDesc;
    @InjectView(R.id.tv_log)
    TextView tvLog;
    @InjectView(R.id.tv_properties_title)
    TextView tvPropertiesTitle;
    @InjectView(R.id.btn_subscribe)
    Button btnSubscribe;
    @InjectView(R.id.btn_unsubscribe)
    Button btnUnsubscribe;
    @InjectView(R.id.btn_get_properties)
    Button btnGetProperties;
    @InjectView(R.id.lv_properties)
    ListView lvProperties;
    @InjectView(R.id.btn_manipulate)
    Button btnManipulate;

    private Service mService;
    private PropertyAdapter mPropertyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_service);
        ButterKnife.inject(this);

        mService = getIntent().getParcelableExtra(TestConstants.EXTRA_SERVICE);
        if (mService == null) {
            Log.e(TAG, "service is null");
            finish();
            return;
        }

        setTitle(mService.getType().getName());
        initLog();
        initPropertyList();

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeNotification();
            }
        });
        btnUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSubscribeNotification();
            }
        });
        btnGetProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyInfo info = PropertyInfoFactory.create(mService);
                for (Property property : mService.getProperties()) {
                    if (property.getDefinition().isGettable()) {
                        Log.d(TAG, "property: " + property.getFriendlyName());
                        info.addProperty(property);
                    }
                }
                getProperties(info);
            }
        });

        String title = String.format("调用方法(%d)", mService.getActions().size());
        btnManipulate.setText(title);
        btnManipulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manipulate();
            }
        });
    }

    @Override
    protected Pair<Integer, Boolean> getCustomTitle() {
        return new Pair<>(R.string.title_toolbar_service, true);
    }

    private void initLog() {
        DeviceDescAdapter adapter = new DeviceDescAdapter(this);
        List<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<>("服务名称", mService.getType().getName()));
        list.add(new Pair<>("属性数", mService.getProperties().size() + ""));
        list.add(new Pair<>("方法数", mService.getActions().size() + ""));
        adapter.setItems(list);
        lvDesc.setAdapter(adapter);

//        String log = String.format("服务名称: %s\r\n属性个数: %s\r\n方法个数: %s\r\n",
//                mService.getType().getName(),
//                mService.getProperties().size(),
//                mService.getActions().size());
//        tvLog.setText(log);
        tvLog.setMovementMethod(new ScrollingMovementMethod());
    }

    public void subscribeNotification() {
        PropertyInfo propertyInfo = PropertyInfoFactory.create(mService);
        for (Property property : mService.getProperties()) {
            if (property.getDefinition().isNotifiable()) {
                propertyInfo.addProperty(property);
            }
        }

        if (propertyInfo.getProperties().size() == 0) {
            showLog("没有属性可以订阅");
            return;
        }

        showLog(String.format("开始订阅属性: %d", propertyInfo.getProperties().size()));
        DeviceManipulator manipulator = MiotManager.getDeviceManipulator();
        try {
            manipulator.addPropertyChangedListener(propertyInfo,
                    new DeviceManipulator.CompletionHandler() {
                        @Override
                        public void onSucceed() {
                            showLog("订阅属性成功！");
                        }

                        @Override
                        public void onFailed(int errCode, String description) {
                            showLog(String.format("订阅属性失败： Code: %d %s！", errCode, description));
                        }
                    },
                    new DeviceManipulator.PropertyChangedListener() {
                        @Override
                        public void onPropertyChanged(PropertyInfo info, String propertyName) {
                            String msg = String.format("属性发生变化： %s=%s", propertyName, info.getValue(propertyName).toString());
                            showLog(msg);
                            Log.i(TAG, msg);
                        }
                    }
            );
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void resubscribeNotification() {
        PropertyInfo propertyInfo = PropertyInfoFactory.create(mService);
        for (Property property : mService.getProperties()) {
            if (property.getDefinition().isNotifiable()) {
                propertyInfo.addProperty(property);
            }
        }

        if (propertyInfo.getProperties().size() == 0) {
            showLog("没有属性可以订阅");
            return;
        }

        showLog(String.format("再次订阅属性: %d", propertyInfo.getProperties().size()));
        DeviceManipulator manipulator = MiotManager.getDeviceManipulator();
        try {
            manipulator.addPropertyChangedListener(propertyInfo,
                    new DeviceManipulator.CompletionHandler() {
                        @Override
                        public void onSucceed() {
                            showLog("订阅属性成功！222");
                        }

                        @Override
                        public void onFailed(int errCode, String description) {
                            showLog(String.format("订阅属性失败222： Code: %d %s！", errCode, description));
                        }
                    },
                    new DeviceManipulator.PropertyChangedListener() {
                        @Override
                        public void onPropertyChanged(PropertyInfo info, String propertyName) {
                            showLog(String.format("[%s=%s]", propertyName, info.getValue(propertyName).toString()));
                        }
                    }
            );
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void unSubscribeNotification() {
        PropertyInfo info = PropertyInfoFactory.create(mService);
        for (Property property : mService.getProperties()) {
            if (property.getDefinition().isNotifiable()) {
                info.addProperty(property);
            }
        }

        if (info.getProperties().size() == 0) {
            showLog("没有属性可以订阅");
            return;
        }

        DeviceManipulator manipulator = MiotManager.getDeviceManipulator();
        try {
            manipulator.removePropertyChangedListener(info, new DeviceManipulator.CompletionHandler() {
                        @Override
                        public void onSucceed() {
                            showLog("取消订阅属性成功！");
                        }

                        @Override
                        public void onFailed(int errCode, String description) {
                            showLog(String.format("订阅属性失败： Code: %d %s！", errCode, description));
                        }
                    }
            );
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private void getProperties(PropertyInfo propertyInfo) {
        DeviceManipulator manipulator = MiotManager.getDeviceManipulator();
        try {
            manipulator.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
                @Override
                public void onSucceed(PropertyInfo info) {
                    String ret = "读取属性成功\r\n";
                    for (Property property : info.getProperties()) {
                        if (property.isValueValid()) {
                            ret += property.getDefinition().getFriendlyName() + "=" + property.getValue() + "\r\n";
                        }
                    }
                    showLog(ret);
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog(String.format("读取属性失败, errCode: %d %s！", errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private void initPropertyList() {
        String title = String.format("属性列表(%d)", mService.getProperties().size());
        tvPropertiesTitle.setText(title);
        mPropertyAdapter = new PropertyAdapter(this);
        mPropertyAdapter.addItems(mService.getProperties());

        lvProperties.setAdapter(mPropertyAdapter);
        lvProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Property property = (Property) mPropertyAdapter.getItem(position);
                if (property != null && property.getDefinition().isGettable()) {
                    getProperty(property);
                }
            }
        });
    }

    private void getProperty(final Property property) {
        PropertyInfo propertyInfo = PropertyInfoFactory.create(mService, property.getDefinition().getFriendlyName());
        DeviceManipulator manipulator = MiotManager.getDeviceManipulator();
        try {
            manipulator.readProperty(propertyInfo, new DeviceManipulator.ReadPropertyCompletionHandler() {
                @Override
                public void onSucceed(PropertyInfo info) {
                    property.setValue(info.getValue(property.getDefinition().getFriendlyName()));
                    showLog(String.format("读取%s成功: %s！", property.getDefinition().getFriendlyName(), property.getValue().toString()));
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog(String.format("读取%s失败, errCode: %d %s！", property.getDefinition().getFriendlyName(), errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    public void manipulate() {
        int size = mService.getActions().size();
        String menu[] = new String[size];
        final String actions[] = new String[size];

        int i = 0;
        for (Action action : mService.getActions()) {
            String name = action.getDescription();
            if (TextUtils.isEmpty(name)) {
                name = action.getFriendlyName();
            }
            menu[i] = name;
            actions[i] = action.getFriendlyName();
            i++;
        }

        String title = String.format("调用方法(%d)", size);

        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle(title)
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: " + which);
                        Action action = mService.getAction(actions[which]);
                        showInvokeDialog(mService, action);
                    }
                })
                .setNeutralButton("取消", null)
                .create();
        alert.show();
    }

    private void showInvokeDialog(final Service service, final Action action) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.dialog_show_invoke, null);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        tvName.setText(action.getName());
        TextView tvDesp = (TextView) view.findViewById(R.id.tv_desp);
        tvDesp.setText(action.getDescription());
        int argId = 0;
        final ActionInfo info = ActionInfoFactory.create(service, action.getFriendlyName());
        for (Property property : info.getArguments()) {
            PropertyDefinition def = property.getDefinition();

            LinearLayout argView = new LinearLayout(this);
            argView.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvPropertyName = new TextView(this);
            tvPropertyName.setText(String.format("%s (%s): ", def.getName(), def.getDataType().toString()));
            tvPropertyName.setTextSize(16);
            argView.addView(tvPropertyName);
            AllowedValue allowedValue = def.getAllowedValue();
            if (allowedValue instanceof AllowedValueList) {
                Spinner spinner = new Spinner(this);
                List<Object> list = ((AllowedValueList) allowedValue).getAllowedValues();
                AllowedValueAdapter adapter = new AllowedValueAdapter(this, list);
                spinner.setAdapter(adapter);
                spinner.setId(argId++);
                argView.addView(spinner);
            } else {
                EditText v = new EditText(this);
                v.setTextSize(15);
                v.setMinWidth(100);
                v.setId(argId++);
                argView.addView(v);
            }
            view.addView(argView);
        }

        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("设备操作")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int argId = 0;
                        for (Property property : info.getArguments()) {
                            PropertyDefinition def = property.getDefinition();
                            Object value = null;
                            if (!(def.getAllowedValue() instanceof AllowedValueList)) {
                                EditText editText = (EditText) (view.findViewById(argId++));
                                String text = editText.getText().toString();
                                value = def.getDataType().toObjectValue(text);
                            } else {
                                Spinner spinner = (Spinner) (view.findViewById(argId++));
                                value = spinner.getSelectedItem();
                            }
                            if (!info.setArgumentValue(def.getFriendlyName(), value)) {
                                showPropertyValueInvalid(def, value);
                                return;
                            }
                        }
                        invokeAction(info);
                    }
                })
                .setNeutralButton("取消", null)
                .create();
        alert.show();
    }

    private void showPropertyValueInvalid(PropertyDefinition def, Object value) {
        String msg = String.format("名称：%s, 类型：%s, 值：%s",
                def.getFriendlyName(),
                def.getDataType().toString(),
                value.toString());

        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("属性值不合法")
                .setMessage(msg)
                .setNeutralButton("我知道了", null)
                .create();
        alert.show();
    }

    private void invokeAction(final ActionInfo info) {
        DeviceManipulator manipulator = MiotManager.getDeviceManipulator();
        try {
            manipulator.invoke(info, new DeviceManipulator.InvokeCompletionHandler() {
                @Override
                public void onSucceed(ActionInfo info) {
                    showLog(String.format("执行: %s 成功！", info.getFriendlyName()));
                }

                @Override
                public void onFailed(int errCode, String description) {
                    showLog(String.format("执行: %s 失败, errCode: %d %s！", info.getInternalName(), errCode, description));
                }
            });
        } catch (MiotException e) {
            e.printStackTrace();
        }
    }

    private void showLog(String info) {
        final String newLog;

        CharSequence oldLog = tvLog.getText();
        if (oldLog.length() > 1024 * 10) {
            newLog = String.format("%s\r\n", info);
        } else {
            newLog = String.format("%s%s\r\n", oldLog, info);
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tvLog.setText(newLog);
            }
        });
    }

    public static class AllowedValueAdapter extends BaseAdapter {
        private List<Object> list;
        private Context context;

        public AllowedValueAdapter(Context context, List<Object> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_allowdvalue, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv.setText(getItem(position).toString());
            return convertView;
        }

        static class ViewHolder {
            @InjectView(R.id.text)
            TextView tv;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }

    }
}