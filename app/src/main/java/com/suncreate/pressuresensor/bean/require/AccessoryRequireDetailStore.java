package com.suncreate.pressuresensor.bean.require;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenzhao on 17-1-7.
 */

public class AccessoryRequireDetailStore implements Serializable {
    /**
     * 配件商店铺主键
     **/
    private long stroeId;
    /**
     * 配件商店铺名称
     **/
    private String storeName;
    /**
     * 配件商报价时间
     **/
    private Long addTime;
    /**
     * 询价单单编号
     **/
    private String inquiryNo;
    /**
     * 状态:0:已失效;1:待报价;2:已报价待确认;3:已确认;4:已拒绝
     **/
    private String status;

    /**
     * 报价配件信息列表
     */
    private List<AccessoryRequireDetailStoreGoods> items;

    public long getStroeId() {
        return stroeId;
    }

    public void setStroeId(long stroeId) {
        this.stroeId = stroeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getInquiryNo() {
        return inquiryNo;
    }

    public void setInquiryNo(String inquiryNo) {
        this.inquiryNo = inquiryNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AccessoryRequireDetailStoreGoods> getItems() {
        return items;
    }

    public void setItems(List<AccessoryRequireDetailStoreGoods> items) {
        this.items = items;
    }
}
