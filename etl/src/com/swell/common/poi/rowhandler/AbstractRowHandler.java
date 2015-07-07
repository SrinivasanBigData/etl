package com.swell.common.poi.rowhandler;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.xml.sax.SAXException;

import com.swell.common.poi.rowfilter.IRowFilter;

/**
 * Excel行处理类
 * 
 * @author Administrator
 *
 */
public abstract class AbstractRowHandler implements IRowHandler {

	/**
	 * 过滤器
	 */
	private IRowFilter rowFilter;

	/**
	 * 按照行号排序的集合
	 */
	private Map<Integer, List<String>> resultMap = new TreeMap<Integer, List<String>>(
			new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			});

	@Override
	public void processRowData(int curRow, List<String> rowlist) throws SAXException {
		if (rowFilter.accept(curRow, rowlist)) {
			getResultMap().put(Integer.valueOf(curRow), rowlist);
		}
	}

	public Map<Integer, List<String>> getResultMap() {
		return resultMap;
	}

	public IRowFilter getRowFilter() {
		return rowFilter;
	}

	public void setRowFilter(IRowFilter rowFilter) {
		this.rowFilter = rowFilter;
	}

}
