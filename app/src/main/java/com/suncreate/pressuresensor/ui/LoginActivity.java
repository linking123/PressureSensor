package com.suncreate.pressuresensor.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.suncreate.pressuresensor.AppConfig;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.AppManager;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.ApiHttpClient;
import com.suncreate.pressuresensor.api.remote.FireiotApi;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.LoginUserBean;
import com.suncreate.pressuresensor.bean.OpenIdCatalog;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.scan.User;
import com.suncreate.pressuresensor.interf.BaseViewInterface;
import com.suncreate.pressuresensor.util.CyptoUtils;
import com.suncreate.pressuresensor.util.DialogHelp;
import com.suncreate.pressuresensor.util.TDevice;
import com.suncreate.pressuresensor.util.TLog;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;

import org.kymjs.kjframe.http.HttpConfig;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * 用户登录界面
 *
 * @author linking
 */
public class LoginActivity extends AppCompatActivity implements BaseViewInterface, View.OnClickListener {

    public static final int REQUEST_CODE_INIT = 0;
    private static final String BUNDLE_KEY_REQUEST_CODE = "BUNDLE_KEY_REQUEST_CODE";
    protected static final String TAG = LoginActivity.class.getSimpleName();
    private UMSocialService mController;

    @Bind(R.id.login_username)
    EditText loginUserName;
    @Bind(R.id.login_password)
    EditText loginPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.btn_reset_pwd)
    Button btnPwd;

    private final int requestCode = REQUEST_CODE_INIT;
    private String mUserName = "";
    private String mPassword = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_fireiot);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                handleLogin();
                break;
            case R.id.btn_register:
                handleRegister();
                break;
            default:
                break;
        }
    }

    private void handleLogin() {

        if (prepareForLogin()) {
            return;
        }

        // if the data has ready
        mUserName = loginUserName.getText().toString();
        mPassword = loginPassword.getText().toString();

//        handleLoginSuccess();  //免登录，直接主页
        FireiotApi.login(mUserName, mPassword, mHandler);
    }

    /**
     * 处理注册按钮点击事件
     */
    private void handleRegister() {

    }

    TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToastShort("登录失败！");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<User> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<User>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    handleLoginBean(resultBean, headers);
                    AppContext.showToastShort("登录成功！");
                } else if (resultBean != null && resultBean.getMessage() != null) {
                    AppContext.getInstance().cleanLoginInfo();
                    AppContext.showToast(resultBean.getMessage());
//                    mRefreshLayout.setNoMoreData();
                }
//                hideWaitDialog();
                //mRefreshLayout.onLoadComplete();
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
//            hideWaitDialog();
        }
    };

    private void handleLoginSuccess() {
        Intent data = new Intent();
        data.putExtra(BUNDLE_KEY_REQUEST_CODE, requestCode);
        setResult(RESULT_OK, data);
        this.sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
        AppManager.getAppManager().finishAllActivity();
        Intent intent = new Intent(AppContext.getInstance(), MainActivity.class);
        startActivity(intent);
    }

    private boolean prepareForLogin() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return true;
        }
        if (loginUserName.length() == 0) {
            loginUserName.setError("请输入用户名");
            loginUserName.requestFocus();
            return true;
        }

        if (loginPassword.length() == 0) {
            loginPassword.setError("请输入密码");
            loginPassword.requestFocus();
            return true;
        }

        return false;
    }

    @Override
    public void initData() {

        loginUserName.setText(AppContext.getInstance()
                .getProperty("user.userMobile"));
        if (null != AppContext.getInstance().getProperty("user.userMobile")) {
            loginPassword.requestFocus();
        }
//        loginPassword.setText(CyptoUtils.decode("oschinaApp", AppContext.getInstance().getProperty("user.pwd")));
    }

    BroadcastReceiver receiver;

    // 处理loginBean
    private void handleLoginBean(ResultBean<User> loginUserBean, Header[] headers) {
        AsyncHttpClient client = ApiHttpClient.getHttpClient();
        HttpContext httpContext = client.getHttpContext();
        CookieStore cookies = (CookieStore) httpContext
                .getAttribute(ClientContext.COOKIE_STORE);
        if (cookies != null) {
            String tmpcookies = "";
            for (Cookie c : cookies.getCookies()) {
                TLog.log(TAG,
                        "cookie:" + c.getName() + " " + c.getValue());
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
            TLog.log(TAG, "cookies:" + tmpcookies);
            AppContext.getInstance().setProperty(AppConfig.CONF_COOKIE,
                    tmpcookies);
            ApiHttpClient.setCookie(ApiHttpClient.getCookie(AppContext
                    .getInstance()));
            HttpConfig.sCookie = tmpcookies;
        }
        // 保存登录信息
        AppContext.getInstance().setProperty("user.token", loginUserBean.getToken());
        AppContext.getInstance().saveUserInfo(loginUserBean.getResult());
        handleLoginSuccess();
    }
}
