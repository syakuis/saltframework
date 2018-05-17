package org.saltframework.module;

import java.util.List;

/**
 * 데이터베이스에 저장된 모듈정보를 얻기 위한 메서드를 정의함. 얻은 모듈 정보는 최종적으로 Modue
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 2.
 */
public interface ModuleContextService {
  /**
   * 서비스를 통해 전체 모듈을 읽어오는 메서드
   * @return List {@link Module}
   */
  List<Module> getModules();

  /**
   * 서비스를 통해 특정 모듈을 읽어오는 메서드
   * @param moduleId 모듈 ID
   * @return Module
   */
  Module getModule(String moduleId);
}
