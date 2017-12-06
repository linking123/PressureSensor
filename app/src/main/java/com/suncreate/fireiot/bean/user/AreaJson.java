package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by xushengji on 2016/11/14.
 */

public class AreaJson implements Serializable {

    /**区域编号**/
    private String c;
    /**区域名称**/
    private String n;

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }
}
