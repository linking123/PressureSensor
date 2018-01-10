package com.suncreate.pressuresensor.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenzhao on 16-12-27.
 */

public class QuoteSettleProjectGoods implements Parcelable {
    private Integer id;
    /**配件名称**/
    private String goodsName;
    /**配件价格**/
    private Double goodsPrice;
    /**配件数量**/
    private Integer goodsCount;
    /**配件费**/
    private String quoteGoodsAmount;
    /**工时费**/
    private String workingPrice;

    protected QuoteSettleProjectGoods(Parcel in) {
        goodsName = in.readString();
        quoteGoodsAmount = in.readString();
        workingPrice = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goodsName);
        dest.writeString(quoteGoodsAmount);
        dest.writeString(workingPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuoteSettleProjectGoods> CREATOR = new Creator<QuoteSettleProjectGoods>() {
        @Override
        public QuoteSettleProjectGoods createFromParcel(Parcel in) {
            return new QuoteSettleProjectGoods(in);
        }

        @Override
        public QuoteSettleProjectGoods[] newArray(int size) {
            return new QuoteSettleProjectGoods[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getQuoteGoodsAmount() {
        return quoteGoodsAmount;
    }

    public void setQuoteGoodsAmount(String quoteGoodsAmount) {
        this.quoteGoodsAmount = quoteGoodsAmount;
    }

    public String getWorkingPrice() {
        return workingPrice;
    }

    public void setWorkingPrice(String workingPrice) {
        this.workingPrice = workingPrice;
    }
}
