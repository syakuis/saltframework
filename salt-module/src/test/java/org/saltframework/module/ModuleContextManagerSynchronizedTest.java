package org.saltframework.module;

import java.util.Arrays;
import java.util.Random;

import org.jmock.lib.concurrent.Blitzer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 11. 30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class ModuleContextManagerSynchronizedTest {
  private static final Logger logger = LoggerFactory.getLogger(ModuleContextManagerSynchronizedTest.class);

  private Blitzer blitzer;

  @Autowired
  private ModuleContextManager moduleContextManager;

  @Before
  public void prepared() {
    blitzer = new Blitzer(1000, 10);
    for (int i = 0; i < 10000; i++) {
      ModuleDetails details = new ModuleDetails("module_" + i , "module_" + i);
      details.setDetails(Arrays.asList("가", "나", "다"));
      moduleContextManager.add(details);
    }
  }

  @After
  public void closed() {
    blitzer.shutdown();
  }

  @Test
  public void start() throws Exception {
    blitzer.blitz(() -> {
      test(Thread.currentThread());
    });
  }

  private void test(Thread thread) {
    int id = new Random().nextInt(10000 + 1);

    Module module = moduleContextManager.get("module_" + id);
    logger.debug("{} -> {} : {}", thread.getName(), thread.getId(), module);
  }
}