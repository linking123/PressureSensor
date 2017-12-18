package com.suncreate.pressuresensor.bean.require;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenzhao on 17-1-7.
 */

public class AccessoryRequireDetail implements Serializable {
    /**
     * 需求编号
     **/
    private long id;
    /**
     * 需求添加时间
     **/
    private long demandAddtime;
    /**
     * 需求描述
     **/
    private String demandDesc;
    /**
     * 需求状态
     **/
    private int orderState;
    /**
     * 需求类型:0-配件需求，1-服务需求
     **/
    private int demandType;
    /**
     * 需求地址
     **/
    private String demandAddress;
    /**
     * 需求图片1
     **/
    private String demandImg1;
    /**
     * 需求图片2
     **/
    private String demandImg2;
    /**
     * 需求图片3
     **/
    private String demandImg3;
    /**
     * 需求人名称
     **/
    private String demandUsername;
    /**
     * 接单时间
     **/
    private long takersDate;
    /**
     * 服务分类
     **/
    private String serivceType;
    /**
     * 预约时间
     **/
    private String appointTimeStart;
    /**
     * 接单人ID
     **/
    private String takersUserId;
    /**
     * 接单人姓名
     **/
    private String takersUserName;
    /**
     * 车型
     **/
    private String carbrandName;

    /**
     * 报价配件列表
     */
    private List<AccessoryRequireDetailStoreGoods> xjItems;

    /**
     * 报价配件商列表（包含其对配件的报价信息列表）
     */
    private List<AccessoryRequireDetailStore> items;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDemandAddtime() {
        return demandAddtime;
    }

    public void setDemandAddtime(long demandAddtime) {
        this.demandAddtime = demandAddtime;
    }

    public String getDemandDesc() {
        return demandDesc;
    }

    public void setDemandDesc(String demandDesc) {
        this.demandDesc = demandDesc;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getDemandType() {
        return demandType;
    }

    public void setDemandType(int demandType) {
        this.demandType = demandType;
    }

    public String getDemandAddress() {
        return demandAddress;
    }

    public void setDemandAddress(String demandAddress) {
        this.demandAddress = demandAddress;
    }

    public String getDemandImg1() {
        return demandImg1;
    }

    public void setDemandImg1(String demandImg1) {
        this.demandImg1 = demandImg1;
    }

    public String getDemandImg2() {
        return demandImg2;
    }

    public void setDemandImg2(String demandImg2) {
        this.demandImg2 = demandImg2;
    }

    public String getDemandImg3() {
        return demandImg3;
    }

    public void setDemandImg3(String demandImg3) {
        this.demandImg3 = demandImg3;
    }

    public String getDemandUsername() {
        return demandUsername;
    }

    public void setDemandUsername(String demandUsername) {
        this.demandUsername = demandUsername;
    }

    public long getTakersDate() {
        return takersDate;
    }

    public void setTakersDate(long takersDate) {
        this.takersDate = takersDate;
    }

    public String getSerivceType() {
        return serivceType;
    }

    public void setSerivceType(String serivceType) {
        this.serivceType = serivceType;
    }

    public String getAppointTimeStart() {
        return appointTimeStart;
    }

    public void setAppointTimeStart(String appointTimeStart) {
        this.appointTimeStart = appointTimeStart;
    }

    public String getTakersUserId() {
        return takersUserId;
    }

    public void setTakersUserId(String takersUserId) {
        this.takersUserId = takersUserId;
    }

    public String getTakersUserName() {
        return takersUserName;
    }

    public void setTakersUserName(String takersUserName) {
        this.takersUserName = takersUserName;
    }

    public String getCarbrandName() {
        return carbrandName;
    }

    public void setCarbrandName(String carbrandName) {
        this.carbrandName = carbrandName;
    }

    public List<AccessoryRequireDetailStoreGoods> getXjItems() {
        return xjItems;
    }

    public void setXjItems(List<AccessoryRequireDetailStoreGoods> xjItems) {
        this.xjItems = xjItems;
    }

    public List<AccessoryRequireDetailStore> getItems() {
        return items;
    }

    public void setItems(List<AccessoryRequireDetailStore> items) {
        this.items = items;
    }
}
