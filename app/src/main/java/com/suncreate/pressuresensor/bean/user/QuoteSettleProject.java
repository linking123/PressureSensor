package com.suncreate.pressuresensor.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by chenzhao on 16-12-26.
 * 报价单/结算单明细
 */

public class QuoteSettleProject implements Parcelable {
    /**作业项目名**/
    private String quoteProjectName;
    /**小计**/
    private String subTotalPrice;
    /**z作业项目商品集合**/
    private List<QuoteSettleProjectGoods> items;


    public QuoteSettleProject(Parcel in) {
        quoteProjectName = in.readString();
        subTotalPrice = in.readString();
        items = in.createTypedArrayList(QuoteSettleProjectGoods.CREATOR);
    }

    public QuoteSettleProject() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quoteProjectName);
        dest.writeString(subTotalPrice);
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuoteSettleProject> CREATOR = new Creator<QuoteSettleProject>() {
        @Override
        public QuoteSettleProject createFromParcel(Parcel in) {
            return new QuoteSettleProject(in);
        }

        @Override
        public QuoteSettleProject[] newArray(int size) {
            return new QuoteSettleProject[size];
        }
    };
}
