package com.suncreate.fireiot.bean;

/**
 * Created by huanghaibin
 * on 16-5-23.
 */
public class Banner extends Base {
    public static final int BANNER_TYPE_URL = 0;//链接新闻

    //广告ID
    private String id;
    //是否黄金广告，0否1是
    private String adGold;
    //附件ID
    private String adAccId;
    //广告名称
    private String adTitle;
    //广告链接
    private String adUrl;
    //广告文字
    private String adText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdGold() {
        return adGold;
    }

    public void setAdGold(String adGold) {
        this.adGold = adGold;
    }

    public String getAdAccId() {
        return adAccId;
    }

    public void setAdAccId(String adAccId) {
        this.adAccId = adAccId;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getAdText() {
        return adText;
    }

    public void setAdText(String adText) {
        this.adText = adText;
    }

}
