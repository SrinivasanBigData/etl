/**
 * @date: 2014年8月6日
 * @copyright: copyright (c) 2014,etonenet.com All Rights Reserved.
 */
package com.swell.tutorial.druid.conf;

import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 
 * JpaConfig
 *
 * @author JoeHe
 * @since 1.0
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.et.repository"},
    transactionManagerRef = "jpaTransactionManager")
@EnableTransactionManagement(proxyTargetClass = true)

@PropertySource(value = {"classpath:jpa.properties"})
public class JpaConfig {

  @Autowired
  private Environment env;

  @Bean
  public DataSource dataSource() throws SQLException {
    DruidDataSource ds = new DruidDataSource();
    ds.setDefaultAutoCommit(false);
    ds.setDriverClassName(env.getProperty("jdbc.driverClassName"));
    ds.setUrl(env.getProperty("jdbc.url"));
    ds.setUsername(env.getProperty("jdbc.username"));
    ds.setPassword(env.getProperty("jdbc.password"));

    ds.setFilters("stat");

    return ds;
  }

  @Bean(name = "jpaTransactionManager")
  public PlatformTransactionManager transactionManager() throws SQLException {
    JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(entityManagerFactory());
    return txManager;
  }

  @Bean
  public HibernateExceptionTranslator hibernateExceptionTranslator() {
    return new HibernateExceptionTranslator();
  }

  @Bean
  public EntityManagerFactory entityManagerFactory() throws SQLException {

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("com.et.entity", "com.et.domain.xbean");
    factory.setDataSource(dataSource());

    Properties jpaProperties = new Properties();

    jpaProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
    jpaProperties.put("hibernate.max_fetch_depth", env.getProperty("hibernate.max_fetch_depth"));
    jpaProperties.put("hibernate.jdbc.fetch_size", env.getProperty("hibernate.jdbc.fetch_size"));
    jpaProperties.put("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
    jpaProperties.put("connection.useUnicode", env.getProperty("connection.useUnicode"));
    jpaProperties.put("connection.characterEncoding",
        env.getProperty("connection.characterEncoding"));
    jpaProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
    jpaProperties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
    jpaProperties.put("hibernate.use_sql_comments", env.getProperty("hibernate.use_sql_comments"));
    jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));


    factory.setJpaProperties(jpaProperties);
    factory.afterPropertiesSet();

    return factory.getObject();
  }
}
