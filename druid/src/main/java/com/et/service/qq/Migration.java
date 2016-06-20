package com.et.service.qq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * Migration 数据迁移
 *
 * @author wxu
 * @since 1.0
 */
public class Migration {

  public static final Logger log = LoggerFactory.getLogger(Migration.class);

  // private static final String fromSql =
  // "SELECT * FROM OPEN_QQ qq WHERE qq.CREATED_DATE >= DATE_FORMAT('2015-12-01','%Y-%m-%d') and
  // qq.CREATED_DATE < DATE_ADD(DATE_FORMAT('2015-12-01','%Y-%m-%d'),INTERVAL 1 MONTH) limit 100";
  // private static final String fromSql =
  // "SELECT * FROM OPEN_QQ qq WHERE qq.CREATED_DATE < DATE_FORMAT('2015-12-01','%Y-%m-%d') limit
  // 100";
  private static final String insertSql =
      "insert into OPEN_QQ_? select ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ";
  private static final String delSql = "delete from OPEN_QQ where QQ_ID=?";


  public static void main(String[] args) throws Exception {

    if (args == null || args.length != 2) {
      log.error("请输入qq_id取值范围，如：Migration tart end ");
      return;
    }

    StringBuilder fromSql = new StringBuilder(" SELECT * FROM OPEN_QQ ");
    fromSql.append(" WHERE qq_id>= ");
    fromSql.append(args[0]);
    fromSql.append(" and qq_id< ");
    fromSql.append(args[1]);
    fromSql.append(" and CREATED_DATE is not NULL ");
    fromSql.append(" limit 100 ");


    SimpleDataSource.init();
    Connection conn = SimpleDataSource.getConnection();
    while (true) {
      conn.setAutoCommit(false);
      // 取100条数据
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(fromSql.toString());
      if (!rs.next()) {
        // 数据集为空时，退出循环
        break;
      }

      // 配置插入语句
      try {
        insert(rs);
        delete(rs);
      } catch (SQLException e) {
        log.error("insert error:{}", e);
        try {
          // 异常时，退出循环
          conn.rollback();
          break;
        } catch (Exception x) {
          log.error("rollback error:{}", x);
        }
      } finally {
        try {
          // 设置事务提交方式为自动提交：
          conn.setAutoCommit(true);
        } catch (SQLException e) {
          log.error("autocommit error:{}", e);
        }
      }

      rs.close();
      stmt.close();

    }

    log.info("finished...");
  }

  /**
   * 
   * 插入目标表
   * 
   * @author wxu
   * @date 2016年1月4日
   * @throws Exception
   * @since 1.0
   *
   * @param rs
   *
   */
  private static void insert(ResultSet rs) throws Exception {
    ResultSetMetaData metaData = rs.getMetaData();
    int columnCount = metaData.getColumnCount();

    Connection conn = SimpleDataSource.getConnection();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
    PreparedStatement pstmt = conn.prepareStatement(insertSql);
    rs.beforeFirst();
    while (rs.next()) {
      pstmt.setLong(1, Long.valueOf(simpleDateFormat.format(rs.getDate(6))));
      for (int i = 1; i <= columnCount; i++) {
        pstmt.setString(i + 1, rs.getString(i));
      }
      pstmt.addBatch();
    }
    pstmt.executeBatch();
    conn.commit();
    pstmt.close();
  }

  private static void delete(ResultSet rs) throws Exception {
    Connection conn = SimpleDataSource.getConnection();
    PreparedStatement pstmt = conn.prepareStatement(delSql);
    rs.beforeFirst();
    while (rs.next()) {
      log.info("delete :{}", rs.getLong(1));
      pstmt.setLong(1, rs.getLong(1));
      pstmt.addBatch();
    }
    pstmt.executeBatch();
    conn.commit();
    pstmt.close();
  }
}
