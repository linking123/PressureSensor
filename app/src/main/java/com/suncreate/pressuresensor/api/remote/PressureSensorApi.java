package com.suncreate.pressuresensor.api.remote;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.suncreate.pressuresensor.api.ApiHttpClient;
import com.suncreate.pressuresensor.util.CyptoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author linking
 *         date: 2017/10/24.
 *         description: description
 */
public class PressureSensorApi {


    /**
     * 用户注册
     *
     * @param userLoginName  必填，用户名
     * @param telephone 必填，手机号码
     * @param password  必填，登录密码(MD5加密后)
     * @param handler   回调
     */
    public static void register(String userLoginName, String telephone, String password, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("telephone", telephone);
        params.put("userLoginName", userLoginName);
        params.put("password", PressureSensorApi.md5(password));
        String sysKey = CyptoUtils.encode("Pressure_Sensor_Register_Code", telephone);
        params.put("sysKey", sysKey);
        ApiHttpClient.post("userModule/register", params, handler);
    }

    /**
     * 用户登录
     *
     * @param username 必填，用户名
     * @param password 必填，登录密码(MD5加密后)
     * @param handler  必填，登录校验码(客户端服务端约定一种加密）
     */
    public static void login(String username, String password, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("userLogin", username);
        params.put("userPassword", md5(password));
        String sysKey = CyptoUtils.encode("Pressure_Sensor_login_Code", username);
        params.put("loginKey", sysKey);
        ApiHttpClient.post("userModule/login", params, handler);
    }

    /**
     * 找回密码
     *
     * @param telephone 必填，手机号
     * @param newpwd    必填，新密码
     */
    public static void resetPassword(String telephone, String newpwd, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("telephone", telephone);
        params.put("resetpwd", PressureSensorApi.md5(newpwd));
        ApiHttpClient.post("v1/user/resetPassword", params, handler);
    }

    /**
     * 修改密码
     *
     * @param newpwd 必填，新密码（MD5加密）
     */
    public static void password(String newpwd, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("newpwd", PressureSensorApi.md5(newpwd));
        ApiHttpClient.post("v1/user/password", params, handler);
    }

    /**
     * 用户退出
     *
     * @param handler 必填，登录校验码(客户端服务端约定一种加密
     */
    public static void logout(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        ApiHttpClient.post("userModule/logout", params, handler);
    }

    /**
     * 用户自动登录
     *
     * @param token   必填，客户端保存的用户登录token
     * @param handler 必填，登录校验码(客户端服务端约定一种加密）
     */
    public static void autoLogin(String token, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("token", token);
        ApiHttpClient.post("userModule/regToken", params, handler);
    }

    /**
     * 保持会话
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void keepalived(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        ApiHttpClient.post("userModule/keepalived", params, handler);
    }

    public static void checkUpdate(AsyncHttpResponseHandler handler) {
        ApiHttpClient.get("common/versionInfo", handler);
    }

    /**
     * 查看用户信息
     *
     * @param userId 用户id
     */
    public static void getUserInfo(String userId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        ApiHttpClient.post("userModule/showUserInfo", params, handler);
    }

    /**
     * 用户信息更新
     *
     * @param userId   必填，用户Id
     * @param userName 必填，用户名
     * @param tel      必填，电话
     * @param email    必填，邮箱
     * @param handler  handler
     */
    public static void updateUser(String userId, String userName, String tel, String email,
                                  AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("userName", userName);
        params.put("tel", tel);
        params.put("email", email);
        ApiHttpClient.post("userModule/update", params, handler);
    }

    /**
     * 新增巡检记录
     *
     * @param equipId            必填，关联设备ID
     * @param patrolInspector    必填，巡检员ID
     * @param fireEquipmentstate 必填，设备状态
     * @param checkEquipIp       选填，设备IP
     * @param desciption         选填，描述
     * @param checkLongitude     选填，经度
     * @param checkLatitude      选填，纬度
     * @param checkEquipNo       选填，设备编码
     */
    public static void addFireCheckRecord(String equipId, String patrolInspector, int fireEquipmentstate,
                                          String checkEquipIp, String desciption, Double checkLongitude,
                                          Double checkLatitude, String checkEquipNo, Map<String, File[]> imgMap,
                                          AsyncHttpResponseHandler handler) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("equipId", equipId);
        params.put("patrolInspector", patrolInspector);
        params.put("fireEquipmentstate", fireEquipmentstate);
        params.put("checkEquipIp", checkEquipIp); //NetUtils.getIPAddress());//params.put("mac", NetUtils.getMac());
        params.put("desciption", desciption);
        params.put("checkLongitude", checkLongitude);
        params.put("checkLatitude", checkLatitude);
        params.put("checkEquipNo", checkEquipNo);
        Iterator it = imgMap.entrySet().iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (((Map.Entry) obj).getKey() == null) {
                return;
            }
            String key = ((Map.Entry) obj).getKey().toString();
            File[] imgFiles = imgMap.get(key);
            for (int i = 0; i < imgFiles.length; i++) {
                if (imgFiles[i] != null) {
                    params.put(key + "i" + i, imgFiles[i]);
                }
            }
        }
        ApiHttpClient.post("fireCheckRec/add", params, handler);
    }

    /**
     * 获取巡检记录列表
     *
     * @param equipId   选填，设备ID
     * @param equipName 选填，设备名称
     * @param sortType  选填，排序方式（ desc或 asc），默认desc
     * @param sortName  选填，排序字段名称(默认id)
     * @param pageNum   必填，当前页，默认为第一页
     * @param pageSize  必填，每页显示数，默认10
     */
    public static void getFireCheckRecList(String equipId, String equipName, String sortType,
                                           String sortName, int pageNum, int pageSize,
                                           AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("equipId", equipId);
        params.put("equipName", equipName);
        params.put("sortType", sortType);
        params.put("sortName", sortName);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("fireCheckRec/list", params, handler);
    }

    /**
     * 获取巡检记录详情
     *
     * @param id 必填，巡检记录ID
     */
    public static void getFireCheckRecDetail(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("fireCheckRec/view", params, handler);
    }

    /**
     * 通过设备标签id 获取设备详情
     *
     * @param labelCode RFID标签编码
     */
    public static void showEquipmentInfoByLabel(String labelCode, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("labelCode", labelCode);
        ApiHttpClient.post("fireEquipment/showEquipmentInfoByLabel", params, handler);
    }

    /**
     * 获取附件图片
     *
     * @param id 必填，附件编号
     */
    public static void getPartImg(String id, AsyncHttpResponseHandler handler) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.get("fileAccessory/getAccessory", params, handler);
    }

    /**
     * 附件图片地址
     *
     * @param id string
     * @return url
     */
    public static String getPartImgUrl(Long id) {
        return getPartImgUrl(String.valueOf(id));
    }

    /**
     * 附件图片地址
     *
     * @param id long
     * @return url
     */
    public static String getPartImgUrl(String id) {
        StringBuffer sb = new StringBuffer(ApiHttpClient.getAbsoluteApiUrl("fileAccessory/getAccessory"));
        if (!TextUtils.isEmpty(id)) {
            sb.append("?id=" + id);
        }
        return String.valueOf(sb);
    }


    /**
     * MD5加密
     *
     * @param string 待加密字符串
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
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
