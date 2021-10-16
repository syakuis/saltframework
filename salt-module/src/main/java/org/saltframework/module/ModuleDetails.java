package org.saltframework.module;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * {@link Module}의 구현 클래스이며 가변으로 설정된다.
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2017. 11. 21.
 */
@Getter
@ToString
public class ModuleDetails extends AbstractModule {
  @Setter
  private Object details;

  public ModuleDetails(String moduleName, String moduleId) {
    super(moduleName, moduleId, false);
  }

  public <T> T getDetails(Class<T> type) {
    if (details == null) {
      return null;
    }

    if (!type.isInstance(details)) {
      throw new ClassCastException();
    }

    return type.cast(details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.getModuleName(), super.getModuleId(), super.isImmutable(), details);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof ModuleDetails)) {
      return false;
    }

    ModuleDetails newObject = (ModuleDetails) obj;

    if (Objects.equals(newObject.getModuleName(), super.getModuleName()) &&
        Objects.equals(newObject.getModuleId(), super.getModuleId()) &&
        Objects.equals(newObject.isImmutable(), super.isImmutable()) &&
        Objects.deepEquals(newObject.getDetails(), details)) {
      return true;
    }

    return false;
  }
}
