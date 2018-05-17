package org.saltframework.module.data.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.saltframework.module.data.domain.JPAModuleEntity;
import org.saltframework.module.data.domain.ModuleEntity;
import org.springframework.stereotype.Repository;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 6.
 */
@Repository(value = "moduleDAO")
public class JPAModuleDAO implements ModuleDAO {

  @PersistenceContext(unitName = "entityManagerFactory")
  private EntityManager entityManager;

//  private EntityManager entityManager2;
//
//  @Autowired
//  public ModuleEntityDAO(EntityManager entityManager2) {
//    Assert.notNull(entityManager2, "the entityManager must not be null");
//    System.out.println(entityManager2.getEntityManagerFactory().getPersistenceUnitUtil());
//    this.entityManager2 = entityManager2;
//  }

  @Override
  public ModuleEntity findOne(String id) {
    return this.entityManager.find(JPAModuleEntity.class, id);
  }

  @Override
  public List<ModuleEntity> findAll() {
    return this.entityManager
        .createQuery("FROM moduleEntity", ModuleEntity.class)
        .getResultList();
  }

  @Override
  public void delete(String id) {
    this.entityManager.remove(this.entityManager.find(JPAModuleEntity.class, id));
  }

  @Override
  public void save(ModuleEntity moduleEntity) {
    this.entityManager.persist(moduleEntity);
  }
}
