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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.suncreate.pressuresensor.util.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;

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
    @Bind(R.id.fb_mode_introduce)
    FButton fb_mode_introduce;
    @Bind(R.id.fb_adapt_training)
    FButton fb_adapt_practice;
    @Bind(R.id.fb_combined_training)
    FButton fb_combined_training;
    @Bind(R.id.fb_constipine_training)
    FButton fb_constipine_training;
    @Bind(R.id.fb_fast_muscle_training)
    FButton fb_fast_muscle_training;
    @Bind(R.id.fb_slow_muscle_training)
    FButton fb_slow_muscle_training;


    public static Notice mNotice;
    //蓝牙对象
    private BluetoothLe mBluetoothLe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fire_index);

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

        fb_mode_introduce.setOnClickListener(this);
        fb_adapt_practice.setOnClickListener(this);
        fb_combined_training.setOnClickListener(this);
        fb_constipine_training.setOnClickListener(this);
        fb_fast_muscle_training.setOnClickListener(this);
        fb_slow_muscle_training.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_mode_introduce:
                UIHelper.showSimpleBack(getApplicationContext(), SimpleBackPage.TRADE_MODE_INTRODUCE);
                break;
            case R.id.fb_adapt_training:
                Intent intent = new Intent(MainActivity.this, AdapterTraningActivity.class);
                startActivity(intent);
                break;
            case R.id.fb_combined_training:
                Intent intent1 = new Intent(MainActivity.this, AdapterTraningActivity.class);
                startActivity(intent1);
                break;
            case R.id.fb_constipine_training:
                Intent inten2t = new Intent(MainActivity.this, AdapterTraningActivity.class);
                startActivity(inten2t);
                break;
            case R.id.fb_fast_muscle_training:
                Intent intent3 = new Intent(MainActivity.this, AdapterTraningActivity.class);
                startActivity(intent3);
                break;
            case R.id.fb_slow_muscle_training:
                Intent intent5 = new Intent(MainActivity.this, AdapterTraningActivity.class);
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
        //关闭GATT
        mBluetoothLe.close();
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
            case R.id.action_select_ble:
                UIHelper.showSimpleBack(getApplicationContext(), SimpleBackPage.CONNECT_BLE_LIST);
                break;
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