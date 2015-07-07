package com.swell.common.poi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.swell.common.poi.rowhandler.DefaultRowHandler;
import com.swell.common.poi.rowhandler.IRowHandler;
import com.swell.common.poi.util.ExcelUtil;

public class SAXSheetReader {

	/**
	 * Excel数据逻辑处理
	 */
	private IRowHandler rowHandler;

	public SAXSheetReader(IRowHandler rowHandler) {
		this.rowHandler = rowHandler;
	}

	/**
	 * 使用默认处理方式，接收全部数据
	 */
	public SAXSheetReader() {
		this.rowHandler = DefaultRowHandler.INSTANCE;
	}

	/**
	 * 遍历工作簿中所有的电子表格
	 *
	 * @param filename
	 * @param sheetName
	 * @throws IOException
	 * @throws OpenXML4JException
	 * @throws SAXException
	 * @throws Exception
	 */
	public void process(String filename, String sheetName) throws IOException,
			OpenXML4JException, SAXException {
		Map<String, String> map = this.getSheetNameMap(filename);

		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader xssfReader = new XSSFReader(pkg);
		XMLReader parser = this.fetchSheetParser(xssfReader);

		InputStream sheet = xssfReader.getSheet(map.get(sheetName));
		InputSource sheetSource = new InputSource(sheet);
		parser.parse(sheetSource);
		sheet.close();
		pkg.close();
	}

	private XMLReader fetchSheetParser(XSSFReader xssfReader)
			throws SAXException, InvalidFormatException, IOException {
		XMLReader parser = XMLReaderFactory
				.createXMLReader("org.apache.xerces.parsers.SAXParser");
		SharedStringsTable sst = xssfReader.getSharedStringsTable();
		StylesTable stylesTable = xssfReader.getStylesTable();
		SheetHandler handler = new SheetHandler(sst, stylesTable, rowHandler);
		parser.setContentHandler(handler);
		return parser;
	}

	public Map<String, String> getSheetNameMap(String filename)
			throws IOException, OpenXML4JException, SAXException {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader xssfReader = new XSSFReader(pkg);
		XMLReader parser = XMLReaderFactory
				.createXMLReader("org.apache.xerces.parsers.SAXParser");
		WorkbookHandler workbookHandler = new WorkbookHandler();
		parser.setContentHandler(workbookHandler);

		InputStream workbookData = xssfReader.getWorkbookData();
		parser.parse(new InputSource(workbookData));
		workbookData.close();
		pkg.close();

		return workbookHandler.getSheetNameMap();
	}

	public IRowHandler getRowHandler() {
		return rowHandler;
	}

	public void setRowHandler(IRowHandler rowHandler) {
		this.rowHandler = rowHandler;
	}

	private class WorkbookHandler extends DefaultHandler {
		private Map<String, String> sheetNameMap = new HashMap<String, String>();

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			if ("sheet".equals(name)) {
				String sheetName = attributes.getValue("name");
				String rid = attributes.getValue("r:id");
				getSheetNameMap().put(sheetName, rid);
			}
		}

