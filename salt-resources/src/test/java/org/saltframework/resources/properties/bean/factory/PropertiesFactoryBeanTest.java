package org.saltframework.resources.properties.bean.factory;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.resources.properties.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Properties Factory Bean Test
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PropertiesConfiguration.class)
@WebAppConfiguration
@TestPropertySource(locations = {
    "/org/saltframework/resources/properties/first.properties",
    "/org/saltframework/resources/properties/second.properties"
})
@ActiveProfiles("test")
public class PropertiesFactoryBeanTest {

  @Autowired
  private Environment environment;

  @Autowired
  private Properties properties;

  @Autowired
  private Config config;

  @Autowired
  @Qualifier("stringPathProperties")
  private Properties stringPathProperties;

  @Autowired
  @Qualifier("profileProperties")
  private Properties profileProperties;

  @Test
  public void environment() {
    Assert.assertFalse(properties.isEmpty());
    Assert.assertEquals(environment.getProperty("env.name"), properties.getProperty("env.name"));
  }

  @Test
  public void test() {
    Assert.assertFalse(properties.isEmpty());
    Assert.assertFalse(config.isEmpty());
    Assert.assertFalse(stringPathProperties.isEmpty());
    Assert.assertFalse(profileProperties.isEmpty());

    // locations 자료형을 배열과 문자로 했을 때 두 결과가 같은지 판단한다.
    Assert.assertTrue(properties.equals(stringPathProperties));
    // Spring PropertySource 과 비교한다.
    Assert.assertEquals(properties.getProperty("name"), environment.getProperty("name"));
    Assert.assertEquals(properties.getProperty("name"), config.getString("name"));
    // profile 사용하여 프로퍼티를 잘 읽어오는 지 판단한다.
    Assert.assertEquals(profileProperties.getProperty("name"), "test");
    Assert.assertEquals(profileProperties.getProperty("i18n"), environment.getProperty("i18n"));

    Assert.assertTrue(properties.containsKey("module.commons.module"));
    Assert.assertTrue(properties.containsKey("module.module.module"));
  }
}

@Configuration
class PropertiesConfiguration {
  @Bean
  public PropertiesFactoryBean properties() {
    PropertiesFactoryBean bean = new PropertiesFactoryBean();
    bean.setPropertySourceName("propertiesEnv");
    bean.setAddToPropertySource(true);
    bean.setLocations(
        "classpath:org/saltframework/resources/**/first.properties",
        "classpath:org/saltframework/resources/*/second.properties",
        "classpath:org/saltframework/resources/properties/app/*/module.properties",
        "classpath:org/saltframework/resources/properties/env.properties"
    );

    return bean;
  }

  @Bean
  public ConfigPropertiesFactoryBean config() {
    ConfigPropertiesFactoryBean bean = new ConfigPropertiesFactoryBean();
    bean.setLocations(
        "classpath:org/saltframework/resources/**/first.properties",
        "classpath:org/saltframework/resources/*/second.properties",
        "classpath:org/saltframework/resources/properties/app/*/module.properties",
        "classpath:org/saltframework/resources/properties/env.properties"
    );

    return bean;
  }

  @Bean
  public PropertiesFactoryBean stringPathProperties() {
    PropertiesFactoryBean bean = new PropertiesFactoryBean();
    bean.setLocation(
        "classpath:org/saltframework/resources/**/first.properties,"
            + "classpath:org/saltframework/resources/*/second.properties,"
            + "classpath:org/saltframework/resources/properties/app/*/module.properties, "
            + "classpath:org/saltframework/resources/properties/env.properties"
    );

    return bean;
  }

  @Bean
  public PropertiesFactoryBean profileProperties() {
    PropertiesFactoryBean bean = new PropertiesFactoryBean();
    bean.setLocations(
        "classpath:org/saltframework/resources/**/first.properties",
        "classpath:org/saltframework/resources/*/second.properties",
        "classpath:org/saltframework/resources/properties/first-{profile}.properties"
    );

    return bean;
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
