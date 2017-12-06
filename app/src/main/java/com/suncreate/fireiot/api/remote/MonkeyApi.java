package com.suncreate.fireiot.api.remote;

import android.text.TextUtils;

import com.suncreate.fireiot.api.ApiHttpClient;
import com.suncreate.fireiot.api.WkApi;
import com.suncreate.fireiot.util.CyptoUtils;
import com.suncreate.fireiot.util.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

public class MonkeyApi {
    /**
     * 获取手机验证码
     *
     * @param telephone  必填，手机号码
     * @param verifyType 验证码类型
     * @param sysKey     必填，加密字符串，客户端服务端约定一种密钥形式SC_Monkey_Code
     */
    public static void getPhoneCode(String telephone, int verifyType, String sysKey, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("tel", telephone);
        params.put("bz", verifyType);
        params.put("sysKey", sysKey);
        ApiHttpClient.post("v1/user/code", params, handler);
    }

    /**
     * 校验验证码
     *
     * @param sysKey 必填，验证码key
     * @param tel    必填，手机号码
     * @param code   必填，验证码
     */
    public static void verficateCode(String sysKey, String tel, String code, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("sysKey", sysKey);
        params.put("tel", tel);
        params.put("code", code);
        ApiHttpClient.post("v1/user/verficate", params, handler);
    }

    /**
     * 用户注册
     *
     * @param sysKey    必填，注册key
     * @param telephone 必填，手机号码
     * @param regType   必填，用户角色(1 车主 2技师 3 快修站)
     * @param realName  必填，真实姓名
     * @param password  必填，登录密码(MD5加密后)
     * @param region    选填，地区编码（详见国家地区编码）
     * @param address   选填，详细地址
     * @param handler   回调
     */
    public static void register(String sysKey, String telephone, int regType, String realName, String password, String region, String address, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("sysKey", sysKey);
        params.put("telephone", telephone);
        params.put("regType", regType);
        params.put("realName", realName);
        params.put("password", WkApi.md5(password));
        params.put("region", region);
        params.put("address", address);
        ApiHttpClient.post("v1/user/register", params, handler);
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
        params.put("userName", username);
        params.put("password", WkApi.md5(password));
        String sysKey = CyptoUtils.encode("SC_Monkey_Code", username);
        params.put("loginKey", sysKey);
        ApiHttpClient.post("v1/sso/login", params, handler);
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
        ApiHttpClient.post("v1/user/regToken", params, handler);
    }

    /**
     * 用户信息更新
     *
     * @param userRealname 选填，真实姓名
     * @param userName     选填，昵称
     * @param userQq       选填，QQ
     * @param userAddress  选填，详细地址
     * @param region       选填，区域
     * @param userSex      选填，性别
     * @param handler      必填，登录校验码(客户端服务端约定一种加密）
     */
    public static void updateUser(String userRealname, String userName, String userQq, String userAddress, String region, String userSex, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("userRealname", userRealname);
        params.put("userName", userName);
        params.put("userQq", userQq);
        params.put("userAddress", userAddress);
        params.put("region", region);
        params.put("userSex", userSex);
        ApiHttpClient.post("v1/user/update", params, handler);
    }

    /**
     * 切换用户角色
     *
     * @param role    必填，用户角色(1 车主 2技师 3 快修站)
     * @param handler 必填，登录校验码(客户端服务端约定一种加密）
     */
    public static void changeRole(String role, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("userRole", role);
        ApiHttpClient.post("v1/sso/changeRole", params, handler);
    }

    /**
     * 用户退出
     *
     * @param handler 必填，登录校验码(客户端服务端约定一种加密
     */
    public static void logout(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        ApiHttpClient.post("v1/sso/logout", params, handler);
    }

    /**
     * 修改密码for找回密码
     *
     * @param telephone 必填，手机号
     * @param newpwd    必填，新密码
     * @param verifykey 必填，验证码校验串
     */
    public static void resetPassword(String telephone, String newpwd, String verifykey, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("telephone", telephone);
        params.put("resetpwd", WkApi.md5(newpwd));
        params.put("sysKey", verifykey);
        ApiHttpClient.post("v1/user/resetPassword", params, handler);
    }

    /**
     * 修改密码
     *
     * @param newpwd 必填，新密码（MD5加密）
     */
    public static void password(String newpwd, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("newpwd", WkApi.md5(newpwd));
        ApiHttpClient.post("v1/user/password", params, handler);
    }

    /**
     * 自动登录
     *
     * @param token   必填，客户端保存的用户登录token
     * @param handler AsyncHttpResponseHandler
     */
    public static void regToken(String token, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("token", token);
        ApiHttpClient.post("v1/user/regToken", params, handler);
    }

    /**
     * 保持会话
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void keepalived(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        ApiHttpClient.post("v1/user/keepalived", params, handler);
    }

    /**
     * 定时上报位置
     *
     * @param lon
     * @param lat
     * @param handler
     */
    public static void uploadLonLat(Double lon, Double lat, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("longitude", lon);
        params.put("latitude", lat);
        ApiHttpClient.post("v1/user/locate", params, handler);
    }

    /**
     * 技师列表
     *
     * @param storeCarmodel    选填，店铺的服务车型编号
     * @param storegradeLevel  选填，技师级别 1-初级 2-中极 3-高级
     * @param storeSerivceType 选填，服务分类 MAINTENANCE-保养修护 CREPAIRS-汽车维修 BDECORATION-美容装饰 IALTERATION-安装改装 查询多个请用英文逗号隔开
     * @param storeTecType     选填，技师类别 MMAINTENANCE-机修 PAINTING-钣金喷漆 CAPACITANCE-电容 BMODIFIED-美容改装 查询多个请用英文逗号隔开
     * @param pageNum          选填，页码（获取第几信息，必须大于0，默认第一页）
     * @param pageSize         选填，每页记录数（必须0~2000之间，默认10条每页）
     * @param sortType         选填，排序方式（ desc或 asc），默认desc
     * @param sortName         选填，排序字段名称(0 ID 1技师修龄2技师级别 3技师真实姓名，默认ID)
     * @param longitude        选填，经度
     * @param latitude         选填，维度
     * @param storeName        选填，店铺名称
     * @param areaCode         选填，地区编码
     * @param handler          AsyncHttpResponseHandler
     */
    public static void getTechList(String storeCarmodel, String storegradeLevel, String storeSerivceType, String storeTecType,
                                   String sortType, String sortName, Double longitude, Double latitude, String storeName, String areaCode,
                                   int pageNum, int pageSize, AsyncHttpResponseHandler handler) {
        final RequestParams params = new RequestParams();
        params.put("storeCarmodel", storeCarmodel);
        params.put("storegradeLevel", storegradeLevel);
        params.put("storeSerivceType", storeSerivceType);
        params.put("storeTecType", storeTecType);
        params.put("sortType", sortType);
        params.put("sortName", sortName);
        params.put("longitude", longitude == null ? "" : longitude.toString());
        params.put("latitude", latitude == null ? "" : latitude.toString());
        params.put("storeName", storeName);
        params.put("areaCode", areaCode);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/technician/list", params, handler);
    }

