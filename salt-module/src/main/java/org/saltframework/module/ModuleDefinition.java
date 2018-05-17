package org.saltframework.module;

import java.lang.annotation.*;

import org.saltframework.module.bean.factory.ModuleContextManagerFactoryBean;

/**
 * 모듈 정의 주석.
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 1. 14.
 * @see ModuleContextManagerFactoryBean
 * @see ModuleDefinitionScanner
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModuleDefinition {
}
