package org.saltframework.resources.properties;


import java.io.IOException;
import java.util.Properties;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2017. 8. 26.
 */
public class PropertiesLoader extends AbstractPropertiesLoader {
  public PropertiesLoader() {
    super();
    super.setIgnoreResourceNotFound(true);
    super.setLocalOverride(true);
  }

  @Override
  public Properties getProperties() throws IOException {
    super.setLocations(this.getLocationResources());
    return super.mergeProperties();
  }
}
