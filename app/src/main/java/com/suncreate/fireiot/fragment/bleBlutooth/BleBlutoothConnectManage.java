package com.suncreate.fireiot.fragment.bleBlutooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qindachang.bluetoothle.BluetoothLe;
import com.qindachang.bluetoothle.OnLeConnectListener;
import com.qindachang.bluetoothle.OnLeIndicationListener;
import com.qindachang.bluetoothle.OnLeNotificationListener;
import com.qindachang.bluetoothle.OnLeReadCharacteristicListener;
import com.qindachang.bluetoothle.OnLeReadRssiListener;
import com.qindachang.bluetoothle.OnLeScanListener;
import com.qindachang.bluetoothle.OnLeWriteCharacteristicListener;
import com.qindachang.bluetoothle.exception.BleException;
import com.qindachang.bluetoothle.exception.ConnBleException;
import com.qindachang.bluetoothle.exception.ReadBleException;
import com.qindachang.bluetoothle.exception.ScanBleException;
import com.qindachang.bluetoothle.exception.WriteBleException;
import com.qindachang.bluetoothle.scanner.ScanRecord;
import com.qindachang.bluetoothle.scanner.ScanResult;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.AppManager;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.base.BaseActivityBlueToothLE;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.Notice;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.scan.User;
import com.suncreate.fireiot.interf.BaseViewInterface;
import com.suncreate.fireiot.interf.BluetoothUUID;
import com.suncreate.fireiot.service.NoticeUtils;
import com.suncreate.fireiot.ui.ApiLevelHelper;
import com.suncreate.fireiot.ui.SimpleBackActivity;
import com.suncreate.fireiot.util.LocationUtils;
import com.suncreate.fireiot.util.UIHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;

/**
 * Created by linking on 12/14/17.
 */

public class BleBlutoothConnectManage extends BaseActivityBlueToothLE implements BaseViewInterface,
        View.OnClickListener {

    public static final String TAG = "BleBlutoothConnect";

    @Bind(R.id.index_scan_start)
    FButton btStartScan;

    public static Notice mNotice;
    //蓝牙对象
    private BluetoothLe mBluetoothLe;
    private BluetoothDevice mBluetoothDevice;
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<BluetoothDevice>();

    private StringBuilder mStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fire);

        ButterKnife.bind(this);
        initView();

        mStringBuilder = new StringBuilder();

        mBluetoothLe = BluetoothLe.getDefault();
        //初始检测设备是否支持蓝牙
        if (!mBluetoothLe.isSupportBluetooth()) {
            //设备不支持蓝牙
            AppContext.showToastShort("很遗憾，您的手机不支持蓝牙，请更换手机后重试");
        } else {
            if (!mBluetoothLe.isBluetoothOpen()) {
                //没有打开蓝牙，请求打开手机蓝牙
                mBluetoothLe.enableBluetooth(this, 666);
            }
        }

        //监听蓝牙回调
        //监听扫描
        mBluetoothLe.setOnScanListener(TAG, new OnLeScanListener() {
            @Override
            public void onScanResult(BluetoothDevice bluetoothDevice, int rssi, ScanRecord scanRecord) {
                mBluetoothDevice = bluetoothDevice;
                //不存在，则添加进蓝牙设备列表
                if (bluetoothDeviceList.size() == 0) {
                    bluetoothDeviceList.add(bluetoothDevice);
                } else if (isNotInBluetoothDeviceList(bluetoothDevice)) {
                    bluetoothDeviceList.add(bluetoothDevice);
//                    showText("扫描到：" + bluetoothDeviceList.size() + " 个设备");
                }
                Log.i(TAG, "扫描到：" + mBluetoothDevice.getName());
//                showText("扫描到设备：" + mBluetoothDevice.getName());

                if ("BT05".equals(mBluetoothDevice.getName())) {
                    mBluetoothLe.startConnect(true, mBluetoothDevice);
                }

            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                Log.i(TAG, "扫描到设备：" + results.toString());
//                showText("扫描到设备：" + results.toString());
            }

            @Override
            public void onScanCompleted() {
                Log.i(TAG, "停止扫描");
//                showText("停止扫描");
            }

            @Override
            public void onScanFailed(ScanBleException e) {
                Log.e(TAG, "扫描错误：" + e.toString());
                showText("扫描错误：" + e.toString());
            }
        });

        //监听连接
        mBluetoothLe.setOnConnectListener(TAG, new OnLeConnectListener() {
            @Override
            public void onDeviceConnecting() {
                Log.i(TAG, "正在连接--->：" + mBluetoothDevice.getAddress());
//                showText("正在连接--->：" + mBluetoothDevice.getAddress());
            }

            @Override
            public void onDeviceConnected() {
                Log.i(TAG, "成功连接！");
//                showText("成功连接！");
            }

            @Override
            public void onDeviceDisconnected() {
                Log.i(TAG, "连接断开！");
//                showText("连接断开！");
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt) {
                Log.i(TAG, "发现服务");
                showText("发现服务");

                //写之前打开通知，以监听通知
                mBluetoothLe.enableNotification(true, BluetoothUUID.psServiceUUID,
                        new UUID[]{BluetoothUUID.PS_HR_NOTIFICATION, BluetoothUUID.PS_STEP_NOTIFICATION});

                //发送数据等必须在发现服务后做
                String writeStr = "0xA5F1010097";
                mBluetoothLe.writeDataToCharacteristic(writeStr.getBytes(),
                        BluetoothUUID.psServiceUUID, BluetoothUUID.psWriteUUID);

                //读数据
                mBluetoothLe.readCharacteristic(BluetoothUUID.psServiceUUID, BluetoothUUID.psReadUUID);
            }

            @Override
            public void onDeviceConnectFail(ConnBleException e) {
                Log.e(TAG, "连接异常：" + e.toString());
                showText("连接异常：" + e.toString());
            }
        });

        //监听通知，类型notification
        mBluetoothLe.setOnNotificationListener(TAG, new OnLeNotificationListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "收到notification : " + Arrays.toString(characteristic.getValue()));
