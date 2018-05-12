package org.saltframework.configuration;

import java.util.Properties;

import org.junit.Assert;
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
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
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
//    PropertySourcesBinder binder = new PropertySourcesBinder(
//      ((ConfigurableEnvironment) environment).getPropertySources());

    Properties properties = config.getProperties("user.", false);
    System.out.println(properties);

    PropertySourcesBinder binder = new PropertySourcesBinder(
      new PropertiesPropertySource("user", properties));

    System.out.println(binder.getPropertySources().get("user").getProperty("name"));

    User user = new User();

    binder.bindTo("", user);

    System.out.println(user);

    Assert.assertEquals(user.getName(), config.getString("user.name"));
    Assert.assertEquals(user.getUsername(), config.getString("user.username"));
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
@PropertiesSource(beanName = "properties", value = "classpath:/test.properties")
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

