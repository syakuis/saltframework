package org.saltframework.module.bean.factory;

import org.saltframework.module.Module;
import org.saltframework.module.ModuleContextManager;
import org.saltframework.module.ModuleContextService;
import org.saltframework.module.ModuleRedefinition;
import org.saltframework.module.aop.ModuleRedefinitionAspectAdvice;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.FactoryBean;

/**
 * AOP 를 이용하여 {@link ModuleRedefinition} 선언된 메서드가 호출될때 마다 {@link ModuleContextService} 에 데이터를 읽어와
 * {@link ModuleContextManager#context} 를 재정의한다.
 *
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 28.
 * @see Module
 * @see ModuleContextManager
 * @see ModuleContextService
 * @see ModuleRedefinition
 * @see AspectJExpressionPointcutAdvisor
 */
public class ModuleRedefinitionAspectFactoryBean implements FactoryBean<Advisor> {
  private AspectJExpressionPointcutAdvisor advisor;
  private final ModuleContextManager moduleContextManager;
  private final ModuleContextService moduleContextService;

  public ModuleRedefinitionAspectFactoryBean(ModuleContextManager moduleContextManager,
      ModuleContextService moduleContextService) {
    this.moduleContextManager = moduleContextManager;
    this.moduleContextService = moduleContextService;
  }

  @Override
  public Advisor getObject() {
    if (advisor == null) {
      createInstance();
    }
    return advisor;
  }

  private void createInstance() {
    this.advisor = new AspectJExpressionPointcutAdvisor();
    advisor.setExpression("@annotation(" + ModuleRedefinition.class.getName() + ")");

    ModuleRedefinitionAspectAdvice advice = new ModuleRedefinitionAspectAdvice(moduleContextManager,
        moduleContextService);
    advisor.setAdvice(advice);
  }

  @Override
  public Class<Advisor> getObjectType() {
    return Advisor.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}