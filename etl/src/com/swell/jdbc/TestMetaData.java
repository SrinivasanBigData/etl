package com.swell.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import com.opencsv.CSVWriter;

public class TestMetaData {

	public static void main(String[] args) {
		try {
			SimpleDataSource.init();
			Connection conn = SimpleDataSource.getConnection();
			descDatabaseInfo(conn);
			Statement stmt = conn.createStatement();
			String sql = "select * from events";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("The data table structure info:");
			descResultSetInfo(rs);

			descRowSet(rs);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 遍历结果集
	 * 
	 * @param rs
	 * @throws SQLException
	 * @throws IOException
	 */
	private static void descRowSet(ResultSet rs) throws SQLException,
			IOException {
		CSVWriter csvWriter = new CSVWriter(new PrintWriter(System.out));
		ResultSetMetaData rm = rs.getMetaData();
		int columnNum = rm.getColumnCount();
		while (rs.next()) {
			String[] arr = new String[columnNum];
			for (int i = 1; i <= columnNum; i++) {
				arr[i - 1] = rs.getString(i);
			}
			csvWriter.writeNext(arr);
		}

		csvWriter.close();
	}

	/**
	 * 表述数据库的基本信息
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	private static void descDatabaseInfo(Connection conn) throws SQLException {
		DatabaseMetaData dmd = conn.getMetaData();
		if (dmd == null) {
			System.out.println("no Meta avaliable.");
		} else {
			System.out.println("Database Name:" + dmd.getDatabaseProductName());
			System.out.println("Database Version:"
					+ dmd.getDatabaseProductVersion());
			System.out.println("Database Driver:" + dmd.getDriverName());
			System.out.println("Database DriverVerion:"
					+ dmd.getDriverVersion());
			System.out.println("Database DataBase TypeList:");
			ResultSet rs = dmd.getTypeInfo();
			rs.next();
			while (rs.next()) {
				for (int i1 = 1; i1 < getColumnSize(rs); i1++) {
					System.out.print("\t" + rs.getString(i1) + "\t");
				}
				System.out.println();
			}
			rs.close();
		}
	}

	/**
	 * 对表字段进行遍历
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	private static void descResultSetInfo(ResultSet rs) throws SQLException {
		ResultSetMetaData rm = rs.getMetaData();
		int columnNum = rm.getColumnCount();
		System.out.println("Num \tcolumnName \tDataType");
		for (int i = 1; i <= columnNum; i++) {
			System.out.println(i + "\t" + rm.getColumnName(i) + "\t"
					+ rm.getColumnTypeName(i));
		}
	}

	/**
	 * 
	 * @param rs
	 * @return resultSet中的列数
	 */
	private static int getColumnSize(ResultSet rs) {
		int typeInfoSize = 1;
		for (int i = 1; i <= typeInfoSize; i++) {
			try {
				if (rs.getString(i) != null) {
					typeInfoSize += 1;
				}
			} catch (Exception e) {
				// 如果有溢出，则停止循环
				break;
			}
		}
		return typeInfoSize;
	}

}
