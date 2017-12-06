package com.suncreate.fireiot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.api.ApiHttpClient;
import com.suncreate.fireiot.api.remote.FireiotApi;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.scan.User;
import com.suncreate.fireiot.ui.LoginActivity;
import com.suncreate.fireiot.ui.MainActivity;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.TDevice;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.KJAsyncTask;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.PreferenceHelper;

import java.io.File;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * 应用启动界面
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年12月22日 上午11:51:56
 */
public class AppStart extends Activity {

    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_start);
//        final View view = View.inflate(this, R.layout.app_start, null);

        // 防止第三方跳转时出现双实例
        Activity aty = AppManager.getActivity(MainActivity.class);
        if (aty != null && !aty.isFinishing()) {
            finish();
        }
//        setContentView(view);
//        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
//        aa.setDuration(800);
//        view.startAnimation(aa);
//        aa.setAnimationListener(new AnimationListener() {
//            @Override
//            public void onAnimationEnd(Animation arg0) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationStart(Animation animation) {
                init();
//            }
//        });
    }

    private void init() {
//        if (AppContext.isFirstStart()) {
//            DataCleanManager.cleanInternalCache(AppContext.getInstance());
//            AppContext.setFristStart(false);
//            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
//        } else {
        mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
//        }
    }

    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        int cacheVersion = PreferenceHelper.readInt(this, "first_install",
                "first_install", -1);
        int currentVersion = TDevice.getVersionCode();
        if (cacheVersion < currentVersion) {
            PreferenceHelper.write(this, "first_install", "first_install",
                    currentVersion);
            cleanImageCache();
        }
    }

    private void cleanImageCache() {
        final File folder = FileUtils.getSaveFolder("monkey/imagecache");

        File[] files = folder.listFiles();

        if (files != null && files.length > 0) {

            KJAsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    if (folder.isDirectory()) {

                        for (File file : folder.listFiles()) {
                            file.delete();
                        }
                    }
                }

            });
        }
    }

    private void goGuide() {
        startActivity(new Intent(AppStart.this, GuideActivity.class));
        finish();
    }

    public void goHome() {
        String regToken = AppContext.getInstance().getProperty("user.token");
        if (!StringUtils.isEmpty(regToken)) {
            FireiotApi.autoLogin(regToken, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    //自动登录失败
                    redirectToLogin(); // 重新登录
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    ResultBean<User> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<User>>() {
                    }.getType());
                    if (resultBean != null && resultBean.isSuccess()) {
                        handleLoginBean(resultBean, headers);
                        redirectToMain();
                    } else {
                        redirectToLogin();
                    }
                }
            });
        } else {
//            redirectToLogin();  先去主页吧
            redirectToMain();
        }
    }

    /**
     * 跳转到登录页面
     */
    private void redirectToLogin() {
        AppContext.getInstance().cleanCookie();
        AppContext.getInstance().cleanLoginInfo();
        Intent intent;
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到主页面...
     */
    private void redirectToMain() {
//        Intent uploadLog = new Intent(this, LogUploadService.class);
//        startService(uploadLog);
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 处理loginBean，保存信息
    private void handleLoginBean(ResultBean<User> loginUserBean, Header[] headers) {
        AsyncHttpClient client = ApiHttpClient.getHttpClient();
        HttpContext httpContext = client.getHttpContext();
        CookieStore cookies = (CookieStore) httpContext
                .getAttribute(ClientContext.COOKIE_STORE);
        if (cookies != null) {
            String tmpcookies = "";
            for (Cookie c : cookies.getCookies()) {
                tmpcookies += (c.getName() + "=" + c.getValue()) + ";";
            }
            if (TextUtils.isEmpty(tmpcookies)) {

                if (headers != null) {
                    for (Header header : headers) {
                        String key = header.getName();
                        String value = header.getValue();
                        if (key.contains("Set-Cookie"))
                            tmpcookies += value + ";";
                    }
                    if (tmpcookies.length() > 0) {
                        tmpcookies = tmpcookies.substring(0, tmpcookies.length() - 1);
                    }
                }
            }
            AppContext.getInstance().setProperty(AppConfig.CONF_COOKIE,
                    tmpcookies);
            ApiHttpClient.setCookie(ApiHttpClient.getCookie(AppContext
                    .getInstance()));
            HttpConfig.sCookie = tmpcookies;
        }
        // 保存登录信息
        AppContext.getInstance().setProperty("user.token", loginUserBean.getToken());
        AppContext.getInstance().saveUserInfo(loginUserBean.getResult());
    }
}
