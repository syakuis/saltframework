package org.saltframework.module.data;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.ExecutorType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 10.
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "org.saltframework.module.data.mybatis", annotationClass = Repository.class)
public class MybatisConfiguration {
  @Bean(destroyMethod = "close")
  public BasicDataSource dataSourceMybatis() {
    BasicDataSource basicDataSource = new BasicDataSource();
    basicDataSource.setDriverClassName("org.h2.Driver");
//    basicDataSource.setUrl("jdbc:h2:file:/Users/syaku/develop/Server/H2/data/salt-mybatis;DB_CLOSE_DELAY=-1");
    basicDataSource.setUrl("jdbc:h2:mem:salt-mybatis;DB_CLOSE_DELAY=-1");
    basicDataSource.setUsername("sa");
    return basicDataSource;
  }

//  @Bean
//  public DataSourceInitializer dataSourceInitializerMybatis() {
//    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
//    resourceDatabasePopulator.addScripts(
//        new ClassPathResource("org/saltframework/module/data/schemas/demo.table.h2.sql")
//    );
//
//    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
//    dataSourceInitializer.setDataSource(dataSourceMybatis());
//    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
//    return dataSourceInitializer;
//  }

  @Bean
  public PlatformTransactionManager myBatisTxManager() {
    return new DataSourceTransactionManager(dataSourceMybatis());
  }

  @Bean
  public SqlSessionFactoryBean sqlSessionFactoryBean() {
    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    bean.setDataSource(dataSourceMybatis());
    bean.setTypeAliasesPackage("org.saltframework.module.data.mybatis");
//    bean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));

    return bean;
  }

  @Bean
  public SqlSessionTemplate sqlSessionTemplate() throws Exception {
    return new SqlSessionTemplate(sqlSessionFactoryBean().getObject(), ExecutorType.BATCH);
  }
}
