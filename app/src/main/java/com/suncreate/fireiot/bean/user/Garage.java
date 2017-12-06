package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by zhangjinguo on 2016/11/24.快修站
 */

public class Garage implements Serializable {
    /**快修站ID**/
    private String id;
    /**头像**/
    private String storeLogoId;
    /**名称**/
    private String storeName;
    /**纬度**/
    private String storeLat;
    /**经度**/
    private String storeLng;
    /**距离**/
    private String dist;
    /**服务车型名称**/
    private String storeCarmodel;
    /**服务品牌ICON**/
    private String brandIcon;
    /**状态**/
    private String storeStatus;
    /**快修站等级**/
    private String storeTechnicianLevel;
    /**地址**/
    private String storeAddress;
    /**描述**/
    private String storeInfo;
    /**服务分**/
    private String storeServicePoint;
    /**发货分**/
    private String storeShipPoint;
    /**描述分**/
    private String storeDescriptionPoint;
    /**服务类别 MAINTENANCE-保养修护 CREPAIRS-汽车维修 BDECORATION-美容装饰 IALTERATION-安装改装 添加多个请用英文逗号隔开**/
    private String storeSerivceType;
    /**店内技师**/
    private String storeTec;
    /**是否开启场地：0是关，1是开**/
    private String openPlace;
    /**营业时间说明**/
    private String storeTimeDesc;
    /**参考费用**/
    private String refCost;
    /**工位数**/
    private String stationNum;
    /**接单数**/
    private String orderCount;
    /**快修站电话**/
    private String storeTelephone;
    /**身份认证 0未认证1认证**/
    private String storeCardApprove;
    /**资格认证 0未认证1认证**/
    private String storeRealstoreApprove;
    /**工位图片**/
    private String jsRecommond;
    /**工位描述**/
    private String siteDesc;

    public String getSiteDesc() {
        return siteDesc;
    }

    public void setSiteDesc(String siteDesc) {
        this.siteDesc = siteDesc;
    }

    public String getJsRecommond() {
        return jsRecommond;
    }

    public void setJsRecommond(String jsRecommond) {
        this.jsRecommond = jsRecommond;
    }

    public String getStoreCardApprove() {
        return storeCardApprove;
    }

    public void setStoreCardApprove(String storeCardApprove) {
        this.storeCardApprove = storeCardApprove;
    }

    public String getStoreRealstoreApprove() {
        return storeRealstoreApprove;
    }

    public void setStoreRealstoreApprove(String storeRealstoreApprove) {
        this.storeRealstoreApprove = storeRealstoreApprove;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreLogoId() {
        return storeLogoId;
    }

    public void setStoreLogoId(String storeLogoId) {
        this.storeLogoId = storeLogoId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(String storeLat) {
        this.storeLat = storeLat;
    }

    public String getStoreLng() {
        return storeLng;
    }

    public void setStoreLng(String storeLng) {
        this.storeLng = storeLng;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getStoreCarmodel() {
        return storeCarmodel;
    }

    public String getBrandIcon() {
        return brandIcon;
    }

    public void setBrandIcon(String brandIcon) {
        this.brandIcon = brandIcon;
    }

    public void setStoreCarmodel(String storeCarmodel) {
        this.storeCarmodel = storeCarmodel;
    }

    public String getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    public String getStoreTechnicianLevel() {
        return storeTechnicianLevel;
    }

    public void setStoreTechnicianLevel(String storeTechnicianLevel) {
        this.storeTechnicianLevel = storeTechnicianLevel;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
    }

    public String getStoreServicePoint() {
        return storeServicePoint;
    }

    public void setStoreServicePoint(String storeServicePoint) {
        this.storeServicePoint = storeServicePoint;
    }

    public String getStoreShipPoint() {
        return storeShipPoint;
    }

    public void setStoreShipPoint(String storeShipPoint) {
        this.storeShipPoint = storeShipPoint;
    }

    public String getStoreDescriptionPoint() {
        return storeDescriptionPoint;
    }

    public void setStoreDescriptionPoint(String storeDescriptionPoint) {
        this.storeDescriptionPoint = storeDescriptionPoint;
    }

    public String getStoreSerivceType() {
        return storeSerivceType;
    }

    public void setStoreSerivceType(String storeSerivceType) {
        this.storeSerivceType = storeSerivceType;
    }

    public String getStoreTec() {
        return storeTec;
    }

    public void setStoreTec(String storeTec) {
        this.storeTec = storeTec;
    }

    public String getOpenPlace() {
        return openPlace;
    }

    public void setOpenPlace(String openPlace) {
        this.openPlace = openPlace;
    }

    public String getStoreTimeDesc() {
        return storeTimeDesc;
    }

    public void setStoreTimeDesc(String storeTimeDesc) {
        this.storeTimeDesc = storeTimeDesc;
    }

    public String getRefCost() {
        return refCost;
    }

    public void setRefCost(String refCost) {
        this.refCost = refCost;
    }

    public String getStationNum() {
        return stationNum;
    }

    public void setStationNum(String stationNum) {
        this.stationNum = stationNum;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getStoreTelephone() {
        return storeTelephone;
    }

    public void setStoreTelephone(String storeTelephone) {
        this.storeTelephone = storeTelephone;
    }
}
