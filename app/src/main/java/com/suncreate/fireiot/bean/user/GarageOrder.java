package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by zhangjinguo on 2016/11/14.
 */

public class GarageOrder implements Serializable {

    /**快修站ID**/
    private Long id;
    /**服务类别 MAINTENANCE-保养修护 CREPAIRS-汽车维修 BDECORATION-美容装饰 IALTERATION-安装改装**/
    private String storeSerivceType;
    /**车型名称**/
    private String carmodelName;
    /**店铺地址**/
    private String storeAddress;
    /**店铺状态0-未开通；1-审核中；2-正常；3-已关闭**/
    private int storeStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreSerivceType() {
        return storeSerivceType;
    }

    public void setStoreSerivceType(String storeSerivceType) {
        this.storeSerivceType = storeSerivceType;
    }

    public String getCarmodelName() {
        return carmodelName;
    }

    public void setCarmodelName(String carmodelName) {
        this.carmodelName = carmodelName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public int getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(int storeStatus) {
        this.storeStatus = storeStatus;
    }
}
