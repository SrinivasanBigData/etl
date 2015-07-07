package com.swell.common.poi.rowfilter;

import java.util.Iterator;
import java.util.List;

public class OrRowFilter extends AbstractRowFilter {

	private List<IRowFilter> rowFilters;

	public OrRowFilter(List<IRowFilter> rowFilters) {
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
			if (rowFilter.accept(rowNum, rowlist)) {
				return true;
			}
		}
		return false;
	}
}
