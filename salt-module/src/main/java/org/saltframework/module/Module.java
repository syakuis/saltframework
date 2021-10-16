package org.saltframework.module;

import java.io.Serializable;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 28.
 */
public interface Module extends Serializable {
  String getModuleName();
  String getModuleId();
  boolean isImmutable();
  <T extends Module> T getObject(Class<T> type);
}
