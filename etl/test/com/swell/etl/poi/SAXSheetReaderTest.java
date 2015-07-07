package com.swell.etl.poi;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.swell.common.poi.SAXSheetReader;

public class SAXSheetReaderTest {

	@Test
	public void a() throws Exception {
	
		URL url = new URL("http://www.baidu.com");
		String str = IOUtils.toString(url);
		
		System.out.println(str);
	}
	
	
	public void test() throws IOException, OpenXML4JException, SAXException {
		
		SAXSheetReader reader = new SAXSheetReader();
		
		String filename ="C:/Users/Administrator/Desktop/������˾������Դ��-��ΰ.xlsx";
		Map<String, String> sheetMap = reader.getSheetNameMap(filename);
		System.out.println(sheetMap);
	}

}
