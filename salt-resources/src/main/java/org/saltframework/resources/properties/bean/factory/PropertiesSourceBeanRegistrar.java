package org.saltframework.resources.properties.bean.factory;

import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 5. 11.
 */
public class PropertiesSourceBeanRegistrar implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
    Map<String, Object> config = annotationMetadata.getAnnotationAttributes(PropertiesSource.class.getName());

    String name = (String) config.get("name");
    String fileEncoding = (String) config.get("fileEncoding");
    String[] locations = (String[]) config.get("locations");

    Assert.notEmpty(locations, "읽을 프로퍼티 경로를 하나이상 입력하세요.");

    registry.registerBeanDefinition(name, propertiesFactoryBean(locations, fileEncoding));
  }

  private BeanDefinition propertiesFactoryBean(String[] locations, String fileEncoding) {
    MutablePropertyValues propertyValues = new MutablePropertyValues();
    propertyValues.add("fileEncoding", fileEncoding);
    propertyValues.add("locations", locations);

    RootBeanDefinition beanDefinition = new RootBeanDefinition();
    beanDefinition.setBeanClass(PropertiesFactoryBean.class);
    beanDefinition.setPropertyValues(propertyValues);

    return beanDefinition;
  }
}
