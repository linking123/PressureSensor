package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;

/**
 * Created by xushengji on 2016/11/14.
 */

public class Area implements Serializable {

    /**区域编号**/
    private Long xzqhId;
    /**区划名称**/
    private String xzqhName;
    /**编码**/
    private String xzqhCode;
    /**全拼**/
    private String xzqhFullSpell;
    /**简拼**/
    private String xzqhLetterSpell;
    /**查询字符串**/
    private String xzqhSearchString;
    /**上级编码**/
    private String xzqhParentCode;
    /**所属区域编码**/
    private String xzqhAreaCode;
    /**排序**/
    private Long xzqhPriority;
    /**standardCode**/
    private String standardCode;
    /**经度**/
    private double longitude;
    /**纬度**/
    private double latitude;
    /**编号**/
    private Long id;
    /**简称**/
    private String shortName;
    /**区域名称**/
    private String name;
    /**区域值**/
    private String value;
    /**上级编号**/
    private String parentID;

    public Long getXzqhId() {
        return xzqhId;
    }

    public void setXzqhId(Long xzqhId) {
        this.xzqhId = xzqhId;
    }

    public String getXzqhName() {
        return xzqhName;
    }

    public void setXzqhName(String xzqhName) {
        this.xzqhName = xzqhName;
    }

    public String getXzqhCode() {
        return xzqhCode;
    }

    public void setXzqhCode(String xzqhCode) {
        this.xzqhCode = xzqhCode;
    }

    public String getXzqhFullSpell() {
        return xzqhFullSpell;
    }

    public void setXzqhFullSpell(String xzqhFullSpell) {
        this.xzqhFullSpell = xzqhFullSpell;
    }

    public String getXzqhLetterSpell() {
        return xzqhLetterSpell;
    }

    public void setXzqhLetterSpell(String xzqhLetterSpell) {
        this.xzqhLetterSpell = xzqhLetterSpell;
    }

    public String getXzqhSearchString() {
        return xzqhSearchString;
    }

    public void setXzqhSearchString(String xzqhSearchString) {
        this.xzqhSearchString = xzqhSearchString;
    }

    public String getXzqhParentCode() {
        return xzqhParentCode;
    }

    public void setXzqhParentCode(String xzqhParentCode) {
        this.xzqhParentCode = xzqhParentCode;
    }

    public String getXzqhAreaCode() {
        return xzqhAreaCode;
    }

    public void setXzqhAreaCode(String xzqhAreaCode) {
        this.xzqhAreaCode = xzqhAreaCode;
    }

    public Long getXzqhPriority() {
        return xzqhPriority;
    }

    public void setXzqhPriority(Long xzqhPriority) {
        this.xzqhPriority = xzqhPriority;
    }

    public String getStandardCode() {
        return standardCode;
    }

    public void setStandardCode(String standardCode) {
        this.standardCode = standardCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }
}
