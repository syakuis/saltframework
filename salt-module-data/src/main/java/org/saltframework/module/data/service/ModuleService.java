package org.saltframework.module.data.service;

import java.util.List;

import org.saltframework.module.data.dao.ModuleDAO;
import org.saltframework.module.data.dao.ModuleOptionDAO;
import org.saltframework.module.data.domain.ModuleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Module and ModuleOption 테이블의 데이터를 조회하고 관리하는 비지니스 로직
 *
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 12.
 */
@Service
@Transactional(readOnly = true)
public class ModuleService {
  private final ModuleDAO moduleDAO;
  private final ModuleOptionDAO moduleOptionDAO;

  @Autowired
  public ModuleService(ModuleDAO moduleDAO, ModuleOptionDAO moduleOptionDAO) {
    this.moduleDAO = moduleDAO;
    this.moduleOptionDAO = moduleOptionDAO;
  }

  public List<ModuleEntity> getModuleEntities() {
    return moduleDAO.findAll();
  }

  public ModuleEntity getModuleEntity(String moduleId) {
    return moduleDAO.findOne(moduleId);
  }

  @Transactional
  public void save(ModuleEntity moduleEntity) {
    this.moduleDAO.save(moduleEntity);
  }
}
