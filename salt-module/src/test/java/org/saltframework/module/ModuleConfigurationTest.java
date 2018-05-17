package org.saltframework.module;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.module.bean.factory.ModuleContextManagerFactoryBean;
import org.saltframework.module.bean.factory.ModuleRedefinitionAspectFactoryBean;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Setting.class)
public class ModuleConfigurationTest {
  @Autowired
  private ModuleContextManager moduleContextManager;

  @Test
  public void test() {
    Assert.assertNotNull(moduleContextManager);
  }
}

@Configuration
class Setting {
  @Bean
  public ModuleContextManagerFactoryBean moduleContextManager() {
    ModuleContextManagerFactoryBean bean = new ModuleContextManagerFactoryBean();
    bean.setBasePackages("org.saltframework.module.test");
    return bean;
  }

  @Bean
  public ModuleContextService moduleContextService() {
    return new BasicModuleContextService();
  }

  @Bean
  public Advisor moduleContextAspect() {
    ModuleRedefinitionAspectFactoryBean bean = new ModuleRedefinitionAspectFactoryBean(
        moduleContextManager().getObject(), moduleContextService()
    );

    return bean.getObject();
  }
}

class BasicModuleContextService implements ModuleContextService {
  @Override
  public List<Module> getModules() {
    return null;
  }

  @Override
  public Module getModule(String moduleId) {
    return null;
  }
}
