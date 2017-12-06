package com.suncreate.fireiot.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xushengji on 2016/11/14.
 */

public class GoodsOrderForTrans implements Serializable {
    /**运费列表**/
    private List<SubmitGoodsOrder> items;

    public List<SubmitGoodsOrder> getItems() {
        return items;
    }

    public void setItems(List<SubmitGoodsOrder> items) {
        this.items = items;
    }
}
