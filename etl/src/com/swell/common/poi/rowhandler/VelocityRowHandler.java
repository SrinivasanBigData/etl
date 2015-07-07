package com.swell.common.poi.rowhandler;

import java.util.Map;

import com.swell.common.poi.rowfilter.VelocityRowFilter;

public class VelocityRowHandler extends AbstractRowHandler {

	public VelocityRowHandler(String vTemplate) {
		super.setRowFilter(new VelocityRowFilter(vTemplate));
	}

	public VelocityRowHandler(String vTemplate, Map<String, Object> map) {
		super.setRowFilter(new VelocityRowFilter(vTemplate, map));
	}

}