    /**
     * 技师详情
     *
     * @param id      选填，技师ID
     * @param handler AsyncHttpResponseHandler
     */
    public static void getTechDetail(Long id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/technician/view", params, handler);
    }

    /**
     * 技师预约页面
     *
     * @param id      选填，技师ID
     * @param handler AsyncHttpResponseHandler
     */
    public static void getTechOrder(Long id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/technician/subList", params, handler);
    }

    /**
     * 技师预约提交
     */
    public static void getTechOrderSubmit(String storeServiceType, String storeCarModel, String orderMsg,
                                          long storeId, long orderAppointTimeStart, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("storeSerivceType", storeServiceType);
        params.put("storeCarmodel", storeCarModel);
        params.put("orderMsg", orderMsg);
        params.put("storeId", storeId);
        params.put("orderAppointTimeStart", orderAppointTimeStart);
        ApiHttpClient.post("v1/technician/order", params, handler);

    }

    /**
     * 查看收货地址列表
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void getAddressItem(String daddress, Long id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("daddress", daddress);
        if (id != 0L) {
            params.put("id", id);
        }
        ApiHttpClient.post("v1/person/viewAddress", params, handler);
    }

    /**
     * 收货地址列表
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void getAddressList(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        ApiHttpClient.post("v1/person/addressList", params, handler);
    }

    /**
     * 编辑收货地址
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void getAddressEdit(long id, String addressInfo, String addressMobile, String addressTelephone,
                                      String addressTruename, String addressZip, String areaId, String daddress, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("addressInfo", addressInfo);
        params.put("addressMobile", addressMobile);
        params.put("addressTelephone", addressTelephone);
        params.put("addressTruename", addressTruename);
        params.put("addressZip", addressZip);
        params.put("areaId", areaId);
        params.put("daddress", daddress);
        ApiHttpClient.post("v1/person/EditAddress", params, handler);
    }

    /**
     * 添加收货地址
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void getAddressAdd(String addressInfo, String addressMobile, String addressTelephone,
                                     String addressTruename, String addressZip, String areaId, String daddress, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("addressInfo", addressInfo);
        params.put("addressMobile", addressMobile);
        params.put("addressTelephone", addressTelephone);
        params.put("addressTruename", addressTruename);
        params.put("addressZip", addressZip);
        params.put("areaId", areaId);
        params.put("daddress", daddress);
        ApiHttpClient.post("v1/person/addAddress", params, handler);
    }

    /**
     * 删除收货地址
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void getAddressDelete(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/person/deleteAddress", params, handler);

    }

    /**
     * 快修站/工位 列表
     *
     * @param areaCode             选填，默认查所有 地区code 市级行政区域编码
     * @param orderNum             选填，接单数量
     * @param storeCarmodel        选填，车品牌编号
     * @param storeSerivceType     选填，服务分类 MAINTENANCE-保养修护 CREPAIRS-汽车维修 BDECORATION-美容装饰 IALTERATION-安装改装
     * @param storeTechnicianLevel 店铺等级1-初级，2-中级，3-高级
     * @param openPlace            选填，是否开启场地：0是关，1是开
     * @param storeName            选填，店铺名称
     * @param stationNum           选填，工位数
     * @param sortType             选填，排序方式（ desc或 asc），默认desc
     * @param sortName             选填，排序字段名称(1评价最高 order_num接单最多dist距离最近，默认ID)
     * @param longitude            选填，经度(当sortName为dist时为必填项)
     * @param latitude             选填，纬度(当sortName为dist时为必填项)
     * @param pageNum              选填，页码（获取第几信息，必须大于0，默认第一页）
     * @param pageSize             选填，每页记录数（必须1~100之间，默认10条每页）
     * @param handler              TextHttpResponseHandler
     */
    public static void getGarageList(String areaCode, String orderNum,String storeCarmodel, String storeSerivceType, String storeTechnicianLevel,
                                     String openPlace, String storeName, String stationNum, String sortType, String sortName,
                                     Double longitude, Double latitude, int pageNum, int pageSize, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("areaCode", areaCode);
        params.put("orderNum", orderNum);
        params.put("storeCarmodel", storeCarmodel);
        params.put("storeSerivceType", storeSerivceType);
        params.put("storeTechnicianLevel", storeTechnicianLevel);
        params.put("openPlace", openPlace);
        params.put("storeName", storeName);
        params.put("stationNum", stationNum);
        params.put("sortType", sortType);
        params.put("sortName", sortName);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/repair/list", params, handler);
    }

    /**
     * 快修站/工位 详情
     *
     * @param id      选填，技师ID
     * @param handler AsyncHttpResponseHandler
     */
    public static void getGarageDetail(Long id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/repair/view", params, handler);
    }

    /**
     * 快修站预约/工位预约
     *
     * @param id      选填，技师ID
     * @param handler AsyncHttpResponseHandler
     */
    public static void getGarageOrder(Long id, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/repair/sublist", params, handler);
    }

