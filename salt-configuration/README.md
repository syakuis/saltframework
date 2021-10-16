```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class, JdbcDataSource.class })
public class JdbcDataSourceTest {

  @Autowired
  private Config config;

  @Autowired
  private DataSource dataSource;

  @Test
  public void test() {
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("demo.table.h2.sql"));
    populator.execute(dataSource);

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    jdbcTemplate.queryForList("select * from demo").size();
  }
}

@Configuration
@PropertiesSource("classpath:/config.properties")
class TestConfiguration {
}
```

config.properties

```
dataSource.type=h2
dataSource.driverClassName=org.h2.Driver
dataSource.url=jdbc:h2:mem:salt-test;DB_CLOSE_DELAY=-1
dataSource.username=sa
dataSource.password=
```