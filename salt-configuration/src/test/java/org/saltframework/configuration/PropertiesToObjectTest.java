package org.saltframework.configuration;

import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.resources.properties.Config;
import org.saltframework.resources.properties.bean.factory.PropertiesSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.bind.PropertySourcesBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Data;
import lombok.ToString;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 5. 11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfig.class, SpringConfig2.class })
public class PropertiesToObjectTest {

  @Autowired
  private Environment environment;

  @Autowired
  private Config config;

  @Test
  public void springBootTest() {
    PropertySourcesBinder binder = new PropertySourcesBinder(
      ((ConfigurableEnvironment) environment).getPropertySources());

    User user = new User();

    binder.bindTo("", user);

    System.out.println(user);
  }

  @Test
  public void beanUtilsTest() {
    System.out.println(config.getString("username"));
  }
}


@Configuration
@PropertySource("classpath:/test.properties")
class SpringConfig {
  @Bean
  public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
    return new PropertyPlaceholderConfigurer();
  }
}

@Configuration
@PropertiesSource(name = "properties", locations = "classpath:/test.properties")
class SpringConfig2 {
  @Autowired
  private Properties properties;

  @Bean
  public Config config() {
    return new Config(properties);
  }
}

@Data
@ToString
class User {
  private String username;
  private String name;
}

