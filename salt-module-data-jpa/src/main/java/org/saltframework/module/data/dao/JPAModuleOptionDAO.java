package org.saltframework.module.data.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.saltframework.module.data.domain.ModuleOptionEntity;
import org.springframework.stereotype.Repository;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 12.
 */
@Repository(value = "moduleOptionDAO")
public class JPAModuleOptionDAO implements ModuleOptionDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void save(ModuleOptionEntity moduleOptionEntity) {
    this.entityManager.persist(moduleOptionEntity);
  }

  @Override
  public void deleteByModuleId(String moduleId) {
    List<ModuleOptionEntity> moduleOptionEntities =
        this.entityManager.createQuery("FROM moduleOptionEntity WHERE module_id = :moduleId",
            ModuleOptionEntity.class)
            .setParameter("moduleId", moduleId).getResultList();

    for (ModuleOptionEntity moduleOptionEntity : moduleOptionEntities) {
      this.entityManager.remove(moduleOptionEntity);
    }
  }

  @Override
  public void deleteOne(String moduleId, String optionName) {

  }
}
