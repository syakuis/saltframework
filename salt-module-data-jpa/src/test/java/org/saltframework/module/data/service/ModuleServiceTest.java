package org.saltframework.module.data.service;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.module.data.JPAConfiguration;
import org.saltframework.module.data.domain.JPAModuleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JPAConfiguration.class, Setting.class})
@Transactional
public class ModuleServiceTest {

  @Autowired
  private ModuleService moduleService;

  @Test
  public void test() {
    JPAModuleEntity moduleEntity = new JPAModuleEntity();
    moduleEntity.setModuleId("good");
    moduleEntity.setModuleName("good");
    moduleEntity.setBrowserTitle("good");
    moduleEntity.setRegistryDate(new Date());

//    moduleEntity.setModuleOptionEntities(JPAModuleOptionEntity.createList(
//        new JPAModuleOptionEntity(
//            moduleEntity.getModuleId(), "a", "b", "a", 0),
//        new JPAModuleOptionEntity(
//            moduleEntity.getModuleId(), "a", "b", "a", 0)
//    ));

    moduleService.save(moduleEntity);
  }

}

@Configuration
@ComponentScan(
    basePackages = "org.saltframework.module.data",
    useDefaultFilters = false,
    includeFilters = @Filter(
        type = FilterType.ANNOTATION, value = {Repository.class, Service.class})
)
class Setting {

}
