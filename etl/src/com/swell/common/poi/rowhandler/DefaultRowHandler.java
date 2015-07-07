package com.swell.common.poi.rowhandler;

import com.swell.common.poi.rowfilter.TrueRowFilter;

public class DefaultRowHandler extends AbstractRowHandler {

	public static final IRowHandler INSTANCE = new DefaultRowHandler();

	public DefaultRowHandler() {
		super.setRowFilter(TrueRowFilter.INSTANCE);
	}
}
