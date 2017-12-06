package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by Pan Zhaoxuan on 2016/12/26.
 */

public class GoodsStore implements Serializable {

    /**
     * 店铺名称
     **/
    private String storeName;
    /**
     * 店铺详情
     **/
    private String storeInfo;
    /**
     * 店铺服务评分
     **/
    private String storeServicePoint;
    /**
     * 店铺地址
     **/
    private String storeAddress;
    /**
     * 纬度
     **/
    private String storeLat;
    /**
     * 单位
     **/
    private String storeCarmodel;
    /**
     * 店铺技师等级
     **/
    private String storeTechnicianLevel;
    /**
     * 店铺id
     **/
    private String id;
    /**
     * 店铺状态
     **/
    private String storeStatus;
    /**
     * 店铺发货评分
     **/
    private String storeShipPoint;
    /**
     * 店铺经度
     **/
    private String storeLng;
    /**
     * 店铺Log
     **/
    private String storeLogoId;
    /**
     * 电话
     **/
    private String storeTelephone;
    /**
     * 店铺描述评分
     **/
    private String storeDescriptionPoint;

    private boolean isCkecked;

    public String getStoreDescriptionPoint() {
        return storeDescriptionPoint;
    }

    public void setStoreDescriptionPoint(String storeDescriptionPoint) {
        this.storeDescriptionPoint = storeDescriptionPoint;
    }

    public String getStoreTelephone() {
        return storeTelephone;
    }

    public void setStoreTelephone(String storeTelephone) {
        this.storeTelephone = storeTelephone;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(String storeLat) {
        this.storeLat = storeLat;
    }

    public String getStoreCarmodel() {
        return storeCarmodel;
    }

    public void setStoreCarmodel(String storeCarmodel) {
        this.storeCarmodel = storeCarmodel;
    }

    public String getStoreTechnicianLevel() {
        return storeTechnicianLevel;
    }

    public void setStoreTechnicianLevel(String storeTechnicianLevel) {
        this.storeTechnicianLevel = storeTechnicianLevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    public String getStoreShipPoint() {
        return storeShipPoint;
    }

    public void setStoreShipPoint(String storeShipPoint) {
        this.storeShipPoint = storeShipPoint;
    }

    public String getStoreLng() {
        return storeLng;
    }

    public void setStoreLng(String storeLng) {
        this.storeLng = storeLng;
    }

    public String getStoreLogoId() {
        return storeLogoId;
    }

    public void setStoreLogoId(String storeLogoId) {
        this.storeLogoId = storeLogoId;
    }

    public boolean isCkecked() {
        return isCkecked;
    }

    public void setCkecked(boolean ckecked) {
        isCkecked = ckecked;
    }
}