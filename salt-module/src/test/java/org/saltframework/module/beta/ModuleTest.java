package org.saltframework.module.beta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 모듈 정보를 관리하는 로직을 테스트한다.
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 6. 28.
 */
public class ModuleTest {
  @Test
  public void 일반_모듈() {

    Module module = WebModule.newInstance("test", "test", 0, "title");
    List<Module> modules = new ArrayList<>();
    modules.add(module);

    assertEquals(modules.get(0), WebModule.newInstance("test", "test", 0, "title"));
    assertTrue(modules.get(0) == module);
  }
}

@AllArgsConstructor
abstract class Module implements Comparable<Module> {
  private final String module;
  private final String moduleId;
  private final int order;

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int compareTo(Module o) {
    return this.order - o.order;
  }
}

final class WebModule extends Module {
  @Getter @Setter(AccessLevel.PROTECTED)
  private String browserTitle;

  private WebModule(String module, String moduleId, int order) {
    super(module, moduleId, order);
  }

  public static WebModule newInstance(String module, String moduleId, int order) {
    return new WebModule(module, moduleId, order);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  public static WebModule newInstance(String module, String moduleId, int order, String browserTitle) {
    WebModule webModule =  new WebModule(module, moduleId, order);
    webModule.browserTitle = browserTitle;
    return webModule;
  }
}
