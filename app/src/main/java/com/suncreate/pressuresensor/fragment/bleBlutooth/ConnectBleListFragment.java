package com.suncreate.pressuresensor.fragment.bleBlutooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
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
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.general.ConnectBleListAdapter;
import com.suncreate.pressuresensor.base.BaseDetailFragment;
import com.suncreate.pressuresensor.bean.Ble.Ble;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.interf.BluetoothUUID;
import com.suncreate.pressuresensor.ui.ApiLevelHelper;
import com.suncreate.pressuresensor.util.LocationUtils;
import com.suncreate.pressuresensor.util.UIHelper;
import com.suncreate.pressuresensor.widget.SuperRefreshLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;

/**
 * 蓝牙连接列表
 */
public class ConnectBleListFragment extends BaseDetailFragment<Ble> implements
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "ConnectBle";

    @Bind(R.id.superRefreshLayout)
    SuperRefreshLayout mRefreshLayout;
    @Bind(R.id.listView)
    ListView listView;

    ConnectBleListAdapter adapter;

    //蓝牙对象,..
    private BluetoothLe mBluetoothLe;
    private BluetoothDevice mBluetoothDevice;
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mBluetoothLe = BluetoothLe.getDefault();
        checkSupport();
//        initData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //根据TAG注销监听，避免内存泄露
        mBluetoothLe.destroy(TAG);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initData() {
        registerListener();
    }

    @Override
    protected void sendRequestDataForNet() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_technician_list;
    }

    @Override
    public void initView(View view) {

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
//            case R.id.car_brand_model_series_box:
//                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_CAR_MANAGER);
//                break;
        }
    }

    public boolean checkSupport() {
        //初始检测设备是否支持蓝牙
        if (!mBluetoothLe.isSupportBluetooth()) {
            //设备不支持蓝牙
            AppContext.showToastShort("很遗憾，您的手机不支持蓝牙，请更换手机后重试");
            return false;
        } else {
            if (!mBluetoothLe.isBluetoothOpen()) {
                //没有打开蓝牙，请求打开手机蓝牙
                mBluetoothLe.enableBluetooth(getActivity());
                return false;
            } else {
                return true;
            }
        }
    }

    //byte数组转化为16进制hex
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

    private void scan() {
        if (!checkSupport()) {
            return;
        }
        //对于Android 6.0以上的版本，申请地理位置动态权限
        if (!checkLocationPermission()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        if (!LocationUtils.isOpenLocService(getContext()) && ApiLevelHelper.isAtLeast(Build.VERSION_CODES.N)) {
            Toast.makeText(getContext(), "您的Android版本在7.0以上，扫描需要打开位置信息。", Toast.LENGTH_LONG).show();
            LocationUtils.gotoLocServiceSettings(getActivity());
            return;
        }
        mBluetoothLe.setScanPeriod(20000)
//                   .setScanWithServiceUUID(BluetoothUUID.psServiceUUID)
                .setReportDelay(0)
                .startScan(getActivity());
    }

    private void registerListener() {

        //监听蓝牙回调
        //监听扫描
        mBluetoothLe.setOnScanListener(TAG, new OnLeScanListener() {
            @Override
            public void onScanResult(BluetoothDevice bluetoothDevice, int rssi, ScanRecord scanRecord) {
                mBluetoothDevice = bluetoothDevice;
                //不存在，则添加进蓝牙设备列表
                boolean flag = false;
                if (bluetoothDeviceList.size() == 0) {
                    bluetoothDeviceList.add(mBluetoothDevice);
                    flag = true;
                } else if (isNotInBluetoothDeviceList(mBluetoothDevice)) {
                    bluetoothDeviceList.add(mBluetoothDevice);
                    flag = true;
                }
                if (flag) {
                    onRefresh(); //扫描到设备，不重复则加入设备列表
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                Log.i(TAG, "扫描到设备：" + results.toString());
            }

            @Override
            public void onScanCompleted() {
                Log.i(TAG, "停止扫描");
            }

            @Override
            public void onScanFailed(ScanBleException e) {
                Log.e(TAG, "扫描错误：" + e.toString());
            }
        });

        //监听连接
        mBluetoothLe.setOnConnectListener(TAG, new OnLeConnectListener() {
            @Override
            public void onDeviceConnecting() {
                Log.i(TAG, "正在连接--->：" + mBluetoothDevice.getAddress());
            }

            @Override
            public void onDeviceConnected() {
                Log.i(TAG, "成功连接！");
                AppContext.showToastShort("连接成功");
                getActivity().finish(); //连接成功，返回主页
            }

            @Override
            public void onDeviceDisconnected() {
                Log.i(TAG, "连接断开！");
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt) {
                Log.i(TAG, "发现服务");

                //写之前打开通知，以监听通知
                mBluetoothLe.enableNotification(true, BluetoothUUID.psServiceUUID,
                        new UUID[]{BluetoothUUID.PS_HR_NOTIFICATION, BluetoothUUID.PS_STEP_NOTIFICATION});
                //读数据
                mBluetoothLe.readCharacteristic(BluetoothUUID.psServiceUUID, BluetoothUUID.psReadUUID);
            }

            @Override
            public void onDeviceConnectFail(ConnBleException e) {
                Log.e(TAG, "连接异常：" + e.toString());
            }
        });

        //监听通知，类型notification
        mBluetoothLe.setOnNotificationListener(TAG, new OnLeNotificationListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
//                Log.i(TAG, "收到notification : " + Arrays.toString(characteristic.getValue()));
                //16进制数
                String backInfo = bytesToHex(characteristic.getValue());
                Log.i(TAG, "收到notification : " + backInfo);

            }

            @Override
            public void onFailed(BleException e) {
                Log.e(TAG, "notification通知错误：" + e.toString());
            }
        });

        //监听通知，类型indicate
        mBluetoothLe.setOnIndicationListener(TAG, new OnLeIndicationListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "收到indication: " + Arrays.toString(characteristic.getValue()));
            }

            @Override
            public void onFailed(BleException e) {
                Log.e(TAG, "indication通知错误：" + e.toString());
            }
        });

        //监听写
        mBluetoothLe.setOnWriteCharacteristicListener(TAG, new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "写数据到特征：" + Arrays.toString(characteristic.getValue()));
            }

            @Override
            public void onFailed(WriteBleException e) {
                Log.e(TAG, "写错误：" + e.toString());
            }
        });

        //监听读
        mBluetoothLe.setOnReadCharacteristicListener(TAG, new OnLeReadCharacteristicListener() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Log.i(TAG, "读取特征数据：" + Arrays.toString(characteristic.getValue()));
                String returnstr = bytesToHex(characteristic.getValue());
                Log.i(TAG, "读取特征数据：" + returnstr);
            }

            @Override
            public void onFailure(ReadBleException e) {
                Log.e(TAG, "读错误：" + e.toString());
            }
        });

        //监听信号强度
        mBluetoothLe.setReadRssiInterval(10000)
                .setOnReadRssiListener(TAG, new OnLeReadRssiListener() {
                    @Override
                    public void onSuccess(int rssi, int cm) {
                        Log.i(TAG, "信号强度：" + rssi + "   距离：" + cm + "cm");
                    }
                });
    }

    @Override
    protected String getCacheKey() {
        return TAG;
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Ble>>>() {
        }.getType();
    }

    @Override
    public void onRefresh() {
        setSwipeRefreshLoadingState();
        Log.i(TAG, "扫描到设备：" + mBluetoothDevice.getName());
        adapter = new ConnectBleListAdapter(bluetoothDeviceList, getContext(), mBluetoothLe);
        listView.setAdapter(adapter);

        executeOnloadFinish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_menu, menu);
        menu.findItem(R.id.search_ble).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search_ble:
                scan();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    private void executeOnloadFinish() {
        setSwipeRefreshLoadedState();
        mState = STATE_NONE;
    }

    protected void setSwipeRefreshLoadingState() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(true);
        } else {
            mRefreshLayout.setEnabled(false);
        }
    }

    protected void setSwipeRefreshLoadedState() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        } else {
            mRefreshLayout.setEnabled(true);
        }
    }
}