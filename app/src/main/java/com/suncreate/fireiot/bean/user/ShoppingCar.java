package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by xushengji on 2016/11/14.
 */

public class ShoppingCar implements Serializable {

    /**购物车编号集合**/
    private String ids;
    /**购物车编号**/
    private String id;
    /**商品编号**/
    private String goodsId;
    /**图片id**/
    private String goodsPhoto;
    /**商品数量**/
    private String goodscartCount;
    /**价格**/
    private String goodscartPrice;
    /**商品名称**/
    private String goodsName;
    /**商品图片**/
    private String image;
    /**删除标记：1锁定，0正常**/
    private String goodscartStatus;
    /**商品状态:0-上架中；1-下架中；-2-举报禁售；-3删除**/
    private String goodsStatus;
    /**是否选中**/
    private Boolean checks;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsPhoto() {
        return goodsPhoto;
    }

    public void setGoodsPhoto(String goodsPhoto) {
        this.goodsPhoto = goodsPhoto;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Boolean getChecks() {
        return checks;
    }

    public void setChecks(Boolean checks) {
        this.checks = checks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodscartCount() {
        return goodscartCount;
    }

    public void setGoodscartCount(String goodscartCount) {
        this.goodscartCount = goodscartCount;
    }

    public String getGoodscartPrice() {
        return goodscartPrice;
    }

    public void setGoodscartPrice(String goodscartPrice) {
        this.goodscartPrice = goodscartPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGoodscartStatus() {
        return goodscartStatus;
    }

    public void setGoodscartStatus(String goodscartStatus) {
        this.goodscartStatus = goodscartStatus;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }
}
