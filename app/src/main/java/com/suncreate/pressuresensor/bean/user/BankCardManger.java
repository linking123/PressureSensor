package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JINWENLIN.
 * On 2017/1/3.
 */

public class BankCardManger implements Serializable {

    /**绑定银行卡主键编号**/
    private String id;
    /**银行名称**/
    private String bankName;
    /**银行卡号**/
    private String bankCardNumber;
    /**真实姓名**/
    private String realname;
    /**电话号码**/
    private String phoneNumber;
    /**身份证编号**/
    private String idCard;
    /**用户编号**/
    private String userId;
    /**删除标识：锁定1，正常0**/
    private String cardStatus;

    /**
     * 银行卡对象  集合属性
     */
    private List<BankCardManger> items;

    public List<BankCardManger> getItems() {
        return items;
    }

    public void setItems(List<BankCardManger> items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }
}
