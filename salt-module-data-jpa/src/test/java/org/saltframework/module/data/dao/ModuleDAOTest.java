package org.saltframework.module.data.dao;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.configuration.JdbcDataSource;
import org.saltframework.configuration.JpaEntityManager;
import org.saltframework.module.data.domain.JPAModuleEntity;
import org.saltframework.module.data.domain.JPAModuleOptionEntity;
import org.saltframework.module.data.domain.ModuleEntity;
import org.saltframework.module.data.domain.ModuleOptionEntity;
import org.saltframework.resources.properties.bean.factory.PropertiesSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * todo 2개의 entityManagerFactory 만들고 transaction 을 하나로 처리할 수 있게?
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigModuleDAOTest.class)
@Transactional
public class ModuleDAOTest {
  @Autowired
  private DataSource dataSource;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private ModuleDAO moduleDAO;

  @Autowired
  private ModuleOptionDAO moduleOptionDAO;


  @Test
  public void 일반적인테스트() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    Assert.assertTrue(jdbcTemplate.queryForList("select * from module").size() > 0);
  }

  @Test
  public void CURD() {
    ModuleEntity newModuleEntity = new JPAModuleEntity("1234", "1234");
    moduleDAO.save(newModuleEntity);

    ModuleEntity moduleEntity = moduleDAO.findOne("1234");
    Assert.assertEquals(moduleEntity, moduleEntity);

    ModuleOptionEntity newModuleOptionEntity = new JPAModuleOptionEntity("1234", "a", "b", "a", 0);
    moduleOptionDAO.save(newModuleOptionEntity);

    List<ModuleOptionEntity> moduleOptionEntities = moduleOptionDAO.findOne("1234");

    Assert.assertFalse(moduleOptionEntities.isEmpty());
    Assert.assertTrue(moduleOptionEntities.indexOf(newModuleOptionEntity) > -1);

    // 디비가 아닌 객체이기 때문에 ??? 널이다...
    Assert.assertNull(moduleDAO.findOne("1234").getModuleOptionEntities());
    // 널이 아니게 하려면... option 도 moduleentity 에서 저장하거나... 양방향 관계설정을 통해 case 전략을 설정한다.

    // 디비에 반영
    entityManager.flush();
    entityManager.clear();

    Assert.assertNotNull(moduleDAO.findOne("1234").getModuleOptionEntities());
    Assert.assertTrue(moduleDAO.findOne("1234").getModuleOptionEntities().size() > -1);

  }

//  @Autowired
//  DemoEntityDAO demoEntityDAO;

//  @Autowired
//  private EntityManager entityManager;
//
//  @Before
//  public void setup() {
//    this.entityManager.getTransaction().begin();
//  }
//
//  @After
//  public void close() {
//    this.entityManager.getTransaction().commit();
//  }

//  @Test
//  public void jpaEntityManager() {
//    EntityManager manager = moduleDAO.getEntityManager();
//    System.out.println(entityManager.getTransaction().isActive());
//    System.out.println(manager.getTransaction().isActive());
//  }
//
//  public void save1() {
//    moduleDAO.save(new JPAModuleEntity("board", "board"));
//  }
//
//  public void save2() {
//    demoEntityDAO.save(new DemoEntity("goo", "2"));
//  }
//
//  @Test
//  public void transaction() {
//    try {
//      save1();
//      save2();
//      System.out.println(moduleDAO.findAll().size());
//      System.out.println(demoEntityDAO.findAll().size());
////      int a = 1 / 0;
//    } catch (Exception e) {
//      throw new RuntimeException();
//    }
//
//    System.out.println(moduleDAO.findAll().size());
//    System.out.println(demoEntityDAO.findAll().size());
//  }
//
//  @Test
//  public void crud() {
//    List<ModuleEntity> moduleEntities = moduleDAO.findAll();
//
//    moduleEntities.stream().forEach(moduleEntity -> {
//      ModuleEntity newModuleEntity = moduleDAO.findOne(moduleEntity.getModuleId());
//      Assert.assertEquals(moduleEntity, newModuleEntity);
//    });
//
//    moduleDAO.save(new JPAModuleEntity("board", "board"));
//
//    Assert.assertTrue(moduleDAO.findAll().size() > moduleEntities.size());
//
//    moduleDAO.delete("board");
//
//    Assert.assertNull(moduleDAO.findOne("board"));
//
//    Assert.assertTrue(moduleDAO.findAll().size() == moduleEntities.size());
//
//    ModuleEntity moduleEntity = moduleDAO.findOne("test");
//
//    moduleEntity.getModuleOptionEntities().stream().forEach(System.out::println);
//
//    moduleOptionDAO.deleteByModuleId("test");
//  }
}

@Configuration
@PropertiesSource({
  "classpath:org/saltframework/boot/config.properties",
  "classpath:config.properties",
  "classpath:config-{profile}.properties"
})

@ComponentScan(
    basePackages = "org.saltframework.module",
    useDefaultFilters = false,
    includeFilters = @Filter(type = FilterType.ANNOTATION, value = Repository.class)
)

@Import({JdbcDataSource.class, JpaEntityManager.class})
class ConfigModuleDAOTest {

  @Bean
  public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
    resourceDatabasePopulator.addScripts(
        new ClassPathResource("org/saltframework/module/data/schemas/module.table.h2.sql"),
        new ClassPathResource("org/saltframework/module/data/schemas/moduleOption.table.h2.sql"),
        new ClassPathResource("org/saltframework/module/data/schemas/demo.table.h2.sql"),
        new ClassPathResource("org/saltframework/module/data/schemas/module.data.h2.sql")
    );

    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
    dataSourceInitializer.setDataSource(dataSource);
    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
    return dataSourceInitializer;
  }
}

