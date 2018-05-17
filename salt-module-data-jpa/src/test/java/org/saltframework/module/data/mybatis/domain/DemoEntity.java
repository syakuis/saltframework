package org.saltframework.module.data.mybatis.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 12.
 */
@Entity(name = "demoEntity")
@Table(name = "DEMO")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DemoEntity implements Serializable {
  private static final long serialVersionUID = 4937218154860090222L;

  @Id
  @Column
  private String name;
  @Column
  private String value;

  public DemoEntity() {
  }

  public DemoEntity(String name, String value) {
    this.name = name;
    this.value = value;
  }
}
