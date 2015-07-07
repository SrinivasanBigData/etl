package com.swell.common.poi.rowhandler;

import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

public interface IRowHandler {

	/**
	 * 处理行数据
	 * 
	 * @param curRow
	 * @param rowlist
	 * @throws SAXException
	 */
	public void processRowData(int curRow, List<String> rowlist)
			throws SAXException;

	/**
	 * 获取最终的结果集合
	 * 
	 * @return
	 */
	public Map<Integer, List<String>> getResultMap();

}
