package com.suncreate.pressuresensor.bean.require;

import java.io.Serializable;

/**
 * Created by JINWENLIN.
 * On 2016/11/26.
 */

public class Requirement implements Serializable {

    /**
     * 需求类型
     **/
    private String demandType;
    /**
     * 需求编号
     **/
    private String id;
    /**
     * 需求订单编号
     **/
    private String orderId;
    /**
     * 需求描述
     **/
    private String demandDesc;
    /**
     * 预约时间
     **/
    private String appointTimeStart;
    /**
     * 需求单发布会员ID
     */
    private String userId;
    /**
     * 需求联系人
     */
    private String demandUserName;
    /**
     * 用户姓名
     */
    private String username;
    /**
     * 用户图像
     */
    private String image;
    /**
     * 需求地址
     **/
    private String demandAddress;
    /**
     * 需求距离
     **/
    private String dist;
    /**
     * 车品牌名称
     **/
    private String carbrandName;
    /**
     * 车品牌ID
     **/
    private String carbrandID;
    /**
     * 需求状态,0新发布，1已接单，2已报价
     **/
    private String orderState;
    /**
     * 需求联系电话
     */
    private String demandPhone;
    /**
     * 发布时间
     */
    private String demandAddtime;
    /**
     * 接单时间
     */
    private String takersDate;
    /**
     * 接单人
     */
    private String takersUser;
    /**
     * 接单人姓名
     */
    private String takersUserName;
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
     * 服务分类
     **/
    private String serivceType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSerivceType() {
        return serivceType;
    }

    public void setSerivceType(String serivceType) {
        this.serivceType = serivceType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getDemandAddtime() {
        return demandAddtime;
    }

    public void setDemandAddtime(String demandAddtime) {
        this.demandAddtime = demandAddtime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDemandUserName() {
        return demandUserName;
    }

    public void setDemandUserName(String demandUserName) {
        this.demandUserName = demandUserName;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getAppointTimeStart() {
        return appointTimeStart;
    }

    public void setAppointTimeStart(String appointTimeStart) {
        this.appointTimeStart = appointTimeStart;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getTakersDate() {
        return takersDate;
    }

    public void setTakersDate(String takersDate) {
        this.takersDate = takersDate;
    }

    public String getTakersUser() {
        return takersUser;
    }

    public void setTakersUser(String takersUser) {
        this.takersUser = takersUser;
    }

    public String getTakersUserName() {
        return takersUserName;
    }

    public void setTakersUserName(String takersUserName) {
        this.takersUserName = takersUserName;
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

    public String getOrderState() {
        return orderState;
    }

    public String getId() {
        return id;
    }

    public String getDemandDesc() {
        return demandDesc;
    }

    public void setDemandDesc(String demandDesc) {
        this.demandDesc = demandDesc;
    }

    public String getDemandType() {
        return demandType;
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    public String getDemandAddress() {
        return demandAddress;
    }

    public void setDemandAddress(String demandAddress) {
        this.demandAddress = demandAddress;
    }

    public String getCarbrandID() {
        return carbrandID;
    }

    public void setCarbrandID(String carbrandID) {
        this.carbrandID = carbrandID;
    }

    public String getCarbrandName() {
        return carbrandName;
    }

    public void setCarbrandName(String carbrandName) {
        this.carbrandName = carbrandName;
    }

    public String getDemandPhone() {
        return demandPhone;
    }

    public void setDemandPhone(String demandPhone) {
        this.demandPhone = demandPhone;
    }
}
