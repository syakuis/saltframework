package org.saltframework.module.data.mybatis.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.saltframework.module.data.mybatis.domain.DemoEntity;
import org.springframework.stereotype.Repository;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 12.
 */
@Repository
public class DemoEntityDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void save(DemoEntity demoEntity) {
    this.entityManager.persist(demoEntity);
  }

  public List<DemoEntity> findAll() {
    return this.entityManager.createQuery("FROM demoEntity", DemoEntity.class).getResultList();
  }
}
