package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;

/**
 * Created by xushengji on 2016/11/14.
 */

public class GoodsBrand implements Serializable {

    /**品牌ID**/
    private Long id;
    /**品牌名称**/
    private String goodsbrandName;
    /**分类id**/
    private String classId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsbrandName() {
        return goodsbrandName;
    }

    public void setGoodsbrandName(String goodsbrandName) {
        this.goodsbrandName = goodsbrandName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
