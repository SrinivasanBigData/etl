package com.swell.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtil {

	/**
	 * BOM文件字节标志
	 */
	public static int FLAG_BOM = 0xFEFF;

	public static String loadString(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		char[] buffer = new char[1000];
		int size = 0;
		while ((size = reader.read(buffer)) > 0) {
			sb.append(buffer, 0, size);
		}
		reader.close();

		// 清除BOM标志
		if (sb.charAt(0) == FLAG_BOM) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}
}
