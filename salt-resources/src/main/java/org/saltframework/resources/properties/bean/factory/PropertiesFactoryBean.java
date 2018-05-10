package org.saltframework.resources.properties.bean.factory;

import java.io.IOException;
import java.util.Properties;

import org.saltframework.resources.properties.PropertiesLoader;

/**
 * Properties Spring Bean 을 생성하기 위한 클래스이다.
 *
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2017. 8. 29.
 *
 * @see AbstractPropertiesFactoryBean
 * @see PropertiesLoader
 */
public class PropertiesFactoryBean extends AbstractPropertiesFactoryBean {
  @Override
  protected Properties createObject() throws IOException {
    PropertiesLoader loader = new PropertiesLoader();
    loader.setLocations(this.getLocations());
    String fileEncoding = this.getFileEncoding();
    if (fileEncoding != null) {
      loader.setFileEncoding(fileEncoding);
    }
    return loader.getProperties();
  }
}
