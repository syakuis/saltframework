package org.saltframework.module.data.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 6.
 */
public interface ModuleEntity extends Serializable {
  String getModuleId();
  String getModuleName();
  String getBrowserTitle();
  Date getRegistryDate();
  List<ModuleOptionEntity> getModuleOptionEntities();
}
