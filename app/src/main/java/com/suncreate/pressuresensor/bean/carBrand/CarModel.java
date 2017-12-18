package com.suncreate.pressuresensor.bean.carBrand;


import java.io.Serializable;
import java.util.List;

/**
 * Created by JINWENLIN.
 * On 2016/11/26.
 *
 * 多个页面公用，某个字段在不同的场景代表不同的意思
 */

public class CarModel implements Serializable {

    /**
     * 用户车型ID,或品牌ID
     **/
    private String id;
    /**
     * 品牌ID
     **/
    private String brandBrand;
    /**
     * 品牌ID
     **/
    private String brandId;
    /**
     * 车型ID
     **/
    private String carmodelId;
    /**
     * 品牌/车型经销商/车系/车型名称
     **/
    private String carbrandName;
    /**
     * 车品牌
     **/
    private String carbrandBrand;
    /**
     * 车型名称
     **/
    private String carmodelName;
    /**
     * 车型经销商名称，或品牌名称
     */
    private String brandName;
    /**
     * 车型经销商名称
     */
    private String brandBrandTypeName;
    /**
     * 车型经销商ID
     */
    private String carbrandBrandType;
    /**
     * 车型经销商ID
     */
    private String brandBrandType;
    /**
     * 车型图片
     */
    private String brandCarIcon;
    /**
     * 车型图片
     */
    private String brandIcon;
    /**
     * 年份
     */
    private String carbrandYear;
    /**
     * 年份
     */
    private String brandYear;
    /**
     * 排量
     */
    private String carbrandDisplacement;
    /**
     * 排量
     */
    private String brandDisplacement;
    /**
     * 品牌状态
     */
    private String brandStatus;
    /**
     * 品牌首字母
     */
    private String brandLetter;
    /**
     * 品牌添加时间
     */
    private String brandAddtime;

    /**
     * 车型对象  集合属性
     */
    private List<CarModel> items;

    private String chassisNumber;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCarBrandName() {
        return carbrandName;
    }

    public void setCarBrandName(String carBrandName) {
        this.carbrandName = carBrandName;
    }

    public String getCarbrandBrand() {
        return carbrandBrand;
    }

    public void setCarbrandBrand(String carbrandBrand) {
        this.carbrandBrand = carbrandBrand;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getCarbrandName() {
        return carbrandName;
    }

    public void setCarbrandName(String carbrandName) {
        this.carbrandName = carbrandName;
    }

    public String getCarbrandBrandType() {
        return carbrandBrandType;
    }

    public void setCarbrandBrandType(String carbrandBrandType) {
        this.carbrandBrandType = carbrandBrandType;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCarbrandYear() {
        return carbrandYear;
    }

    public void setCarbrandYear(String carbrandYear) {
        this.carbrandYear = carbrandYear;
    }

    public String getCarbrandDisplacement() {
        return carbrandDisplacement;
    }

    public void setCarbrandDisplacement(String carbrandDisplacement) {
        this.carbrandDisplacement = carbrandDisplacement;
    }

    public List<CarModel> getItems() {
        return items;
    }

    public void setItems(List<CarModel> items) {
        this.items = items;
    }

    public String getBrandBrandType() {
        return brandBrandType;
    }

    public void setBrandBrandType(String brandBrandType) {
        this.brandBrandType = brandBrandType;
    }

    public String getBrandBrand() {
        return brandBrand;
    }

    public void setBrandBrand(String brandBrand) {
        this.brandBrand = brandBrand;
    }

    public String getBrandBrandTypeName() {
        return brandBrandTypeName;
    }

    public void setBrandBrandTypeName(String brandBrandTypeName) {
        this.brandBrandTypeName = brandBrandTypeName;
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

    public String getBrandCarIcon() {
        return brandCarIcon;
    }

    public void setBrandCarIcon(String brandCarIcon) {
        this.brandCarIcon = brandCarIcon;
    }

    public String getBrandIcon() {
        return brandIcon;
    }

    public void setBrandIcon(String brandIcon) {
        this.brandIcon = brandIcon;
    }

    public String getBrandYear() {
        return brandYear;
    }

    public void setBrandYear(String brandYear) {
        this.brandYear = brandYear;
    }

    public String getBrandDisplacement() {
        return brandDisplacement;
    }

    public void setBrandDisplacement(String brandDisplacement) {
        this.brandDisplacement = brandDisplacement;
    }

    public String getBrandStatus() {
        return brandStatus;
    }

    public void setBrandStatus(String brandStatus) {
        this.brandStatus = brandStatus;
    }

    public String getBrandLetter() {
        return brandLetter;
    }

    public void setBrandLetter(String brandLetter) {
        this.brandLetter = brandLetter;
    }

    public String getBrandAddtime() {
        return brandAddtime;
    }

    public void setBrandAddtime(String brandAddtime) {
        this.brandAddtime = brandAddtime;
    }

    public String getCarModelName() {
        return carmodelName;
    }

    public void setCarModelName(String carModelName) {
        this.carmodelName = carModelName;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }
}
