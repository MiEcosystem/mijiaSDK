package com.mi.activity.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.ByteUtils;
import com.mi.test.R;
import com.mi.utils.BaseActivity;
import com.miot.api.bluetooth.BindStyle;
import com.miot.api.bluetooth.BluetoothDeviceConfig;
import com.miot.api.bluetooth.BtFirmwareUpdateInfo;
import com.miot.api.bluetooth.XmBluetoothManager;
import com.miot.api.bluetooth.response.BleConnectResponse;
import com.miot.api.bluetooth.response.GetBeaconKeyResponse;
import com.miot.api.bluetooth.response.GetFirmwareUpdateInfoResponse;
import com.miot.bluetooth.BluetoothConstants;
import com.miot.bluetooth.Code;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ConnActivity extends BaseActivity {
    private static String TAG = ConnActivity.class.getSimpleName();


    @InjectView(R.id.btn_sconn)
    Button mBtnSConn;
    @InjectView(R.id.btn_sconn_strong)
    Button mBtnSConnStrong;
    @InjectView(R.id.btn_sconn_kaifaban)
    Button mBtnKaiFaBan;
    @InjectView(R.id.btn_firmware_update)
    Button mBtnFirmwareUpdate;
    @InjectView(R.id.btn_beaconkey)
    Button mBtnBeaconKey;


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

        mBtnKaiFaBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sKaiFaBanConn();
            }
        });

        mBtnFirmwareUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firmwareUpdate();
            }
        });
        mBtnBeaconKey.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                XmBluetoothManager.getInstance().getDeviceBeaconKey("blt.BrGTFdNqU9oGyF7s",new GetBeaconKeyResponse() {
                    @Override
                    public void onResponse(int code, String data) {
                        BluetoothLog.v(String.format("ConnActivity getDeviceBeaocnKey response: code = %d, beaconKey = %s",
                                code, data));
                    }
                });
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
               Log.d(TAG, ByteUtils.byteToString(data.getByteArray(BluetoothConstants.KEY_DID))+"TOKEN"+ByteUtils.byteToString(data.getByteArray(BluetoothConstants.KEY_TOKEN)));
//               Toast.makeText(ConnActivity.this,"code"+code+"codes"+ Code.toString(code),Toast.LENGTH_SHORT).show();
           }
       });

    }

    private void sConn(){
        String macDianShuiHu = "80:EA:CA:00:00:72";
        BluetoothDeviceConfig config = new BluetoothDeviceConfig();
        config.bindStyle = BindStyle.WEAK;
        config.model = "hhcc.plantmonitor.v1";
        config.productId = 152;
        /*config.model = "yunmi.kettle.v2";
        config.productId = 275;*/
        XmBluetoothManager.getInstance().setDeviceConfig(config);
        XmBluetoothManager.getInstance().secureConnect(macDianShuiHu, new BleConnectResponse() {
            @Override
            public void onResponse(int code, Bundle data) {
                Log.d(TAG,"code"+code+"codes"+ Code.toString(code));
            }
        });
    }

    /**
     * 测试双模开发版链接
     */
    private void sKaiFaBanConn(){
        String mac = "28:6C:07:73:51:BE";
        BluetoothDeviceConfig config = new BluetoothDeviceConfig();
        config.bindStyle = BindStyle.WEAK;
        config.model = "mijia.demo.v1";
        config.productId = 222;
        /*config.model = "yunmi.kettle.v2";
        config.productId = 275;*/
        XmBluetoothManager.getInstance().setDeviceConfig(config);
        XmBluetoothManager.getInstance().secureConnect(mac, new BleConnectResponse() {
            @Override
            public void onResponse(int code, Bundle data) {
                Log.d(TAG,"code"+code+"codes"+ Code.toString(code));
            }
        });
    }

    private void firmwareUpdate(){
        String macDianShuiHu = "08:7C:BE:86:00:7D";
        BluetoothDeviceConfig config = new BluetoothDeviceConfig();
        config.bindStyle = BindStyle.WEAK;
        config.model = "yunmi.kettle.v1";
        config.productId = 131;
                /*BluetoothDeviceConfig config = new BluetoothDeviceConfig();
                config.bindStyle = BindStyle.WEAK;
                config.model = "roidmi.carairpuri.v1";
                config.productId = 332*/;

        XmBluetoothManager.getInstance().setDeviceConfig(config);
        XmBluetoothManager.getInstance().getBluetoothFirmwareUpdateInfo("yunmi.kettle.v1",new GetFirmwareUpdateInfoResponse() {
            @Override
            public void onResponse(int code, BtFirmwareUpdateInfo data) {
                Log.d(TAG, " getBluetoothFirmwareUpdateInfo code: " + code );
                if (data!=null){
                    Log.d(TAG, "data: "+data.toString());
                }

            }
        });

    }
}