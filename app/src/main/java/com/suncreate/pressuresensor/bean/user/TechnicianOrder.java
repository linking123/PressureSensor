package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;

/**
 * Created by zhangjinguo on 2016/11/14.
 */

public class TechnicianOrder implements Serializable {

    /**技师编号**/
    private String id;
    /**店铺编号**/
    private long storeId;
    /**服务类别MAINTENANCE-保养修护 CREPAIRS-汽车维修 BDECORATION-美容装饰 IALTERATION-安装改装**/
    private String storeSerivceType;
    /**车型名称**/
    private String carmodelName;
    /**用户状态1锁定0正常**/
    private String userStatus;
    /**店铺状态0-未开通；1-审核中；2-正常；3-已关闭**/
    private String storeStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
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

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }
}
