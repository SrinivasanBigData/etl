package com.swell.tutorial.druid.conf;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class AppConfigTest {

  @Autowired
  private DataSource dataSource;

  @Test
  public void test() throws Exception {
    System.out.println(dataSource);

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      String readLine = bufferedReader.readLine();
      System.out.println(readLine);
    }
  }

}
