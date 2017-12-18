package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xushengji on 2016/11/14.
 */

public class ShoppingStore implements Serializable {

    /**
     * 店铺名
     **/
    private String storeName;
    /**
     * 店铺ID
     **/
    private Long id;
    /**
     * 店铺Logo ID
     **/
    private String storeLogoId;
    /**订单唯一编号**/
    private String orderId;
    /**
     * 购物车对象
     **/
    private List<ShoppingCar> items;
    /**
     * 是否选中
     **/
    private Boolean checks;
    /**
     * 店铺商品价格
     **/
    private double sumprice;
    /**
     * 店铺选中商品id
     **/
    private String ids;
    /**
     * 商品费用
     **/
    private String goodsPrices;
    /**
     * 服务费用
     **/
    private String servicePrices;
    /**配件总费**/
    private String orderGoodsAmount;
    /**
     * 运费
     **/
    private String transPrices;
    /**
     * 总费用
     **/
    private String truePrices;
    /**
     * 发票抬头
     **/
    private String orderInvoice;
    /**
     * 件数
     **/
    private String count;
    /**
     * 总价
     **/
    private String orderTotalprice;
    /**
     * 店铺地址
     **/
    private String address;
    /**
     * 店铺描述
     **/
    private String info;
    /**
     * 用户编号
     **/
    private String userId;
    /**
     * 用户姓名
     **/
    private String userName;;
    /**
     * 店铺电话号码
     **/
    private String phone;

    private String tag;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderGoodsAmount() {
        return orderGoodsAmount;
    }

    public void setOrderGoodsAmount(String orderGoodsAmount) {
        this.orderGoodsAmount = orderGoodsAmount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOrderTotalprice() {
        return orderTotalprice;
    }

    public void setOrderTotalprice(String orderTotalprice) {
        this.orderTotalprice = orderTotalprice;
    }

    public String getOrderInvoice() {
        return orderInvoice;
    }

    public void setOrderInvoice(String orderInvoice) {
        this.orderInvoice = orderInvoice;
    }

    public String getGoodsPrices() {
        return goodsPrices;
    }

    public void setGoodsPrices(String goodsPrices) {
        this.goodsPrices = goodsPrices;
    }

    public String getServicePrices() {
        return servicePrices;
    }

    public void setServicePrices(String servicePrices) {
        this.servicePrices = servicePrices;
    }

    public String getTransPrices() {
        return transPrices;
    }

    public void setTransPrices(String transPrices) {
        this.transPrices = transPrices;
    }

    public String getTruePrices() {
        return truePrices;
    }

    public void setTruePrices(String truePrices) {
        this.truePrices = truePrices;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public double getSumprice() {
        return sumprice;
    }

    public void setSumprice(double sumprice) {
        this.sumprice = sumprice;
    }

    public Boolean getChecks() {
        return checks;
    }

    public void setChecks(Boolean checks) {
        this.checks = checks;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreLogoId() {
        return storeLogoId;
    }

    public void setStoreLogoId(String storeLogoId) {
        this.storeLogoId = storeLogoId;
    }

    public List<ShoppingCar> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCar> items) {
        this.items = items;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
