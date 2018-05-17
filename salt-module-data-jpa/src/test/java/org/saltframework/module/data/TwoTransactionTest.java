package org.saltframework.module.data;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.module.data.dao.ModuleDAO;
import org.saltframework.module.data.domain.JPAModuleEntity;
import org.saltframework.module.data.mybatis.dao.DemoDAO;
import org.saltframework.module.data.mybatis.domain.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Setting.class, JPAConfiguration.class, MybatisConfiguration.class})
@Transactional
public class TwoTransactionTest {
  @Autowired
  private ModuleDAO moduleDAO;

  @Autowired
  private DemoDAO demoDAO;

  @Test(expected = RuntimeException.class)
  public void rollback() {
    int hibernate = moduleDAO.findAll().size();
    int mybatis = demoDAO.findAll().size();

    try {
      moduleDAO.save(new JPAModuleEntity("aaa", "aaa"));
      demoDAO.insert(new Demo("a", "b"));
      int a = 1 / 0;
    } catch (Exception e) {
      throw new RuntimeException();
    }

    Assert.assertTrue(hibernate == moduleDAO.findAll().size());
    Assert.assertTrue(mybatis == demoDAO.findAll().size());
  }
}

@Configuration
@ComponentScan(
    basePackages = "org.saltframework.module.data.dao",
    useDefaultFilters = false,
    includeFilters = @Filter(type = FilterType.ANNOTATION, value = Repository.class)
)
class Setting {
}
