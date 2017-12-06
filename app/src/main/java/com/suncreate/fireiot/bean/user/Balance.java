package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by xushengji on 2016/11/14.
 */

public class Balance implements Serializable {

    /**用户真实姓名**/
    private String user_realname;
    /**店铺编号**/
    private Long store_id;
    /**数量**/
    private Long goodscart_count;
    /**价格**/
    private double goodscart_price;
    /**手机**/
    private String user_mobile;
    /**商品名称**/
    private String goods_name;
    /**商品图片**/
    private String image;

    public String getUser_realname() {
        return user_realname;
    }

    public void setUser_realname(String user_realname) {
        this.user_realname = user_realname;
    }

    public Long getStore_id() {
        return store_id;
    }

    public void setStore_id(Long store_id) {
        this.store_id = store_id;
    }

    public Long getGoodscart_count() {
        return goodscart_count;
    }

    public void setGoodscart_count(Long goodscart_count) {
        this.goodscart_count = goodscart_count;
    }

    public double getGoodscart_price() {
        return goodscart_price;
    }

    public void setGoodscart_price(double goodscart_price) {
        this.goodscart_price = goodscart_price;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
