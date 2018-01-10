package com.suncreate.pressuresensor.bean.user;

import java.io.Serializable;

/**
 * Created by xushengji on 2016/11/14.
 */

public class PhoneCheck implements Serializable {

    /**验证手机号返回验证码key**/
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
