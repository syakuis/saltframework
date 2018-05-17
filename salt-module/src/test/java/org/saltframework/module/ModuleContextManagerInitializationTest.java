package org.saltframework.module;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.module.bean.factory.ModuleContextManagerFactoryBean;
import org.saltframework.module.test.ModuleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ModuleContextManagerInitializationTest {

  @Configuration
  static class ContextConfiguration {
    @Bean
    public ModuleContextManagerFactoryBean ModuleContextManagerFactoryBean() {
      ModuleContextManagerFactoryBean bean = new ModuleContextManagerFactoryBean();
      bean.setBasePackages("org.saltframework.module.test");
      return bean;
    }
  }

  @Autowired
  private ModuleContextManager moduleContextManager;

  @Test
  public void test() {
    Assert.assertTrue(moduleContextManager.toList().size() > 0);

    Module newModule = new ModuleBean();
    Module contextModule = moduleContextManager.get(newModule.getModuleId());
    Assert.assertEquals(newModule, contextModule);

    ModuleBean moduleBean = contextModule.getObject(ModuleBean.class);
    Assert.assertEquals(newModule.getObject(ModuleBean.class), moduleBean);
  }

}
