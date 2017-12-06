package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by JINWENLIN.
 * On 2017/1/3.
 */

public class FreezeBalanceOrder implements Serializable {

    /**
     * 订单的编号
     */
    private String id;
    /**
     * 订单冻结时间
     */
    private String orderFreezeTime;
    /**
     * 订单冻结金额
     */
    private String orderFreezeBlance;
    /**
     * 订单唯一的编号
     */
    private String orderId;
    /**
     * 订单分类
     */
    private String orderClass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderFreezeTime() {
        return orderFreezeTime;
    }

    public void setOrderFreezeTime(String orderFreezeTime) {
        this.orderFreezeTime = orderFreezeTime;
    }

    public String getOrderFreezeBlance() {
        return orderFreezeBlance;
    }

    public void setOrderFreezeBlance(String orderFreezeBlance) {
        this.orderFreezeBlance = orderFreezeBlance;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderClass() {
        return orderClass;
    }

    public void setOrderClass(String orderClass) {
        this.orderClass = orderClass;
    }
}
