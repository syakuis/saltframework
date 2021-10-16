package org.saltframework.module.bean.factory;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import org.saltframework.module.Module;
import org.saltframework.module.ModuleContextManager;
import org.saltframework.module.ModuleDefinition;
import org.saltframework.module.ModuleDefinitionScanner;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * {@link ModuleDefinition} 을 검색하여 {@link Module} 로 정의하고 객체를 {@link ModuleContextManager#context} 저장한다.
 *
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 28.
 * @see Module
 * @see ModuleContextManager
 * @see ModuleDefinitionScanner
 * @see ModuleDefinition
 */
public class ModuleContextManagerFactoryBean implements FactoryBean<ModuleContextManager>,
    InitializingBean {
  private ModuleContextManager moduleContextManager;
  private String[] basePackages;
  private Class<? extends Annotation> annotationTypeFilter = ModuleDefinition.class;

  /**
   * {@link ModuleDefinition} 를 검색할 대상 패키지 경로를 지정한다.
   * @param basePackage 문자열로 입력하며 , 여러개 입력할 수 있다.
   */
  public void setBasePackage(String basePackage) {
    this.basePackages = StringUtils.commaDelimitedListToStringArray(basePackage);
  }

  /**
   * {@link ModuleDefinition} 를 검색할 대상 패키지 경로를 지정한다.
   * @param basePackages 여러개 입력할 수 있다.
   */
  public void setBasePackages(String... basePackages) {
    this.basePackages = basePackages;
  }

  /**
   * 검색하고자 하는 Annotation 을 설정할 수 있다.
   * @param annotationTypeFilter default {@link ModuleDefinition}
   */
  public void setAnnotationTypeFilter(
      Class<? extends Annotation> annotationTypeFilter) {
    this.annotationTypeFilter = annotationTypeFilter;
  }

  @Override
  public Class<ModuleContextManager> getObjectType() {
    return ModuleContextManager.class;
  }

  @Override
  public ModuleContextManager getObject() {
    return this.moduleContextManager;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public void afterPropertiesSet() {
    this.createInstance();
  }

  /**
   * 대상 basePackages 에 annotationTypeFilter 을 찾고 {@link Module} 객체를 {@link ModuleContextManager} 저장한다.
   */
  private void createInstance() {
    if (this.basePackages == null) {
      this.moduleContextManager = new ModuleContextManager();
    }

    ModuleDefinitionScanner scanner = new ModuleDefinitionScanner(basePackages, annotationTypeFilter);
    this.moduleContextManager = scanner.getModules().stream()
        .collect(
            ModuleContextManager::new,
            ModuleContextManager::add,
            (moduleContextManager1, moduleContextManager2) -> Function.identity());
  }
}