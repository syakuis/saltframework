package org.saltframework.resources.properties;

import java.util.Properties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * Properties Loader Test
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 19.
 */
public class PropertiesLoaderTest {
  private PropertiesLoader loader;
  private Properties first;
  private Properties second;

  @Before
  public void setup() throws Exception {
    loader = new PropertiesLoader();

    loader.setLocations(
        "classpath:org/saltframework/resources/**/first.properties",
        "classpath:org/saltframework/resources/*/second.properties"
    );

    first = PropertiesLoaderUtils.loadProperties(new ClassPathResource("org/saltframework/resources/properties/first.properties"));
    second = PropertiesLoaderUtils.loadProperties(new ClassPathResource("org/saltframework/resources/properties/second.properties"));
  }

  @Test
  public void test() throws Exception {
    Properties properties = loader.getProperties();
    Assert.assertNotNull(properties);
    String kor = properties.getProperty("kor");
    String name = properties.getProperty("name");

    Assert.assertEquals(kor, first.getProperty("kor"));
    Assert.assertEquals(name, second.getProperty("name"));
  }
}
