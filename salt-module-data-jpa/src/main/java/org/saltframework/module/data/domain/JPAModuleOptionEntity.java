package org.saltframework.module.data.domain;

import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 6.
 */
@Entity(name = "moduleOptionEntity")
@Table(name = "MODULE_OPTION")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class JPAModuleOptionEntity implements ModuleOptionEntity {
  @Id
  @Column(name = "MODULE_ID", unique = true, nullable = false)
  private String moduleId;

  @Id
  @Column(name = "OPTION_NAME", nullable = false)
  private String optionName;

  @Column(name = "OPTION_VALUE")
  private String optionValue;

  @Column(name = "OPTION_TITLE")
  private String optionTitle;

  @Column(name = "OPTION_ORDER")
  private int optionOrder;

  @ManyToOne(targetEntity = JPAModuleEntity.class)
  @JoinColumn(name = "MODULE_ID")
  private ModuleEntity moduleEntity;

  public JPAModuleOptionEntity() {
  }

  public JPAModuleOptionEntity(String moduleId, String optionName, String optionValue,
                               String optionTitle, int optionOrder) {
    this.moduleId = moduleId;
    this.optionName = optionName;
    this.optionValue = optionValue;
    this.optionTitle = optionTitle;
    this.optionOrder = optionOrder;
  }

  public static List<ModuleOptionEntity> createList(JPAModuleOptionEntity... moduleOptions) {
    return Arrays.asList(moduleOptions);
  }
}
