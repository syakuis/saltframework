package org.saltframework.module.data.domain;

import java.io.Serializable;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 6.
 */
public interface ModuleOptionEntity extends Serializable {
  String getModuleId();
  String getOptionName();
  String getOptionValue();
  String getOptionTitle();
  int getOptionOrder();
}
