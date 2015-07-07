package com.swell.common.poi.rowfilter;

import java.util.Iterator;
import java.util.List;

public class AndRowFilter extends AbstractRowFilter {

	private List<IRowFilter> rowFilters;

	public AndRowFilter(List<IRowFilter> rowFilters) {
		this.rowFilters = rowFilters;
	}

	public void addRowFilter(IRowFilter rowFilter) {
		rowFilters.add(rowFilter);
	}

	@Override
	public boolean accept(int rowNum, List<String> rowlist) {
		for (Iterator<IRowFilter> iterator = rowFilters.iterator(); iterator
				.hasNext();) {
			IRowFilter rowFilter = iterator.next();
			if (!rowFilter.accept(rowNum, rowlist)) {
				return false;
			}
		}
		return true;
	}
}
