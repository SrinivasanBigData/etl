package com.swell.common.poi.rowfilter;

import java.util.Arrays;

public abstract class AbstractRowFilter implements IRowFilter {

	@Override
	public boolean accept(int rowNum, String[] rowlist) {
		return this.accept(rowNum, Arrays.asList(rowlist));
	}

}
