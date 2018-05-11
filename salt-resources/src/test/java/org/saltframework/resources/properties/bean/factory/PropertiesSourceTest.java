package org.saltframework.resources.properties.bean.factory;

import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 5. 11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class PropertiesSourceTest {

  @Autowired
  private Properties config;

  @Test
  public void test() {
    System.out.println(config.getProperty("env.name"));
  }
}

@Configuration
@PropertiesSource("classpath:org/saltframework/resources/properties/env.properties")
class Config {

}
