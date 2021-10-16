package org.saltframework.resources.properties.bean.factory;

import org.saltframework.resources.properties.Config;
import org.saltframework.resources.properties.PropertiesLoader;

/**
 * {@link Config} Properties Spring Bean 을 생성하기 위한 클래스이다.
 *
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2017. 8. 29.
 *
 * @see AbstractPropertiesFactoryBean
 * @see PropertiesLoader
 * @see Config
 */
public class ConfigPropertiesFactoryBean extends AbstractPropertiesFactoryBean<Config> {

  @Override
  protected Config createObject() throws Exception {
    return new Config(super.loader());
  }

  @Override
  public Class<Config> getObjectType() {
    return Config.class;
  }
}
