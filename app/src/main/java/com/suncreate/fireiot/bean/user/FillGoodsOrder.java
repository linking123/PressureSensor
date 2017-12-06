package com.suncreate.fireiot.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xushengji on 2016/11/14.
 */

public class FillGoodsOrder implements Serializable {

    /**商品总金额**/
    private double goodsPrices;
    /**平台服务费**/
    private double servicePrices;
    /**运费**/
    private double transPrices;
    /**实付款**/
    private double truePrices;
    /**店铺对象**/
    private List<ShoppingStore> items;
    /**返回订单ID**/
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getGoodsPrices() {
        return goodsPrices;
    }

    public void setGoodsPrices(double goodsPrices) {
        this.goodsPrices = goodsPrices;
    }

    public double getServicePrices() {
        return servicePrices;
    }

    public void setServicePrices(double servicePrices) {
        this.servicePrices = servicePrices;
    }

    public double getTransPrices() {
        return transPrices;
    }

    public void setTransPrices(double transPrices) {
        this.transPrices = transPrices;
    }

    public double getTruePrices() {
        return truePrices;
    }

    public void setTruePrices(double truePrices) {
        this.truePrices = truePrices;
    }

    public List<ShoppingStore> getItems() {
        return items;
    }

    public void setItems(List<ShoppingStore> items) {
        this.items = items;
    }
}
