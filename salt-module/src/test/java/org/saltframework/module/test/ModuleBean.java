package org.saltframework.module.test;

import java.util.Arrays;
import java.util.List;

import org.saltframework.module.AbstractModule;
import org.saltframework.module.ModuleDefinition;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 30.
 */
@ModuleDefinition
@ToString
public class ModuleBean extends AbstractModule {
  @Getter
  private List<String> options;
  public ModuleBean() {
    super("test", "test");
    this.options = Arrays.asList("1", "2");
  }
}