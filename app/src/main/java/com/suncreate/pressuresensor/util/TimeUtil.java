package com.suncreate.pressuresensor.util;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author HuangWenwei
 * @date 2014年10月9日
 */
public class TimeUtil {

    /**
     * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        boolean defaultVaule = true;
        if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
            defaultVaule = true;
        else
            defaultVaule = false;
        return defaultVaule;
    }

    /**
     * 根据不同时区，转换时间 2014年7月31日
     *
     * @return
     */
    public static Date transformTime(Date date, TimeZone oldZone, TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime())
                    - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }

    public static String getTime(int time) {
        int seconds = time % 60;
        time = time / 60;
        int minutes = time % 60;
        time = time / 60;
        int hour = time;
        String ss = seconds < 10 ? "0" + seconds : seconds + "";
        String mm = minutes < 10 ? "0" + minutes : minutes + "";
        String hh = hour < 10 ? "0" + hour : hour + "";
        return hh + ":" + mm + ":" + ss;
    }

    public static int getIntTime(String time) {
        if (time != null) {
            String timeString = time.trim();
            String[] strings = timeString.split(":");
            return Integer.valueOf(strings[0]) * 60 * 60 + Integer.valueOf(strings[1]) *
                    60 + Integer.valueOf(strings[2]);
        }
        return 0;
    }
}
