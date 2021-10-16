package org.saltframework.module.data.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.saltframework.module.data.mybatis.dao.DemoDAO;
import org.saltframework.module.data.mybatis.domain.Demo;
import org.springframework.stereotype.Repository;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 10.
 */
@Repository(value = "demoDAO")
public interface DemoMapper extends DemoDAO {
  @Insert("INSERT INTO DEMO (NAME, VALUE) VALUES (#{name}, #{value})")
  void insert(Demo demo);

  @Select("SELECT * FROM DEMO")
  List<Demo> findAll();
}
