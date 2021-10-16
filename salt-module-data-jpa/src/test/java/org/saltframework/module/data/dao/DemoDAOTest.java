package org.saltframework.module.data.dao;

import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.module.data.mybatis.dao.DemoEntityDAO;
import org.saltframework.module.data.mybatis.domain.DemoEntity;
import org.saltframework.module.data.mybatis.domain.Member;
import org.saltframework.module.data.mybatis.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DemoDAOSetting.class)
@Transactional
public class DemoDAOTest {
  @Autowired
  DemoEntityDAO demoDAO;

  @PersistenceContext
  private EntityManager entityManager;

  @Test
  @Commit
  public void hibernatetest() {
    Member member = new Member("a", "a", new Date());
    member.addUser(new User("b", "a", member));
    member.addUser(new User("b", "a", member));
    member.addUser(new User("b", "a", member));
    member.addUser(new User("b", "a", member));

    this.entityManager.persist(member);

    this.entityManager.createQuery("FROM Member", Member.class).getResultList()
        .stream().forEach(m -> {
          System.out.println(m);
          m.getUsers().stream().forEach(System.out::println);
    });
  }

  @Test
  @Commit
  public void test() {
    DemoEntity demoEntity = new DemoEntity("a", "a");
    demoDAO.save(demoEntity);

    demoEntity.setValue("ewqew");
//    demoDAO.findAll().stream().forEach(System.out::println);
  }

  @Test
  public void test2() {
//    demoDAO.save(new DemoEntity("a", "a"));
    demoDAO.findAll().stream().forEach(System.out::println);
  }

}

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "org.saltframework.module.data.mybatis")
class DemoDAOSetting {

  @PostConstruct
  public void setup() {
    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
    resourceDatabasePopulator.addScripts(
        new ClassPathResource("org/saltframework/module/data/schemas/demo.table.h2.sql")
    );

    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
    dataSourceInitializer.setDataSource(dataSource());
    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
    dataSourceInitializer.afterPropertiesSet();
  }

  @Bean(destroyMethod = "close")
  public BasicDataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUsername("sa");
    dataSource.setUrl("jdbc:h2:mem:salt;DB_CLOSE_DELAY=-1");
    return dataSource;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
        new LocalContainerEntityManagerFactoryBean();

    entityManagerFactoryBean.setDataSource(dataSource());
    entityManagerFactoryBean.setPackagesToScan("org.saltframework.module.data.mybatis");
    entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManagerFactoryBean.setJpaProperties(hibernateProperties());
    return entityManagerFactoryBean;
  }

  private Properties hibernateProperties() {
    Properties hibernateProperties = new Properties();
    hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    hibernateProperties.setProperty("hibernate.show_sql", "true");
    hibernateProperties.setProperty("hibernate.format_sql", "true");
    hibernateProperties.setProperty("hibernate.use_sql_comments", "true");

    return hibernateProperties;
  }

  @Bean
  @Primary
  public PlatformTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
    return transactionManager;
  }
}
