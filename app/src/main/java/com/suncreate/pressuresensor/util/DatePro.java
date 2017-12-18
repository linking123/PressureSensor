package com.suncreate.pressuresensor.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期处理类
 */
public class DatePro {

    public DatePro() {
    }

    /**
     * 月份补零操作,如果月份<=0或>12，则直接将月份转为字符串
     *
     * @param month 月份
     * @return 转换后的字符串
     */
    public static String switchMonth(int month) {
        switch (month) {
            case 1:
                return "01";
            case 2:
                return "02";
            case 3:
                return "03";
            case 4:
                return "04";
            case 5:
                return "05";
            case 6:
                return "06";
            case 7:
                return "07";
            case 8:
                return "08";
            case 9:
                return "09";
            case 10:
                return "10";
            case 11:
                return "11";
            case 12:
                return "12";
            default:
                return String.valueOf(month);
        }
    }

    /**
     * 获取日历对象
     *
     * @param thisYear  当前年份
     * @param thisMonth 当前月份
     * @return
     */
    public static Calendar getCalendar(String thisYear, String thisMonth) {
        GregorianCalendar currentDay = new GregorianCalendar();
        int month = currentDay.get(Calendar.MONTH);
        int year = currentDay.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        if (thisYear != "0" && thisMonth != "0") {
            year = Integer.parseInt(thisYear);
            month = Integer.parseInt(thisMonth);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.YEAR, year);
        } else {
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);
        }
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

    /**
     * 判断所给的日期为星期几
     *
     * @param date 日期 格式必须为：yyyy-MM-dd
     * @return
     */
    public static String getWeekday(String date) throws Exception {
        String[] x = date.split("-");
        int i = 0;
        if (Integer.parseInt(x[1]) == 0) {
            i = Integer.parseInt(x[1]) + 1;
        } else {
            i = Integer.parseInt(x[1]);
        }
        // 重新组装日期
        date = x[0] + "-" + i + "-" + x[2];
        if (date == "" || date == null) {
            return "";
        }
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdw = new SimpleDateFormat("E");
        Date d = null;
        try {
            d = sd.parse(date);
        } catch (ParseException e) {
            throw e;
        }
        return sdw.format(d);
    }

    /**
     * 获取日期是星期几,month从1到 12
     *
     * @param year  年份
     * @param month 从1到12
     * @param date  日
     * @return 星期几
     */
    public static int getWeekday(int year, int month, int date) {
        int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取在指定时间上加秒后的日期,如果加负数，则表示在指定的时间上减去多少秒
     *
     * @param dateHead 时间
     * @param second   秒
     * @return 相加后的时间
     */
    public static String addDateTime(String dateHead, int second) {
        if (!TypeChk.checkDateTime(dateHead)) {
            return null;
        } else {
            Date date1 = getDate(dateHead);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date1);
            calendar.add(Calendar.SECOND, second);
            date1 = calendar.getTime();
            return getDateTime(date1);
        }
    }

    /**
     * 获取在指定时间上加天后的日期,如果加负数，则表示在指定的时间上减去多少天
     *
     * @param dateHead 时间
     * @param day      天
     * @return 相加后的时间
     */
    public static String addDay(String dateHead, int day) throws Exception {
        if (!TypeChk.checkDateStr(dateHead)) {
            return null;
        } else {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = format.parse(dateHead);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date1);
            calendar.add(Calendar.DAY_OF_MONTH, day);
            date1 = calendar.getTime();
            return getDate(date1);
        }
    }

    /**
     * 获取日期的年龄
     *
     * @param dateHead 日期 格式为：yyyy-MM-dd
     * @return
     */
    public static long getAge(String dateHead) {
        if (TypeChk.checkNull(dateHead)) {
            return -1L;
        } else {
            dateHead = dateHead + " 00:00:00";
            String time = getDateTime();
            Date date1 = getDate(dateHead);
            Date date2 = getDate(time);
            long apple = date2.getTime() - date1.getTime();
            return apple / 1000L / 3600L / 24L / 365L;
        }
    }

    /**
     * 返回中文形式表示日期,如：2012年05月03日
     *
     * @param myDate 日期，格式:yyyy-MM-dd
     * @return 中文形式日期
     */
    public static String getChDate(String myDate) {
        String my_year;
        String my_month;
        String my_day;
        String dateString = "";
        if (!TypeChk.checkNull(myDate)) {
            my_year = myDate.substring(0, 4);
            if (myDate.substring(5, 6).equals("0"))
                my_month = myDate.substring(6, 7);
            else
                my_month = myDate.substring(5, 7);
            if (myDate.substring(8, 9).equals("0"))
                my_day = myDate.substring(9, 10);
            else
                my_day = myDate.substring(8, 10);
            dateString = my_year + "\u5E74" + my_month + "\u6708" + my_day + "\u65E5";
        }
        return dateString;
    }

    /**
     * 获取当前日期，并以中文形式表示,如：2012年05月03日
     *
     * @return 日期
     */
    public static String getChDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date currentTime_1 = new Date();
        String dateString = formatter.format(currentTime_1);
        return getChDate(dateString);
    }

    /**
     * 转换日期为中文表示方法,格式:yyyy年MM月dd日 HH时mm分
     *
     * @param myDate 需要转换的日期 格式：yyyy-MM-dd HH:mm:ss
     * @return 转换后的日期
     */
    public static String getChDateTime(String myDate) {
        String newDate = getChFullDateTime(myDate);
        if (!TypeChk.checkNull(newDate)) {
            if (newDate.indexOf("\u5206") > 0)
                return newDate.substring(0, newDate.indexOf("\u5206") + 1);
        }
        return "";
    }

    /**
     * 转换日期为中文表示形式,格式:yyyy年MM月dd日 HH时mm分ss秒
     *
     * @param myDate 需要转换的日期 格式：yyyy-MM-dd Hh:mm:ss
     * @return 转换后的日期
     */
    public static String getChFullDateTime(String myDate) {
        String my_year;
        String my_month;
        String my_day;
        String my_hour;
        String my_mi;
        String my_ss;
        String dateString = "";
        if (!TypeChk.checkNull(myDate)) {
            if (myDate.length() > 18) {
                my_year = myDate.substring(0, 4);
                if (myDate.substring(5, 6).equals("0"))
                    my_month = myDate.substring(6, 7);
                else
                    my_month = myDate.substring(5, 7);
                if (myDate.substring(8, 9).equals("0"))
                    my_day = myDate.substring(9, 10);
                else
                    my_day = myDate.substring(8, 10);
                if (myDate.substring(11, 12).equals("0"))
                    my_hour = myDate.substring(12, 13);
                else
                    my_hour = myDate.substring(10, 13);
                if (myDate.substring(14, 15).equals("0"))
                    my_mi = myDate.substring(15, 16);
                else
                    my_mi = myDate.substring(14, 16);
                if (myDate.substring(17, 18).equals("0"))
                    my_ss = myDate.substring(18, 19);
                else
                    my_ss = myDate.substring(17, 19);
                dateString = my_year + "\u5E74" + my_month + "\u6708" + my_day + "\u65E5" + my_hour + "\u65F6" + my_mi
                        + "\u5206" + my_ss + "\u79D2";
            }
        }
        return dateString;
    }

    /**
     * 获取日期为一年里的多少周
     *
     * @param date 日期
     * @return 周
     */
    @SuppressWarnings("deprecation")
    public static int getCurWeek(Date date) {
        int curMonth = date.getMonth();
        int curDay = date.getDay();
        int curYear = date.getYear();
        int sum = 0;
        for (int i = 1; i < curMonth + 1; i++)
            sum += (new Date(curYear, i, 0)).getDate();
        sum += curDay;
        int temp = (new Date(curYear, 0, 1)).getDay();
        if (temp == 0)
            temp = 7;
        if (temp != 1)
            sum -= 8 - temp;
        temp = sum % 7 != 0 ? (sum - sum % 7) / 7 + 1 : sum / 7;
        return temp;
    }

    /**
     * 获取日期为一年里的多少周
     *
     * @param iYear  年
     * @param iMonth 月
     * @param iDay   日
     * @return 周
     */
    @SuppressWarnings("deprecation")
    public static int getCurWeek(int iYear, int iMonth, int iDay) {
        int i = 1;
        int sum = 0;
        for (; i < iMonth; i++)
            sum += (new Date(iYear, i, 0)).getDate();

        sum += iDay;
        int temp = (new Date(iYear, 0, 1)).getDay();
        if (temp == 1)
            temp = 7;
        if (temp != 2)
            sum -= 8 - temp;
        temp = sum % 7 != 0 ? (sum - sum % 7) / 7 + 1 : sum / 7;
        return temp;
    }

    /**
     * 转换日期
     *
     * @param datetime 需要转换的日期
     * @return Date
     */
    public static Date getDate(String datetime) {
        Date date;
        int d[] = new int[3];
        int t[] = new int[3];
        String dateStr = datetime.substring(0, datetime.indexOf(' '));
        String timeStr = "";
        if (datetime.length() > 19)
            timeStr = datetime.substring(datetime.indexOf(' ') + 1, 19);
        else
            timeStr = datetime.substring(datetime.indexOf(' ') + 1, datetime.length());
        String ds[] = StrPro.divideString(dateStr, "-");
        String ts[] = StrPro.divideString(timeStr, ":");
        for (int ii = 0; ii < ds.length; ii++) {
            d[ii] = Integer.parseInt(ds[ii]);
            t[ii] = Integer.parseInt(ts[ii]);
        }
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(d[0], d[1] - 1, d[2], t[0], t[1], t[2]);
        date = calendar.getTime();
        return date;
    }

    /**
     * 格式化日期,格式：yyyy-MM-dd
     *
     * @param date 日期
     * @return String
     */
    public static String getDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取当前日期,格式:yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date currentTime_1 = new Date();
        String dateString = formatter.format(currentTime_1);
        return dateString;
    }

    /**
     * 格式化时间,格式:yyyy-MM-dd HH:mm:ss
     *
     * @param date 时间
     * @return String
     */
    public static String getDateTime(Date date) {
        if (null != date) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(date);
            return dateString;
        } else {
            return "";
        }
    }

    /**
     * 获取当前时间,格式为：yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static String getDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime_1 = new Date();
        String dateString = formatter.format(currentTime_1);
        return dateString;
    }

    /**
     * 获取两个时间相差的秒数
     *
     * @param dateHead
     * @param dateTail
     * @return
     */
    public static long getDateTimeTGap(String dateHead, String dateTail) {
        if (!TypeChk.checkDateTime(dateHead) || !TypeChk.checkDateTime(dateTail)) {
            return -1L;
        } else {
            Date date1 = getDate(dateHead);
            Date date2 = getDate(dateTail);
            long apple = date2.getTime() - date1.getTime();
            return apple / 1000L;
        }
    }

    /**
     * 获取日
     *
     * @param dateHead 日期，格式:yyyy-MM-dd
     * @return
     */
    public static long getDay(String dateHead) {
        if (TypeChk.checkNull(dateHead)) {
            return -1L;
        } else {
            dateHead = dateHead + " 00:00:00";
            String time = getDateTime();
            Date date1 = getDate(dateHead);
            Date date2 = getDate(time);
            long apple = date2.getTime() - date1.getTime();
            return apple / 1000L / 3600L / 24L;
        }
    }

    /**
     * 获取日期的天
     *
     * @param date 日期
     * @return 天
     */
    public static int getDay(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 获取小时
     *
     * @param date 日期
     * @return
     */
    public static int getHour(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    /**
     * 获取分钟
     *
     * @param date 日期
     * @return
     */
    public static int getMinute(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int minute = cal.get(Calendar.MINUTE);
        return minute;
    }

    /**
     * 获取月
     *
     * @param date 日期
     * @return
     */
    public static int getMonth(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取时间 格式：HH:mm:ss
     *
     * @return
     */
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime_1 = new Date();
        String timeString = formatter.format(currentTime_1);
        return timeString;
    }

    /**
     * 获取当前时间戳 格式：yyyy-MM-dd hh:mm:ss.SSS zzz
     *
     * @return
     */
    public static String getTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS zzz");
        Date currentTime_1 = new Date();
        return formatter.format(currentTime_1);
    }

    /**
     * 获取年份
     *
     * @param date 指定日期
     * @return
     */
    public static int getYear(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    /**
     * 获取指定月份的实际天数,month从1到12月
     *
     * @param year  年份
     * @param month 月份
     * @return
     */
    public static int getDayNum(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        int dayNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return dayNum;
    }

    /**
     * 判断两个日期是否为同一天
     *
     * @param d1 日期一
     * @param d2 日期二
     * @return 同一天true，不是同一天false
     */
    public static boolean isSameDate(Date d1, Date d2) {
        boolean result = false;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
            result = true;
        }
        return result;
    }

    /**
     * 判断两个日期是否为同一月
     *
     * @param d1 日期一
     * @param d2 日期二
     * @return 同一月true，不是同一月false
     */
    public static boolean isSameMonth(Date d1, Date d2) {
        boolean result = false;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
            result = true;
        }
        return result;
    }

    /**
     * 判断两个日期是否为同一月
     *
     * @param df 日期格式，如：yyyy-MM-dd HH:mm:ss
     * @param d1 日期一
     * @param d2 日期二
     * @return 同一月true，不是同一月false
     */
    public static boolean isSameMonth(String df, String d1, String d2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(df);
        Date date1;
        Date date2;
        try {
            date1 = dateFormat.parse(d1);
            date2 = dateFormat.parse(d2);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return DatePro.isSameMonth(date1, date2);
    }

    /**
     * 获取日期是当月的第几天
     *
     * @param df 日期格式,如：yyyy-MM-dd HH:mm:ss
     * @param d  日期
     * @return 如:1，表示为第一天
     */
    public static int getDay(String df, String d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(df);
        Date date1;
        try {
            date1 = dateFormat.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期的年月
     *
     * @param df 日期格式,如：yyyy-MM-dd HH:mm:ss
     * @param d  日期
     * @return 如:201004
     */
    public static int getYearMonth(String df, String d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(df);
        Date date1;
        try {
            date1 = dateFormat.parse(d);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            String temp = "" + year + IntegerPro.addZero(month + 1);
            return Integer.parseInt(temp);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取两个时间相关的天数
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return
     * @throws ParseException
     */
    public static int getDayPoor(String beginDate, String endDate) throws ParseException {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sim.parse(beginDate);
        Date d2 = sim.parse(endDate);
        return (int) ((d2.getTime() - d1.getTime()) / (3600L * 1000 * 24));
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取指定年-月(yyyy-mm)的工作日天数
     *
     * @param year_month
     * @return
     */
    public static int getWorkingDayOfMonth(String year_month) {
        int year = Integer.parseInt(year_month.substring(0, year_month.indexOf("-")));
        int month = Integer.parseInt(year_month.substring(year_month.indexOf("-") + 1));
        Calendar start = Calendar.getInstance();
        start.clear();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month - 1);
        start.set(Calendar.DAY_OF_MONTH, 1);

        Calendar end = Calendar.getInstance();
        end.clear();
        end = (Calendar) start.clone();
        end.add(Calendar.MONTH, 1);
        end.add(Calendar.DAY_OF_MONTH, -1);

        return getWorkingDay(start, end);
    }

    /**
     * 计算2个日期之间的工作日相隔天数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int getWorkingDay(Calendar d1, Calendar d2) {
        if (d1.after(d2)) {
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        // 开始日期的日期偏移量
        int charge_start_date = 0;
        // 结束日期的日期偏移量
        int charge_end_date = 0;
        // 日期不在同一个日期内
        int stmp;
        int etmp;
        stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
        etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
        // 开始日期为星期六和星期日时偏移量为0
        if (stmp != 0 && stmp != 6) {
            charge_start_date = stmp - 1;
        }
        // 结束日期为星期六和星期日时偏移量为0
        if (etmp != 0 && etmp != 6) {
            charge_end_date = etmp - 1;
        }
        return (getDaysBetween(getNextMonday(d1), getNextMonday(d2)) / 7) * 5 + charge_start_date - charge_end_date + 1;
    }

    /**
     * 获得日期的下一个星期一的日期
     *
     * @param date
     * @return
     */
    public static Calendar getNextMonday(Calendar date) {
        Calendar result;
        result = date;
        do {
            result = (Calendar) result.clone();
            result.add(Calendar.DATE, 1);
        } while (result.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY);
        return result;
    }

    /**
     * 计算两个日期的相隔天数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) {
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 取得当月天数
     */
    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 得到指定月的天数
     *
     * @param year  年
     * @param month 月
     * @return
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 格式化日期
     *
     * @param day 日期
     * @param df  格式
     * @return
     */
    public static String formatDay(String day, String df) {
        String timeString = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(df);
            if (day.length() <= 10) {
                timeString = dateFormat.format(DatePro.getDate(day + " 00:00:01"));
            } else {
                timeString = dateFormat.format(DatePro.getDate(day));
            }
        } catch (Exception e) {
        }
        return timeString;
    }

    /**
     * 格式化日期
     *
     * @param time 日期毫秒形式
     * @param df   格式
     * @return
     */
    public static String formatDay(Long time, String df) {
        String timeString = "";
        try {
            Date date = new Date(time);
            SimpleDateFormat format = new SimpleDateFormat(df);
            timeString = format.format(date);
        } catch (Exception e) {
        }
        return timeString;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFriendlyTime(String dateStr) {
        String frdTime = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (null != dateStr) {
            try {
                frdTime += "最近更新：";
                Date date = df.parse(dateStr);
                Date now = new Date();
                long result = now.getTime() - date.getTime();
                if (result < 60000) {// 一分钟内
                    frdTime += "1分钟内";
                } else if (result >= 60000 && result < 3600000) {// 一小时内
                    long seconds = result / 60000;
                    frdTime += seconds + "分钟前";
                } else if (result >= 3600000 && result < 86400000) {// 一天内
                    long seconds = result / 3600000;
                    frdTime += seconds + "小时前";
                } else {// 日期格式
                    frdTime = dateStr;
                }
            } catch (ParseException e) {
                frdTime = dateStr;
                e.printStackTrace();
            }
        }
        return frdTime;
    }
}
