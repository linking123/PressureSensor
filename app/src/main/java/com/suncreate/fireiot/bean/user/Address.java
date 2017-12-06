package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by Pan Zhaoxuan on 2016/12/1.
 */

public class Address implements Serializable {


    /**收货地址编号**/
    private Long id;
    /**添加时间**/
    private String addressAddtime;
    /**删除标识：锁定1，正常0**/
    private String addressStatus;
    /**详细地址**/
    private String addressInfo;
    /**手机号**/
    private String addressMobile;
    /**电话**/
    private String addressTelephone;
    /**真实姓名**/
    private String addressTruename;
    /**邮编**/
    private String addressZip;
    /**区域地址**/
    private String areaName;
    /**1 代表默认  0为一般（一个用户只有一个默认地址）**/
    private String daddress;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    private String areaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressAddtime() {
        return addressAddtime;
    }

    public void setAddressAddtime(String addressAddtime) {
        this.addressAddtime = addressAddtime;
    }

    public String getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(String addressStatus) {
        this.addressStatus = addressStatus;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getAddressMobile() {
        return addressMobile;
    }

    public void setAddressMobile(String addressMobile) {
        this.addressMobile = addressMobile;
    }

    public String getAddressTelephone() {
        return addressTelephone;
    }

    public void setAddressTelephone(String addressTelephone) {
        this.addressTelephone = addressTelephone;
    }

    public String getAddressTruename() {
        return addressTruename;
    }

    public void setAddressTruename(String addressTruename) {
        this.addressTruename = addressTruename;
    }

    public String getAddressZip() {
        return addressZip;
    }

    public void setAddressZip(String addressZip) {
        this.addressZip = addressZip;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDaddress() {
        return daddress;
    }

    public void setDaddress(String daddress) {
        this.daddress = daddress;
    }
}


