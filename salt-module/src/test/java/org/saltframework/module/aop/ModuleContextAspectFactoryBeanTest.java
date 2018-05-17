package org.saltframework.module.aop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jmock.lib.concurrent.Blitzer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.module.*;
import org.saltframework.module.ModuleRedefinition.Mode;
import org.saltframework.module.ModuleRedefinition.Scope;
import org.saltframework.module.bean.factory.ModuleContextManagerFactoryBean;
import org.saltframework.module.bean.factory.ModuleRedefinitionAspectFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AspectConfiguration.class})
public class ModuleContextAspectFactoryBeanTest {
  private final Logger logger = LoggerFactory.getLogger(ModuleContextAspectFactoryBeanTest.class);

  private Blitzer blitzer;

  @Autowired
  private BoardService boardService;

  @Autowired
  private ModuleContextManager moduleContextManager;

  @Before
  public void setup() {
    blitzer = new Blitzer(100, 100);
  }

  @After
  public void close() {
    blitzer.shutdown();
  }

  @Test
  public void test() {
    flow();
  }

  @Test
  public void sync() throws Exception {
    moduleContextManager.toList().stream().forEach(System.out::println);

    blitzer.blitz(() -> {
      try {
        Thread thread = Thread.currentThread();
        logger.debug("#{} - {}", thread.getName(), thread.getId());
        flow(thread);
        Thread.sleep(5000);
        flow2(thread);
      } catch (Exception e) {

      }
    });

    moduleContextManager.toList().stream()
        .forEach(module -> logger.debug("{}, {}", module.getModuleId(), module.getModuleName()));
  }

  private void flow() {
    Board board = new Board();

    boardService.insert(board);

//    Assert.assertEquals(moduleContextManager.get(board.getModuleId()).getModuleId(), board.getModuleId());

    boardService.update(board);

//    Assert.assertEquals(moduleContextManager.get(board.getModuleId()).getModuleId(), board.getModuleId());

    boardService.del(board.getModuleId());

//    Assert.assertEquals(moduleContextManager.get(board.getModuleId()), null);
  }

  private void flow(Thread thread) {
    Board board = new Board();
    board.setModuleName(thread.getName());
    board.setModuleId("module_" + thread.getId());

    boardService.insert(board);
    boardService.update(board);
    boardService.del(board.getModuleId());
  }

  private void flow2(Thread thread) {
    Board board = new Board();
    board.setModuleName(thread.getName());
    board.setModuleId("module_" + thread.getId());

    boardService.insert(board);
  }
}

class BoardService {

  private Database database;

  public BoardService(Database database) {
    this.database = database;
  }

  @ModuleRedefinition(expression = "args[0].moduleId", scope = Scope.THIS)
  public void insert(Board board) {

    if (board.getModuleId() == null) {
      String moduleId = "board_" + new Random().nextInt(100);
      board.setModuleId(moduleId);
      board.setModuleName(moduleId);
    }

    database.add(board);
  }

  @ModuleRedefinition(expression = "result.moduleId", scope = Scope.THIS)
  public Board update(Board board) {
    return board;
  }

  @ModuleRedefinition(expression = "args[0]", scope = Scope.ALL, mode = Mode.REMOVE)
  public void del(String moduleId) {
    // del code
  }
}

@Getter @Setter
class Board {
  private String moduleId;
  private String moduleName;

  public Board() {
  }

  public Board(String moduleId, String moduleName) {
    this.moduleId = moduleId;
    this.moduleName = moduleName;
  }
}

class BasicModuleContextService implements ModuleContextService {
  private Database database;

  public BasicModuleContextService(Database database) {
    this.database = database;
  }

  @Override
  public List<Module> getModules() {
    return null;
  }

  @Override
  public Module getModule(String moduleId) {
    Board board = database.getBoard(moduleId);

    return new ModuleDetails(board.getModuleName(), board.getModuleId());
  }
}

class Database {
  private final static Map<String, Board> store = new HashMap<>();

  public void add(Board board) {
    store.put(board.getModuleId(), board);
  }

  public Board getBoard(String moduleId) {
    return store.get(moduleId);
  }
}


@Configuration
@EnableAspectJAutoProxy
class AspectConfiguration {
  @Autowired
  private ModuleContextManager moduleContextManager;

  @Bean
  public ModuleContextManagerFactoryBean ModuleContextManagerFactoryBean() {
    ModuleContextManagerFactoryBean bean = new ModuleContextManagerFactoryBean();
    bean.setBasePackages("org.saltframework.module.test");
    return bean;
  }

  @Bean
  public Database database() {
    return new Database();
  }

  @Bean
  public BoardService boardService() {
    return new BoardService(database());
  }

  @Bean
  public ModuleContextService moduleContextService() {
    return new BasicModuleContextService(database());
  }

  @Bean
  public ModuleRedefinitionAspectFactoryBean moduleContextAspect() {
    ModuleRedefinitionAspectFactoryBean bean = new ModuleRedefinitionAspectFactoryBean(
        moduleContextManager, moduleContextService()
    );

    return bean;
  }
}