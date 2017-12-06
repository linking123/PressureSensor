package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * 服务订单详情
 * Created by Macintosh on 12/10/16.
 */

public class ServicesOrderDetail implements Serializable {
    /**暂无信息*/
    private Long id;
    /**订单号*/
    private String orderId;
    /**工时费*/
    private double orderWorkingAmount;
    /**配件费*/
    private double orderPartsAmount;
    /**场地费用*/
    private double placeAmount;
    /**辅料费用*/
    private double accessoriesAmount;
    /**服务预约开始时间*/
    private Long orderAppointTimeStart;
    /**服务分类*/
    private String storeSerivceType;
    /**买家备注*/
    private String orderMsg;
    /**服务车型*/
    private String orderCarType;
    /**订单状态**/
    private Integer orderStatus;
    /**车主姓名*/
    private String userName;
    /**车主手机号*/
    private String userMobile;
    /**技师姓名*/
    private String storeName;
    /**技师手机号*/
    private String storeMobile;
    /**车正前方拍照*/
    private Long beforePhoto1;
    /**仪表信息*/
    private Long beforePhoto2;
    /**故障位置*/
    private Long beforePhoto3;
    /**车主确认维修签字单*/
    private Long beforePhoto4;
    /**服务后照片1*/
    private Long afterPhoto1;
    /**服务后照片2*/
    private Long afterPhoto2;
    /**服务后照片3*/
    private Long afterPhoto3;
    /**技师报价上传相关状态，0-都未上传，1-报价单已上传，2-服务前照片已上传，3-服务后照片已上传，4-结算单已上传×**/
    private Integer photoStatus;
    /**总价(实付款)*/
    private double orderTotalprice;
    /**上传报价单时间*/
    private Long orderQuotationTime;
    /**上传服务前时间*/
    private Long orderBeforeTime;
    /**上传服务后时间*/
    private Long orderAfterTime;
    /**上传结算单时间*/
    private Long orderSettlementTime;




    /**支付时间*/
    private Long orderPayTime;
    /**支付留言*/
    private String orderPayMsg;
    /**退款金额*/
    private double orderRefund;
    /**发货时间*/
    private Long orderShipTime;
    /**运费*/
    private double orderShipPrice;
    /**平台服务费（买家支付给平台的）*/
    private double orderServicePrice;
    /**卖家订单分润（卖家订单抽成）*/
    private double orderSellerprofit;
    /**订单类型：weixin-微信订单;android-Android订单;ios-IOS订单;web-PC订单*/
    private String orderType;
    /**订单分类：0-配件订单，1-抢单订单，2-服务订单，3-询价订单*/
    private Long orderClass;
    /**经度*/
    private double orderLng;
    /**纬度*/
    private double orderLat;
    /**接单时间*/
    private Long orderServertime1;
    /**开始服务时间*/
    private Long orderServertime2;
    /**服务完成时间*/
    private Long orderServertime3;
    /**结算时间*/
    private Long orderServertime4;
    /**是否录入悟空车服网条码*/
    private Long noInputStatus;

    public Long getOrderQuotationTime() {
        return orderQuotationTime;
    }

    public void setOrderQuotationTime(Long orderQuotationTime) {
        this.orderQuotationTime = orderQuotationTime;
    }

    public Long getOrderBeforeTime() {
        return orderBeforeTime;
    }

    public void setOrderBeforeTime(Long orderBeforeTime) {
        this.orderBeforeTime = orderBeforeTime;
    }

    public Long getOrderAfterTime() {
        return orderAfterTime;
    }

    public void setOrderAfterTime(Long orderAfterTime) {
        this.orderAfterTime = orderAfterTime;
    }

    public Long getOrderSettlementTime() {
        return orderSettlementTime;
    }

