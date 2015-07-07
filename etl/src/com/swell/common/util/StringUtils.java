package com.swell.common.util;
public class StringUtils {

	/**
	 * 
	 * StringUtils.removeUnderScores("foo_bar")="FooBar"
	 * 
	 * @param data
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String removeUnderScores(String data){
		return org.apache.velocity.util.StringUtils.removeUnderScores(data);
	}
}
