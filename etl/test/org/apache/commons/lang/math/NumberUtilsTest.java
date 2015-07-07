package org.apache.commons.lang.math;

import org.junit.Test;

public class NumberUtilsTest {

	@Test
	public void test() {
		System.out.println(NumberUtils.isDigits("1.2"));
		System.out.println(NumberUtils.isNumber("1.2"));
	}

}
