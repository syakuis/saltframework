package org.saltframework.module.data.dao;

import java.util.List;

import org.saltframework.module.data.domain.ModuleOptionEntity;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 12.
 */
public interface ModuleOptionDAO {
  /**
   * {@link ModuleOptionEntity} 데이터 조회
   * @param moduleId moduleId
   * @return List
   */
  List<ModuleOptionEntity> findOne(String moduleId);
  void save(ModuleOptionEntity moduleOptionEntity);
  void deleteByModuleId(String moduleId);
  void deleteOne(String moduleId, String optionName);
}
