package com.suncreate.pressuresensor.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.suncreate.pressuresensor.bean.Entity;

/**
 * 服务订单
 * Created by zhangjinguo on 2016/11/29.
 */

public class ServicesOrder extends Entity implements Parcelable {
    /**添加时间(预约时间)**/
    private long orderAppointTimeStart;
    /**订单状态，删除状态-1 已取消 0待接受 10 待服务 20 服务中 30 待结算 40 已结算 50  (等待买家评价)已评价 60（买家已评价）已完成 70**/
    private int orderStatus;
    /**价格**/
    private double orderTotalprice;
    /**技师/店铺名称**/
    private String storeName;
    /**技师/店铺头像**/
    private long image;
    /**车主名称**/
    private String userName;
    /**车主头像**/
    private long userPhoto;
    /**技师报价上传相关状态，0-都未上传，1-报价单已上传，2-服务前照片已上传，3-服务后照片已上传，4-结算单已上传×**/
    private Integer photoStatus;
    /**车主Id**/
    private String userId;

    protected ServicesOrder(Parcel in) {
        orderAppointTimeStart = in.readLong();
        orderStatus = in.readInt();
        orderTotalprice = in.readDouble();
        storeName = in.readString();
        image = in.readLong();
        userName = in.readString();
        userPhoto = in.readLong();
        userId = in.readString();
    }

    public static final Creator<ServicesOrder> CREATOR = new Creator<ServicesOrder>() {
        @Override
        public ServicesOrder createFromParcel(Parcel in) {
            return new ServicesOrder(in);
        }

        @Override
        public ServicesOrder[] newArray(int size) {
            return new ServicesOrder[size];
        }
    };

    public long getOrderAppointTimeStart() {
        return orderAppointTimeStart;
    }

    public void setOrderAppointTimeStart(long orderAppointTimeStart) {
        this.orderAppointTimeStart = orderAppointTimeStart;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getOrderTotalprice() {
        return orderTotalprice;
    }

    public void setOrderTotalprice(double orderTotalprice) {
        this.orderTotalprice = orderTotalprice;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public long getImage() {
        return image;
    }

    public void setImage(long image) {
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(long userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Integer getPhotoStatus() {
        return photoStatus;
    }

    public void setPhotoStatus(Integer photoStatus) {
        this.photoStatus = photoStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(orderAppointTimeStart);
        dest.writeInt(orderStatus);
        dest.writeDouble(orderTotalprice);
        dest.writeString(storeName);
        dest.writeLong(image);
        dest.writeString(userName);
        dest.writeLong(userPhoto);
        dest.writeString(userId);
    }
}
