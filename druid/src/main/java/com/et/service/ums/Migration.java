package com.et.service.ums;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Migration implements Runnable {
  public static final Logger log = LoggerFactory.getLogger(Migration.class);

  private static String insertSql =
      // "INSERT INTO tt_mt (SP_MT_ID, SP_ID, SP_SERVICE_CODE, FROM_USER, TO_USER, TEMPLATE_ID,
      // MSG_ID, MSG_STATUS, MSG_STATUS1, MSG_STATUS2, SP_MT_TIME, CARRIER_MT_TIME, CARRIER_RT_TIME,
      // SP_RT_TIME, PROVINCE_CODE, CITY_CODE ) "
      "INSERT INTO tt_mt VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

  private static Connection mysqlConnection;
  private static Connection mariaConnection;

  /** 间隔 */
  private static final int STEP = 10000;
  /** 查询起始值 */
  private static AtomicInteger start = new AtomicInteger(0);

  private static final int THREAD_MAX = 20;
  private static AtomicInteger threadNum = new AtomicInteger(0);

  private static ExecutorService threadPool;

  private static long begin;

  private static Date d1;

  private static Date d2;

  public static void main(String arg[]) throws Exception {
    mysqlConnection = getMysqlConnection();
    mariaConnection = getMariaConnection();
    mariaConnection.setAutoCommit(false);

    begin = System.currentTimeMillis();

     SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
     d1 = fmt.parse("2016-01-15 08:00:00,000");
     d2 = fmt.parse("2016-01-15 09:00:00,000");
//    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//    d1 = fmt.parse(arg[0]);
//    d2 = fmt.parse(arg[1]);
    log.info("from {} to {} , [{},{})",arg[0],arg[1],d1.getTime(),d2.getTime());

    threadPool = Executors.newFixedThreadPool(THREAD_MAX);
    for (int i = 0; i < THREAD_MAX; i++) {
      threadPool.execute(new Migration());
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      String command = reader.readLine();
      switch (command) {
        case "show":
          System.out.println(threadPool.isTerminated());
          System.out.println(threadPool.isShutdown());
          break;
        default:
          break;
      }

    }
  }

  /** 如果查不到数据，则返回false */
  private boolean execute() throws SQLException, ParseException {
    long i = start.getAndAdd(STEP);
    log.info("thread start ======================{}", i);
    long start = System.currentTimeMillis();

    // 查询mysql记录
    String sql = "SELECT * from TT_MT where SP_MT_ID >= ? and sp_mt_id < ? limit ?,?";
    PreparedStatement stmt = mysqlConnection.prepareStatement(sql);
    stmt.setLong(1, d1.getTime() * 10000);
    stmt.setLong(2, d2.getTime() * 10000);
    stmt.setLong(3, i);
    stmt.setLong(4, STEP);
    ResultSet rs = stmt.executeQuery();
    log.info("query cost time:{}", System.currentTimeMillis() - start);

    boolean result = true;
    if (rs.next()) {
      // 插入到maria
      insert(rs);
    } else {
      result = false;
    }


    rs.close();
    stmt.close();

    return result;
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
  private void insert(ResultSet rs) throws SQLException {
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
    pstmt.executeBatch();
    mariaConnection.commit();
    pstmt.close();
    log.info("insert cost time:{}", System.currentTimeMillis() - start);
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
        "jdbc:mariadb://10.1.0.189:3306/ums?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&useServerPrepStmts=false&rewriteBatchedStatements=true";
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

  @Override
  public void run() {
    boolean result = true;
    while (result) {
      try {
        result = this.execute();
      } catch (SQLException | ParseException e) {
        e.printStackTrace();
      }
    }

    log.info("thread finished, total cost time:{}", System.currentTimeMillis() - begin);
    if (threadNum.incrementAndGet() >= THREAD_MAX) {
      System.exit(0);
    }
  }
}
