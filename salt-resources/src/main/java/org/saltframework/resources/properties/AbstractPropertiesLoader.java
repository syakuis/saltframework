package org.saltframework.resources.properties;


import java.io.IOException;
import java.util.Properties;
import org.saltframework.core.io.MultiplePathMatchingResourcePatternResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.util.Assert;

/**
 * 프로퍼티 파일을 읽는 다.
 * 경로는 Ant-style pattern 을 사용하고 delimiter 을 이용하여 여러 파일을 설정할 수 있다.
 *
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2017. 8. 22.
 * @see MultiplePathMatchingResourcePatternResolver
 * @see Resource
 */
public abstract class AbstractPropertiesLoader extends PropertiesLoaderSupport {
  private String[] locations;

  public void setLocations(String... locations) {
    this.locations = locations;
  }

  protected Resource[] getLocationResources() throws IOException {
    Assert.notEmpty(this.locations, "The locations must contain elements.");

    MultiplePathMatchingResourcePatternResolver matchingPattern =
        new MultiplePathMatchingResourcePatternResolver();
    return matchingPattern.getResources(this.locations);
  }

  public abstract Properties getProperties() throws IOException;
}
