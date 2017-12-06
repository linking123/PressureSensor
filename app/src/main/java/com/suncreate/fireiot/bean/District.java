package com.suncreate.fireiot.bean;


public class District {
    /**
     * 区县编码
     */
    private String areaCode;
    /**
     * 区县名称
     */
    private String areaName;

    public District(String areaCode, String areaName) {
        super();
        this.areaCode = areaCode;
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

}
