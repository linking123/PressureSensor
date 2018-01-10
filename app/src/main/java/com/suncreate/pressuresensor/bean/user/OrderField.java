package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;

/**
 * Created by panzhaoxuan on 2016/12/09.
 */

public class OrderField implements Serializable {
    /**
     * 订单id
     **/
   private String id;
    /**
     * 场地状态
     **/
    private String storeStatus;
    /**
     * 场地名称
     **/
    private String storeName;
    /**
     * 添加时间
     **/
    private String orderAppointTimeStart;
    /**
     * 场地费用
     **/
    private String placeAmount;
    /**
     * 场地图片
     **/
    private String storeLogoId;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOrderAppointTimeStart() {
        return orderAppointTimeStart;
    }

    public void setOrderAppointTimeStart(String orderAppointTimeStart) {
        this.orderAppointTimeStart = orderAppointTimeStart;
    }

    public String getPlaceAmount() {
        return placeAmount;
    }

    public void setPlaceAmount(String placeAmount) {
        this.placeAmount = placeAmount;
    }

    public String getStoreLogoId() {
        return storeLogoId;
    }

    public void setStoreLogoId(String storeLogoId) {
        this.storeLogoId = storeLogoId;
    }

    public String getStoreTechnicianLevel() {
        return storeTechnicianLevel;
    }

    public void setStoreTechnicianLevel(String storeTechnicianLevel) {
        this.storeTechnicianLevel = storeTechnicianLevel;
    }

    /**
     *  店铺编号
     **/
    private String storeId;
    /**
     *   店铺等级
     **/
    private String storeTechnicianLevel;


}