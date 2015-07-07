package com.swell.common.poi.rowfilter;

import java.util.List;

public class TrueRowFilter extends AbstractRowFilter {

	public static final IRowFilter INSTANCE = new TrueRowFilter();

	@Override
	public boolean accept(int rowNum, List<String> rowlist) {
		return true;
	}


}
