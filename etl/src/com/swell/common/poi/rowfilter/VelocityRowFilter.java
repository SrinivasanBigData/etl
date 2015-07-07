package com.swell.common.poi.rowfilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swell.common.poi.util.ExcelUtil;
import com.swell.common.util.VelocityUtil;

/**
 * 根据velocity模版进行过滤
 * 
 * @author Administrator
 *
 */
public class VelocityRowFilter extends AbstractRowFilter {

	private Map<String, Object> map = new HashMap<String, Object>();

	private String vTemplate;

	/**
	 * 
	 * @param vTemplate
	 *            velocity模版字符串
	 */
	public VelocityRowFilter(String vTemplate) {
		this.vTemplate = vTemplate;
	}

	public VelocityRowFilter(String vTemplate, Map<String, Object> map) {
		this.vTemplate = vTemplate;
		this.map = map;
	}

	@Override
	public boolean accept(int rowNum, List<String> rowlist) {
		getMap().put(ExcelUtil.ROW_NUM, Integer.valueOf(rowNum));
		getMap().put(ExcelUtil.ROWLIST, rowlist);

		return VelocityUtil.evlBoolean(getMap(), vTemplate);
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

}
