package com.swell.common.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.junit.Test;

public class DateUtilTest {

	@Test
	public void test() throws ParseException {
		System.out.println(DateUtil.toDate("2000/1/12"));
		
		FastDateFormat fastDateFormat = DateFormatUtils.ISO_DATE_FORMAT;
		System.out.println(fastDateFormat.format(new Date()));
		System.out.println(fastDateFormat.getLocale());
		
		System.out.println(DateUtil.format(new Date(), DateUtil.ISO_YYYY_MM_DD));
		System.out.println(DateUtil.format("2014-04-14",DateUtil.CN_YYYYMMDDHHMMSS));
		
		System.out.println(DateUtil.isDate("2014-04-14", DateUtil.EN_YYYY_MM_DD));
		System.out.println(DateUtil.isDate("2014-04-14", DateUtil.ISO_YYYY_MM_DD));
	}

}
