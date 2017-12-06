package com.suncreate.fireiot.bean.require;

import java.io.Serializable;

/**
 * Created by chenzhao on 17-1-7.
 */

public class AccessoryRequireDetailStoreGoods implements Serializable {
    /**
     * 配件商报价单号
     **/
    private String quoteNo;
    /**
     * 运费
     **/
    private double shipPrice;
    /**
     * 配件分类id
     **/
    private long gcId;
    /**
     * 配件分类名称
     **/
    private String gcName;
    /**
     * 配件名称
     **/
    private String partsName;
    /**
     * 采购数量
     **/
    private int partsCount;
    /**
     * 商品id
     **/
    private long goodsId;
    /**
     * 商品名称
     **/
    private String goodsName;
    /**
     * 商品单价
     **/
    private double goodsPrice;

    private boolean isCkecked;

    public String getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public double getShipPrice() {
        return shipPrice;
    }

    public void setShipPrice(double shipPrice) {
        this.shipPrice = shipPrice;
    }

    public long getGcId() {
        return gcId;
    }

    public void setGcId(long gcId) {
        this.gcId = gcId;
    }

    public String getGcName() {
        return gcName;
    }

    public void setGcName(String gcName) {
        this.gcName = gcName;
    }

    public String getPartsName() {
        return partsName;
    }

    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    public int getPartsCount() {
        return partsCount;
    }

    public void setPartsCount(int partsCount) {
        this.partsCount = partsCount;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public boolean isCkecked() {
        return isCkecked;
    }

    public void setCkecked(boolean ckecked) {
        isCkecked = ckecked;
    }
}
