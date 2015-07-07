package com.swell.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * 文本格式检查
 * 
 * @author Administrator
 *
 */
public class StringValidation {

	/**
	 * 比较value是否在[start,end]之间，含等号<br>
	 * 
	 * StringValidation.chkInterval("10.2", "11", null)=false
	 * 
	 * @param value
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean chkInterval(String value, String start, String end) {
		if (!StringUtils.isBlank(start)) {
			if (Double.valueOf(start).compareTo(Double.valueOf(value)) > 0) {
				return false;
			}
		}
		if (!StringUtils.isBlank(end)) {
			if (Double.valueOf(end).compareTo(Double.valueOf(value)) < 0) {
				return false;
			}
		}

		return true;
	}
}
