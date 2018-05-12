package org.saltframework.configuration;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.resources.properties.Config;
import org.saltframework.resources.properties.bean.factory.PropertiesSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class, JdbcDataSource.class })
public class JdbcDataSourceTest {

  @Autowired
  private Config config;

  @Autowired
  private DataSource dataSource;

  @Test
  public void test() {
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("demo.table.h2.sql"));
    populator.execute(dataSource);

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    jdbcTemplate.queryForList("select * from demo").size();
  }
}

@Configuration
@PropertiesSource("classpath:/config.properties")
class TestConfiguration {
}
