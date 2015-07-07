package com.swell.common.poi.rowfilter;

import java.util.List;

/**
 * 根据行号过滤
 * 
 * @author Administrator
 *
 */
public class RownumRowFilter extends AbstractRowFilter {

	private Integer end;

	private Integer start;

	/**
	 * <p>
	 * curRow [start,end]
	 * </p>
	 * 
	 * <p>
	 * 如果start为null，则只检查end端，curRow<=end；反之，start<=curRow
	 * </p>
	 * 
	 * @param start
	 *            起始行号
	 * @param end
	 *            终止行号
	 */
	public RownumRowFilter(Integer start, Integer end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean accept(int rowNum, List<String> rowlist) {
		boolean startFlag = true, endFlag = true;

		if (start != null && start > rowNum) {
			startFlag = false;
		}

		if (end != null && end < rowNum) {
			endFlag = false;
		}

		return startFlag && endFlag;
	}
}
