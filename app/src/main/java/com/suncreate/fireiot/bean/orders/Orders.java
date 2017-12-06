package com.suncreate.fireiot.bean.orders;

import java.io.Serializable;

/**
 * Created by fei on 2016/5/24.
 * desc:  question bean
 */
public class Orders implements Serializable {

    private long id;
    private long orderNum;
    private String orderName;
    private String orderPrice;
    private String orderTime;
    private int orderState;
    private String serviceAddress;
    private String serviceSupplier;
    private String carOwner;
    private String carOwnerAddress;
    private long carOwnerPhone;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(long orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServiceSupplier() {
        return serviceSupplier;
    }

    public void setServiceSupplier(String serviceSupplier) {
        this.serviceSupplier = serviceSupplier;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    public String getCarOwnerAddress() {
        return carOwnerAddress;
    }

    public void setCarOwnerAddress(String carOwnerAddress) {
        this.carOwnerAddress = carOwnerAddress;
    }

    public long getCarOwnerPhone() {
        return carOwnerPhone;
    }

    public void setCarOwnerPhone(long carOwnerPhone) {
        this.carOwnerPhone = carOwnerPhone;
    }
}