//                showText("收到notification : " + Arrays.toString(characteristic.getValue()));
                showText("收到notification : " + bytesToHex(characteristic.getValue()));
            }

            @Override
            public void onFailed(BleException e) {
                Log.e(TAG, "notification通知错误：" + e.toString());
                showText("notification通知错误：" + e.toString());
            }
        });

        //监听通知，类型indicate
        mBluetoothLe.setOnIndicationListener(TAG, new OnLeIndicationListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "收到indication: " + Arrays.toString(characteristic.getValue()));
                showText("收到indication: " + Arrays.toString(characteristic.getValue()));
            }

            @Override
            public void onFailed(BleException e) {
                Log.e(TAG, "indication通知错误：" + e.toString());
                showText("indication通知错误：" + e.toString());
            }
        });

        //监听写
        mBluetoothLe.setOnWriteCharacteristicListener(TAG, new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "写数据到特征：" + Arrays.toString(characteristic.getValue()));
                showText("写数据到特征：" + Arrays.toString(characteristic.getValue()));
            }

            @Override
            public void onFailed(WriteBleException e) {
                Log.e(TAG, "写错误：" + e.toString());
                showText("写错误：" + e.toString());
            }
        });

        //监听读
        mBluetoothLe.setOnReadCharacteristicListener(TAG, new OnLeReadCharacteristicListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "读取特征数据：" + Arrays.toString(characteristic.getValue()));
                showText("读取特征数据：" + Arrays.toString(characteristic.getValue()));
                String returnstr = bytesToHex(characteristic.getValue());
            }

            @Override
            public void onFailure(ReadBleException e) {
                Log.e(TAG, "读错误：" + e.toString());
                showText("读错误：" + e.toString());
            }
        });

        //监听信号强度
        mBluetoothLe.setReadRssiInterval(10000)
                .setOnReadRssiListener(TAG, new OnLeReadRssiListener() {
                    @Override
                    public void onSuccess(int rssi, int cm) {
                        Log.i(TAG, "信号强度：" + rssi + "   距离：" + cm + "cm");
//                        showText("信号强度：" + rssi + "   距离：" + cm + "cm");
                    }
                });

    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    //判断扫描到的蓝牙是否已经存在列表中
    private boolean isNotInBluetoothDeviceList(BluetoothDevice mBluetoothDevice) {
        for (BluetoothDevice btd : bluetoothDeviceList) {
            if (mBluetoothDevice.equals(btd)) {
                return false;
            }
        }
        return true;
    }

    //需要开始扫描按钮case R.id.btn_scan:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 666 && resultCode == RESULT_OK) {
            scan();
        }
    }

    private void scan() {
        //对于Android 6.0以上的版本，申请地理位置动态权限
        if (!checkLocationPermission()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("权限需求")
                    .setMessage("Android 6.0 以上的系统版本，扫描蓝牙需要地理位置权限。请允许。")
                    .setNeutralButton("取消", null)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestLocationPermission();
                        }
                    })
                    .show();
            return;
        }
        //如果系统版本是7.0以上，则请求打开位置信息
        if (!LocationUtils.isOpenLocService(this) && ApiLevelHelper.isAtLeast(Build.VERSION_CODES.N)) {
            Toast.makeText(this, "您的Android版本在7.0以上，扫描需要打开位置信息。", Toast.LENGTH_LONG).show();
            LocationUtils.gotoLocServiceSettings(this);
            return;
        }
        mBluetoothLe.setScanPeriod(20000)
                //   .setScanWithServiceUUID(BluetoothUUID.SERVICE)
                .setReportDelay(0)
                .startScan(this);
    }

    private void showText(String text) {
        mStringBuilder.append(text).append("\n");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void initView() {

        btStartScan.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.index_scan_start:
                break;
            default:
                break;
        }
    }


}
