package org.saltframework.resources.properties.bean.factory;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 5. 11.
 *
 * @see AbstractPropertiesFactoryBean
 * @see PropertiesFactoryBean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(PropertiesSourceBeanRegistrar.class)
public @interface PropertiesSource {
  String beanName() default "config";
  boolean configEnable() default true;
  String propertySourceName() default "";
  boolean addToPropertySource() default false;
  String fileEncoding() default "UTF-8";

  /**
   * {@link AbstractPropertiesFactoryBean#locations}
   * @return String[]
   */
  String[] value();
}
