package org.saltframework.module.data.mybatis.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 10.
 */
@Getter @Setter @EqualsAndHashCode @ToString
public class Demo {
  private String name;
  private String value;

  public Demo() {
  }

  public Demo(String name, String value) {
    this.name = name;
    this.value = value;
  }
}
