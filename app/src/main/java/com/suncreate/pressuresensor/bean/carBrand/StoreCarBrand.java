package com.suncreate.pressuresensor.bean.carBrand;


import java.io.Serializable;

/**
 * Created by JINWENLIN.
 * On 2016/11/26.
 */

public class StoreCarBrand implements Serializable {

    /**
     * 品牌ID
     **/
    private String id;
    /**
     * 品牌名称
     **/
    private String brandName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
/**
 * 品牌首字母
 **/
}