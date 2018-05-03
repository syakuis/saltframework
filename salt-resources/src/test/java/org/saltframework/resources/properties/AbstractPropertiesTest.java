package org.saltframework.resources.properties;

import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.saltframework.resources.properties.bean.factory.PropertiesFactoryBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
@TestPropertySource("/org/saltframework/resources/properties/first.properties")
public class AbstractPropertiesTest {
  @Autowired
  Config config;

  @Autowired
  private Environment env;

  private String[] getStringArray(String key) {
    return StringUtils.delimitedListToStringArray(env.getProperty(key), ",");
  }

  private List<String> getList(String key) {
    return Arrays.asList(getStringArray(key));
  }

  @Test
  public void test() {

    Iterator iterator = config.getProperties().entrySet().iterator();

    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }

    String[] stringArray = config.getArray("test.list");
    List<String> stringList = config.getList("test.stringArray");

    Assert.assertEquals(config.getString("test.string"), env.getProperty("test.string"));
    Assert.assertArrayEquals(config.getStringArray("test.stringArray"), this.getStringArray("test.stringArray"));
    Assert.assertArrayEquals(stringArray, new String[]{ "a", "b b", "c" });
    Assert.assertThat(stringList, is(getList("test.stringArray")));
    Assert.assertTrue(config.getBoolean("test.boolean"));
    Assert.assertEquals(config.getLonger("test.long"), Long.decode(env.getProperty("test.long")));
    Assert.assertEquals(config.getLong("test.long"), Long.decode(env.getProperty("test.long")).longValue());
    Assert.assertEquals(config.getLong("test.long"), Long.parseLong(env.getProperty("test.long")));
    Assert.assertEquals(config.getInteger("test.integer"), Integer.decode(env.getProperty("test.integer")));
    Assert.assertEquals(config.getInt("test.integer"), Integer.decode(env.getProperty("test.integer")).intValue());
    Assert.assertEquals(config.getInt("test.integer"), Integer.parseInt(env.getProperty("test.integer")));
  }
}

class Config extends AbstractProperties {
  public Config(Properties properties) {
    super(properties);
  }
}

@Configuration
class TestConfiguration {
    @Bean
    public PropertiesFactoryBean properties() {
      PropertiesFactoryBean bean = new PropertiesFactoryBean();
      bean.setLocations(
          "classpath:org/saltframework/resources/**/first.properties",
          "classpath:org/saltframework/resources/*/second.properties"
      );

      return bean;
    }

    @Bean
    public Config config() throws Exception {
      return new Config(properties().getObject());
    }

}
