package org.saltframework.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.saltframework.resources.properties.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.PropertySourcesBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Apache DBCP 2 를 이용하여 {@link DataSource} 빈을 생성한다.
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 5. 12.
 */
@Configuration
public class JdbcDataSource {

  @Autowired
  private Config config;

  @Bean
  public DataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();

    Properties properties = config.getProperties("dataSource.", false);
    PropertySourcesBinder binder = new PropertySourcesBinder(
      new PropertiesPropertySource("dataSource", properties));

    binder.bindTo("", dataSource);

    return dataSource;
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    return new DataSourceTransactionManager(dataSource());
  }
}