    /**
     * 快修站/工位预约提交
     *
     * @param storeSerivceType      快修站必填/工位非必填 服务分类 MAINTENANCE-保养修护 CREPAIRS-汽车维修 BDECORATION-美容装饰 IALTERATION-安装改装 添加多个请用英文逗号隔开
     * @param storeCarmodel         必填，预约车型
     * @param orderMsg              选填，描述
     * @param storeId               必填，快修站ID
     * @param orderAppointTimeStart 必填，预约时间
     * @param handler
     */
    public static void getGarageOrderSubmit(String status, String storeSerivceType, String storeCarmodel, String orderMsg, String storeId, String orderAppointTimeStart, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("status", status);
        params.put("storeSerivceType", storeSerivceType);
        params.put("storeCarmodel", storeCarmodel);
        params.put("orderMsg", orderMsg);
        params.put("storeId", storeId);
        params.put("orderAppointTimeStart", orderAppointTimeStart);
        ApiHttpClient.post("v1/repair/order", params, handler);
    }

    /**
     * 查看店铺
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void getStoreInfo(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        ApiHttpClient.post("v1/technician/storeInfo", params, handler);
    }

    /**
     * 店铺设置
     *
     * @param storeName    必填，店铺名称
     * @param storeAddress 必填，店铺地址
     * @param storeLng     选填，经度
     * @param storeLat     选填，纬度
     * @param specialBrand 多个品牌ID以英文逗号分割必填，专修品牌
     * @param storeInfo    必填，店铺描述
     * @param serviceType  必填，服务分类
     * @param storeTec     必填，店内技师
     * @param handler      AsyncHttpResponseHandler
     */
    public static void getStore(String storeName, String storeAreaCode, String storeAddress, String siteDesc,
                                String openPlace, String stationNum,
                                String storeLng,
                                String storeLat, String specialBrand,
                                String storeTec, String serviceType,
                                String storeInfo, String storeTelephone,
                                String storeBankaccount, String storeAccountname, String storeTecType,
                                String storeBankname, String storeTimeDesc, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("storeName", storeName);
        params.put("storeAreaCode", storeAreaCode);
        params.put("storeAddress", storeAddress);
        params.put("siteDesc", siteDesc);
        params.put("openPlace", openPlace);
        params.put("stationNum", stationNum);
        params.put("storeLng", storeLng);
        params.put("storeLat", storeLat);
        params.put("specialBrand", specialBrand);
        params.put("storeInfo", storeInfo);
        params.put("serviceType", serviceType);
        params.put("storeTec", storeTec);
        params.put("storeTelephone", storeTelephone);
        params.put("storeBankaccount", storeBankaccount);
        params.put("storeAccountname", storeAccountname);
        params.put("storeTecType", storeTecType);
        params.put("storeBankname", storeBankname);
        params.put("storeTimeDesc", storeTimeDesc);
        ApiHttpClient.post("v1/technician/store", params, handler);
    }


    /**
     * 上传店铺相关图片
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void getUploadStoreImg(String type, File portrait, AsyncHttpResponseHandler handler) throws
            FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("portrait", portrait);
        ApiHttpClient.post("v1/technician/uploadStoreImg", params, handler);
    }

    /**
     * 个人头像
     *
     * @param userId  选填，个人ID
     * @param handler AsyncHttpResponseHandler
     */
    public static void getMyInformationHead(Long userId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        ApiHttpClient.get("v1/user/headImg", params, handler);
    }

    /**
     *  person_info
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void getMyInformation(AsyncHttpResponseHandler handler) {
        ApiHttpClient.post("v1/user/view?v=" + new Date().getTime(), handler);
    }

    /**
     * 配件商列表
     *
     * @param storeName 店铺名称 query	string
     * @param longitude 经度 query	string
     * @param latitude  纬度 query	string
     * @param sortType  排序方式（ desc或 asc），默认desc string
     * @param sortName  排序字段名称(1评价最高 order_num接单最多dist距离最近，默认id) string
     * @param brandId   车品牌Id
     * @param pageNum   1当前页，默认为第一页 string
     * @param pageSize  10 每页显示数 string
     * @param handler
     */

    public static void getGoodsStoreList(String storeName, String longitude, String latitude,
                                         String sortType, String sortName, String brandId, int pageNum, int pageSize, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("storeName", storeName);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("sortType", sortType);
        params.put("sortName", sortName);
        params.put("brandId", brandId);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/parts/storeList", params, handler);
    }

    /**
     * 配件列表
     *
     * @param goodsBrandId   选填，商品品牌ID
     * @param brandIds       选填，车品牌ID
     * @param usergoodsclass 选填，自定义分类编号
     * @param cateId         选填，类别编号
     * @param storeId        选填，店铺的编号
     * @param sortType       选填，排序方式（ desc或 asc），默认desc
     * @param sortName       选填，排序字段名称(1评价最高 2价格，默认ID)
     * @param xzqhCode       选填，区域ID(详见国家地区编码）
     * @param goodsName      选填，配件名·配件OE号
     * @param pageNum        选填，页码（获取第几信息，必须大于0，默认第一页）
     * @param pageSize       选填，每页记录数（必须0~100之间，默认10条每页）
     * @param handler        AsyncHttpResponseHandler
     */
    public static void getGoodsList(String goodsBrandId, String brandIds, String usergoodsclass, String cateId,
                                    String storeId, String sortType, String sortName, String xzqhCode, String goodsName,
                                    int pageNum, int pageSize, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("goodsBrandId", goodsBrandId);
        params.put("brandIds", brandIds);
        params.put("usergoodsclass", usergoodsclass);
        params.put("cateId", cateId);
        params.put("storeId", storeId);
        params.put("sortType", sortType);
        params.put("sortName", sortName);
        params.put("xzqhCode", xzqhCode);
        params.put("goodsName", goodsName);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/parts/list", params, handler);
    }

    /**
     * 商品类别
     *
     * @param parentId  选填，父节点编号(默认全部)
     * @param id        选填，商品类别编号
     * @param className 商品类别名称
     * @param level     商品级别
     * @param handler   AsyncHttpResponseHandler
     */
    public static void getGoodsType(String parentId, String id, String className, String level, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("parentId", parentId);
        params.put("id", id);
        params.put("className", className);
        params.put("level", level);
        ApiHttpClient.post("v1/common/cateNodes", params, handler);
    }

