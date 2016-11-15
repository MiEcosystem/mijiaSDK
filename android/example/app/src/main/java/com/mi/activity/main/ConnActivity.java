package com.mi.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.inuker.bluetooth.library.BluetoothService;
import com.mi.test.R;
import com.mi.utils.BaseActivity;
import com.miot.api.bluetooth.BindStyle;
import com.miot.api.bluetooth.BluetoothDeviceConfig;
import com.miot.api.bluetooth.XmBluetoothManager;
import com.miot.api.bluetooth.response.BleConnectResponse;
import com.miot.bluetooth.Code;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ConnActivity extends BaseActivity {
    private static String TAG = ConnActivity.class.getSimpleName();


    @InjectView(R.id.btn_sconn)
    Button mBtnSConn;
    @InjectView(R.id.btn_sconn_strong)
    Button mBtnSConnStrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_security_conn);
        ButterKnife.inject(this);
        mBtnSConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sConn();
            }
        });

        mBtnSConnStrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strongSConn();
            }
        });
    }

    private void strongSConn(){
        String macToothBrush = "D8:AC:B5:11:5E:D7";
        String macToothBrush2 = "F3:E7:91:03:F4:8E";
        BluetoothDeviceConfig config = new BluetoothDeviceConfig();
        config.bindStyle = BindStyle.STRONG;
        config.model = "soocare.toothbrush.x3";
        config.productId = 206;
        /*config.model = "yunmi.kettle.v2";
        config.productId = 275;*/
        XmBluetoothManager.getInstance().setDeviceConfig(config);
        XmBluetoothManager.getInstance().secureConnect(macToothBrush2, new BleConnectResponse() {
           @Override
           public void onResponse(int code, Bundle data) {
               Log.d(TAG,"code"+code+"codes"+ Code.toString(code));
//               Toast.makeText(ConnActivity.this,"code"+code+"codes"+ Code.toString(code),Toast.LENGTH_SHORT).show();
           }
       });
/*        XmBluetoothManager.getInstance().connect(macDianShuiHu, new BleConnectResponse() {
            @Override
            public void onResponse(int code, Bundle data) {
                Log.d(TAG,"code2222&&&"+code+"codes222222&&&&"+ Code.toString(code));
//               Toast.makeText(ConnActivity.this,"code"+code+"codes"+ Code.toString(code),Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void sConn(){
        String mac = "D8:AC:B5:11:5E:D7";
        String macDianShuiHu = "08:7C:BE:86:00:7D";
        BluetoothDeviceConfig config = new BluetoothDeviceConfig();
        config.bindStyle = BindStyle.WEAK;
        config.model = "yunmi.kettle.v1";
        config.productId = 131;
        /*config.model = "yunmi.kettle.v2";
        config.productId = 275;*/
        XmBluetoothManager.getInstance().setDeviceConfig(config);
        XmBluetoothManager.getInstance().secureConnect(macDianShuiHu, new BleConnectResponse() {
            @Override
            public void onResponse(int code, Bundle data) {
                Log.d(TAG,"code"+code+"codes"+ Code.toString(code));
//               Toast.makeText(ConnActivity.this,"code"+code+"codes"+ Code.toString(code),Toast.LENGTH_SHORT).show();
            }
        });
/*        XmBluetoothManager.getInstance().connect(macDianShuiHu, new BleConnectResponse() {
            @Override
            public void onResponse(int code, Bundle data) {
                Log.d(TAG,"code2222&&&"+code+"codes222222&&&&"+ Code.toString(code));
//               Toast.makeText(ConnActivity.this,"code"+code+"codes"+ Code.toString(code),Toast.LENGTH_SHORT).show();
            }
        });*/
    }


}