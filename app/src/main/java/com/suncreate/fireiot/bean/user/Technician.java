package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * 技师
 * Created by xushengji on 2016/11/14.
 */

public class Technician implements Serializable {

    /**服务品牌**/
    private String storeCarmodel;
    /**服务品牌车标名称**/
    private String brandIcon;
    /**用户真实姓名**/
    private String userRealname;
    /**头像**/
    private String storeLogoId;
    /**经度**/
    private String storeLng;
    /**纬度**/
    private String storeLat;
    /**距离**/
    private String dist;
    /**店铺描述**/
    private String storeInfo;
    /**技师编号**/
    private long id;
    /**店铺编号**/
    private String userStoreId;
    /**店铺地址**/
    private String storeAddress;
    /**用户昵称**/
    private String userName;
    /**店铺访问数**/
    private String storeCount;
    /**店铺名称**/
    private String storeName;
    /**技师修龄**/
    private String storeTechnicianYear;
    /**技师级别1-初级 2-中极 3-高级**/
    private String storeTechnicianLevel;
    /**店铺发货评分**/
    private String storeShipPoint;
    /**店铺描述评分**/
    private String storeDescriptionPoint;
    /**店铺服务评分**/
    private String storeServicePoint;
    /**接单数**/
    private String orderCount;
    /**身份认证 0未认证1认证**/
    private String storeCardApprove;
    /**资格认证 0未认证1认证**/
    private String storeRealstoreApprove;
    /**营业时间**/
    private String storeTimeDesc;
    /**技师的简介图片地址**/
    private String jsRecommondId;
    /**技师电话**/
    private String userMobile;
    /**店铺审核状态**/
    private String storeStatus;

    public String getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    public String getStoreLogoId() {
        return storeLogoId;
    }

    public void setStoreLogoId(String storeLogoId) {
        this.storeLogoId = storeLogoId;
    }

    public String getStoreCarmodel() {
        return storeCarmodel;
    }

    public void setStoreCarmodel(String storeCarmodel) {
        this.storeCarmodel = storeCarmodel;
    }

    public String getBrandIcon() {
        return brandIcon;
    }

    public void setBrandIcon(String brandIcon) {
        this.brandIcon = brandIcon;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }


    public String getStoreLng() {
        return storeLng;
    }

    public void setStoreLng(String storeLng) {
        this.storeLng = storeLng;
    }

    public String getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(String storeLat) {
        this.storeLat = storeLat;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserStoreId() {
        return userStoreId;
    }

    public void setUserStoreId(String userStoreId) {
        this.userStoreId = userStoreId;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(String storeCount) {
        this.storeCount = storeCount;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreTechnicianYear() {
        return storeTechnicianYear;
    }

    public void setStoreTechnicianYear(String storeTechnicianYear) {
        this.storeTechnicianYear = storeTechnicianYear;
    }

    public String getStoreTechnicianLevel() {
        return storeTechnicianLevel;
    }

    public void setStoreTechnicianLevel(String storeTechnicianLevel) {
        this.storeTechnicianLevel = storeTechnicianLevel;
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

    public String getStoreServicePoint() {
        return storeServicePoint;
    }

    public void setStoreServicePoint(String storeServicePoint) {
        this.storeServicePoint = storeServicePoint;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
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

    public String getStoreTimeDesc() {
        return storeTimeDesc;
    }

    public void setStoreTimeDesc(String storeTimeDesc) {
        this.storeTimeDesc = storeTimeDesc;
    }

    public String getJsRecommondId() {
        return jsRecommondId;
    }

    public void setJsRecommondId(String jsRecommondId) {
        this.jsRecommondId = jsRecommondId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
}
