package com.suncreate.fireiot.bean.citypicker;

/**
 * Created by JINWENLIN.
 * On 2016/12/30.
 */

public class CityWebSites
{
    /**区域编号主键**/
    private String id;
    /**分站编号**/
    private String websiteId;
    /**分站添加时间**/
    private String websiteAddtime;
    /**分站名称**/
    private String websiteName;
    /**分站首字母**/
    private String websiteLetter;
    /**分站二级域名**/
    private String websiteSld;
    /**区域编号**/
    private String areaId;
    /**分站状态**/
    private String websiteStatus;
    /**是否默认分站**/
    private String websiteIsDefault;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(String websiteId) {
        this.websiteId = websiteId;
    }

    public String getWebsiteAddtime() {
        return websiteAddtime;
    }

    public void setWebsiteAddtime(String websiteAddtime) {
        this.websiteAddtime = websiteAddtime;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsiteLetter() {
        return websiteLetter;
    }

    public void setWebsiteLetter(String websiteLetter) {
        this.websiteLetter = websiteLetter;
    }

    public String getWebsiteSld() {
        return websiteSld;
    }

    public void setWebsiteSld(String websiteSld) {
        this.websiteSld = websiteSld;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getWebsiteStatus() {
        return websiteStatus;
    }

    public void setWebsiteStatus(String websiteStatus) {
        this.websiteStatus = websiteStatus;
    }

    public String getWebsiteIsDefault() {
        return websiteIsDefault;
    }

    public void setWebsiteIsDefault(String websiteIsDefault) {
        this.websiteIsDefault = websiteIsDefault;
    }

}
