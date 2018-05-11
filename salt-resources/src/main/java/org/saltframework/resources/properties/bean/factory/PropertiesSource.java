package org.saltframework.resources.properties.bean.factory;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 5. 11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(PropertiesSourceBeanRegistrar.class)
public @interface PropertiesSource {
  String name() default "config";
  String fileEncoding() default "UTF-8";
  @AliasFor("locations")
  String[] value() default {};
  @AliasFor("value")
  String[] locations() default {};
}
