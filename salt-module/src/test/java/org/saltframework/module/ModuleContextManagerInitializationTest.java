package org.saltframework.module;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.module.test.ModuleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class ModuleContextManagerInitializationTest {

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
