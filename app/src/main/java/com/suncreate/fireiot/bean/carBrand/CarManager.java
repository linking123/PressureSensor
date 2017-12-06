package com.suncreate.fireiot.bean.carBrand;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JINWENLIN.
 * On 2016/12/8.
 */

public class CarManager implements Serializable {

    /**用户车辆编号**/
    private String id;
    /**车型ID（shopping_car_model）**/
    private String carmodelId;
    /**车品牌+车型+排量+年份**/
    private String carmodelName;
    /**车标**/
    private String brandIcon;
    /**选填，车牌如：皖A12345**/
    private String plate;
    /**发动机号**/
    private String engineNumber;
    /**车架号(VIN码)**/
    private String chassisNumber;
    /**公里数**/
    private String mileage;
    /**上路时间**/
    private String roadTime;
    /**是否默认1 代表默认  0为一般**/
    private String isDefault;

    /**
     * 车辆对象  集合属性
     */
    private List<CarManager> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarmodelId() {
        return carmodelId;
    }

    public void setCarmodelId(String carmodelId) {
        this.carmodelId = carmodelId;
    }

    public String getCarmodelName() {
        return carmodelName;
    }

    public void setCarmodelName(String carmodelName) {
        this.carmodelName = carmodelName;
    }

    public String getBrandIcon() {
        return brandIcon;
    }

    public void setBrandIcon(String brandIcon) {
        this.brandIcon = brandIcon;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getRoadTime() {
        return roadTime;
    }

    public void setRoadTime(String roadTime) {
        this.roadTime = roadTime;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public List<CarManager> getItems() {
        return items;
    }

    public void setItems(List<CarManager> items) {
        this.items = items;
    }
}
