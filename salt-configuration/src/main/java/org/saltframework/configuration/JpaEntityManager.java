package org.saltframework.configuration;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.saltframework.resources.properties.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 14.
 */
@Configuration
@EnableTransactionManagement
public class JpaEntityManager {

  @Autowired
  private Config config;

  @Autowired
  private DataSource dataSource;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setDataSource(dataSource);

    bean.setPackagesToScan(
        ArrayUtils.add(config.getStringArray("jpa.packageToScan"),
            config.getString("default.jpa.packageToScan")));

    bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

    List<String> keys = config.getKeys("jpa.properties.");

    if (!keys.isEmpty()) {
      bean.setJpaProperties(hibernateProperties(keys));
    }

    return bean;
  }

  private Properties hibernateProperties(List<String> keys) {
    Properties properties = new Properties();

    for (String key : keys) {
      properties.setProperty(key.replace(".properties.", "."),
          config.getString(key));
    }

    return properties;
  }

  @Bean
  @Primary
  public JpaTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
    return transactionManager;
  }
}