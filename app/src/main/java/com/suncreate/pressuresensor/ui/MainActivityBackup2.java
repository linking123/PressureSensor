package com.suncreate.pressuresensor.ui;

import android.bluetooth.BluetoothAdapter;
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
import android.widget.RelativeLayout;
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
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.AppManager;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.base.BaseActivityBlueToothLE;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.Notice;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.scan.User;
import com.suncreate.pressuresensor.interf.BaseViewInterface;
import com.suncreate.pressuresensor.interf.BluetoothUUID;
import com.suncreate.pressuresensor.service.NoticeUtils;
import com.suncreate.pressuresensor.util.LocationUtils;
import com.suncreate.pressuresensor.util.UIHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;

public class MainActivityBackup2 extends BaseActivityBlueToothLE implements BaseViewInterface, View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    /**
     * Sets whether vector drawables on older platforms (< API 21) can be used within DrawableContainer resources.
     * https://developer.android.com/reference/android/support/v7/app/AppCompatDelegate.html#setCompatVectorFromResourcesEnabled(boolean)
     */
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.index_scan_start)
    FButton btStartScan;
    @Bind(R.id.v_pressure)
    View mPressure;
    @Bind(R.id.tv_value)
    TextView mNumText;


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
        AppManager.getAppManager().addActivity(this);
        handleIntent(getIntent());

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        //修改用户信息广播监听
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_USER_CHANGE);
        registerReceiver(mReceiver, filter);

        mStringBuilder = new StringBuilder();

        mBluetoothLe = BluetoothLe.getDefault();
        //监听蓝牙状态广播
        registerReceiver(blueStateBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

        mBluetoothLe = BluetoothLe.getDefault();

        checkSupport();

        initData();
    }

    @Override
    public void initView() {

        //初始化用户名
        initUserName();

        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_NOTICE);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        NoticeUtils.bindToService(this);

        btStartScan.setOnClickListener(this);
    }

    @Override
    public void initData() {
        registerListener();
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NoticeUtils.unbindFromService(this);
        NoticeUtils.tryToShutDown(this);
        AppManager.getAppManager().removeActivity(this);
        //注销广播接收
        unregisterReceiver(mReceiver);
        unregisterReceiver(blueStateBroadcastReceiver);

        //根据TAG注销监听，避免内存泄露
        mBluetoothLe.destroy(TAG);
        //关闭GATT
        //mBluetoothLe.close();
    }

    public void checkSupport() {
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
        mBluetoothLe.setScanPeriod(200000)
                //   .setScanWithServiceUUID(BluetoothUUID.SERVICE)
                .setReportDelay(0)
                .startScan(this);
    }

    private void registerListener() {

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
                }
                Log.i(TAG, "扫描到设备：" + mBluetoothDevice.getName());

                if ("BT05".equals(mBluetoothDevice.getName())) {  //用名称了连接
                    //连接设备之前最好先停止扫描（小米手机可能会出现不能发现服务的情况)
//                    mBluetoothLe.stopScan();
                    mBluetoothLe.startConnect(true, mBluetoothDevice);
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

                //发送开始游戏命令
                String writeStr = "0xA5F1010097";
                mBluetoothLe.writeDataToCharacteristic(writeStr.getBytes(),
                        BluetoothUUID.psServiceUUID, BluetoothUUID.psWriteUUID);

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

                if (backInfo.length() < 8) {
                    return;
                }

                // 十六进制转化为十进制
                //根据测试说明：机器周期性发送压力值数据指令（A5 F1 03 00 00 00），
                // 目前只需要用到第4位数据即蓝色那位。 其值的大小，会根据按压气囊的值而相应改变。
                // 所以需要得到第四部分，转化为10进制，然后在改变高度
                String the4Num = bytesToHex(characteristic.getValue()).substring(6, 8);

                int pressureNum = Integer.parseInt(the4Num, 16);  //转化为10进制的压力值,最大值120左右
                //改变图形的高度
                changeHeight(pressureNum);
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
//                        AppContext.showToastShort("信号强度：" + rssi + "   距离：" + cm + "cm");
                        //超过10m，提醒距离过远，信号弱
                        if (cm > 1000) {
                            AppContext.showToastShort("距离太远，信号弱");
                        }
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


    @Override
    public void setTitle(CharSequence title) {
        super.setTitle("");
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                //修改用户信息广播
                case Constants.INTENT_ACTION_USER_CHANGE:
                    initUserName();
                    break;
            }
        }
    };

    private final BroadcastReceiver blueStateBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            switch (blueState) {
                case BluetoothAdapter.STATE_OFF:
                    Log.i(TAG, "blueState: STATE_OFF");
                    openBlutooth();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.i(TAG, "blueState: STATE_TURNING_ON");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.i(TAG, "blueState: STATE_ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.i(TAG, "blueState: STATE_TURNING_OFF");
                    break;
                default:
                    break;
            }
        }
    };

    private void openBlutooth() {
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
    }

    //初始化用户名
    public void initUserName() {
        if (navigationView != null) {
            navigationView.setItemIconTintList(null);
            //获取NavigationView上的控件id
            View headerView = navigationView.getHeaderView(0);
            TextView mUserName = headerView.findViewById(R.id.tv_user_name);
            User user = AppContext.getInstance().getLoginUser();
            mUserName.setText(user != null ? user.getUserName() : "未登录");
        }
    }

    private void changeHeight(int pressureNum) {
//        Random rand = new Random();
//        int height = rand.nextInt(400);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPressure.getLayoutParams();
        params.height = 3 * pressureNum;
        mPressure.setLayoutParams(params);
        mNumText.setText(String.valueOf(params));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.index_scan_start:
                mBluetoothLe.setScanPeriod(20000)
//                        .setScanWithServiceUUID(BluetoothUUID.psServiceUUID)//设置根据服务uuid过滤扫描
                        .setReportDelay(0)//如果为0，则回调onScanResult()方法，如果大于0, 则每隔你设置的时长回调onBatchScanResults()方法，不能小于0
                        .startScan(this);
                scan();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    /**
     * 处理传进来的intent
     */
    public void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_VIEW)) {
            UIHelper.showUrlRedirect(this, intent.getDataString());
        } else if (intent.getBooleanExtra("NOTICE", false)) {
            notifitcationBarClick(intent);
        }
    }

    /**
     * 从通知栏点击的时候相应
     */
    private void notifitcationBarClick(Intent fromWhich) {
        if (fromWhich != null) {
            boolean fromNoticeBar = fromWhich.getBooleanExtra("NOTICE", false);
            if (fromNoticeBar) {
                Intent toMyInfor = new Intent(this, SimpleBackActivity.class);
                startActivity(toMyInfor);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_scan_record) {
//            UIHelper.showSimpleBack(getApplicationContext(), SimpleBackPage.SCAN_RECORD);
            return true;
        }
       /* if (id == R.id.action_blutooth) {
//            UIHelper.showSimpleBack(getApplicationContext(), SimpleBackPage.SCAN_RECORD);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.user_info) {
            UIHelper.showSimpleBack(getApplicationContext(), SimpleBackPage.MY_PERSON_INFO);
        } else if (id == R.id.clear_cache) {
            UIHelper.clearAppCache(this);
        } else if (id == R.id.about_sys) {
            UIHelper.showAboutFireIot(getApplicationContext());
        } else if (id == R.id.btn_logout) {
            AppContext.getInstance().Logout();
            //注销账户，退出到登录页
            UIHelper.showLoginActivity(this);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}