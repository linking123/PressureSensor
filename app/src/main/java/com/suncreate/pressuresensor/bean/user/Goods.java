package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;

/**
 * Created by xushengji on 2016/11/14.
 */

public class Goods implements Serializable {

    /**纬度**/
    private String storeLat;
    /**配件OE号**/
    private String goodsOe;
    /**商品id**/
    private String id;
    /**车品牌**/
    private String goodsbrandName;
    /**商品图片地址**/
    private String goodsPhoto;
    /**级别**/
    private String totalLevel;
    /**店铺服务评分**/
    private String storeServicePoint;
    /**地区名**/
    private String xzqhName;
    /**经度**/
    private String storeLng;
    /**当前价格**/
    private String goodsCurrentPrice;
    /**商品名称**/
    private String goodsName;
    /**分类名称**/
    private String totalName;
    /**商品销售数量**/
    private String goodsSalenum;
    /**店铺发货评分**/
    private String storeShipPoint;
    /**店铺描述评分**/
    private String storeDescriptionPoint;
    /**店铺ID**/
    private String storeId;
    /**店铺地址**/
    private String storeAddress;
    /**店铺名称**/
    private String storeName;
    /**型号**/
    private String goodsMode;
    /**规格**/
    private String goodsSpec;
    /**单位**/
    private String goodscarmodelName;
    /**单位**/
    private String goodsUnit;
    /**商品状态:0-上架中；1-下架中；-2-举报禁售；-3删除**/
    private String goodsStatus;
    /**店铺状态：0-未开通；1-审核中；2-正常；3-已关闭**/
    private String storeStatus;
    /**品牌状态：1锁定，0正常**/
    private String goodsbrandStatus;
    /**删除标记：1锁定0正常**/
    private String totalStatus;
    /**自定义分类**/
    private String usergoodsclassName;
    /**电话**/
    private String storeTelephone;
    /**商品库存**/
    private String goodsInventory;

    public String getGoodsInventory() {
        return goodsInventory;
    }

    public void setGoodsInventory(String goodsInventory) {
        this.goodsInventory = goodsInventory;
    }

    public String getGoodscarmodelName() {
        return goodscarmodelName;
    }

    public void setGoodscarmodelName(String goodscarmodelName) {
        this.goodscarmodelName = goodscarmodelName;
    }

    public String getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(String storeLat) {
        this.storeLat = storeLat;
    }

    public String getGoodsOe() {
        return goodsOe;
    }

    public void setGoodsOe(String goodsOe) {
        this.goodsOe = goodsOe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsbrandName() {
        return goodsbrandName;
    }

    public void setGoodsbrandName(String goodsbrandName) {
        this.goodsbrandName = goodsbrandName;
    }

    public String getGoodsPhoto() {
        return goodsPhoto;
    }

    public void setGoodsPhoto(String goodsPhoto) {
        this.goodsPhoto = goodsPhoto;
    }

    public String getTotalLevel() {
        return totalLevel;
    }

    public void setTotalLevel(String totalLevel) {
        this.totalLevel = totalLevel;
    }

    public String getStoreServicePoint() {
        return storeServicePoint;
    }

    public void setStoreServicePoint(String storeServicePoint) {
        this.storeServicePoint = storeServicePoint;
    }

    public String getXzqhName() {
        return xzqhName;
    }

    public void setXzqhName(String xzqhName) {
        this.xzqhName = xzqhName;
    }

    public String getStoreLng() {
        return storeLng;
    }

    public void setStoreLng(String storeLng) {
        this.storeLng = storeLng;
    }

    public String getGoodsCurrentPrice() {
        return goodsCurrentPrice;
    }

    public void setGoodsCurrentPrice(String goodsCurrentPrice) {
        this.goodsCurrentPrice = goodsCurrentPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getTotalName() {
        return totalName;
    }

    public void setTotalName(String totalName) {
        this.totalName = totalName;
    }

    public String getGoodsSalenum() {
        return goodsSalenum;
    }

    public void setGoodsSalenum(String goodsSalenum) {
        this.goodsSalenum = goodsSalenum;
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getGoodsMode() {
        return goodsMode;
    }

    public void setGoodsMode(String goodsMode) {
        this.goodsMode = goodsMode;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    public String getGoodsbrandStatus() {
        return goodsbrandStatus;
    }

    public void setGoodsbrandStatus(String goodsbrandStatus) {
        this.goodsbrandStatus = goodsbrandStatus;
    }

    public String getTotalStatus() {
        return totalStatus;
    }

    public void setTotalStatus(String totalStatus) {
        this.totalStatus = totalStatus;
    }

    public String getUsergoodsclassName() {
        return usergoodsclassName;
    }

    public void setUsergoodsclassName(String usergoodsclassName) {
        this.usergoodsclassName = usergoodsclassName;
    }

    public String getStoreTelephone() {
        return storeTelephone;
    }

    public void setStoreTelephone(String storeTelephone) {
        this.storeTelephone = storeTelephone;
    }
}