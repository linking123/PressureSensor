package com.suncreate.fireiot.bean.user;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by JINWENLIN.
 * On 2016/12/9.
 */
@SuppressWarnings("serial")
@XStreamAlias("pay")
public class Pay implements Serializable {

    /**收支id**/
    private String id;
    /**明细类型**/
    private String payType;
    /**金额**/
    private String amount;
    /**添加时间**/
    private String addtime;
    /**消费标识(0支出 1收入,默认全部)**/
    private String consumeCost;
    /**订单号**/
    private String orderId;
    /**交易号,支付宝，微信等生成的**/
    private String consumeSn;
    /**充值编号**/
    private String rechargeSn;
    /**提现状态 0审核中 1成功 2驳回**/
    private String withdrawPayStatus;
    /**提现银行**/
    private String withdrawBank;
    /**提现信息**/
    private String withdrawInfo;
    /**提现到账银行开户人**/
    private String withdrawUsername;
    /**提现标识（从数据字典取alipay支付宝，chinabank网银在线，tenpay财付通，bill快钱，outline线下支付）**/
    private String withdrawPayment;
    /**消费金额**/
    private String consumeAmount;
    /**必填，0充值，1提现，2退款，3服务订单费，4工位订单费，5配件订单费**/
    private String consumeMode;

    public String getRechargeSn() {
        return rechargeSn;
    }

    public void setRechargeSn(String rechargeSn) {
        this.rechargeSn = rechargeSn;
    }

    public String getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(String consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public String getConsumeMode() {
        return consumeMode;
    }

    public void setConsumeMode(String consumeMode) {
        this.consumeMode = consumeMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getConsumeCost() {
        return consumeCost;
    }

    public void setConsumeCost(String consumeCost) {
        this.consumeCost = consumeCost;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getConsumeSn() {
        return consumeSn;
    }

    public void setConsumeSn(String consumeSn) {
        this.consumeSn = consumeSn;
    }

    public String getWithdrawPayStatus() {
        return withdrawPayStatus;
    }

    public void setWithdrawPayStatus(String withdrawPayStatus) {
        this.withdrawPayStatus = withdrawPayStatus;
    }

    public String getWithdrawBank() {
        return withdrawBank;
    }

    public void setWithdrawBank(String withdrawBank) {
        this.withdrawBank = withdrawBank;
    }

    public String getWithdrawInfo() {
        return withdrawInfo;
    }

    public void setWithdrawInfo(String withdrawInfo) {
        this.withdrawInfo = withdrawInfo;
    }

    public String getWithdrawUsername() {
        return withdrawUsername;
    }

    public void setWithdrawUsername(String withdrawUsername) {
        this.withdrawUsername = withdrawUsername;
    }

    public String getWithdrawPayment() {
        return withdrawPayment;
    }

    public void setWithdrawPayment(String withdrawPayment) {
        this.withdrawPayment = withdrawPayment;
    }
}