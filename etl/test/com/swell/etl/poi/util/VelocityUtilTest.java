package com.swell.etl.poi.util;

import java.text.ParseException;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

public class VelocityUtilTest {

	@Test
	public void test() throws ParseException {
		String[] pattern = new String[] { "yyyy-mm -dd ", "yyyy/mm/dd" };
		String strDate = "2014/04/14";
		System.out.println(DateUtils.parseDate(strDate, pattern));

		String format = "yyyy-mm -dd ";
		char[] compiledPattern = format.toCharArray();

		int i1;
		int i2;

		int l = 0;
		while (l < compiledPattern.length) {
			i1 = compiledPattern[l] >>> '\b';
			i2 = compiledPattern[(l)] & 0xFF;

			System.out.println(compiledPattern[l] + " "
					+ (int) compiledPattern[l] + " " + i1 + " " + i2);
			l++;
		}
		System.out.println(compiledPattern);
	}

}
