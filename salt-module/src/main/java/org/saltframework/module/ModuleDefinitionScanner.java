package org.saltframework.module;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

/**
 * 특정 주석을 검색하여 객체를 {@link Module}로 정의한 목록을 구한다.
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 2.
 */
public class ModuleDefinitionScanner {
  private final Logger logger = LoggerFactory.getLogger(ModuleDefinitionScanner.class);
  private ClassPathScanningCandidateComponentProvider provider;
  private final String[] basePackages;
  private final Class<? extends Annotation> annotationTypeFilter;

  public ModuleDefinitionScanner(String[] basePackages,
      Class<? extends Annotation> annotationTypeFilter) {
    Assert.notNull(basePackages,
        "The basePackages must not be null.");
    Assert.notNull(annotationTypeFilter,
        "The annotationTypeFilter must not be null.");

    this.basePackages = Arrays.copyOf(basePackages, basePackages.length);
    this.annotationTypeFilter = annotationTypeFilter;

    provider = new ClassPathScanningCandidateComponentProvider(false);
    provider.addIncludeFilter(new AnnotationTypeFilter(annotationTypeFilter));
  }

  public List<Module> getModules() {
    return Arrays.stream(basePackages)
        .map(this::findAnnotatedClasses)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  /**
   * Module 를 찾는 다.
   * @param basePackage 찾을 대상 경로
   * @return 찾은 Module 을 {@link List} 로 반환한다.
   */
  private List<Module> findAnnotatedClasses(String basePackage) {
    provider = new ClassPathScanningCandidateComponentProvider(false);
    provider.addIncludeFilter(new AnnotationTypeFilter(annotationTypeFilter));

    return provider.findCandidateComponents(basePackage).stream()
      .map(Unchecked.function(beanDefinition -> {
        Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());

        if (clazz != null && clazz.getClass() == Module.class.getClass()) {
          logger.debug("New {} class instance. Prepared to add ModuleContext.", clazz.getCanonicalName());
          return (Module) clazz.newInstance();
        }

        return null;
      }))
      .filter(module -> module != null && module.getModuleId() != null)
      .collect(Collectors.toList());
  }
}
