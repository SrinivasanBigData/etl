package com.swell.common.util;

import org.junit.Test;

public class StringValidationTest {

	@Test
	public void test() {
		boolean chkInterval = StringValidation.chkInterval("10.2", "11", null);
		
		System.out.println(chkInterval);
	}

}
