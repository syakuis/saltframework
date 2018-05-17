package org.saltframework.module;

import lombok.ToString;

/**
 * {@link Module}의 구현 클래스이며 가변으로 설정된다.
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 4.
 */
@ToString
public class MutableModule extends AbstractModule {
  public MutableModule(String moduleName, String moduleId) {
    super(moduleName, moduleId, true);
  }
}
