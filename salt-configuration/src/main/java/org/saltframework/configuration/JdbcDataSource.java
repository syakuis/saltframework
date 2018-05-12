package org.saltframework.configuration;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.saltframework.configuration.condition.DataSourceBeanExistsIdentifier;
import org.saltframework.resources.properties.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.PropertySourcesBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 5. 12.
 */
@Configuration
public class JdbcDataSource {

  @Autowired
  private Config config;

  @Bean
  @Conditional(DataSourceBeanExistsIdentifier.class)
  public DataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();

    Properties properties = config.getProperties("dataSource.", false);
    PropertySourcesBinder binder = new PropertySourcesBinder(
      new PropertiesPropertySource("dataSource", properties));

    binder.bindTo("", dataSource);

    return dataSource;
  }

  @Bean
  @Conditional(DataSourceBeanExistsIdentifier.class)
  public PlatformTransactionManager transactionManager() {
    return new DataSourceTransactionManager(dataSource());
  }
}
