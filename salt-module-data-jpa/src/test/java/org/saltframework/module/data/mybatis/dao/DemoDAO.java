package org.saltframework.module.data.mybatis.dao;

import java.util.List;

import org.saltframework.module.data.mybatis.domain.Demo;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 10.
 */
public interface DemoDAO {
  void insert(Demo demo);
  List<Demo> findAll();
}
