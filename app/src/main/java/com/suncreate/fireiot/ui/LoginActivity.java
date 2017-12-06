package com.suncreate.fireiot.ui;

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
import com.suncreate.fireiot.AppConfig;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.AppManager;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.ApiHttpClient;
import com.suncreate.fireiot.api.remote.FireiotApi;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.LoginUserBean;
import com.suncreate.fireiot.bean.OpenIdCatalog;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.scan.User;
import com.suncreate.fireiot.interf.BaseViewInterface;
import com.suncreate.fireiot.util.DialogHelp;
import com.suncreate.fireiot.util.TDevice;
import com.suncreate.fireiot.util.TLog;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

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

    private final int requestCode = REQUEST_CODE_INIT;
    private String mUserName = "";
    private String mPassword = "";

    private static final int LOGIN_TYPE_SINA = 1;
    private static final int LOGIN_TYPE_QQ = 2;
    private static final int LOGIN_TYPE_WX = 3;

    private int loginType;

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

    Type getType;

    @Override
    public void initData() {

        loginUserName.setText(AppContext.getInstance()
                .getProperty("user.userMobile"));
        if (null != AppContext.getInstance().getProperty("user.userMobile")) {
            loginPassword.requestFocus();
        }
//        loginPassword.setText(CyptoUtils.decode("oschinaApp", AppContext.getInstance().getProperty("user.pwd")));

        TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //mRefreshLayout.onLoadComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    ResultBean<PageBean<User>> resultBean = AppContext.createGson().fromJson(responseString, getType);
                    if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {
                    } else {
                        //mRefreshLayout.setNoMoreData();
                    }
                    //mRefreshLayout.onLoadComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }
        };
    }

    /**
     * QQ登陆
     */
    private void qqLogin() {
        loginType = LOGIN_TYPE_QQ;
        Tencent mTencent = Tencent.createInstance(AppConfig.APP_QQ_KEY,
                this);
//        mTencent.login(this, "all", this);
    }

    BroadcastReceiver receiver;

    /**
     * 微信登陆
     */
    private void wxLogin() {
        loginType = LOGIN_TYPE_WX;
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.WEICHAT_APPID, false);
        api.registerApp(Constants.WEICHAT_APPID);

        if (!api.isWXAppInstalled()) {
            AppContext.showToast("手机中没有安装微信客户端");
            return;
        }
        // 唤起微信登录授权
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_login";
        api.sendReq(req);
        // 注册一个广播，监听微信的获取openid返回（类：WXEntryActivity中）
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OpenIdCatalog.WECHAT);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String openid_info = intent.getStringExtra(LoginBindActivityChooseActivity.BUNDLE_KEY_OPENIDINFO);
                    openIdLogin(OpenIdCatalog.WECHAT, openid_info);
                    // 注销这个监听广播
                    if (receiver != null) {
                        unregisterReceiver(receiver);
                    }
                }
            }
        };

        registerReceiver(receiver, intentFilter);
    }

    /**
     * 新浪登录
     */
    private void sinaLogin() {
        if (mController == null)
            mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        loginType = LOGIN_TYPE_SINA;
        SinaSsoHandler sinaSsoHandler = new SinaSsoHandler();
        mController.getConfig().setSsoHandler(sinaSsoHandler);
        mController.doOauthVerify(this, SHARE_MEDIA.SINA,
                new SocializeListeners.UMAuthListener() {

                    @Override
                    public void onStart(SHARE_MEDIA arg0) {
                    }

                    @Override
                    public void onError(SocializeException arg0,
                                        SHARE_MEDIA arg1) {
                        AppContext.showToast("新浪授权失败");
                    }

                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA arg1) {
                        if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                            // 获取平台信息
                            mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, new SocializeListeners.UMDataListener() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onComplete(int i, Map<String, Object> map) {
                                    if (i == 200 && map != null) {
                                        StringBuilder sb = new StringBuilder("{");
                                        Set<String> keys = map.keySet();
                                        int index = 0;
                                        for (String key : keys) {
                                            index++;
                                            String jsonKey = key;
                                            if (jsonKey.equals("uid")) {
                                                jsonKey = "openid";
                                            }
                                            sb.append(String.format("\"%s\":\"%s\"", jsonKey, map.get(key).toString()));
                                            if (index != map.size()) {
                                                sb.append(",");
                                            }
                                        }
                                        sb.append("}");
                                        openIdLogin(OpenIdCatalog.WEIBO, sb.toString());
                                    } else {
                                        AppContext.showToast("发生错误：" + i);
                                    }
                                }
                            });
                        } else {
                            AppContext.showToast("授权失败");
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA arg0) {
                        AppContext.showToast("已取消新浪登陆");
                    }
                });
    }

    /***
     * @param catalog    第三方登录的类别
     * @param openIdInfo 第三方的信息
     */
    private void openIdLogin(final String catalog, final String openIdInfo) {
        final ProgressDialog waitDialog = DialogHelp.getWaitDialog(this, "登陆中...");
//        WukongApi.open_login(catalog, openIdInfo, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                LoginUserBean loginUserBean = XmlUtils.toBean(LoginUserBean.class, responseBody);
//                if (loginUserBean.getResult().OK()) {
//                    // handleLoginBean(loginUserBean, headers);
//                } else {
//                    // 前往绑定或者注册操作
//                    Intent intent = new Intent(LoginActivity.this, LoginBindActivityChooseActivity.class);
//                    intent.putExtra(LoginBindActivityChooseActivity.BUNDLE_KEY_CATALOG, catalog);
//                    intent.putExtra(LoginBindActivityChooseActivity.BUNDLE_KEY_OPENIDINFO, openIdInfo);
//                    startActivityForResult(intent, REQUEST_CODE_OPENID);
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                AppContext.showToast("网络出错" + statusCode);
//            }
//
//            @Override
//            public void onStart() {
//                super.onStart();
//                waitDialog.show();
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                waitDialog.dismiss();
//            }
//        });
    }

    public static final int REQUEST_CODE_OPENID = 1000;
    // 登陆实体类
    public static final String BUNDLE_KEY_LOGINBEAN = "bundle_key_loginbean";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (loginType == LOGIN_TYPE_SINA) {
            UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
            if (ssoHandler != null) {
                ssoHandler.authorizeCallBack(requestCode, resultCode, data);
            }
        } else {
            switch (requestCode) {
                case REQUEST_CODE_OPENID:
                    if (data == null) {
                        return;
                    }
                    LoginUserBean loginUserBean = (LoginUserBean) data.getSerializableExtra(BUNDLE_KEY_LOGINBEAN);
                    if (loginUserBean != null) {
                        //handleLoginBean(loginUserBean, null);
                    }
                    break;
                default:
                    break;
            }
        }
    }

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