		public Map<String, String> getSheetNameMap() {
			return sheetNameMap;
		}
	}

	private static class SheetHandler extends DefaultHandler {
		public SheetHandler(SharedStringsTable sst, StylesTable stylesTable,
				IRowHandler rowHandler) {
			this.sst = sst;
			this.stylesTable = stylesTable;
			this.rowHandler = rowHandler;
		}

		/**
		 * 共享字符串表
		 */
		private SharedStringsTable sst;
		/**
		 * 上一次的内容
		 */
		private String lastContents;
		/**
		 * 字符串标识
		 */
		private boolean nextIsString;

		/**
		 * 行集合
		 */
		private List<String> rowlist = new ArrayList<String>();
		/**
		 * 当前行
		 */
		private int curRow = -1;
		/**
		 * T元素标识
		 */
		private boolean isTElement;
		/**
		 * Excel数据逻辑处理
		 */
		private IRowHandler rowHandler;
		/**
		 * 单元格数据类型，默认为字符串类型
		 */
		private CellDataType nextDataType = CellDataType.SSTINDEX;
		private final DataFormatter formatter = new DataFormatter();
		private short formatIndex;
		private String formatString;

		/**
		 * 列名
		 */
		private String cellRef;
		/**
		 * 单元格
		 */
		private StylesTable stylesTable;

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			if ("row".equals(name)) {
				curRow++;
			} else if (curRow >= 0) {
				// c => 单元格
				if ("c".equals(name)) {
					// 设定单元格类型
					this.setNextDataType(attributes);
					this.cellRef = attributes.getValue("r");
				}
				// 当元素为t时
				if ("t".equals(name)) {
					isTElement = true;
				} else {
					isTElement = false;
				}
				// 置空
				lastContents = "";
			}
		}

		/**
		 * 单元格中的数据可能的数据类型
		 */
		enum CellDataType {
			BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
		}

		/**
		 * 处理数据类型
		 *
		 * @param attributes
		 */
		private void setNextDataType(Attributes attributes) {
			nextDataType = CellDataType.NUMBER;
			formatIndex = -1;
			formatString = null;
			String cellType = attributes.getValue("t");
			String cellStyleStr = attributes.getValue("s");
			if ("b".equals(cellType)) {
				nextDataType = CellDataType.BOOL;
			} else if ("e".equals(cellType)) {
				nextDataType = CellDataType.ERROR;
			} else if ("inlineStr".equals(cellType)) {
				nextDataType = CellDataType.INLINESTR;
			} else if ("s".equals(cellType)) {
				nextDataType = CellDataType.SSTINDEX;
			} else if ("str".equals(cellType)) {
				nextDataType = CellDataType.FORMULA;
			}
			if (cellStyleStr != null) {
				int styleIndex = Integer.parseInt(cellStyleStr);
				XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
				formatIndex = style.getDataFormat();
				formatString = style.getDataFormatString();
				if ("m/d/yy" == formatString) {
					nextDataType = CellDataType.DATE;
					formatString = "yyyy-MM-dd hh:mm:ss";
				}
				if (formatString == null) {
					nextDataType = CellDataType.NULL;
					formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
				}
			}
		}

		/**
		 * 对解析出来的数据进行类型处理
		 *
		 * @param value
		 *            单元格的值（这时候是一串数字）
		 * @param thisStr
		 *            一个空字符串
		 * @return
		 */
		private String getDataValue(String value, String thisStr) {
			switch (nextDataType) {
			// 这几个的顺序不能随便交换，交换了很可能会导致数据错误
			case BOOL:
				char first = value.charAt(0);
				thisStr = first == '0' ? "FALSE" : "TRUE";
				break;
			case ERROR:
				thisStr = "\"ERROR:" + value.toString() + '"';
				break;
			case FORMULA:
				thisStr = '"' + value.toString() + '"';
				break;
			case INLINESTR:
				XSSFRichTextString rtsi = new XSSFRichTextString(
						value.toString());
				thisStr = rtsi.toString();
				rtsi = null;
				break;
			case SSTINDEX:
				String sstIndex = value.toString();
				try {
					int idx = Integer.parseInt(sstIndex);
					XSSFRichTextString rtss = new XSSFRichTextString(
							sst.getEntryAt(idx));
					thisStr = rtss.toString();
					rtss = null;
				} catch (NumberFormatException ex) {
					thisStr = value.toString();
				}
				break;
			case NUMBER:
				if (formatString != null) {
					thisStr = formatter.formatRawCellContents(
							Double.parseDouble(value), formatIndex,
							formatString).trim();
				} else {
					thisStr = value;
				}
				thisStr = thisStr.replace("_", "").trim();
				break;
			case DATE:
				thisStr = formatter.formatRawCellContents(
						Double.parseDouble(value), formatIndex, formatString);
				break;
			default:
				thisStr = " ";
				break;
			}
			return thisStr;
		}

		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (curRow >= 0) {
				// 根据SST的索引值的到单元格的真正要存储的字符串
				// 这时characters()方法可能会被调用多次
				if (nextIsString) {
					int idx = Integer.parseInt(lastContents);
					lastContents = new XSSFRichTextString(sst.getEntryAt(idx))
							.toString();
				}
				// t元素也包含字符串
				if (isTElement) {
					// 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
					String value = lastContents.trim();
					addToList(value);
					isTElement = false;
				} else if ("v".equals(name)) {
					// v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
					String value = this.getDataValue(lastContents.trim(), "");
					addToList(value);
				} else {
					// 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
					if (name.equals("row")) {
						rowHandler.processRowData(curRow, rowlist);
						rowlist.clear();
						curRow++;
					}
				}
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// 得到单元格内容的值
			lastContents += new String(ch, start, length);
		}

		private void addToList(String value) {
			int colNum = ExcelUtil.columnToNum(this.cellRef);
			for (int i = 0; i < colNum - rowlist.size(); i++) {
				rowlist.add("");
			}
			rowlist.add(value);
		}

	}
}
