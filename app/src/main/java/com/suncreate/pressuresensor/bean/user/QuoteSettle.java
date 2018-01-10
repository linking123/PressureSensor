package com.suncreate.pressuresensor.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by chenzhao on 16-12-26.
 * 报价单/结算单明细
 */

public class QuoteSettle implements Parcelable {
    /**编号**/
    private String id;
    /**1.报价单，2.结算单**/
    private String type;
    /**工位费**/
    private String placeAmount;
    /**辅料费**/
    private String accessoriesAmount;
    /**总工时费**/
    private String workingAmount;
    /**总配件费**/
    private String goodsAmount;
    /**合计费用**/
    private String totalAmount;
    /**作业项目集合**/
    private List<QuoteSettleProject> items;
    /**报价单时间**/
    private Long orderQuotationTime;

    public QuoteSettle(Parcel in) {
        id = in.readString();
        type = in.readString();
        placeAmount = in.readString();
        accessoriesAmount = in.readString();
        workingAmount = in.readString();
        goodsAmount = in.readString();
        totalAmount = in.readString();
        items = in.createTypedArrayList(QuoteSettleProject.CREATOR);
    }

    public static final Creator<QuoteSettle> CREATOR = new Creator<QuoteSettle>() {
        @Override
        public QuoteSettle createFromParcel(Parcel in) {
            return new QuoteSettle(in);
        }

        @Override
        public QuoteSettle[] newArray(int size) {
            return new QuoteSettle[size];
        }
    };

    public QuoteSettle() {

    }

    public Long getOrderQuotationTime() {
        return orderQuotationTime;
    }

    public void setOrderQuotationTime(Long orderQuotationTime) {
        this.orderQuotationTime = orderQuotationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlaceAmount() {
        return placeAmount;
    }

    public void setPlaceAmount(String placeAmount) {
        this.placeAmount = placeAmount;
    }

    public String getAccessoriesAmount() {
        return accessoriesAmount;
    }

    public void setAccessoriesAmount(String accessoriesAmount) {
        this.accessoriesAmount = accessoriesAmount;
    }

    public String getWorkingAmount() {
        return workingAmount;
    }

    public void setWorkingAmount(String workingAmount) {
        this.workingAmount = workingAmount;
    }

    public String getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(String goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<QuoteSettleProject> getItems() {
        return items;
    }

    public void setItems(List<QuoteSettleProject> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(placeAmount);
        dest.writeString(accessoriesAmount);
        dest.writeString(workingAmount);
        dest.writeString(goodsAmount);
        dest.writeString(totalAmount);
        dest.writeTypedList(items);
    }
}

