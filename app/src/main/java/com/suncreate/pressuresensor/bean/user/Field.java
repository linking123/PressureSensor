package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/28.
 */

public class Field implements Serializable {
    /**
     * 快修站ID
     **/
    public String id;
    /**
     * 快修站图片
     **/
    public String storeLogoId;
    /**
     * 快修站经度
     **/
    public String storeLng;
    /**
     * 快修站纬度
     **/
    public String storeLat;

    /**
     * 快修站等级
     **/
    public String storeTechnicianLevel;
    /**
     * 店铺名称
     **/
    public String storeName;
    /**
     * 发货分
     **/

    public String storeShipPoint;
    /**
     * 描述分
     **/
    public String storeDescriptionPoint;

    /**
     * 店铺地址
     **/
    public String storeAddress;
    /**
     * 店铺的描述
     **/
    public String storeInfo;
    /**
     * 店铺状态
     **/
    public String storeStatus;
    /**
     * 经营的品牌图表地址
     **/
    public String storeCarmodel;
    /**
     * 是否开启场地
     **/
    public String openPlace;
    /**
     * 参考经费
     **/
    public String refCost;
    /**
     * 工位数
     **/
    public String stationNum;

    /**
     * 接单数
     **/
    public String  orderCount;

    /**
     * 专修品牌icon
     **/
    public String  brandIcon;

    /**
     * 营业时间说明
     **/

    public String  storeTimeDesc;
    public String getStoreTimeDesc() {
        return storeTimeDesc;
    }

    public void setStoreTimeDesc(String storeTimeDesc) {
        this.storeTimeDesc = storeTimeDesc;
    }


    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getBrandIcon() {
        return brandIcon;
    }

    public void setBrandIcon(String brandIcon) {
        this.brandIcon = brandIcon;
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

    public String getStoreTechnicianLevel() {
        return storeTechnicianLevel;
    }

    public void setStoreTechnicianLevel(String storeTechnicianLevel) {
        this.storeTechnicianLevel = storeTechnicianLevel;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public String getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    public String getStoreCarmodel() {
        return storeCarmodel;
    }

    public void setStoreCarmodel(String storeCarmodel) {
        this.storeCarmodel = storeCarmodel;
    }

    public String getOpenPlace() {
        return openPlace;
    }

    public void setOpenPlace(String openPlace) {
        this.openPlace = openPlace;
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

}
