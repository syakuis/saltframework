package org.saltframework.resources.properties.bean.factory;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.resources.properties.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 5. 11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class, TestConfiguration2.class })
@ActiveProfiles("test")
public class PropertiesSourceTest {

  @Autowired
  private Properties properties;

  @Autowired
  private Config config;

  @Test
  public void test() {
    Assert.assertNotNull(properties);
    Assert.assertEquals(properties.getProperty("name"), "test");
    Assert.assertNotNull(config);
  }
}

@Configuration
@PropertiesSource(
  beanName = "properties",
  value = {
    "classpath:org/saltframework/resources/properties/env.properties",
    "classpath:org/saltframework/resources/properties/first-{profile}.properties"
  },
  configEnable = false
)
class TestConfiguration {

}

@Configuration
@PropertiesSource("classpath:org/saltframework/resources/properties/env.properties")
class TestConfiguration2 {

}
