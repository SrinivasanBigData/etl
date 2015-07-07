package com.swell.common.poi.rowfilter;

import java.util.List;

public interface IRowFilter {

	/**
	 * 
	 * @param rowNum
	 *            行号
	 * @param rowlist
	 *            行数据
	 * @return
	 */
	public boolean accept(int rowNum, List<String> rowlist);
	
	public boolean accept(int rowNum, String[] rowlist);

}
