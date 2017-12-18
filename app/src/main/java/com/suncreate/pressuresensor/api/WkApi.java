package com.suncreate.pressuresensor.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.suncreate.pressuresensor.fragment.MyInformationFragment;
import com.suncreate.pressuresensor.util.CyptoUtils;
import com.suncreate.pressuresensor.util.NetUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 接口API
 *
 * @author chenzhao
 *         created on 2015/12/23 16:33
 */
public class WkApi {

    private static final String SECRET_KEY = "secret";//secret key
    //    private static final String SECRET_KEY = "eshore";//secret key
    public static final String uid = "suncreate";//客户端标识
//    public static final String uid = "portal";//客户端标识
    public static final long AC_ACCOUNT_TYPE= 7L;//认证方式（1.短信认证 ；2.微信连WIFI； 3.微信无感知 ；4.qq认证； 5. 一键认证； 6.微博认证；7.固定账户认证）

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param handler
     */
    public static void login(String username, String password, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", CyptoUtils.encode("suncreateApp", password));
//        params.put("uid", uid);
//        params.put("clientip", clientIp);
//        params.put("nasip", nasIp);
        params.put("mac", NetUtils.getMac());
//        params.put("timestamp", timestamp);
//        params.put("authenticator", getAuthenticator(uid, clientIp, nasIp, timestamp));//校验串
        ApiHttpClient.post("login", params, handler);
//        ApiHttpClient.get("login?mobile_phone=" + username + "&mobile_pwd=" + password, handler);
    }

    /**
     * 登出
     *
     * @param accountid
     * @param handler
     */
    public static void logout(long accountid, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("accountid", accountid);
        params.put("mac", NetUtils.getMac());
        ApiHttpClient.post("logout", params, handler);
    }

    /**
     * 保持会话
     *
     * @param handler
     */
    public static void keepAlive(AsyncHttpResponseHandler handler) {
        ApiHttpClient.post("keepalived", handler);
    }

    /**
     * 客户端上线接口
     */
    public static void loginAc(String clientip, String nasip, String mac, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("clientip", clientip);
        params.put("nasip", nasip);
        params.put("mac", mac);
//        params.put("radiususername", radiususername);
//        params.put("radiuspassword", radiuspassword);
        params.put("accounttype", AC_ACCOUNT_TYPE);
        ApiHttpClient.post("login/v1", params, handler);
    }

    /**
     * 客户端上线接口
     * @param handler
     */
    public static void logoutAc(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("mac", NetUtils.getMac());
        ApiHttpClient.post("logout/v1", params, handler);
    }

    /**
     * 获取个人资料
     *
     * @param accountid
     * @param handler
     */
    public static void getMyInformation(long accountid, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("accountid", accountid);
        ApiHttpClient.post("show", params, handler);
    }

    /**
     * 提交个人信息
     *
     * @param accountid
     * @param name
     * @param gender
     * @param idCard
     * @param areaCode
     * @param plateNum
     * @param handler
     */
    public static void updateMyInformation(int accountid, String name, long gender, String idCard, String areaCode, String plateNum, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("accountid", accountid);
        params.put("userName", name);
        params.put("gender", gender);
//        params.put("userEmail", userEmail);
//        params.put("idCard", idCard);
//        params.put("areaCode", areaCode);
//        params.put("plateNum", plateNum);
        params.put("userEmail", "");
        ApiHttpClient.post("edit", params, handler);
    }

    /**
     * 获取短信验证码
     *
     * @param userName
     * @param codeType
     * @param handler
     */
    public static void getVerifyCode(String userName, Integer codeType, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("tel", userName);
        params.put("sysKey", CyptoUtils.encode("SC_Monkey_Code",userName));
        ApiHttpClient.post("v1/user/code", params, handler);
//        ApiHttpClient.get("getCode?mobile_phone="+userName+"&code_type="+codeType, handler);
    }

    /**
     * 验证短信验证码
     *
     * @param codeType
     * @param verifyCode
     * @param handler
     */
    public static void checkVerifyCode(String userName, Integer codeType, String verifyCode, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("username", userName);
        params.put("code", verifyCode);
        params.put("phone", verifyCode);
        params.put("sysKey",  CyptoUtils.encode("SC_Monkey_Code",userName));
        ApiHttpClient.post("v1/user/verficate", params, handler);
    }

