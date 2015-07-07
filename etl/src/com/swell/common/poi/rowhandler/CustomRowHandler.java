package com.swell.common.poi.rowhandler;

import com.swell.common.poi.rowfilter.IRowFilter;

/**
 * 自定义处理方法
 * 
 * @author Administrator
 *
 */
public class CustomRowHandler extends AbstractRowHandler {

	public CustomRowHandler(IRowFilter rowFilter) {
		super.setRowFilter(rowFilter);
	}

}
