package org.syaku.test;

import java.util.Properties;

import org.junit.Test;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 5. 13.
 */
public class PropertiesTest {

  @Test
  public void test() {
    Properties properties = new Properties();
    properties.setProperty("good", "ok");

    PropertiesTest2 test2 = new PropertiesTest2(properties);

    System.out.println(test2);
  }
}

class PropertiesTest2 extends Properties {
  public PropertiesTest2(Properties properties) {
    super(properties);
    super.defaults = properties;
    System.out.println(properties);
  }

  public Properties getPropertis() {
    return super.defaults;
  }
}
