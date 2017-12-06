package com.suncreate.fireiot.util;

/**
 * 整型处理工具类
 * 
 */
public class IntegerPro {

	/**
	 * 将0~9数值补零,如果不为0~9则返回“”
	 * 
	 * @param value
	 *            数值
	 * @return
	 */
	public static String addZero(int value) {
		if (value >= 0 && value < 10)
			return "0" + value;
		else
			return value + "";
	}

	/**
	 * 判断是否为整形
	 * 
	 * @param value
	 *            值
	 * @return true为整型，false不为整型
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
