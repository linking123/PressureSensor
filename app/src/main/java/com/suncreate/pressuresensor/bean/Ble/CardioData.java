package com.suncreate.pressuresensor.bean.Ble;

/**
 * 包括时间  压力值 的实体类
 *
 * Created by linking on 5/11/18.
 */

public class CardioData {

    /**
     * 时间
     */
    private Float time;

    /**
     * 压力值
     */
    private Float pressureData;

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    public Float getPressureData() {
        return pressureData;
    }

    public void setPressureData(Float pressureData) {
        this.pressureData = pressureData;
    }
}
