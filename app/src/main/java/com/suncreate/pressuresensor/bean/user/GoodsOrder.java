package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xushengji on 2016/11/14.
 */

public class GoodsOrder implements Serializable {

    /**订单编号**/
    private Long id;
    /**订单唯一编号**/
    private String orderId;
    /**总价**/
    private String orderTotalprice;
    /**配件总费**/
    private String orderGoodsAmount;
    /**服务总价**/
    private String orderServicePrice;
    /**邮费**/
    private String transPrices;
    /**发票抬头**/
    private String orderInvoice;
    /**店铺名称**/
    private String storeName;
    /**店铺状态**/
    private String storeStatus;
    /**订单状态**/
    private String orderStatus;
    /**配件列表**/
    private List<ShoppingCar> items;
    /**数量**/
    private String count;
    /**收货地址**/
    private String address;
    /**下单时间**/
    private String addtime;
    /**店铺电话**/
    private String storeTelephone;
    /**店铺ID**/
    private String storeId;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreTelephone() {
        return storeTelephone;
    }

    public void setStoreTelephone(String storeTelephone) {
        this.storeTelephone = storeTelephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTotalprice() {
        return orderTotalprice;
    }

    public void setOrderTotalprice(String orderTotalprice) {
        this.orderTotalprice = orderTotalprice;
    }

    public String getOrderGoodsAmount() {
        return orderGoodsAmount;
    }

    public void setOrderGoodsAmount(String orderGoodsAmount) {
        this.orderGoodsAmount = orderGoodsAmount;
    }

    public String getOrderServicePrice() {
        return orderServicePrice;
    }

    public void setOrderServicePrice(String orderServicePrice) {
        this.orderServicePrice = orderServicePrice;
    }

    public String getTransPrices() {
        return transPrices;
    }

    public void setTransPrices(String transPrices) {
        this.transPrices = transPrices;
    }

    public String getOrderInvoice() {
        return orderInvoice;
    }

    public void setOrderInvoice(String orderInvoice) {
        this.orderInvoice = orderInvoice;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<ShoppingCar> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCar> items) {
        this.items = items;
    }
}
