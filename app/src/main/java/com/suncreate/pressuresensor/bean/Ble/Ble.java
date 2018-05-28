package com.suncreate.pressuresensor.bean.Ble;

import java.io.Serializable;

/**
 * Created by linking on 1/14/18.
 */

public class Ble implements Serializable {

    /* id */
    private int id;
    /* 名称 */
    private String bleName;
    /* 状态 */
    private int bleStatus;
    /* 状态 */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBleName() {
        return bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }

    public int getBleStatus() {
        return bleStatus;
    }

    public void setBleStatus(int bleStatus) {
        this.bleStatus = bleStatus;
    }
}