    public void setOrderSettlementTime(Long orderSettlementTime) {
        this.orderSettlementTime = orderSettlementTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getOrderWorkingAmount() {
        return orderWorkingAmount;
    }

    public void setOrderWorkingAmount(double orderWorkingAmount) {
        this.orderWorkingAmount = orderWorkingAmount;
    }

    public double getOrderPartsAmount() {
        return orderPartsAmount;
    }

    public void setOrderPartsAmount(double orderPartsAmount) {
        this.orderPartsAmount = orderPartsAmount;
    }

    public double getPlaceAmount() {
        return placeAmount;
    }

    public void setPlaceAmount(double placeAmount) {
        this.placeAmount = placeAmount;
    }

    public double getAccessoriesAmount() {
        return accessoriesAmount;
    }

    public void setAccessoriesAmount(double accessoriesAmount) {
        this.accessoriesAmount = accessoriesAmount;
    }

    public Long getOrderAppointTimeStart() {
        return orderAppointTimeStart;
    }

    public void setOrderAppointTimeStart(Long orderAppointTimeStart) {
        this.orderAppointTimeStart = orderAppointTimeStart;
    }

    public String getStoreSerivceType() {
        return storeSerivceType;
    }

    public void setStoreSerivceType(String storeSerivceType) {
        this.storeSerivceType = storeSerivceType;
    }

    public String getOrderMsg() {
        return orderMsg;
    }

    public void setOrderMsg(String orderMsg) {
        this.orderMsg = orderMsg;
    }

    public String getOrderCarType() {
        return orderCarType;
    }

    public void setOrderCarType(String orderCarType) {
        this.orderCarType = orderCarType;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreMobile() {
        return storeMobile;
    }

    public void setStoreMobile(String storeMobile) {
        this.storeMobile = storeMobile;
    }

    public Long getBeforePhoto1() {
        return beforePhoto1;
    }

    public void setBeforePhoto1(Long beforePhoto1) {
        this.beforePhoto1 = beforePhoto1;
    }

    public Long getBeforePhoto2() {
        return beforePhoto2;
    }

    public void setBeforePhoto2(Long beforePhoto2) {
        this.beforePhoto2 = beforePhoto2;
    }

    public Long getBeforePhoto3() {
        return beforePhoto3;
    }

    public void setBeforePhoto3(Long beforePhoto3) {
        this.beforePhoto3 = beforePhoto3;
    }

    public Long getBeforePhoto4() {
        return beforePhoto4;
    }

    public void setBeforePhoto4(Long beforePhoto4) {
        this.beforePhoto4 = beforePhoto4;
    }

    public Long getAfterPhoto1() {
        return afterPhoto1;
    }

    public void setAfterPhoto1(Long afterPhoto1) {
        this.afterPhoto1 = afterPhoto1;
    }

    public Long getAfterPhoto2() {
        return afterPhoto2;
    }

    public void setAfterPhoto2(Long afterPhoto2) {
        this.afterPhoto2 = afterPhoto2;
    }

    public Long getAfterPhoto3() {
        return afterPhoto3;
    }

    public void setAfterPhoto3(Long afterPhoto3) {
        this.afterPhoto3 = afterPhoto3;
    }

    public Long getOrderPayTime() {
        return orderPayTime;
    }

    public void setOrderPayTime(Long orderPayTime) {
        this.orderPayTime = orderPayTime;
    }

    public String getOrderPayMsg() {
        return orderPayMsg;
    }

    public void setOrderPayMsg(String orderPayMsg) {
        this.orderPayMsg = orderPayMsg;
    }

    public double getOrderRefund() {
        return orderRefund;
    }

    public void setOrderRefund(double orderRefund) {
        this.orderRefund = orderRefund;
    }

    public Long getOrderShipTime() {
        return orderShipTime;
    }

    public void setOrderShipTime(Long orderShipTime) {
        this.orderShipTime = orderShipTime;
    }

    public double getOrderShipPrice() {
        return orderShipPrice;
    }

    public void setOrderShipPrice(double orderShipPrice) {
        this.orderShipPrice = orderShipPrice;
    }

    public double getOrderServicePrice() {
        return orderServicePrice;
    }

    public void setOrderServicePrice(double orderServicePrice) {
        this.orderServicePrice = orderServicePrice;
    }

    public double getOrderTotalprice() {
        return orderTotalprice;
    }

    public void setOrderTotalprice(double orderTotalprice) {
        this.orderTotalprice = orderTotalprice;
    }

    public double getOrderSellerprofit() {
        return orderSellerprofit;
    }

    public void setOrderSellerprofit(double orderSellerprofit) {
        this.orderSellerprofit = orderSellerprofit;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Long getOrderClass() {
        return orderClass;
    }

    public void setOrderClass(Long orderClass) {
        this.orderClass = orderClass;
    }

    public double getOrderLng() {
        return orderLng;
    }

    public void setOrderLng(double orderLng) {
        this.orderLng = orderLng;
    }

    public double getOrderLat() {
        return orderLat;
    }

    public void setOrderLat(double orderLat) {
        this.orderLat = orderLat;
    }

    public Long getOrderServertime1() {
        return orderServertime1;
    }

    public void setOrderServertime1(Long orderServertime1) {
        this.orderServertime1 = orderServertime1;
    }

    public Long getOrderServertime2() {
        return orderServertime2;
    }

    public void setOrderServertime2(Long orderServertime2) {
        this.orderServertime2 = orderServertime2;
    }

    public Long getOrderServertime3() {
        return orderServertime3;
    }

    public void setOrderServertime3(Long orderServertime3) {
        this.orderServertime3 = orderServertime3;
    }

    public Long getOrderServertime4() {
        return orderServertime4;
    }

    public void setOrderServertime4(Long orderServertime4) {
        this.orderServertime4 = orderServertime4;
    }

    public Long getNoInputStatus() {
        return noInputStatus;
    }

    public void setNoInputStatus(Long noInputStatus) {
        this.noInputStatus = noInputStatus;
    }

    public Integer getPhotoStatus() {
        return photoStatus;
    }

    public void setPhotoStatus(Integer photoStatus) {
        this.photoStatus = photoStatus;
    }
}
