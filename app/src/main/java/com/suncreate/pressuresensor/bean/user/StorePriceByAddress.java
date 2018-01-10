package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xushengji on 2016/11/14.
 */

public class StorePriceByAddress implements Serializable {

    /**店铺id**/
    private String storeId;
    /**地区Code**/
    private String addressCode;
    /**配件列表**/
    private List<PriceByAddress> goodsArray;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public List<PriceByAddress> getGoodsArray() {
        return goodsArray;
    }

    public void setGoodsArray(List<PriceByAddress> goodsArray) {
        this.goodsArray = goodsArray;
    }
}
