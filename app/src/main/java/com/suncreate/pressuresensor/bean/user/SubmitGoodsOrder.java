package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;

/**
 * Created by xushengji on 2016/11/14.
 */

public class SubmitGoodsOrder implements Serializable {

    /**必填, 加入购物车订单ID字符串id之间英文逗号隔开**/
    private String ids;
    /**选填，发票抬头**/
    private String orderInvoice;
    /**必填，商品总金额**/
    private String goodsPrices;
    /**必填，平台服务费**/
    private String servicePrices;
    /**必填，运费**/
    private String transPrices;
    /**必填，总价**/
    private String truePrices;
    /**必填，店铺编号**/
    private String storeId;
    /**必填，地址区域**/
    private String addressCode;
    /**必填，收货地址**/
    private String addressID;
    /**选填，备注**/
    private String orderMsg;

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getOrderMsg() {
        return orderMsg;
    }

    public void setOrderMsg(String orderMsg) {
        this.orderMsg = orderMsg;
    }
}
