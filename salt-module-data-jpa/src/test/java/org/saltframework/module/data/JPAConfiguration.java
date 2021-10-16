package org.saltframework.module.data;

import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 6.
 */
@Configuration
@EnableTransactionManagement
public class JPAConfiguration {
  @Bean(destroyMethod = "close")
  public BasicDataSource dataSource() {
    BasicDataSource basicDataSource = new BasicDataSource();
    basicDataSource.setDriverClassName("org.h2.Driver");
    basicDataSource.setUrl("jdbc:h2:mem:salt;DB_CLOSE_DELAY=-1");
//    basicDataSource.setUrl("jdbc:h2:file:/Users/syaku/develop/Server/H2/data/salt2;DB_CLOSE_DELAY=-1");
    basicDataSource.setUsername("sa");

    return basicDataSource;
  }

//  @Bean
//  @Primary
//  public PlatformTransactionManager txManager() {
//    return new DataSourceTransactionManager(dataSource());
//  }

  @Bean
  public DataSourceInitializer dataSourceInitializer() {
    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
    resourceDatabasePopulator.addScripts(
        new ClassPathResource("org/saltframework/module/data/schemas/module.table.h2.sql"),
        new ClassPathResource("org/saltframework/module/data/schemas/moduleOption.table.h2.sql"),
        new ClassPathResource("org/saltframework/module/data/schemas/module.data.h2.sql"),
        new ClassPathResource("org/saltframework/module/data/schemas/demo.table.h2.sql")
    );

    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
    dataSourceInitializer.setDataSource(dataSource());
    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
    return dataSourceInitializer;
  }

//  @Bean
//  public LocalSessionFactoryBean sessionFactory() {
//    Properties hibernateProperties = new Properties();
//    hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//
//    LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
//    sessionFactoryBean.setDataSource(dataSource());
//    sessionFactoryBean.setPackagesToScan("org.saltframework.module.data.domain");
//    sessionFactoryBean.setHibernateProperties(hibernateProperties);
//
//    return sessionFactoryBean;
//  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    Properties hibernateProperties = new Properties();
    hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    hibernateProperties.setProperty("hibernate.show_sql", "true");
    hibernateProperties.setProperty("hibernate.format_sql", "true");
    hibernateProperties.setProperty("hibernate.use_sql_comments", "true");

    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setDataSource(dataSource());
    bean.setPackagesToScan("org.saltframework.module.data.domain");

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    bean.setJpaVendorAdapter(vendorAdapter);
    bean.setJpaProperties(hibernateProperties);

    return bean;
  }

//  @Bean
//  public EntityManager entityManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
//    return entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
//  }

  @Bean
  @Primary
  public PlatformTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    return transactionManager;
  }
}
