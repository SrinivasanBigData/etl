package com.swell.common.poi.rowfilter;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 根据主键做过滤
 * 
 * @author Administrator
 *
 */
public class PKRowFilter extends AbstractRowFilter {

	/**
	 * 主键列号，默认为0
	 */
	public int pkColNum = 0;

	public PKRowFilter(int pkColNum) {
		this.pkColNum = pkColNum;
	}

	@Override
	public boolean accept(int rowNum, List<String> rowlist) {
		if (rowlist == null || rowlist.size() < pkColNum + 1
				|| StringUtils.isEmpty(rowlist.get(pkColNum))) {
			return false;
		}
		return true;
	}

}
