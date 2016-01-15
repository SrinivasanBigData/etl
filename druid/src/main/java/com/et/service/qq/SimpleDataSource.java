package com.et.service.qq;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SimpleDataSource {

	private static String password;
	private static String user;
	private static String url;
	private static String driver;
	private static Connection connection;

	public static void init() throws Exception {
		init("/jpa.properties");
	}

	public static void init(String propertiesFile) throws IOException,
			FileNotFoundException {
		Properties properties = new Properties();
		properties.load(SimpleDataSource.class.getResourceAsStream(propertiesFile));

		driver = (String) properties.get("jdbc.driverClassName");
		url = (String) properties.get("jdbc.url");
		user = (String) properties.get("jdbc.username");
		password = (String) properties.get("jdbc.password");
		System.out.println(url);
	}

	public static Connection getConnection() throws Exception {
		if (connection == null) {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		}
		return connection;
	}

	/**
	 * 执行SQL查询
	 * 
	 * @param sql
	 * @return 数据列表
	 * @throws Exception
	 */
	public static List<String[]> query(String sql) throws Exception {
		Statement stmt = getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		List<String[]> rowList = new ArrayList<String[]>();
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		while (rs.next()) {
			String[] row = new String[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				row[i - 1] = rs.getString(i);
			}
			rowList.add(row);
		}

		rs.close();
		return rowList;
	}

	/**
	 * 执行SQL查询
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> queryToMap(String sql)
			throws Exception {
		Statement stmt = getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		List<Map<String, String>> rowList = new ArrayList<Map<String, String>>();
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		while (rs.next()) {
			Map<String, String> row = new HashMap<String, String>();
			for (int i = 1; i <= columnCount; i++) {
				row.put(metaData.getColumnName(i), rs.getString(i));
			}
			rowList.add(row);
		}

		rs.close();
		return rowList;
	}

	public static void main(String[] args) throws Exception {
		init("/jpa.properties");
	}
}
