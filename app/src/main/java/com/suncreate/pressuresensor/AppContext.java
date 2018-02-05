package com.suncreate.pressuresensor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qindachang.bluetoothle.BluetoothConfig;
import com.qindachang.bluetoothle.BluetoothLe;
import com.suncreate.pressuresensor.api.ApiHttpClient;
import com.suncreate.pressuresensor.api.remote.PressureSensorApi;
import com.suncreate.pressuresensor.base.BaseApplication;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.scan.User;
import com.suncreate.pressuresensor.bean.user.AreaJson;
import com.suncreate.pressuresensor.cache.DataCleanManager;
import com.suncreate.pressuresensor.service.LocationService;
import com.suncreate.pressuresensor.util.CityDataUtils;
import com.suncreate.pressuresensor.util.MethodsCompat;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.TLog;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.TextHttpResponseHandler;

import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.utils.KJLoger;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.EncodingUtils;
import cz.msebera.android.httpclient.util.TextUtils;

import static com.suncreate.pressuresensor.AppConfig.KEY_FIRST_START;
import static com.suncreate.pressuresensor.AppConfig.KEY_LOAD_IMAGE;
import static com.suncreate.pressuresensor.AppConfig.KEY_NIGHT_MODE_SWITCH;
import static com.suncreate.pressuresensor.AppConfig.KEY_TWEET_DRAFT;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 *
 *
 */
public class AppContext extends BaseApplication {

    public static final String TAG = "AppContext";

    public static final int PAGE_SIZE = 10;// 默认分页大小

    public static Double lat;// 维度
    public static Double lon;// 经度
    public static String city;// 城市名称
    public static String citycode;// 城市编码,市级编码如340100，后两位不需要，用户表取出时后两位替换为00
    public static String addressDetail;// 省市县(区)街道具体场所

    private static AppContext instance;

    private int loginUid;

    private boolean login;

    public LocationService locationService;
    public Vibrator mVibrator;
    public String areaJson = "";
    List<AreaJson> areaList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        /**
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());

        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(this);
        /*
        // AppException 取消
        Thread.setDefaultUncaughtExceptionHandler(AppException
                .getAppExceptionHandler(this));
                */
        UIHelper.sendBroadcastForNotice(this);
//        areaJson = getFromAssets(Constants.AREA_JSON_NAME);
//        areaList = JSON.parseArray(areaJson, AreaJson.class);

        //蓝牙配置
        BluetoothConfig config = new BluetoothConfig.Builder()
                .enableQueueInterval(true)
                .setQueueIntervalTime(BluetoothConfig.AUTO)//发送时间间隔将根据蓝牙硬件自动得出
                .build();
        BluetoothLe.getDefault().init(this, config);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void init() {
        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        client.setResponseTimeout(20000);
        ApiHttpClient.setHttpClient(client);
        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));

        // Log控制器
        KJLoger.openDebutLog(BuildConfig.DEBUG);
        TLog.DEBUG = BuildConfig.DEBUG;

        // Bitmap缓存地址
        HttpConfig.CACHEPATH = "fireIot/imagecache";
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUE_ID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUE_ID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 保存登录信息
     *
     * @param user 用户信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final User user) {
        this.loginUid = user.getUserId();
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getUserId()));
                setProperty("user.name", StringUtils.isEmpty(user.getUserName()) ? "" : user.getUserName());
                setProperty("user.phone", StringUtils.isEmpty(user.getUserMobile()) ? "" : user.getUserMobile());
                setProperty("user.email", StringUtils.isEmpty(user.getUserEmail()) ? "" : user.getUserEmail());
            }
        });
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final User user) {
        setProperties(new Properties() {
            {
                setProperty("user.name", user.getUserName());
            }
        });
    }

    /**
     * 获得登录用户的信息【for本系统用户】
     *
     * @return
     */
    public User getLoginUser() {
        if (!login) {
            return null;
        }
        User user = new User();
        try {
            user.setUserId(StringUtils.toInt(getProperty("user.uid")));
            user.setUserName(getProperty("user.name"));
            user.setUserMobile(getProperty("user.phone"));
            user.setUserEmail(getProperty("user.email"));
        } catch (Exception e) {
            Log.e(TAG, "获取登录信息失败");
        }
        return user;
    }
