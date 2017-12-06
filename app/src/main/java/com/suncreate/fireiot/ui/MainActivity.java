package com.suncreate.fireiot.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.AppManager;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.Notice;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.scan.User;
import com.suncreate.fireiot.interf.BaseViewInterface;
import com.suncreate.fireiot.service.NoticeUtils;
import com.suncreate.fireiot.util.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements BaseViewInterface, View.OnClickListener,
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
    //    @Bind(R.id.fab)
//    FloatingActionButton fab;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.index_scan_start)
    FButton btStartScan;
    @Bind(R.id.index_scan_logo)
    GifImageView index_scan_logo;
//    ImageView index_scan_logo;

    public static Notice mNotice;


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

    }

    @Override
    protected void onStop() {
        super.onStop();
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
        NoticeUtils.unbindFromService(this);
        NoticeUtils.tryToShutDown(this);
        AppManager.getAppManager().removeActivity(this);
        //注销广播接收
        unregisterReceiver(mReceiver);
    }

    @Override
    public void initData() {
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

    public void initUserName() {
        if (navigationView != null) {
            navigationView.setItemIconTintList(null);
            //获取NavigationView上的控件id
            View headerView = navigationView.getHeaderView(0);
            TextView mUserName = headerView.findViewById(R.id.tv_user_name);
            User user = AppContext.getInstance().getLoginUser();
            mUserName.setText(user != null ? user.getUserName() : "欢迎您，巡检员");
        }
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