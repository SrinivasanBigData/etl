package com.swell.common.poi.rowfilter;

import java.util.List;

/**
 * 根据名称进行过滤
 * 
 * @author Administrator
 *
 */
public class ColValueRowFilter extends AbstractRowFilter {

	/**
	 * 列编号
	 */
	private int colNum;

	/**
	 * 列值
	 */
	private String colValue;
	public ColValueRowFilter(int colNum, String colValue) {
		super();
		this.colNum = colNum;
		this.colValue = colValue;
	}

	@Override
	public boolean accept(int rowNum, List<String> rowlist) {
		if (rowlist.size() > colNum && colValue.equals(rowlist.get(colNum))) {
			return true;
		}
		return false;
	}

}
