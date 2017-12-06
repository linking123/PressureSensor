package com.suncreate.fireiot.bean.carBrand;


import com.suncreate.fireiot.bean.Entity;

import java.io.Serializable;

/**
 * Created by JINWENLIN.
 * On 2016/11/26.
 */

public class CarBrand extends Entity implements Serializable {

    /**
     * 品牌ID
     **/
//    private String id;
    /**
     * 品牌名称
     **/
    private String brandName;
    /**
     * 品牌首字母
     **/
    private String brandLetter;
    /**
     * 1品牌/2车型经销商
     */
    private String brandType;
    /**
     * 品牌图标
     */
    private String brandIcon;
    /**
     * 国系
     **/
    private String brandCountryId;
    /**
     * 热门品牌0：否，1：是
     **/
    private String brandHot;
    /**
     * 删除标识：锁定1，正常0
     **/
    private String brandStatus;
    /**
     * 上级
     **/
    private String brandParentId;
    /**
     * 添加时间
     **/
    private String brandAddtime;

//    public String getId() {
//        return id;
//    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandLetter() {
        return brandLetter;
    }

    public void setBrandLetter(String brandLetter) {
        this.brandLetter = brandLetter;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public String getBrandIcon() {
        return brandIcon;
    }

    public void setBrandIcon(String brandIcon) {
        this.brandIcon = brandIcon;
    }

    public String getBrandCountryId() {
        return brandCountryId;
    }

    public void setBrandCountryId(String brandCountryId) {
        this.brandCountryId = brandCountryId;
    }

    public String getBrandHot() {
        return brandHot;
    }

    public void setBrandHot(String brandHot) {
        this.brandHot = brandHot;
    }

    public String getBrandStatus() {
        return brandStatus;
    }

    public void setBrandStatus(String brandStatus) {
        this.brandStatus = brandStatus;
    }

    public String getBrandParentId() {
        return brandParentId;
    }

    public void setBrandParentId(String brandParentId) {
        this.brandParentId = brandParentId;
    }

    public String getBrandAddtime() {
        return brandAddtime;
    }

    public void setBrandAddtime(String brandAddtime) {
        this.brandAddtime = brandAddtime;
    }
}
