package com.suncreate.fireiot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类型检查
 *
 * @author chenzhao
 *         created on 2015/12/22 13:36
 */
public class TypeChk {
    public String message;

    public TypeChk() {
        this.message = "";
    }

    /**
     * 检查日期
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return true为是
     */
    public static boolean checkDate(String year, String month, String day) {
        if (!(StrPro.isDigitStr(year, 4, 4)))
            return false;
        int iyear = Integer.parseInt(year);
        if ((iyear < 1900) || (iyear > 2100)) {
            return false;
        }

        if (!(StrPro.isDigitStr(month, 2, 2)))
            return false;
        int imonth = Integer.parseInt(month);
        if ((imonth < 1) || (imonth > 12)) {
            return false;
        }

        if (!(StrPro.isDigitStr(day, 1, 31)))
            return false;
        int iday = Integer.parseInt(day);
        if ((iday < 1) || (iday > 31)) {
            return false;
        }
        return (checkMonthLength(imonth, iday, iyear));
    }

    /**
     * 检查是否为日期
     *
     * @param date 日期字符串
     * @return true为是
     */
    public static boolean checkDateStr(String date) {
        if ((date == null) || (date == ""))
            return false;
        if (date.length() < 10)
            return false;

        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8);

        return checkDate(year, month, day);
    }

    /**
     * 检查是否为日期
     *
     * @param dateStr 日期字符串
     * @return true为是
     */
    public static boolean checkDateTime(String dateStr) {
        if (checkNull(dateStr))
            return false;
        if (dateStr.length() < 19)
            return false;

        String year = dateStr.substring(0, 4);
        if (!(StrPro.isDigitStr(year, 4, 4)))
            return false;
        int iyear = Integer.parseInt(year);
        if ((iyear < 1900) || (iyear > 2100)) {
            return false;
        }

        String month = dateStr.substring(5, 7);
        if (!(StrPro.isDigitStr(month, 2, 2)))
            return false;
        int imonth = Integer.parseInt(month);
        if ((imonth < 1) || (imonth > 12)) {
            return false;
        }

        String day = dateStr.substring(8, 10);
        if (!(StrPro.isDigitStr(day, 1, 31)))
            return false;
        int iday = Integer.parseInt(day);
        if ((iday < 1) || (iday > 31)) {
            return false;
        }
        String hour = dateStr.substring(11, 13);
        if (!(StrPro.isDigitStr(hour, 2, 2)))
            return false;
        int ihour = Integer.parseInt(hour);
        if ((ihour < 0) || (ihour > 24)) {
            return false;
        }
        String minute = dateStr.substring(14, 16);
        if (!(StrPro.isDigitStr(minute, 2, 2)))
            return false;
        int iminute = Integer.parseInt(minute);
        if ((iminute < 0) || (iminute > 60)) {
            return false;
        }
        String second = dateStr.substring(17);
        if (!(checkFloat(second)))
            return false;
        float isecond = Float.parseFloat(second);
        if ((isecond < 0.0F) || (isecond > 60.0F)) {
            return false;
        }
        String tmp = dateStr.substring(4, 5);
        if (!(tmp.equals("-")))
            return false;

        tmp = dateStr.substring(7, 8);
        if (!(tmp.equals("-")))
            return false;

        tmp = dateStr.substring(10, 11);
        if (!(tmp.equals(" ")))
            return false;

        tmp = dateStr.substring(13, 14);
        if (!(tmp.equals(":")))
            return false;

        tmp = dateStr.substring(16, 17);
        return (tmp.equals(":"));
    }

    /**
     * 检查是否为Email
     *
     * @param emailAddress email地址
     * @return true为是
     */
    public static boolean checkEmail(String emailAddress) {
        if ((emailAddress == null) || (emailAddress == ""))
            return false;
        int dot = 0;
        int at = 0;
        int end = 0;
        for (int i = 0; i < emailAddress.length(); ++i) {
            if (emailAddress.charAt(i) == '@')
                ++at;
            if (emailAddress.charAt(i) != '.')
                continue;
            ++dot;
        }
        if (emailAddress.charAt(emailAddress.length() - 1) != '.')
            end = 1;
        return ((at == 1) && (dot > 0) && (end == 1));
    }

    /**
     * 检查数组里的数据是否都为空
     *
     * @param strArray 数组
     * @return true表示数组里的数据都不为空
     */
    public static boolean checkEmpty(String[] strArray) {
        for (int i = 0; i < strArray.length; ++i) {
            if ((strArray[i] == null) || (strArray[i].length() == 0))
                return false;
        }
        return true;
    }

    public static boolean checkEng(String str) {
        String s = str;
        for (int i = 0; i < s.length(); ++i) {
            if ((((s.charAt(i) < '0') || (s.charAt(i) > '9'))) && (((s.charAt(i) < 'a') || (s.charAt(i) > 'z')))
                    && (((s.charAt(i) < 'A') || (s.charAt(i) > 'Z')))
                    && (((s.charAt(i) != '_') || (s.charAt(i) != '.'))))
                return false;
        }
        return true;
    }

    /**
     * 检查是否为浮点类型
     *
     * @param str 字符串
     * @return true为是
     */
    public static boolean checkFloat(String str) {
        boolean success = false;
        try {
            @SuppressWarnings("unused")
            Float tmp = new Float(str);
            success = true;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    /**
     * 检查是否为整形，并且长度是否在最大值与最小值之前
     *
     * @param str    字符串
     * @param maxLen 长度最大值
     * @param minLen 长度最小值
     * @return
     */
    public static boolean checkInteger(String str, int maxLen, int minLen) {
        if (str == null)
            return false;
        if ((str.length() < minLen) || (str.length() > maxLen)) {
            return false;
        }
        for (int i = 0; i < str.length(); ++i) {
            char temp = str.charAt(i);
            if ((temp < '0') || (temp > '9'))
                return false;
        }
        return true;
    }

    /**
     * 检查是否为整型
     *
     * @param str 字符串
     * @return true为整型
     */
    public static boolean checkInteger(String str) {
        if (str == null)
            return false;

        for (int i = 0; i < str.length(); ++i) {
            char temp = str.charAt(i);
            if ((temp < '0') || (temp > '9'))
                return false;
        }
        return true;
    }

    /**
     * 检查是否为金钱类型
     *
     * @param str 字符串
     * @return
     */
    public static boolean checkMoney(String str) {
        if (str == null)
            return false;
        if (str.length() < 1)
            return false;
        int pos;
        if ((pos = str.indexOf(".")) != -1) {
            if (str.length() == 1)
                return false;
            if ((pos = str.indexOf(".", pos + 1)) != -1) {
                return false;
            }
        }
        for (int i = 0; i < str.length(); ++i) {
            if ((((str.charAt(i) < '0') || (str.charAt(i) > '9'))) && (str.charAt(i) != '.'))
                return false;
        }
        return true;
    }

    /**
     * 检查日期是否有效
     *
     * @param month 月
     * @param day   日
     * @param year  年
     * @return true 日期有效
     */
    public static boolean checkMonthLength(int month, int day, int year) {
        if ((((month == 4) || (month == 6) || (month == 9) || (month == 11))) && (day > 30))
            return false;
        if (month == 2) {
            if ((year % 4 > 0) && (day > 28))
                return false;
            if ((year == 2000) && (day > 29))
                return false;
            if ((year % 100 > 0) && (day > 28))
                return false;
        } else if ((day > 31) || (day < 1)) {
            return false;
        }
        return ((month <= 12) && (month >= 1));
    }

    /**
     * 查询是否为null域空
     *
     * @param str
     * @return
     */
    public static boolean checkNull(String str) {
        return ((str == null) || (str.trim().equals("")) || (str.trim().equals("null")));
    }

    /**
     * 检查字符串的长度，并且长度是否在最大长度与最小长度之前
     *
     * @param str    字符串
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @return
     */
    public static boolean checkStrLen(String str, int minLen, int maxLen) {
        if (str == null)
            return false;
        int strLength = getStrLength(str);

        return ((strLength >= minLen) && (strLength <= maxLen));
    }

    /**
     * 检查是否为时间类型
     *
     * @param timeStr 时间
     * @return
     */
    public static boolean checkTime(String timeStr) {
        if (checkNull(timeStr))
            return false;
        if (timeStr.length() < 8)
            return false;

        String hour = timeStr.substring(0, 2);
        if (!(StrPro.isDigitStr(hour, 2, 2)))
            return false;
        int ihour = Integer.parseInt(hour);
        if ((ihour < 0) || (ihour > 23)) {
            return false;
        }
        String minute = timeStr.substring(3, 5);
        if (!(StrPro.isDigitStr(minute, 2, 2)))
            return false;
        int iminute = Integer.parseInt(minute);
        if ((iminute < 0) || (iminute > 60)) {
            return false;
        }
        String second = timeStr.substring(6);
        if (!(checkFloat(second)))
            return false;
        float isecond = Float.parseFloat(second);
        if ((isecond < 0.0F) || (isecond > 60.0F)) {
            return false;
        }
        String tmp = timeStr.substring(2, 3);
        if (!(tmp.equals(":")))
            return false;

        tmp = timeStr.substring(5, 6);
        return (tmp.equals(":"));
    }

    /**
     * 检查是否为Y或N
     *
     * @param str 字符串
     * @return true 为正确
     */
    public static boolean checkYN(String str) {
        if (str == null)
            return false;
        return ((str.equalsIgnoreCase("Y")) || (str.equalsIgnoreCase("N")));
    }

    /**
     * 获取字符串长度
     *
     * @param str 字符串
     * @return
     */
    public static int getStrLength(String str) {
        String s = str;
        int len = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (((s.charAt(i) >= '0') && (s.charAt(i) <= '9')) || ((s.charAt(i) >= 'a') && (s.charAt(i) <= 'z'))
                    || ((s.charAt(i) >= 'A') && (s.charAt(i) <= 'Z')) || ((s.charAt(i) == '_') && (s.charAt(i) == '.')))
                continue;
            ++len;
        }
        return (str.length() - len + len * 2);
    }

    /**
     * 检查是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否为手机号
     *
     * @param num
     * @return
     */
    public static boolean isPhoneNum(String num) {
        String phoneRegex = "^[1][3,4,5,7,8][0-9]{9}$";
        if (null == num) {
            return false;
        } else {
            Pattern p = Pattern.compile(phoneRegex);
            Matcher m = p.matcher(num);
            return m.matches();
        }

    }
}