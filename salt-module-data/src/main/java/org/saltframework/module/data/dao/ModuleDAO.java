package org.saltframework.module.data.dao;

import java.util.List;

import org.saltframework.module.data.domain.ModuleEntity;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 6.
 */
public interface ModuleDAO {

  ModuleEntity findOne(String moduleId);

  List<ModuleEntity> findAll();

  void delete(String moduleId);

  void save(ModuleEntity moduleEntity);
}
