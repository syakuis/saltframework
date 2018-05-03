# Spring Properties and MessageSource Loader

스프링 프로퍼티와 국제화 메세지를 기존 방식에서 개선된 방식을 제공합니다.


```xml
<repository>
	<id>jCenter</id>
	<name>jCenter</name>
	<url>http://jcenter.bintray.com</url>
</repository>

...

<dependency>
	<groupId>org.fxb</groupId>
	<artifactId>fxb-resources</artifactId>
	<version>1.0.0.BUILD-SNAPSHOT</version>
</dependency>
```

## Properties Loader

- 프로퍼티 경로 속성에 `Ant-Style Pattern` 을 사용할 수 있습니다.
- 여러 프로퍼티 파일을 한번에 불러올 수 있습니다.
- 여러 프로퍼티에 같은 속성은 Override 할 수 있습니다.
- 스프링 서비스 환경에 맞는 프로퍼티 파일을 불러올 수 있습니다.

### Spring Configuration

```java
@Configuration
@ActiveProfiles("test")
public class PropertiesConfiguration {

  // Basic
  @Bean
  public PropertiesFactoryBean propertiesFactoryBean() {
    PropertiesFactoryBean bean = new PropertiesFactoryBean();
    bean.setLocations(
        "classpath:org/fxb/resources/**/first.properties",
        "classpath:org/fxb/resources/*/second.properties"
    );

    return bean;
  }

  @Bean
  public Properties config() throws IOException {
    return propertiesFactoryBean().getObject();
  }

  // Spring profiles
  @Bean
  public PropertiesFactoryBean profilePropertiesFactoryBean() {
    PropertiesFactoryBean bean = new PropertiesFactoryBean();
    bean.setLocations(
        "classpath:org/fxb/resources/**/first.properties",
        "classpath:org/fxb/resources/*/second.properties",
        "classpath:org/fxb/resources/properties/first-[profile].properties"
    );

    return bean;
  }

  @Bean
  public Properties profileConfig() throws IOException {
    return profilePropertiesFactoryBean().getObject();
  }
}
```

### Spring DI

```java
public class PropertiesFactoryBeanTest {
  @Autowired
  private Properties config;

  @Autowired
  @Qualifier("profileConfig")
  private Properties profileConfig;
}
```

## MessageSource Loader


### Configuration

```java
@Configuration
class MessageSourceConfiguration {

  @Bean
  public MessageSourceFactoryBean messageSourceFactoryBean() {
    MessageSourceFactoryBean bean = new MessageSourceFactoryBean();
    bean.setBaseNames(
        "classpath:org/fxb/resources/i18n/message.properties",
        "classpath:org/fxb/resources/i18n/*/message.properties",
        "classpath:org/fxb/resources/i18n/*/*.test.properties"
    );

    return bean;
  }

  @Bean
  public MessageSource messageSource() throws Exception {
    return messageSourceFactoryBean().getObject();
  }
}
```

### Test

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MessageSourceConfiguration.class)
@WebAppConfiguration
public class MessageSourceTest {
  @Autowired
  private MessageSource messageSource;

  @Before
  public void setup() {
    Locale.setDefault(Locale.US);
  }

  @Test
  public void test() {
    Assert.assertEquals(Locale.getDefault(), Locale.US);
    MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
    Assert.assertEquals(messageSourceAccessor.getMessage("name"), "Seokkyun Choi");
    Assert.assertEquals(messageSourceAccessor.getMessage("title"), "good");
    Assert.assertEquals(messageSourceAccessor.getMessage("locale"), "us");
    // 같은 속성이 2개이지만 먼저 설정된 것에 나중에 설정된 값이 덮어쓸 수 없다.
    // 먼저 속성을 지정하려면 setParentMessageSource 이용하거나 setBaseNames 에서 먼저 프러퍼티를 우선수위에 두어야 한다.
    Assert.assertEquals(messageSourceAccessor.getMessage("test"), "test");
    Assert.assertEquals(messageSourceAccessor.getMessage("nickname"), "syaku");
    Assert.assertEquals(messageSourceAccessor.getMessage("commons"), "공통");

    messageSourceAccessor = new MessageSourceAccessor(messageSource, Locale.KOREA);
    Assert.assertEquals(messageSourceAccessor.getMessage("name"), "최석균");
    Assert.assertEquals(messageSourceAccessor.getMessage("title"), "좋음");
    Assert.assertEquals(messageSourceAccessor.getMessage("locale"), "ko");
    Assert.assertEquals(messageSourceAccessor.getMessage("test"), "테스트");
  }
}
```