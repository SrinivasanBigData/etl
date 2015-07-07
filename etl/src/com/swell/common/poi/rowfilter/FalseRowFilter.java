package com.swell.common.poi.rowfilter;

import java.util.List;

public class FalseRowFilter extends AbstractRowFilter {

	@Override
	public boolean accept(int rowNum, List<String> rowlist) {
		return false;
	}

}