    /**
     * 商品品牌
     *
     * @param classId 选填，分类编号，默认全部
     * @param handler AsyncHttpResponseHandler
     */
    public static void getGoodsBrand(String classId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("classId", classId);
        ApiHttpClient.post("v1/common/goodsBrand", params, handler);
    }

    /**
     * 配件详情
     *
     * @param id      必填，商品ID
     * @param handler AsyncHttpResponseHandler
     */
    public static void getGoodsDetail(Long id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/parts/view", params, handler);
    }

    /**
     * 车品牌列表
     *
     * @param id          选填，父节点Code编号，默认全部
     * @param brandName   选填，品牌名称
     * @param brandLetter 选填，品牌首字母
     * @param brandType   选填，1品牌 2车型经销商
     * @param handler     AsyncHttpResponseHandler
     */
    public static void getCarBrandNameAndIcon(String id, String brandName, String brandLetter, String brandType, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("brandName", brandName);
        params.put("brandLetter", brandLetter);
        params.put("brandType", brandType);
        ApiHttpClient.post("v1/common/brandNodes", params, handler);
    }

    /**
     * 获取车型车系列表
     *
     * @param id                   选填，父节点Code编号，默认全部
     * @param carbrandName         选填，品牌/车型经销商/车系/车型名称
     * @param carbrandYear         选填，年份
     * @param carbrandDisplacement 选填，排量
     * @param carbrandBrandType    选填，车型经销商
     * @param type                 选填，查询类型 1:车系，2：排量，3：年份
     */
    public static void getCarmodel(String id, String carbrandName, String carbrandYear, String carbrandDisplacement, String carbrandBrandType, String type, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("carbrandName", carbrandName);
        params.put("carbrandYear", carbrandYear);
        params.put("carbrandDisplacement", carbrandDisplacement);
        params.put("carbrandBrandType", carbrandBrandType);
        params.put("type", type);
        ApiHttpClient.post("v1/common/carModel", params, handler);
    }

    /**
     * 获取用户车型数据，通过carmodel唯一id指定到一条数据
     *
     * @param id 车辆车型唯一id
     */
    public static void getUserCarByModelId(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/common/carModelByID", params, handler);
    }

    /**
     * 获取用户车辆
     *
     * @param userBrandId 车辆唯一id，对应用户表 brand_id
     */
    public static void getUserCarByUserBrandId(String userBrandId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("userBrandId", userBrandId);
        ApiHttpClient.post("v1/common/carModelByUserBrandId", params, handler);
    }

    /**
     * 用户车辆列表
     */
    public static void getUserCarmodelList(AsyncHttpResponseHandler handler) {
        ApiHttpClient.post("v1/person/userCarmodelList", handler);
    }

    /**
     * 配件店铺简介查看
     *
     * @param id 必填，店铺编号
     */
    public static void viewStoreBriefIntroduce(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/parts/storeView", params, handler);
    }

    /**
     * 自定义分类
     *
     * @param id       选填，自定义类别编号
     * @param parentId 选填，父节点编号(默认全部)
     */
    public static void getUserGoodsClass(String parentId, String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("parentId", parentId);
        ApiHttpClient.post("v1/common/userGoodsClass", params, handler);
    }

    /**
     * 服务需求列表
     *
     * @param orderState 选填，需求状态 0-新发布,1-已接单,2-已取消,3-已关闭
     * @param pageNum    选填，页码（获取第几信息，必须大于0，默认第一页）
     * @param pageSize   选填，每页记录数（必须0~2000之间，默认10条每页）
     * @param handler    AsyncHttpResponseHandler
     */
    public static void getServiceRequirementList(String orderState, int pageNum, int pageSize,
                                                 AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("orderState", orderState);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/demand/serviceList", params, handler);
    }

    /**
     * 附近所有的需求
     *
     * @param longitude 选填，经度(当sortName为dist时为必填项)
     * @param latitude  选填，纬度(当sortName为dist时为必填项)
     * @param pageNum   选填，页码（获取第几信息，必须大于0，默认第一页）
     * @param pageSize  选填，每页记录数（必须0~100之间，默认10条每页）
     * @param handler   AsyncHttpResponseHandler
     */
    public static void getAllNearServiceRequirementList(Double longitude, Double latitude, int pageNum, int pageSize,
                                                        AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        if (longitude != null && latitude != null) {
            params.put("longitude", longitude.toString());
            params.put("latitude", latitude.toString());
        } else {
            params.put("longitude", "0");
            params.put("latitude", "0");
        }
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/demand/allList", params, handler);
    }

    /**
     * 服务需求详情
     *
     * @param id      必填，服务需求编号
     * @param handler AsyncHttpResponseHandler
     */
    public static void getServiceRequirementDetail(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/demand/serviceView", params, handler);
    }

