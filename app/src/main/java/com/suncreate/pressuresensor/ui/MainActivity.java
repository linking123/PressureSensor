package com.suncreate.pressuresensor.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qindachang.bluetoothle.BluetoothLe;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.AppManager;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.base.BaseActivityBlueToothLE;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.Notice;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.scan.User;
import com.suncreate.pressuresensor.interf.BaseViewInterface;
import com.suncreate.pressuresensor.service.NoticeUtils;
import com.suncreate.pressuresensor.ui.ble.AdapterTrainingActivity;
import com.suncreate.pressuresensor.ui.ble.CombinedTrainingActivity;
import com.suncreate.pressuresensor.ui.ble.ConstipineTrainingActivity;
import com.suncreate.pressuresensor.ui.ble.FastMuscleTrainingActivity;
import com.suncreate.pressuresensor.ui.ble.FloorDetectionActivity;
import com.suncreate.pressuresensor.ui.ble.FloorDetectionActivity1;
import com.suncreate.pressuresensor.ui.ble.SlowMuscleTrainingActivity;
import com.suncreate.pressuresensor.util.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivityBlueToothLE implements BaseViewInterface, View.OnClickListener,
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

    @Bind(R.id.fb_floor_detection)
    Button fb_floor_detection;
    @Bind(R.id.fb_floor_exerciese)
    Button fb_floor_exerciese;
    @Bind(R.id.fb_childcare_knowledge)
    Button fb_childcare_knowledge;
    @Bind(R.id.fb_use_manual)
    Button fb_use_manual;
    @Bind(R.id.fb_game_introduce)
    Button fb_game_introduce;
    @Bind(R.id.fb_server_introduce)
    Button fb_server_introduce;


    public static Notice mNotice;
    //蓝牙对象
    private BluetoothLe mBluetoothLe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ps_index);

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

        fb_floor_detection.setOnClickListener(this);
        fb_floor_exerciese.setOnClickListener(this);
        fb_childcare_knowledge.setOnClickListener(this);
        fb_use_manual.setOnClickListener(this);
        fb_game_introduce.setOnClickListener(this);
        fb_server_introduce.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_floor_detection:
//                UIHelper.showSimpleBack(getApplicationContext(), SimpleBackPage.FLOOR_DETECTION);
                Intent intent0 = new Intent(MainActivity.this, FloorDetectionActivity1.class);
                startActivity(intent0);
                break;
            case R.id.fb_floor_exerciese:
                Intent intent = new Intent(MainActivity.this, AdapterTrainingActivity.class);
                startActivity(intent);
                break;
            case R.id.fb_childcare_knowledge:
                Intent intent1 = new Intent(MainActivity.this, CombinedTrainingActivity.class);
                startActivity(intent1);
                break;
            case R.id.fb_use_manual:
                Intent inten2t = new Intent(MainActivity.this, ConstipineTrainingActivity.class);
                startActivity(inten2t);
                break;
            case R.id.fb_game_introduce:
                Intent intent3 = new Intent(MainActivity.this, FastMuscleTrainingActivity.class);
                startActivity(intent3);
                break;
            case R.id.fb_server_introduce:
                Intent intent5 = new Intent(MainActivity.this, SlowMuscleTrainingActivity.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
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

        //根据TAG注销监听，避免内存泄露
        mBluetoothLe.destroy(TAG);
//        //关闭GATT，只在activity中
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
        switch (id) {
            case R.id.action_scan_record:
                UIHelper.showSimpleBack(getApplicationContext(), SimpleBackPage.SCAN_RECORD);
                break;
          /*  case R.id.action_select_ble:
                UIHelper.showSimpleBack(getApplicationContext(), SimpleBackPage.CONNECT_BLE_LIST);
//                Intent intent;
//                intent = new Intent(MainActivity.this, BluetoothActivity.class);
//                startActivity(intent);
                break;*/
            default:
                break;
        }
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