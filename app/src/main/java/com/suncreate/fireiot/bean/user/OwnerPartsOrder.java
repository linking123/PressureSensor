package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by Pan Zhaoxuan on 2016/12/1
 */

public class OwnerPartsOrder implements Serializable {
//    orderAddtime
//    orderStatus
//            id
//    goodscartCount
//            goodscartPrice
//    goodsName
//            gId
//    image
    /**订单添加的时间**/
    private String orderAddtime;
    /**订单状态**/
    private String orderStatus;
    /**订单id**/
    private String id;
    /**数量**/
    private String goodscartCount;
    /**价格**/
    private String goodscartPrice;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOrderAddtime() {
        return orderAddtime;
    }

    public void setOrderAddtime(String orderAddtime) {
        this.orderAddtime = orderAddtime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**商品名称**/
    private String goodsName;
    /**商品id**/
    private String gId;
    /**商品图片地址**/
    private String image;



}