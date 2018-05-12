package org.saltframework.resources.properties.bean.factory;

import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 5. 11.
 *
 * @see PropertiesSource
 * @see PropertiesFactoryBean
 * @see ConfigPropertiesFactoryBean
 */
public class PropertiesSourceBeanRegistrar implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
    Map<String, Object> config = annotationMetadata.getAnnotationAttributes(PropertiesSource.class.getName());

    // todo PropertySource 필드명이 변경되면 Map 의 key 명을 변경하지 않으면 컴파일시 오류가 발생될 수 있도록 하기 위한 방법?
    String beanName = (String) config.get("beanName");
    boolean configEnable = (boolean) config.get("configEnable");
    String propertySourceName = (String) config.get("propertySourceName");
    if ("".equals(propertySourceName)) {
      propertySourceName = null;
    }
    String fileEncoding = (String) config.get("fileEncoding");
    String[] value = (String[]) config.get("value");

    Assert.notEmpty(value, "PropertiesSource#value is required.");

    MutablePropertyValues propertyValues = new MutablePropertyValues();
    propertyValues.add("fileEncoding", fileEncoding);
    propertyValues.add("locations", value);
    propertyValues.add("propertySourceName", propertySourceName);

    RootBeanDefinition beanDefinition = new RootBeanDefinition();
    if (configEnable) {
      beanDefinition.setBeanClass(ConfigPropertiesFactoryBean.class);
    } else {
      beanDefinition.setBeanClass(PropertiesFactoryBean.class);
    }
    beanDefinition.setPropertyValues(propertyValues);

    registry.registerBeanDefinition(beanName, beanDefinition);
  }
}
