package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;

/**
 * Created by panzhaoxuan on 2016/12/09.
 */

public class OrderFieldDetail implements Serializable {

//   1 id	long	订单编号
//   1 orderId	string	订单唯一编号
//   1 storeAddress	string	店铺地址 "id": "85",
//           1 "storeStatus": "2",
//            "orderAppointTimeStart": "1480477769728",
//           1 "storeName": "修理擦",
//           1 "placeAmount": "",
//            1"storeAddress": "安徽省合肥市蜀山区天智路29号",
//           1 "orderMsg": "Yuyaidyaidyiuayidayiudysiauyduauida",
//           1 "storeTelephone": "",
//            1"orderServicePrice": "",
//            1"orderId": "repa100000000184",
//           1 "orderCarType": "??A?(??)A160-2004?"
//   1 placeAmount	double	场地费用
//    1orderServicePrice	double	服务总价
//  1  storeName	String	店铺名称
//   1 storeStatus	long	店铺状态
//   1 storeTelephone	string	店铺电话
//  1  orderMsg	string	描述
//    1orderCarType	string	预约车型}
    /**
     * 订单id
     **/
private String id;
    /**
     * 订单唯一编号
     **/
private String orderId;
    /**
     * 店铺地址
     **/
private String storeAddress;
    /**
     * 场地费用
     **/
private String placeAmount;
    /**
     * 服务总价
     **/
private String orderServicePrice;
    /**
     * 店铺名称
     **/
private String storeName;
    /**
     * 店铺状态
     **/
private String storeStatus;
    /**
     * 店铺电话
     **/
private String storeTelephone;
    /**
     * 描述
     **/
private String orderMsg;
    /**
     * 预约车型}
     **/
private String orderCarType;

    public String getOrderAppointTimeStart() {
        return orderAppointTimeStart;
    }

    public void setOrderAppointTimeStart(String orderAppointTimeStart) {
        this.orderAppointTimeStart = orderAppointTimeStart;
    }

    /**
     * 预约时间
     **/

 private String  orderAppointTimeStart;
    public String getOrderCarType() {
        return orderCarType;
    }

    public void setOrderCarType(String orderCarType) {
        this.orderCarType = orderCarType;
    }

    public String getOrderMsg() {
        return orderMsg;
    }

    public void setOrderMsg(String orderMsg) {
        this.orderMsg = orderMsg;
    }

    public String getStoreTelephone() {
        return storeTelephone;
    }

    public void setStoreTelephone(String storeTelephone) {
        this.storeTelephone = storeTelephone;
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

    public String getOrderServicePrice() {
        return orderServicePrice;
    }

    public void setOrderServicePrice(String orderServicePrice) {
        this.orderServicePrice = orderServicePrice;
    }

    public String getPlaceAmount() {
        return placeAmount;
    }

    public void setPlaceAmount(String placeAmount) {
        this.placeAmount = placeAmount;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}