//
//    /**
//     * 获得登录用户的信息【for本系统用户】
//     *
//     * @return
//     */
//    public User getLoginUserExt() {
//        if (!login) {
//            return null;
//        }
//        User user = new User();
//        try {
//            user.setUserId(Integer.getInteger(getProperty("user.uid")));
//            user.setUserName(getProperty("user.name"));
//            user.setUserMobile(getProperty("user.phone"));  //手机号 jin
//        } catch (Exception e) {
//            Log.e(TAG, "获取登录信息失败");
//        }
//        return user;
//    }

    /**
     * 获取当前用户
     * 1.车主
     * 2.技师
     * 3.修理厂
     **/

    public int getCurrentRole() {
        if (TextUtils.isEmpty(getProperty("user.currentRole"))) {
            return 1;
        } else {
            return Integer.parseInt(getProperty("user.currentRole"));
        }
    }

    public String getUserRole() {
        try {
            return "1";
        } catch (Exception e) {
            Log.e(TAG, "获取登录信息失败，返回默认角色！");
            e.printStackTrace();
            return "1";
        }
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.login = false;
        removeProperty("user.uid", "user.name", "user.phone", "user.email", "user.location",
                "user.followers", "user.fans", "user.score",
                "user.isRememberMe", "user.gender",
                "user.token");
    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        PressureSensorApi.logout(mLogoutHandler);
    }

    TextHttpResponseHandler mLogoutHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("注销失败！");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    AppContext.showToastShort(R.string.tip_logout_success);
                } else {
                    AppContext.showToast(resultBean.getMessage());
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }
        }

        @Override
        public void onFinish() {
            logoutOperation();
            super.onFinish();

        }
    };

    /**
     * 退出相关操作
     */
    public void logoutOperation() {
        sendBroadcast(new Intent(Constants.INTENT_ACTION_LOGOUT));
        cleanCookie();
        cleanLoginInfo();
    }

    /**
     * 清除保存的缓存cookie
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        DataCleanManager.cleanDatabases(this);
        // 清除数据缓存
        DataCleanManager.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanManager.cleanCustomCache(MethodsCompat
                    .getExternalCacheDir(this));
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
        Core.getKJBitmap().cleanCache();
    }

    public static void setLoadImage(boolean flag) {
        set(KEY_LOAD_IMAGE, flag);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public static String getTweetDraft() {
        return getPreferences().getString(
                KEY_TWEET_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setTweetDraft(String draft) {
        set(KEY_TWEET_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static String getNoteDraft() {
        return getPreferences().getString(
                AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setNoteDraft(String draft) {
        set(AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static boolean isFirstStart() {
        return getPreferences().getBoolean(KEY_FIRST_START, true);
    }

    public static void setFristStart(boolean frist) {
        set(KEY_FIRST_START, frist);
    }

    //夜间模式
    public static boolean getNightModeSwitch() {
        //return getPreferences().getBoolean(KEY_NIGHT_MODE_SWITCH, false);
        return false;
    }

    // 设置夜间模式
    public static void setNightModeSwitch(boolean on) {
        set(KEY_NIGHT_MODE_SWITCH, on);
    }

    public static Gson createGson() {
        com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder();
        //gsonBuilder.setExclusionStrategies(new SpecificClassExclusionStrategy(null, Model.class));
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
//        gsonBuilder.serializeNulls();
//        gsonBuilder.registerTypeAdapter(int.class, new TypeAdapter<Number>() {
//            @Override
//            public Number read(JsonReader in) throws IOException {
//                if (in.peek() == JsonToken.NULL) {
//                    in.nextNull();
//                    return 0;
//                }
//                return in.nextInt();
//            }
//
//            @Override
//            public void write(JsonWriter out, Number value) throws IOException {
//                out.value(value);
//            }
//        });
        return gsonBuilder.create();
    }

    public static GlideUrl getGlideUrlByUser(String url) {
        if (AppContext.getInstance().isLogin()) {
            return new GlideUrl(url,
                    new LazyHeaders
                            .Builder()
                            .addHeader("Cookie", ApiHttpClient.getCookie(AppContext.getInstance()))
                            .build());
        } else {
            return new GlideUrl(url);
        }
    }

    /**
     * 从assets读取城市列表json
     *
     * @param fileName
     * @return
     */
    public String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStream in = getAssets().open(fileName);//获取资源
            int length = in.available();//获取文字字数
            byte[] buffer = new byte[length];
            in.read(buffer);//读到数组中
            //设置编码
            result = EncodingUtils.getString(buffer, "utf-8");
        } catch (Exception e) {
        }
        return result;
    }

    public String getAreaNameByCode(String areaCode) {
        String cityName = "";
        if (!android.text.TextUtils.isEmpty(areaCode) && 6 == areaCode.length()) {
            cityName = CityDataUtils.getNameByCode(areaList, areaCode);
        }
        return cityName;
    }

    /**
     * 根据名称获取对应城市编码 getCityCodeByName("合肥市")
     *
     * @param cityName
     * @return
     */
    public String getCityCodeByName(String cityName) {
        String areaCode = "";
        if (!android.text.TextUtils.isEmpty(cityName)) {
            areaCode = CityDataUtils.getCodeByName(areaList, cityName);
        }
        return areaCode;
    }

    /**
     * 更句区域名称和所在城市名称获取其对应区域编码（不同城市区县名称可能有重复）getAreaCodeByName("高新区","合肥市")
     *
     * @param areaName
     * @param cityName
     * @return
     */
    public String getAreaCodeByName(String areaName, String cityName) {
        String areaCode = "";
        String cityCode = CityDataUtils.getCodeByName(areaList, cityName);
        if (!android.text.TextUtils.isEmpty(areaName)) {
            areaCode = CityDataUtils.getCodeByName(areaList, areaName, cityCode);
        }
        return areaCode;
    }

    /**
     * 根据区域编码获取区域全称（省市区）
     * 安徽省　合肥市　蜀山区　(340104)
     *
     * @param areaCode
     * @return
     */
    public String getAreaFullNameByCode(String areaCode) {
        String areaFullName = "";
        try {
            String city = getAreaNameByCode(areaCode.substring(0, 2) + "0000");
            if ("北京".equals(city) || "上海".equals(city) || "天津".equals(city) || "重庆".equals(city) || "澳门".equals(city) || "香港".equals(city)) {
                areaFullName = getAreaNameByCode(areaCode.substring(0, 4) + "00")
                        + " " + getAreaNameByCode(areaCode);
            } else {
                areaFullName = city
                        + " " + getAreaNameByCode(areaCode.substring(0, 4) + "00")
                        + " " + getAreaNameByCode(areaCode);
            }

        } catch (Exception e) {
            areaFullName = "未知";
        }

        return areaFullName;
    }

    /**
     * 根据区域编码获取区域到市级
     * 安徽省　合肥市
     *
     * @param areaCode
     * @return
     */
    public String getAreaProCityNameByCode(String areaCode) {
        String areaFullName = "";
        try {
            String city = getAreaNameByCode(areaCode.substring(0, 2) + "0000");
            if ("北京".equals(city) || "上海".equals(city) || "天津".equals(city) || "重庆".equals(city) || "澳门".equals(city) || "香港".equals(city)) {
                areaFullName = getAreaNameByCode(areaCode.substring(0, 4) + "00")
                        + " " + getAreaNameByCode(areaCode);
            } else {
                areaFullName = city
                        + " " + getAreaNameByCode(areaCode.substring(0, 4) + "00");
            }

        } catch (Exception e) {
            areaFullName = "未知";
        }

        return areaFullName;
    }
}