    /**
     * 发布服务需求
     *
     * @param demandDesc       选填，需求描述选填，需求描述
     * @param demandType       必填，需求类型:0-配件需求，1-服务需求
     * @param demandAddress    必填，需求地址
     * @param serivceType      必填，服务类别
     * @param carModelId       必填，需求车型id
     * @param demandUsername   必填，用户姓名
     * @param positionLat      必填，纬度
     * @param demandPhone      必填，需求人电话号码
     * @param positionLng      必填，经度
     * @param appointTimeStart 必填，预约时间
     * @throws FileNotFoundException
     */
    public static void getServiceRequirementSubmit(String demandDesc, String demandType, String demandAddress,
                                                   String carModelId, String serivceType, String demandUsername, String demandPhone,
                                                   String positionLat, String positionLng, String appointTimeStart, File demandImg1, File demandImg2, File demandImg3, TextHttpResponseHandler handler)
            throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("demandDesc", demandDesc);
        params.put("demandType", demandType);
        params.put("demandAddress", demandAddress);
        params.put("carModelId", carModelId);
        params.put("serivceType", serivceType);
        params.put("demandUsername", demandUsername);
        params.put("demandPhone", demandPhone);
        params.put("positionLat", StringUtils.isEmpty(positionLat) ? "0" : positionLat);
        params.put("positionLng", StringUtils.isEmpty(positionLng) ? "0" : positionLng);
        params.put("appointTimeStart", StringUtils.isEmpty(appointTimeStart) ? "时间未填" : appointTimeStart);
        params.put("demandImg1", demandImg1);
        if (demandImg2 != null) {
            params.put("demandImg2", demandImg2);
        }
        if (demandImg3 != null) {
            params.put("demandImg3", demandImg3);
        }
        ApiHttpClient.post("v1/demand/createServiceDemand", params, handler);
    }

    /**
     * 技师抢单
     *
     * @param id 必填需求编号
     */
    public static void grabOrder(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/demand/demandOrder", params, handler);
    }

    /**
     * 取消服务需求
     *
     * @param id 必填需求编号
     */
    public static void deleteDemand(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/demand/deleteDemand", params, handler);
    }

    /**
     * 配件需求列表
     *
     * @param orderState 选填，需求状态 1-新发布（待报价）,2-待确认（已报价）,3-已确认
     * @param pageNum    选填，页码（获取第几信息，必须大于0，默认第一页）
     * @param pageSize   选填，每页记录数（必须0~2000之间，默认10条每页）
     * @param handler    AsyncHttpResponseHandler
     */
    public static void getPartRequirementList(Integer orderState, int pageNum, int pageSize,
                                              AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("orderState", orderState);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/demand/partList", params, handler);
    }

    /**
     * 创建配件需求（第一步）
     *
     * @param demandDesc
     * @param demandAddress
     * @param areaId
     * @param carModelId
     * @param positionLng
     * @param positionLat
     * @param handler
     */
    public static void createProductsDemand(String demandDesc, String demandAddress, String areaId,
                                            String carModelId, Double positionLng, Double positionLat,
                                            File demandImg1, File demandImg2, File demandImg3,
                                            AsyncHttpResponseHandler handler) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("demandDesc", demandDesc);
        params.put("demandAddress", demandAddress);
        params.put("areaId", areaId);
        params.put("carModelId", carModelId);
        params.put("positionLng", positionLng);
        params.put("positionLat", positionLat);
        params.put("demandImg1", demandImg1);
        if (demandImg2 != null) {
            params.put("demandImg2", demandImg2);
        }
        if (demandImg3 != null) {
            params.put("demandImg3", demandImg3);
        }
        ApiHttpClient.post("v1/demand/createProductsDemand", params, handler);
    }

    /**
     * 补充配件需求
     *
     * @param inquirysheetId
     * @param detailsJson
     * @param handler
     */
    public static void supplementProductsDemand(String inquirysheetId, String detailsJson, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("inquirysheetId", inquirysheetId);
        params.put("detailsJson", detailsJson);
        ApiHttpClient.post("v1/demand/supplementProductsDemand", params, handler);

    }

    /**
     * 发布配件需求
     *
     * @param inquirysheetId
     * @param storeids
     * @param handler
     */
    public static void putProductsDemand(String inquirysheetId, String storeids, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("inquirysheetId", inquirysheetId);
        params.put("storeIds", storeids);
        ApiHttpClient.post("v1/demand/putProductsDemand", params, handler);

    }

    /**
     * 查看配件需求
     *
     * @param demandId
     * @param handler
     */
    public static void viewProductsDemand(String demandId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", demandId);
        ApiHttpClient.post("v1/demand/viewProductsDemand", params, handler);
    }

    /**
     * 工位订单列表
     *
     * @param orderStatus 选填，订单状态:已预约20 待结算30 待评价40 已完成 50 (等待买家评价)
     * @param pageNum
     * @param pageSize
     * @param handler
     */
    public static void getPlaceOrderList(Integer orderStatus, int pageNum, int pageSize, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("orderStatus", orderStatus);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/owner/placeOrderList", params, handler);
    }

    public static String goodsStatus_Obligations = "10";//待付款
    public static String goodsStatus_Shipped = "20";//待发货
    public static String goodsStatus_Receipt = "30";//待收货
    public static String goodsStatus_Evaluated = "40";//待评价
    public static String goodsStatus_Already_Evaluated = "50";//已评价
    public static String goodsStatus_Cancel = "0";//取消
    public static String goodsStatus_Del = "-1";//删除

    /**
     * 车主配件订单列表
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void getOwnerPartsOrder(String orderStatus, int pageNum, int pageSize, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("orderStatus", orderStatus);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/owner/ownerPartsOrder", params, handler);
    }

    /**
     * 配件订单详情
     *
     * @param id      必填，订单编号
     * @param handler AsyncHttpResponseHandler
     */
    public static void getPartsOrderView(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/owner/partsOrderView", params, handler);
    }

    /**
     * 添加购物车
     *
     * @param storeId 店铺编号
     * @param id      商品编号
     * @param count   商品数量
     * @param status  状态  立即购买1
     * @param handler TextHttpResponseHandler
     */
    public static void addGoodsCar(String storeId, String id, String count, String status, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("storeId", storeId);
        params.put("id", id);
        params.put("count", count);
        if (status.equals("1")) {
            params.put("status", status);
        }
        ApiHttpClient.post("v1/parts/goodsCar", params, handler);
    }

    /**
     * 更新订单状态
     *
     * @param id          必填，订单编号
     * @param orderStatus 必填，订单状态
     * @param handler     AsyncHttpResponseHandler
     */
    public static void updateServiceOrder(String id, String orderStatus, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("orderStatus", orderStatus);
        ApiHttpClient.post("v1/owner/updateServiceOrder", params, handler);
    }

    /**
     * 确认收货
     *
     * @param orderId 必填，订单编号
     * @param handler AsyncHttpResponseHandler
     */
    public static void confirmOrder(String orderId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("orderId", orderId);
        ApiHttpClient.post("v1/person/confirmOrder", params, handler);
    }

    /**
     * 更新购物车数量
     *
     * @param id      必填，购物车编号
     * @param count   必填，商品数量
     * @param handler TextHttpResponseHandler
     */
    public static void updateGoodsCar(String id, String count, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("count", count);
        ApiHttpClient.post("v1/parts/updateGoodsCar", params, handler);
    }

    /**
     * 立即购买
     *
     * @param id      必填, 商品ID
     * @param storeId 必填，店铺编号
     * @param count   必填，商品数量
     * @param handler TextHttpResponseHandler
     */

    public static void buyGoods(String storeId, String id, String count, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("storeId", storeId);
        params.put("id", id);
        params.put("count", count);
        ApiHttpClient.post("v1/parts/order", params, handler);
    }

    /**
     * 点击结算
     *
     * @param ids     必填, 加入购物车订单ID字符串id之间英文逗号隔开
     * @param handler TextHttpResponseHandler
     */

    public static void buyShoppingCar(String ids, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("ids", ids);
        ApiHttpClient.post("v1/parts/balance", params, handler);
    }

    /**
     * 配件提交订单
     *
     * @param orderJson 必填, 加入购物车订单ID字符串id之间英文逗号隔开
     * @param handler   TextHttpResponseHandler
     */

    public static void carOrder(String orderJson, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("orderJson", orderJson);
        ApiHttpClient.post("v1/parts/carOrder", params, handler);
    }

    /**
     * 查看购物车
     *
     * @param pageNum  选填，页码（获取第几信息，必须大于0，默认第一页）
     * @param pageSize 选填，每页记录数（必须0~100之间，默认10条每页）
     * @param handler  TextHttpResponseHandler
     */
    public static void getShoppingCar(int pageNum, int pageSize, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/parts/goodsCarList", params, handler);
    }

    /**
     * 删除购物车
     *
     * @param ids     购物车编号
     * @param handler TextHttpResponseHandler
     */
    public static void deleteShoppingCar(String ids, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("ids", ids);
        ApiHttpClient.post("v1/parts/goodsCardelete", params, handler);
    }

    public static final int CATALOG_BANNER_NEWS = 1; // 新闻Banner
    public static final int CATALOG_BANNER_EVENT = 2; // 活动Banner

    /**
     * 首页广告
     *
     * @param handler AsyncHttpResponseHandler
     */
    public static void getBannerList(AsyncHttpResponseHandler handler) {
        ApiHttpClient.post("v1/common/advertList", handler);
    }

    /**
     * 上传头像
     *
     * @param uid
     * @param portrait
     * @param handler
     * @throws FileNotFoundException
     */
    public static void updatePortrait(int uid, File portrait,
                                      AsyncHttpResponseHandler handler) throws
            FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("portrait", portrait);
        ApiHttpClient.post("v1/user/uploadHeadImg", params, handler);
    }

    /**
     * 服务订单列表
     *
     * @param orderStatus 选填，选填，订单状态 已取消-0  待接单-10  待服务-20  服务中-30  待结算-40  待评价-50 已完成-70
     * @param pageNum     选填，页码（获取第几信息，必须大于0，默认第一页）
     * @param pageSize    选填，每页记录数（必须0~100之间，默认10条每页）
     * @param handler     AsyncHttpResponseHandler
     */
    public static void getServiceOrderList(Integer orderStatus, int pageNum, int pageSize, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("orderStatus", orderStatus);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/owner/serviceOrder", params, handler);
    }

    /**
     * 服务订单详情
     *
     * @param id      订单编号
     * @param handler
     */
    public static void getServiceOrderDetail(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/owner/serviceOrderView", params, handler);
    }

    /**
     * 工位订单详情
     *
     * @param id      订单编号
     * @param handler
     */
    public static void getPlaceViewDetail(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/owner/getPlaceView", params, handler);
    }

    /**
     * 技师报价
     *
     * @param quotes       报价单JsonString
     * @param techOfferImg
     * @param handler
     * @throws FileNotFoundException
     */
    public static void techOffer(String quotes, File techOfferImg, AsyncHttpResponseHandler handler) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("quotes", quotes);
        params.put("techOfferImg", techOfferImg);
        ApiHttpClient.post("v1/owner/tecOffer", params, handler);
    }

    /**
     * 技师结算
     *
     * @param quotes  报价单JsonString
     * @param handler
     * @throws FileNotFoundException
     */
    public static void techOfferSSettle(String quotes, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("quotes", quotes);
        ApiHttpClient.post("v1/owner/tecOffer", params, handler);
    }

    /**
     * 报价单详情
     *
     * @param id
     * @param type    1-报价单 2-结算单
     * @param handler
     */
    public static void repairReport(String id, String type, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("type", type);
        ApiHttpClient.post("v1/owner/repairReport", params, handler);
    }

    /**
     * 上传服务前照片
     *
     * @param handler
     */
    public static void uploadServiceOrderImagePre(Long id, File[] imgBefore, AsyncHttpResponseHandler handler) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("id", id);
        for (int i = 0; i < imgBefore.length; i++) {
            params.put("ImageBefore" + i, imgBefore[i]);
        }
        ApiHttpClient.post("v1/owner/uploadHeadImagePre", params, handler);
    }

    /**
     * 上传服务后照片
     *
     * @param handler
     */
    public static void uploadServiceOrderImagePost(Long id, File[] imgAfter, AsyncHttpResponseHandler handler) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("id", id);
        for (int i = 0; i < imgAfter.length; i++) {
            params.put("ImageAfter" + i, imgAfter[i]);
        }
        ApiHttpClient.post("v1/owner/uploadHeadImagePost", params, handler);
    }

    /**
     * 获取签名信息
     *
     * @param orderId  必填，订单编号
     * @param subject  必填，订单名称
     * @param totalFee 必填，付款金额,只能为double型字符串
     * @param goodInfo 选填，商品描述
     */
    public static void getAlipaySignInfo(String orderId, String subject, String totalFee, String goodInfo, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("orderId", orderId);
        params.put("subject", subject);
        params.put("totalFee", totalFee);
        params.put("goodInfo", goodInfo);
        ApiHttpClient.post("v1/person/aliPay", params, handler);
    }

    /**
     * 验证签名结果
     *
     * @param content    必填，签名的原始字符串( 提取alipay_trade_app_pay_response字段值)
     * @param sign       必填，签名结果
     * @param outTradeNo 必填，唯一订单号
     * @param totalFee   必填，商品金额 只能为double型字符串
     */
    public static void verifyRsa(String content, String sign, String outTradeNo, String totalFee, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("content", content);
        params.put("sign", sign);
        params.put("outTradeNo", outTradeNo);
        params.put("totalFee", totalFee);
        ApiHttpClient.post("v1/person/verifyRsa", params, handler);
    }

    /**
     * 新增充值记录
     *
     * @param rechargeAmount   必填，预存款金额
     * @param rechargePaySatus 必填，支付状态:0等待支付，1等待审核，2支付完成，3关闭支付
     * @param rechargePayment  必填，支付标识（从数据字典取alipay支付宝，chinabank网银在线，tenpay财付通，bill快钱，outline线下支付）
     */
    public static void addRechargeRecord(String rechargeAmount, String rechargePaySatus, String rechargePayment, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("rechargeAmount", rechargeAmount);
        params.put("rechargePaySatus", rechargePaySatus);
        params.put("rechargePayment", rechargePayment);
        ApiHttpClient.post("v1/person/addRecharge", params, handler);

    }

    /**
     * 更新充值记录
     *
     * @param rechargeSn       必填，预存款编号
     * @param rechargePaySatus 必填，必填，支付状态:0等待支付，1等待审核，2支付完成，3关闭支付
     * @param consumeSn        选填，交易号(如该交易在支付宝系统中的交易流水号)
     */
    public static void updateRechargeRecord(String rechargeSn, String rechargePaySatus, String consumeSn, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("rechargeSn", rechargeSn);
        params.put("rechargePaySatus", rechargePaySatus);
        params.put("consumeSn", consumeSn);
        ApiHttpClient.post("v1/person/updateRecharge", params, handler);
    }

    /**
     * 查看充值记录
     *
     * @param id 必填，预存款编号
     */
    public static void viewRechargeRecord(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/person/viewRecharge", params, handler);
    }

    /**
     * 新增提取记录
     *
     * @param withdrawAmount   必填，提现金额
     * @param withdrawUsername 必填，账户名称
     * @param withdrawBank     必填，开户银行
     * @param withdrawAccount  必填，银行账户
     * @param withdrawInfo     选填，提现备注
     * @param withdrawPayment  必填，支付标识
     */
    public static void addWithdrawRecord(String withdrawAmount, String withdrawUsername, String withdrawBank, String withdrawAccount, String withdrawInfo, String withdrawPayment, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("withdrawAmount", withdrawAmount);
        params.put("withdrawUsername", withdrawUsername);
        params.put("withdrawBank", withdrawBank);
        params.put("withdrawAccount", withdrawAccount);
        params.put("withdrawInfo", withdrawInfo);
        params.put("withdrawPayment", withdrawPayment);
        ApiHttpClient.post("v1/person/addWithdraw", params, handler);
    }

    /**
     * 查看提取记录
     *
     * @param withdrawSn 必填，提现编号
     */
    public static void viewWithdrawRecord(String withdrawSn, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("withdrawSn", withdrawSn);
        ApiHttpClient.post("v1/person/viewWithdraw", params, handler);
    }

    /**
     * 新增消费记录
     *
     * @param consumeAmount 必填，消费金额
     * @param consumeInfo   选填，消费内容
     * @param orderId       必填，订单号
     */
    public static void addConsumeRecord(String consumeAmount, String consumeInfo, String orderId, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("consumeAmount", consumeAmount);
        params.put("consumeInfo", consumeInfo);
        params.put("orderId", orderId);
        ApiHttpClient.post("v1/person/addConsume", params, handler);
    }

    public static String addPayRecord_consumeMode_recharge = "0";//充值
    public static String addPayRecord_consumeMode_withdraw = "1";//提现
    public static String addPayRecord_consumeMode_refunds = "2";//退款
    public static String addPayRecord_consumeMode_service = "3";//服务订单费
    public static String addPayRecord_consumeMode_workplace = "4";//工位订单费
    public static String addPayRecord_consumeMode_goods = "5";//配件订单费

    /**
     * 新增余额支付
     *
     * @param orderArrayJson 必填，消费金额,订单id,消费类型
     */
    public static void addPayRecord(String orderArrayJson, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("orderArrayJson", orderArrayJson);
        ApiHttpClient.post("v1/person/addPay", params, handler);
    }

    /**
     * 收支明细列表
     *
     * @param consumeCost 消费标识(0支出 1收入,默认全部)
     * @param pageNum     选填，页码（获取第几信息，必须大于0，默认第一页）
     * @param pageSize    选填，每页记录数（必须1~100之间，默认10条每页）
     */
    public static void getPayList(String consumeCost, int pageNum, int pageSize, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("consumeCost", consumeCost);
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/person/payList", params, handler);
    }

    /**
     * 冻结订单列表
     */
    public static void getFreezeOrderList(int pageNum, int pageSize,AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        ApiHttpClient.post("v1/user/freezeOrderList", handler);
    }

    /**
     * 获取用户的开户行
     *
     * @param bankCardNumber 必填，银行卡号
     */
    public static void getUserBankName(String bankCardNumber, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("bankCardNumber", bankCardNumber);
        ApiHttpClient.post("v1/person/getBankName", params, handler);
    }

    /**
     * 绑定银行卡
     *
     * @param realname       必填，真实姓名
     * @param bankCardNumber 必填，银行卡号
     * @param bankName       必填，开户行名称
     * @param idCard         必填，开户行名称
     * @param phoneNumber    必填，电话号码
     */
    public static void addUserBankCard(String realname, String bankCardNumber, String bankName, String idCard, String phoneNumber, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("realname", realname);
        params.put("bankCardNumber", bankCardNumber);
        params.put("bankName", bankName);
        params.put("idCard", idCard);
        params.put("phoneNumber", phoneNumber);
        ApiHttpClient.post("v1/person/addUserBank", params, handler);
    }

    /**
     * 查看用户绑定的银行卡列表
     */
    public static void getUserBankList(AsyncHttpResponseHandler handler) {
        ApiHttpClient.post("v1/person/userBankList", handler);
    }

    /**
     * 删除用户绑定的银行卡
     *
     * @param id 必填，银行卡编号
     */
    public static void deleteUserBank(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/person/deleteUserBank", params, handler);
    }

    /**
     * 获取附件图片
     *
     * @param id 必填，附件编号
     */
    public static void getPartImg(String id, AsyncHttpResponseHandler handler) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.get("v1/demand/demangImg", params, handler);
    }

    /**
     * 添加车辆
     *
     * @param carmodelId    必填，车型ID（shopping_car_model）
     * @param plate         选填，车牌如：皖A12345
     * @param engineNumber  选填，发动机号
     * @param chassisNumber 必填，车发动机号
     * @param mileage       选填，公里数
     * @param roadTime      选填，上路时间
     * @param isDefault     选填，1 代表默认  0为一般
     */
    public static void doAddCar(String carmodelId, String plate, String engineNumber, String chassisNumber,
                                String mileage, String roadTime, String isDefault, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("carmodelId", carmodelId);
        params.put("plate", plate);
        params.put("engineNumber", engineNumber);
        params.put("chassisNumber", chassisNumber);
        params.put("mileage", mileage);
        params.put("roadTime", roadTime);
        params.put("isDefault", isDefault);
        ApiHttpClient.post("v1/person/addUserCarModel", params, handler);
    }

    /**
     * 编辑用户车辆
     * <p>
     * 可用于设置用户默认车辆，带id，和isDefault=1
     * </p>
     *
     * @param id            必填，用户车辆ID
     * @param carmodelId    必填，车型ID（shopping_car_model）
     * @param plate         选填，车牌如：皖A12345
     * @param engineNumber  选填，发动机号
     * @param chassisNumber 必填，车发动机号
     * @param mileage       选填，公里数
     * @param roadTime      选填，上路时间
     * @param isDefault     选填，1 代表默认  0为一般
     */
    public static void doEditCar(String id, String carmodelId, String plate, String engineNumber, String chassisNumber,
                                 String mileage, String roadTime, String isDefault, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("carmodelId", carmodelId);
        params.put("plate", plate);
        params.put("engineNumber", engineNumber);
        params.put("chassisNumber", chassisNumber);
        params.put("mileage", mileage);
        params.put("roadTime", roadTime);
        params.put("isDefault", isDefault);
        ApiHttpClient.post("v1/person/editUserCarModel", params, handler);
    }

    /**
     * 查看用户车辆
     *
     * @param isDefault 必填，是否默认(1 代表默认 0为一般)
     * @param id        选填，用户车辆编号
     */
    public static void doViewUserCar(String isDefault, String id, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("isDefault", isDefault);
        params.put("id", id);
        ApiHttpClient.post("v1/person/viewUserCarmodel", params, handler);
    }

    /**
     * 删除用户车辆
     *
     * @param id 用户车辆ID
     */
    public static void doDelCar(String id, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        ApiHttpClient.post("v1/person/deleteUserCarmodel", params, handler);
    }

    /**
     * 附件图片地址
     *
     * @param id
     * @return
     */
    public static String getPartImgUrl(Long id) {
        return getPartImgUrl(String.valueOf(id));
    }

    /**
     * 附件图片地址
     *
     * @param id
     * @return
     */
    public static String getPartImgUrl(String id) {
        StringBuffer sb = new StringBuffer(ApiHttpClient.getAbsoluteApiUrl("v1/demand/demangImg"));
        if (!TextUtils.isEmpty(id)) {
            sb.append("?id=" + id);
        }
        return String.valueOf(sb);
    }

    /**
     * 用户头像地址
     *
     * @param userId
     * @return
     */
    public static String getUserPortraitImgUrl(Long userId, boolean isLarge) {
        return getUserPortraitImgUrl(String.valueOf(userId), isLarge);
    }

    /**
     * 用户头像地址
     *
     * @param userId
     * @return
     */
    public static String getUserPortraitImgUrl(String userId, boolean isLarge) {
        StringBuffer sb = new StringBuffer(ApiHttpClient.getAbsoluteApiUrl("v1/user/headImg"));
        if (!TextUtils.isEmpty(userId)) {
            sb.append("?userId=" + userId);
            if (isLarge) {
                sb.append("&picSize=normal&time=" + new Date().getTime());
            }
        }
        return String.valueOf(sb);
    }

    /**
     * 数据字典取值
     *
     * @param classCode 必填
     */
    public static void getDataFromDict(String classCode, AsyncHttpResponseHandler handler) {
        RequestParams param = new RequestParams();
        param.put("classCode", classCode);
        ApiHttpClient.post("v1/secu/dictData", param, handler);
    }

    public static void checkUpdate(AsyncHttpResponseHandler handler) {
        ApiHttpClient.get("v1/common/versionInfo", handler);
    }

    /**
     * 分站列表
     *
     * @param id 选填，区域编号主键
     */
    public static void getSubstation(String id, AsyncHttpResponseHandler handler) {
        RequestParams param = new RequestParams();
        param.put("id", id);
        ApiHttpClient.post("v1/common/websiteList", param, handler);
    }

    /**
     * 地区列表
     *
     * @param xzqhCode 选填，父节点Code编号，默认全部
     */
    public static void getArea(String xzqhCode, AsyncHttpResponseHandler handler) {
        RequestParams param = new RequestParams();
        param.put("xzqhCode", xzqhCode);
        ApiHttpClient.post("v1/common/areaNodes", param, handler);
    }

    /**
     * 根据收货地址计算运费
     *
     * @param calculateArray 计算运费jsonarray字符串【例：calculateArray:[{"goodsArray":[{"goodsIds":"3","goodsCount":"2"}],"addressCode":"340121","storeId":"1"}]
     * @param handler
     */
    public static void getPriceByAddress(String calculateArray, AsyncHttpResponseHandler handler) {
        RequestParams param = new RequestParams();
        param.put("calculateArray", calculateArray);
        ApiHttpClient.post("v1/parts/postPriceByAddress", param, handler);
    }

    /**
     * 更新百度云推送用户通道表示
     * @param userChannelId
     * @param handler
     */
    public static void updateBaiDuYun(String userChannelId, String yunUserId, AsyncHttpResponseHandler handler) {
        RequestParams param = new RequestParams();
        param.put("userChannelId", userChannelId);
        param.put("yunUserId", yunUserId);
        param.put("platType", 3);//3:Android,4:iOS
        ApiHttpClient.post("v1/user/updateBaiDuYun", param, handler);
    }
}
