package com.suncreate.fireiot.util;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 字符串工具类
 * @author chenzhao
 * created on 2015/12/22 13:37
 */
public class StrPro {

	/**
	 * 字符串编码转换，GBK2ISO
	 * 
	 * @param strIn
	 *            字符串
	 * @return 编码后的字符串
	 */
	public static String GBK2ISO(String strIn) throws Exception {
		String strOut = null;
		if ((strIn == null) || (strIn.trim().equals(""))) {
			return "";
		}
		try {
			byte[] b = strIn.getBytes("GBK");
			strOut = new String(b, "ISO8859_1");
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
		return strOut;
	}

	/**
	 * 字符串编码转换，ISO2GBK
	 * 
	 * @param strIn
	 *            字符串
	 * @return 编码后的字符串
	 */
	public static String ISO2GBK(String strIn) throws Exception {
		String strOut = null;
		if ((strIn == null) || (strIn.trim().equals(""))) {
			return "";
		}
		try {
			byte[] b = strIn.getBytes("ISO8859_1");
			strOut = new String(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
		return strOut;
	}

	/**
	 * 字符串编码转换，ISO2UTF8
	 * 
	 * @param strIn
	 *            字符串
	 * @return 编码后的字符串
	 */
	public static String ISO2UTF8(String strIn) throws Exception {
		String strOut = null;
		if ((strIn == null) || (strIn.trim().equals(""))) {
			return "";
		}
		try {
			byte[] b = strIn.getBytes("ISO8859_1");
			strOut = new String(b, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
		return strOut;
	}

	/**
	 * 删除回车
	 * 
	 * @param str
	 *            字符串
	 * @return 删除后字符串
	 */
	public static String delEnter(String str) {
		String finalStr = "";
		if (TypeChk.checkNull(str)) {
			return null;
		}
		for (int i = 0; i < str.length(); ++i) {
			if ((str.charAt(i) != '\r') && (str.charAt(i) != '\n')) {
				finalStr = finalStr + str.charAt(i);
			}
		}
		return finalStr;
	}

	/**
	 * 将字符串的回车替换
	 * 
	 * @param str
	 *            原字符串
	 * @param repStr
	 *            替换字符串
	 * @return 替换后的字符串
	 */
	public static String replaceEnter(String str, String repStr) {
		String strLeft = "";
		String strRight = "";
		if (TypeChk.checkNull(str)) {
			return null;
		}
		for (int i = 0; i < str.length(); ++i) {
			char a = str.charAt(i);
			if (a == '\n') {
				strLeft = str.substring(0, i);
				strRight = str.substring(i + 1);
				str = strLeft + repStr + strRight;
			} else if (a == '\r') {
				strLeft = str.substring(0, i);
				strRight = str.substring(i + 1);
				str = strLeft + strRight;
			}
		}
		return str;
	}

	/**
	 * 分隔字符串
	 * 
	 * @param s
	 *            源字符串
	 * @param separator
	 *            分隔符
	 * @return
	 */
	public static ArrayList<String> split(String s, String separator) {
		if (s == null) {
			throw new NullPointerException("source String cannot be null");
		}
		if (separator == null) {
			throw new NullPointerException("separator cannot be null");
		}
		if (separator.length() == 0) {
			throw new IllegalArgumentException("separator cannot be empty");
		}

		ArrayList<String> result = new ArrayList<String>();

		int start = 0;
		int end = s.indexOf(separator);
		while (end != -1) {
			result.add(s.substring(start, end));
			start = end + separator.length();
			end = s.indexOf(separator, start);
		}
		result.add(s.substring(start, s.length()));
		return result;
	}

	/**
	 * 字符串去前后空格
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String trim(String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 分割字符串
	 * 
	 * @param sourceString
	 *            原字符串
	 * @param divideFlag
	 *            分割标识
	 * @return
	 */
	public static String[] divideString(String sourceString, String divideFlag) {
		if ((sourceString == null) || (sourceString.trim().equals(""))) {
			return null;
		}
		int e = 0;
		int s = 0;
		int i = 0;
		String tmpStr = null;

		while ((e = sourceString.indexOf(divideFlag, s)) >= 0) {
			tmpStr = sourceString.substring(s, e);
			if (!(TypeChk.checkNull(tmpStr))) {
				s = e + divideFlag.length();
				++i;
			} else {
				s = e + divideFlag.length();
			}
		}
		tmpStr = sourceString.substring(s, sourceString.length());
		String[] finalString;
		if (TypeChk.checkNull(tmpStr)) {
			finalString = new String[i];
		} else {
			finalString = new String[i + 1];
		}

		e = 0;
		s = 0;
		i = 0;

		while ((e = sourceString.indexOf(divideFlag, s)) >= 0) {
			tmpStr = sourceString.substring(s, e);

			if (!(TypeChk.checkNull(tmpStr))) {
				finalString[i] = sourceString.substring(s, e);
				s = e + divideFlag.length();
				++i;
			} else {
				s = e + divideFlag.length();
			}
		}
		tmpStr = sourceString.substring(s, sourceString.length());
		if (!(TypeChk.checkNull(tmpStr))) {
			finalString[i] = tmpStr;
		}
		return finalString;
	}

	/**
	 * 判断字符串是否为数字，并且字符串长度是否在最大值与最小值之前
	 * 
	 * @param str
	 *            字符串
	 * @param minLen
	 *            最小长度
	 * @param maxLen
	 * @return
	 */
	public static boolean isDigitStr(String str, int minLen, int maxLen) {
		if (str == null) {
			return false;
		}
		if ((str.length() < minLen) || (str.length() > maxLen)) {
			return false;
		}

		for (int i = 0; i < str.length(); ++i) {
			char temp = str.charAt(i);
			if ((temp < '0') || (temp > '9')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 *            字符串
	 * @return true 表示是数字
	 */
	public static boolean isDigitStr(String str) {
		if (str == null) {
			return false;
		}
		for (int i = 0; i < str.length(); ++i) {
			char temp = str.charAt(i);
			if ((temp < '0') || (temp > '9')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取GBK编码字符串的长度
	 * 
	 * @param strIn
	 *            字符串
	 * @return
	 */
	public static int getGBKLength(String strIn) {
		int length = 0;
		if ((strIn == null) || (strIn.trim().equals("")))
			return length;
		try {
			byte[] buff = strIn.getBytes("GBK");
			length = buff.length;
		} catch (UnsupportedEncodingException e) {
		}
		return length;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            字符串
	 * @return true 为空
	 */
	public static boolean isBlank(String str) {
		int strLen = 0;
		if (str != null) {
			if ((strLen = str.length()) != 0)
				return false;
		}
		for (int i = 0; i < strLen; ++i) {
			if (Character.isWhitespace(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串不为空
	 * 
	 * @param str
	 *            字符串
	 * @return true 不为空
	 */
	public static boolean isNotBlank(String str) {
		int strLen = 0;
		if (str != null) {
			if ((strLen = str.length()) != 0)
				return true;
		}
		for (int i = 0; i < strLen; ++i) {
			if (Character.isWhitespace(str.charAt(i)) == false) {
				return true;
			}
		}
		return false;
	}

	/**
	 * null转空
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String null2Str(String str) {
		if (null == str) {
			return "";
		}
		return str;
	}

	/**
	 * 千位分隔符
	 * 
	 * @param money
	 * @return
	 */
	public static String moneyFormat(Double money) {
		String moneyFmt = "";
		DecimalFormat df = new DecimalFormat("#,###.00");
		moneyFmt = df.format(money);
		return moneyFmt;
	}
}