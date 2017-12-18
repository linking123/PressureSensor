package com.suncreate.pressuresensor.bean;


public class City {
    /**
     * 城市编码
     */
    private String cityCode;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 城市拼音
     */
    private String fullName;

    public City(String cityName, String fullName) {
        super();
        this.cityName = cityName;
        this.fullName = fullName;
    }

    public City(String cityCode, String cityName, String fullName) {
        super();
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.fullName = fullName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


}
