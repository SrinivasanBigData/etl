package com.et.service.ums;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Migration {
  public static final Logger log = LoggerFactory.getLogger(Migration.class);

  private static String insertSql =
//      "INSERT INTO tt_mt (SP_MT_ID, SP_ID, SP_SERVICE_CODE, FROM_USER, TO_USER, TEMPLATE_ID, MSG_ID, MSG_STATUS, MSG_STATUS1, MSG_STATUS2, SP_MT_TIME, CARRIER_MT_TIME, CARRIER_RT_TIME, SP_RT_TIME, PROVINCE_CODE, CITY_CODE ) "
      "INSERT INTO tt_mt VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

  private static Connection mysqlConnection;
  private static Connection mariaConnection;

  public static void main(String[] args) throws Exception {

    mysqlConnection = getMysqlConnection();
    mariaConnection = getMariaConnection();
    execute();
  }

  private static void execute() throws SQLException, ParseException {
    long start = System.currentTimeMillis();
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    Date d1 = fmt.parse("2016-01-01");
    Date d2 = fmt.parse("2016-01-02");
    // 查询mysql记录
    String sql = "SELECT * from TT_MT where SP_MT_ID >= ? and sp_mt_id < ? limit ?,?";
    PreparedStatement stmt = mysqlConnection.prepareStatement(sql);
    stmt.setLong(1, d1.getTime() * 10000);
    stmt.setLong(2, d2.getTime() * 10000);
    stmt.setLong(3, 0);
    stmt.setLong(4, 1000);
    ResultSet rs = stmt.executeQuery();

    log.info("query cost time:{}",System.currentTimeMillis()-start);
    // Statement stmt = mysqlConnection.createStatement();
    // ResultSet rs = stmt.executeQuery(sql);

    // 插入到maria
    insert(rs);

    rs.close();
    stmt.close();
  }

  /**
   * 插入到maria中
   *
   * @author wxu
   * @date 2016年1月15日
   * @since 1.0
   *
   * @param rs
   * @throws SQLException
   *
   */
  private static void insert(ResultSet rs) throws SQLException {
    long start = System.currentTimeMillis();
    ResultSetMetaData metaData = rs.getMetaData();
    int columnCount = metaData.getColumnCount();

    PreparedStatement pstmt = mariaConnection.prepareStatement(insertSql);
    // 数据集指向第一个记录
    rs.beforeFirst();
    while (rs.next()) {
      for (int i = 1; i <= columnCount; i++) {
        pstmt.setString(i, rs.getString(i));
      }
      pstmt.addBatch();
    }
    log.info("prepare cost time:{}",System.currentTimeMillis()-start);
    pstmt.executeBatch();
    mariaConnection.commit();
    pstmt.close();
    log.info("insert cost time:{}",System.currentTimeMillis()-start);
  }

  private static Connection getMysqlConnection() {
    String driver = "com.mysql.jdbc.Driver";
    String url =
        "jdbc:mysql://10.1.60.24:3306/ums?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false";
    String user = "ums";
    String password = "ums20150519";
    return getConnection(driver, url, user, password);
  }

  private static Connection getMariaConnection() {
    String driver = "org.mariadb.jdbc.Driver";
    String url =
        "jdbc:mariadb://10.1.0.189:3306/ums?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false";
    String user = "ums";
    String password = "ums20150519";
    return getConnection(driver, url, user, password);
  }

  private static Connection getConnection(String driver, String url, String user, String password) {

    Connection connection = null;
    try {
      Class.forName(driver);
      connection = DriverManager.getConnection(url, user, password);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }

    return connection;
  }
}
