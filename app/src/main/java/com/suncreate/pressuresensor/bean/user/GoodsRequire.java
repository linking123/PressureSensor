package com.suncreate.pressuresensor.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenzhao on 17-1-5.
 */

public class GoodsRequire implements Parcelable {
    /**
     * 配件分类Id
     **/
    private String gcId;
    /**
     * 配件分类名称
     */
    private String gcName;
    /**
     * 配件名称
     **/
    private String partsName;
    /**
     * 配件数量
     **/
    private String partsCount;

    public GoodsRequire(Parcel in) {
        gcId = in.readString();
        gcName = in.readString();
        partsName = in.readString();
        partsCount = in.readString();
    }

    public static final Creator<GoodsRequire> CREATOR = new Creator<GoodsRequire>() {
        @Override
        public GoodsRequire createFromParcel(Parcel in) {
            return new GoodsRequire(in);
        }

        @Override
        public GoodsRequire[] newArray(int size) {
            return new GoodsRequire[size];
        }
    };

    public GoodsRequire() {

    }

    public String getGcId() {
        return gcId;
    }

    public void setGcId(String gcId) {
        this.gcId = gcId;
    }

    public String getGcName() {
        return gcName;
    }

    public void setGcName(String gcName) {
        this.gcName = gcName;
    }

    public String getPartsName() {
        return partsName;
    }

    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    public String getPartsCount() {
        return partsCount;
    }

    public void setPartsCount(String partsCount) {
        this.partsCount = partsCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gcId);
        dest.writeString(gcName);
        dest.writeString(partsName);
        dest.writeString(partsCount);
    }
}