    /**
     * 注册/找回密码
     *
     * @param userName
     * @param password
     * @param codeType
     * @param handler
     */
    public static void registerRetrieve(String userName, String password, Integer codeType, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("username", userName);
        params.put("password", CyptoUtils.encode("SC_Monkey_Code",password));
        params.put("codeType", codeType);
        params.put("mac", NetUtils.getMac());
        ApiHttpClient.post("registerRetrieve", params, handler);
//        ApiHttpClient.get("register?mobile_phone="+userName+"&mobile_pwd="+password+"&code_type="+codeType, handler);
    }

    /**
     * 根据模块编码获取功能列表
     *
     * @param code
     * @param handler
     */
    public static void getAppFunList(String code, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("code", code);
        ApiHttpClient.post("getAppFunListByCode", params, handler);
    }


    /**
     * 上传用户头像
     *
     * @param uid
     * @param portrait
     * @param handler
     * @throws FileNotFoundException
     */
    public static void uploadPortrait(long uid, File portrait,
                                      AsyncHttpResponseHandler handler) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("accountid", uid);
        params.put("portrait", portrait);
        params.put("mac", NetUtils.getMac());
        ApiHttpClient.post("upload", params, handler);
    }

    /**
     * 获取头像相对路径
     *
     * @param originPath 原始格式：\\icon\\u_{new Date().getTime()}.jpg
     * @param type
     * @return
     */
    public static String getPortrait(String originPath, int type) {
        String portraitPath = "";
        try {
            switch (type) {
                case MyInformationFragment.AVATAR_TYPE_BIG:
                    portraitPath = ApiHttpClient.getAbsoluteApiUrl("getIcon/" + originPath.substring(6));
                    break;
                case MyInformationFragment.AVATAR_TYPE_SMALL:
                    portraitPath = ApiHttpClient.getAbsoluteApiUrl("getIcon/" + originPath.substring(6, originPath.lastIndexOf(".")) + "_100.jpg");
                    break;
            }
        } catch (Exception e) {
            portraitPath = "";
        }
        return portraitPath;
    }

    /**
     * 获取图片相对路径
     *
     * @param originPath
     * @return
     */
    public static String getIconPath(String originPath) {
        String iconPath;
        try {
            iconPath = ApiHttpClient.getAbsoluteApiUrl("getIcon/" + originPath.substring(6));
        } catch (Exception e) {
            iconPath = "";
        }
        return iconPath;
    }

    /**
     * `
     * 校验串 以字节流uid + clientip + nasip + timestamp做MD5加密成32位字符串，secret为通信密钥
     *
     * @param uid
     * @param clientip
     * @param nasip
     * @param timestamp
     * @return
     */
    public static String getAuthenticator(String uid, String clientip, String nasip, long timestamp) {
        StringBuilder sb = new StringBuilder();
        sb.append(uid).append(clientip).append(nasip).append(timestamp);
        System.out.println(sb);
        return md5(sb.toString());
    }

    /**
     * @param string
     * @return
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 检查App更新
     *
     * @param handler
     */
    public static void checkUpdate(AsyncHttpResponseHandler handler) {
        ApiHttpClient.post("checkAppVersion", handler);
    }

    private static void uploadLog(String data, String report,
                                  AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("app", "1");
        params.put("report", report);
        params.put("msg", data);
        ApiHttpClient.post("action/api/user_report_to_admin", params, handler);
    }

    /**
     * BUG上报
     *
     * @param data
     * @param handler
     */
    public static void uploadLog(String data, AsyncHttpResponseHandler handler) {
        uploadLog(data, "1", handler);
    }

    /**
     * 反馈意见
     *
     * @param data
     * @param handler
     */
    public static void feedback(String data, AsyncHttpResponseHandler handler) {
        uploadLog(data, "2", handler);
    }

    public static void singnIn(String url, AsyncHttpResponseHandler handler) {
        ApiHttpClient.getDirect(url, handler);
    }

    /***
     * 客户端扫描二维码登陆
     *
     * @param url
     * @param handler
     * @return void
     * @author 火蚁 2015-3-13 上午11:45:47
     */
    public static void scanQrCodeLogin(String url,
                                       AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        String uuid = url.substring(url.lastIndexOf("=") + 1);
        params.put("uuid", uuid);
        ApiHttpClient.getDirect(url, handler);
    }

}
