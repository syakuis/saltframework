package org.saltframework.module;

import static java.util.stream.Collectors.toMap;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import lombok.*;

/**
 * ModuleManager Test Classes
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 27.
 * @see ModuleDetails
 * @see ModuleContextManager
 */
public class ModuleContextManagerTest {
  private ModuleContextManager manager;
  private String name = "good";
  private String id = "good";

  private String test = "test";
  private String test2 = "test2";

  private List<String> options = Arrays.asList("1", "2", "3");

  @Before
  public void setup() {
    manager = new ModuleContextManager();

    ModuleDetails module = new ModuleDetails(test, test);
    module.setDetails(options);
    manager.add(module);

    ModuleEntity moduleEntity = moduleEntityInit(test2);
    manager.add(moduleEntity);
  }

  private ModuleEntity moduleEntityInit(String name) {
    ModuleEntity entity = new ModuleEntity(name, name);
    entity.setModuleOptions(ModuleEntity.listModuleOptions(
        new ModuleOption("a", "1"),
        new ModuleOption("b", "2"),
        new ModuleOption("c", "3")
    ));

    return entity;
  }

  @Test
  public void moduleDetails() {
    Module moduleContext = manager.get(test);
    Assert.assertNotNull(moduleContext);
  }

  @Test
  public void getModuleTest() {
    Module moduleContext = manager.get(test2);
    Assert.assertNotNull(moduleContext);
    ModuleEntity entity = moduleContext.getObject(ModuleEntity.class);

    ModuleEntity newEntity = moduleEntityInit(test2);
    Assert.assertEquals(entity, newEntity);

    Assert.assertEquals(entity.getModuleOptions(), newEntity.getModuleOptions());
    Assert.assertTrue(entity.getModuleOptions().size() == newEntity.getModuleOptions().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void addModuleByNonModuleIdExceptionTest() {
    ModuleDetails module = new ModuleDetails(name, "");
    module.setDetails(Arrays.asList("a", "b"));
    manager.add(module);
  }

  @Test
  public void addModuleTest() {
    ModuleDetails moduleContext = new ModuleDetails(name, id);
    moduleContext.setDetails(Arrays.asList("a", "b"));
    manager.add(moduleContext);

    Module newModuleContext = manager.get(id);
    Assert.assertEquals(moduleContext, newModuleContext);
  }

  // addModuleTest 모듈을 하나 추가하여 총 3개가 됨.
  @Test(expected = UnsupportedOperationException.class)
  public void toListTest() {
    List<Module> moduleContexts = manager.toList();
    Module moduleContext = manager.get(test);
    Assert.assertEquals(moduleContext,
        moduleContexts.stream()
            .filter(module -> module.getModuleId().equals(test))
            .findFirst().get());

    // UnsupportedOperationException
    moduleContexts.add(new ModuleDetails("test2", "test2"));
  }


}

@ToString
class ModuleEntity extends AbstractModule {
  @Getter
  @Setter
  private Map<String, ModuleOption> moduleOptions;

  public ModuleEntity(String moduleName, String moduleId) {
    super(moduleName, moduleId, false);
  }

  public static Map<String, ModuleOption> listModuleOptions(ModuleOption... options) {
    return ModuleEntity.listModuleOptions(Arrays.asList(options));
  }

  public static Map<String, ModuleOption> listModuleOptions(List<ModuleOption> options) {
    return options.stream().collect(toMap(ModuleOption::getName, moduleOption -> moduleOption));
  }
}

@Data
@AllArgsConstructor
@ToString
class ModuleOption implements Serializable {
  private static final long serialVersionUID = 433404693389110573L;
  private String name;
  private String value;
}
