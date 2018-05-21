package org.saltframework.boot;

import org.saltframework.configuration.JdbcDataSource;
import org.saltframework.resources.properties.bean.factory.PropertiesSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 5. 17.
 */
@Configuration
@PropertiesSource({
  "classpath:org/saltframework/boot/config.properties",
  "classpath:config.properties",
  "classpath:config-{profile}.properties"
})
@Import({
  JdbcDataSource.class
})
public class Bootstrap {
}
