package org.saltframework.module;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.ToString;

/**
 * {@link Module}을 불변 추상 클래스이다. 가변 구현 클래스는 {@link MutableModule} 혹은
 * {@link ModuleDetails} 를 사용할 수 있다.
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 30.
 */
@Getter
@ToString
public abstract class AbstractModule implements Module, Serializable {
  private static final long serialVersionUID = 4963113037670019088L;
  private final String moduleName;
  private final String moduleId;
  private boolean immutable;

  /**
   * ModuleContext 이름을 설정한다.
   * @param moduleName 중복가능 한 모듈명을 가진다. 그룹과 같은 역활을 한다.
   * @param moduleId 유일한 모듈명을 가진다.
   */
  public AbstractModule(String moduleName, String moduleId) {
    this(moduleName, moduleId, true);
  }

  /**
   * ModuleContext 이름을 설정한다.
   * @param moduleName 중복가능 한 모듈명을 가진다. 그룹과 같은 역활을 한다.
   * @param moduleId 유일한 모듈명을 가진다.
   * @param immutable 불변 여부를 설정하며 불변이 아닌경우 {@link ModuleRedefinition} 으로 재정의될 수 있다.
   */
  public AbstractModule(String moduleName, String moduleId, boolean immutable) {
    this.moduleName = moduleName;
    this.moduleId = moduleId;
    this.immutable = immutable;
  }

  protected void setImmutable(boolean immutable) {
    this.immutable = immutable;
  }

  public <T extends Module> T getObject(Class<T> type) {
    if (type == null || !type.isInstance(this)) {
      throw new ClassCastException();
    }

    return type.cast(this);
  }

  @Override
  public int hashCode() {
    return Objects.hash(moduleName, moduleId, immutable);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Module)) {
      return false;
    }

    Module newObject = (Module) obj;

    if (Objects.equals(newObject.getModuleName(), moduleName) &&
        Objects.equals(newObject.getModuleId(), moduleId) &&
        Objects.equals(newObject.isImmutable(), immutable)) {
      return true;
    }

    return false;
  }
}
