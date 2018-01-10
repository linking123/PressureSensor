package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;

/**
 * Created by xushengji on 2016/11/14.
 */

public class GoodsType implements Serializable {
    /**商品分类id**/
    private Long id;
    /**上级id**/
    private String totalParentId;
    /**分类名称**/
    private String totalName;

    public String getTotalLevel() {
        return totalLevel;
    }

    public void setTotalLevel(String totalLevel) {
        this.totalLevel = totalLevel;
    }

    public String getTotalParentId() {
        return totalParentId;
    }

    public void setTotalParentId(String totalParentId) {
        this.totalParentId = totalParentId;
    }

    public String getTotalName() {
        return totalName;
    }

    public void setTotalName(String totalName) {
        this.totalName = totalName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**级别**/
    private String totalLevel;



}
