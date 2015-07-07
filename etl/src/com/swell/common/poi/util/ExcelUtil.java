package com.swell.common.poi.util;

public class ExcelUtil {

	/**
	 * Excel列名转换为列编号，编号起始值为0
	 * 
	 * @param cellRef
	 *            列名，如，AA3,BC3
	 * @return
	 */
	public static int columnToNum(String cellRef) {
		char[] charArr = cellRef.toUpperCase().replaceAll("\\d", "")
				.toCharArray();

		int result = -1;
		for (int i = 0; i < charArr.length; i++) {
			int base = (int) charArr[i] - 65;
			result = (result + 1) * 26 + base;
		}

		return result;
	}

	/**
	 * 列序号转换为列名称，如<br>
	 * ExcelUtil.numToColumn("2")="B"<br>
	 * ExcelUtil.numToColumn("112")="DH"<br>
	 * 
	 * @param colnum
	 * @return
	 */
	public static String numToColumn(String colnum) {
		StringBuilder result = new StringBuilder();
		int num = Integer.valueOf(colnum);

		while (num > 0) {
			int vMod = num % 26;
			num = num / 26;
			if (vMod == 0) {
				result.insert(0, (char) (26 + 64));
				num--;
			} else {
				result.insert(0, (char) (vMod + 64));
			}

		}
		return result.toString();
	}

	/**
	 * 行数据：代表一行数据的list
	 */
	public static final String ROWLIST = "rowlist";
	/**
	 * 行号：代表Excel中的行号
	 */
	public static final String ROW_NUM = "rowNum";
}
