package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by xushengji on 2016/11/14.
 */

public class BuyGoods implements Serializable {

    /**购物车商品订单编号**/
    private String id;
    /**数量**/
    private String goodscartCount;
    /**价格**/
    private String goodscartPrice;
    /**商品名称**/
    private String goodsName;
    /**商品图片**/
    private String image;

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

    private String goodscartStatus;
    /**商品状态:0-上架中；1-下架中；-2-举报禁售；-3删除**/
    private String goodsStatus;
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
}
