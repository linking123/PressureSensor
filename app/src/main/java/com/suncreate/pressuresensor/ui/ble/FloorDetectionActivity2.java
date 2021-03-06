package com.suncreate.pressuresensor.ui.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
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
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.interf.BaseViewInterface;
import com.suncreate.pressuresensor.interf.BluetoothUUID;
import com.suncreate.pressuresensor.interf.PsNumCallback;
import com.suncreate.pressuresensor.service.NoticeUtils;
import com.suncreate.pressuresensor.ui.ApiLevelHelper;
import com.suncreate.pressuresensor.ui.SimpleBackActivity;
import com.suncreate.pressuresensor.util.LocationUtils;
import com.suncreate.pressuresensor.util.MyTimeRunnable;
import com.suncreate.pressuresensor.util.TimeUtil;
import com.suncreate.pressuresensor.util.UIHelper;
import com.suncreate.pressuresensor.widget.CircleTextProgressbar;
import com.suncreate.pressuresensor.widget.ps.EcgView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * 盆底检测-快肌检测(IIA纤维)
 *
 * @author linking
 *         created on 2018/6/11 22:55
 */
public class FloorDetectionActivity2 extends BaseActivityBlueToothLE implements View.OnClickListener,
        BaseViewInterface {

    protected static final String TAG = FloorDetectionActivity2.class.getSimpleName();

    @Bind(R.id.ev_box)
    EcgView ev_box;
    @Bind(R.id.btn_start_or_stop)
    Button btn_start_or_stop;
    @Bind(R.id.tbn_play_next)
    Button tbn_play_next;
    @Bind(R.id.tc_ps_num)
    TextView tc_ps_num;
    @Bind(R.id.progressBar)
    MaterialProgressBar mProgressBar;
    @Bind(R.id.tv_time_count)
    TextView tv_time_count;

    //蓝牙对象,..
    private BluetoothLe mBluetoothLe;
    private BluetoothDevice mBluetoothDevice;
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();

    int detectionType; // 检测类型 0-前静息期，1-耐力检测，2-快肌检测\nIIA类肌纤维，3-快肌检测\nIIB类肌纤维，
    // 4-慢肌检测\nI类肌纤维，5-慢肌检测\nI类肌纤维
    private static final int dtcType0 = 0;
    private static final int dtcType1 = 1;
    private static final int dtcType2 = 2;
    private static final int dtcType3 = 3;
    private static final int dtcType4 = 4;

    private Queue<Integer> dataQueue = new LinkedList<>();

    private MyTimeRunnable runnable;
    int time = 0;
    Handler mHandler;
    boolean isPause = false;//是否暂停
    private MediaPlayer mPlayer;
    boolean isRuning = false;//是否运行中

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ps_floor_detection2);

        ButterKnife.bind(this);
        initView();
        AppManager.getAppManager().addActivity(this);
        handleIntent(getIntent());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mBluetoothLe = BluetoothLe.getDefault();
        initData();
        simulator();
        checkSupport();
        mHandler = new Handler();
    }

    /**
     * 模拟心电数据发送
     */
    private void simulator() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (EcgView.isRunning) {
                    if (dataQueue.size() > 0) {
                        EcgView.addEcgData(dataQueue.poll());
                    }
                }
            }
        }, 0, 2);
    }

    @Override
    public void initView() {

        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_NOTICE);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        NoticeUtils.bindToService(this);
        btn_start_or_stop.setOnClickListener(this);
        tbn_play_next.setOnClickListener(this);

    }

    @Override
    public void initData() {
        registerListener();

        final PsNumCallback psNumCallback = new PsNumCallback() {
            @Override
            public void setNowPsNum(final String psNum) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tc_ps_num.setText(psNum);
                    }
                });
            }
        };
        ev_box.setSetNowPsNumCallback(psNumCallback);
    }

    public void startScan() {
        AppContext.showToastShort("开始连接设备，请稍后");
        scan();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.btn_start_or_stop:
                tonggleEcgRunning();
                break;
            case R.id.tbn_play_next:
                nextGame();
                break;
            default:
                break;
        }
    }

    //切换心电图的运行停止状态
    private void tonggleEcgRunning() {
        if (EcgView.isRunning) {
            btn_start_or_stop.setBackgroundResource(R.drawable.start_32);
            ev_box.stopThread();
        } else {
            if (!mBluetoothLe.getConnected()) {
                AppContext.showToastShort("设备未连接，请稍候");
                return;
            }
            btn_start_or_stop.setBackgroundResource(R.drawable.pause_32);
            ev_box.startThread();
            playRunnableSound();
        }
    }

    private void playRunnableSound() {
        if (!EcgView.isRunning) {
            time = Integer.valueOf(tv_time_count.getText().toString());
            if (time == 0) {
                runnable = new MyTimeRunnable(mHandler, time) {
                    @Override
                    protected void onPlayMusic0s() {
//                        mPlayer = new MediaPlayer();
                        onPlay();
                    }

                    @Override
                    protected void onPlayMusic20s() {
                        AppContext.showToastShort("20");
                    }

                    @Override
                    protected void onPlayMusic30s() {
                        AppContext.showToastShort("30");
                    }

                    /**
                     * 结束时调用
                     */
                    @Override
                    protected void overTime() {
                        if (mPlayer != null)
                            mPlayer.stop();
                        tv_time_count.setText("开始");
                    }

                    /**
                     * 更新显示时间
                     */
                    @Override
                    protected void turnoverTime(int time) {
                        tv_time_count.setText(TimeUtil.getTime(time));
                    }
                };
                mHandler.postDelayed(runnable, 1000);
                tv_time_count.setText("暂停");
            }
        } else {
            mHandler.removeCallbacks(runnable);
            tv_time_count.setText("继续");
        }
    }

    /**
     * 音乐播放
     */
    private void onPlay() {
        try {
            AssetFileDescriptor fd = getAssets().openFd("voice/C03A.wav");
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(fd.getFileDescriptor());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        //根据TAG注销监听，避免内存泄露
        mBluetoothLe.destroy(TAG);
        //关闭GATT
//        //mBluetoothLe.close();
    }

    //跳到下一个项目
    private void nextGame() {
        Intent intent0 = new Intent(FloorDetectionActivity2.this, FloorDetectionActivity3.class);
        startActivity(intent0);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
        finish();
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
            if (mBluetoothLe.getConnected()) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                startScan();
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
                    .setMessage("Android 6.0 以上的系统版本，扫描蓝牙需要地理位置权限，请允许。")
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
            Toast.makeText(this, "您的Android版本在7.0以上，扫描蓝牙需要打开位置信息。", Toast.LENGTH_LONG).show();
            LocationUtils.gotoLocServiceSettings(this);
            return;
        }
        mBluetoothLe.setScanPeriod(1000000)
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

                if ("BT05".equals(mBluetoothDevice.getName())) {
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
                mProgressBar.setVisibility(View.GONE);
                AppContext.showToastShort("连接成功，点击开始按钮，愉快的玩耍吧");
                mBluetoothLe.stopScan();  //连接成功后停止扫描
//                EcgView.isRunning = true;
            }

            @Override
            public void onDeviceDisconnected() {
                Log.i(TAG, "连接断开！");
                AppContext.showToastShort("设备连接断开");
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt) {
                Log.i(TAG, "发现服务");

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

                Log.i("the4num is", the4Num);
                int pressureNum = Integer.parseInt(the4Num, 16);  //转化为10进制的压力值,最大值120左右
                Log.i("pressureNum", String.valueOf(pressureNum));

                //1.数据大于0才传输
                if (pressureNum == 0) {
                    return;
                }

                //设置一层过滤器，固定时间间隔内，才传输一次数据，忽略间隔中间的数据

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
                    }
                });
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle("");
    }

    private void changeHeight(int pressureNum) {

        if (pressureNum > 0 && EcgView.isRunning) {
            Log.i("has pressureNum: ", "success");

            /**
             * 数据分为展示数据和存储数据。
             * 1.展示数据可以用全部的数据，x轴根据时间，把1s细化；y轴用真实的压力值换算得来。再辅助在旁边显示压力值。
             * 2.存储的数据，需要分粒度，目前倾向于保存1s间隔的数据。可以设计为时间过一秒就，加一个<time,data>到list。
             */
            try {

                dataQueue.add(pressureNum);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "changeHeight: 数据加入队列失败");
            }
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
        super.onBackPressed();
    }

    /*@Override
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
        switch (id) {
            case android.R.id.home:
                UIHelper.returnHome(this);
                break;
            *//*case R.id.action_select_ble:
                UIHelper.showSimpleBack(getApplicationContext(), SimpleBackPage.CONNECT_BLE_LIST);
                break;*//*
            case R.id.action_scan_record:
                UIHelper.showSimpleBack(getApplicationContext(), SimpleBackPage.SCAN_RECORD);
                break;

        }

        return super.onOptionsItemSelected(item);
    }*/

}
