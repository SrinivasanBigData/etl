package com.swell.common.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class DateUtil {
	public static final String CN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String ISO_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String ISO_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String EN_YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
	public static final String EN_YYYY_MM_DD = "yyyy/MM/dd";

	private static String[] patterns = new String[] {
			DateFormatUtils.ISO_DATE_FORMAT.getPattern(),
			DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.getPattern(),
			DateFormatUtils.ISO_DATETIME_FORMAT.getPattern(),
			DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern(),
			DateFormatUtils.ISO_TIME_FORMAT.getPattern(),
			DateFormatUtils.ISO_TIME_NO_T_FORMAT.getPattern(),
			DateFormatUtils.ISO_TIME_NO_T_TIME_ZONE_FORMAT.getPattern(),
			DateFormatUtils.ISO_TIME_TIME_ZONE_FORMAT.getPattern(),
			ISO_YYYY_MM_DD_HH_MM_SS, EN_YYYY_MM_DD_HH_MM_SS, EN_YYYY_MM_DD };

	/**
	 * DateUtil.toDate("2014-04-14")
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date toDate(String date) throws ParseException {
		if (StringUtils.isBlank(date)) {
			return null;
		}

		return DateUtils.parseDate(date, patterns);
	}

	/**
	 * DateUtil.format(new Date(), DateUtil.ISO_YYYY_MM_DD)
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * DateUtil.format("2014-04-14",DateUtil.CN_YYYY_M_MDD_H_HMMSS)
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static String format(String date, String pattern)
			throws ParseException {
		return DateFormatUtils.format(toDate(date), pattern);
	}

	/**
	 * DateUtil.isDate("2014-04-14")
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDate(String date) {
		try {
			toDate(date);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * DateUtil.isDate("2014-04-14", DateUtil.EN_YYYY_MM_DD)
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static boolean isDate(String date, String pattern) {
		try {
			DateUtils.parseDate(date, new String[] { pattern });
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

}
