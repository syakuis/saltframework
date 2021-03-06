package org.saltframework.resources.properties.bean.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.saltframework.core.environment.Env;
import org.saltframework.resources.properties.Config;
import org.saltframework.resources.properties.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Properties Spring Bean 을 생성하기 위한 추상 클래스이다.
 *
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2017. 8. 26.
 *
 * @see FactoryBean
 * @see Properties
 * @see Environment
 * @see InitializingBean
 * @see PropertiesFactoryBean
 */
public abstract class AbstractPropertiesFactoryBean<T> implements FactoryBean<T>,
    EnvironmentAware, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(AbstractPropertiesFactoryBean.class);

  private String propertySourceName;
  private boolean addToPropertySource;
  private Environment environment;
  private String fileEncoding;
  private String[] locations;
  private boolean singleton = true;
  private T singletonInstance;

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  /**
   * 프로퍼티를 {@link org.springframework.core.env.PropertySources} 에 등록하여 {@link Environment} 에서 반영한다.
   * @param addToPropertySource 등록여부 설정한다.
   */
  public void setAddToPropertySource(boolean addToPropertySource) {
    this.addToPropertySource = addToPropertySource;
  }

  /**
   * 프로퍼티를 {@link org.springframework.core.env.PropertySources} 에 등록할때 사용하는 이름.
   * {@link org.springframework.core.env.PropertySources#get(String)}
   * @param propertySourceName {@link PropertiesPropertySource#name}
   */
  public void setPropertySourceName(String propertySourceName) {
    this.propertySourceName = propertySourceName;
  }

  /**
   * Properties 에 사용될 encoding 을 설정한다.
   * @param fileEncoding The String of encoding
   */
  public void setFileEncoding(String fileEncoding) {
    this.fileEncoding = fileEncoding;
  }

  /**
   * 불러올 대상 경로를 설정한다.
   * @param location 문자열로 입력하며 여러개 입력하기 위해 , 구분할 수 있다.
   */
  public void setLocation(String location) {
    this.setLocations(StringUtils.tokenizeToStringArray(location, ","));
  }

  /**
   * 불러올 대상 경로를 설정한다.
   * @param locations 여러 개의 인수를 사용할 수 있다. Ant-Style Pattern 사용할 수 있다.
   */
  public void setLocations(String... locations) {
    this.locations = locations;
  }

  /**
   * Spring Bean 생성시 singleton 여부를 설정한다.
   * @param singleton The boolean of singleton
   */
  public void setSingleton(boolean singleton) {
    this.singleton = singleton;
  }

  /**
   * 설정된 encoding 속성을 얻는 다.
   * {@code
   * fileEncoding > environment.getProperty("file.encoding")
   * > Charset.defaultCharset().name() > UTF-8}
   *
   * @return The String of encoding
   */
  protected String getFileEncoding() {
    return this.fileEncoding;
  }

  /**
   * locations 경로를 얻는 다.
   *
   * @return The String Array of locations
   */
  protected String[] getLocations() {
    return this.locations;
  }

  /**
   * Spring profiles 에서 운영중인 서비스 환경 값을 얻는 다.
   * Spring profiles 에서 profiles 값을 비교해 일치하는 값을 찾고 찾은 즉시 반환한다.
   * 반환된 값은 현재 운영중인 서비스 환경 값을 사용된다.
   *
   * @return The String is one of the Spring Profiles
   *
   * @see Env
   * @see Environment
   */
  private static String getProfile(Environment environment) {
    String[] environmentProfiles = Env.getProfiles(environment);

    List<String> activeProfiles = Arrays.asList(environmentProfiles);
    // todo 외부에서 설정할 수 있도록 개선한다.
    String[] profiles = { "prod", "test", "dev", "default" };

    for (String profile : profiles) {
      if (activeProfiles.indexOf(profile) > -1) {
        return profile;
      }
    }

    return null;
  }

  /**
   * locations 경로에 "[profile]" 구문을 찾아 현재 운영중인 서비스 환경 값으로 치환한다.
   *
   * @return The String Array of locations
   */
  private static String[] getLocationProfileReplace(String[] locations, String profile) {
    List<String> result = new ArrayList<>();
    for (String location : locations) {
      result.add(location.replace("{profile}", profile));
    }

    return result.toArray(new String[result.size()]);
  }

    /**
   * Properties 생성한다.
   * @return Properties
   * @throws Exception PropertiesLoader
   * @see PropertiesLoader
   */
  protected abstract T createObject() throws Exception;

  @Override
  public T getObject() throws Exception {
    if (this.isSingleton()) {
      Assert.notNull(this.singletonInstance, "The instance has not been initialized. Run afterPropertiesSet().");
      return this.singletonInstance;
    } else {
     return createObject();
    }
  }

  @Override
  public abstract Class<?> getObjectType();

  @Override
  public boolean isSingleton() {
    return this.singleton;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.isSingleton()) {
      Assert.notNull(environment, "The environment must not be null.");

      this.locations = this.getLocationProfileReplace(locations, this.getProfile(environment));

      if (logger.isDebugEnabled()) {
        for (String location : this.locations) {
          logger.debug("locations : {} added", location);
        }
      }

      this.fileEncoding = Env.defaultFileEncoding(
          environment.getProperty(Env.FILE_ENCODING_NAMING), fileEncoding);

      this.singletonInstance = createObject();

      this.addPropertySources();
    }
  }

  protected Properties loader() throws IOException  {
    PropertiesLoader loader = new PropertiesLoader();
    loader.setLocations(this.getLocations());
    String fileEncoding = this.getFileEncoding();
    if (fileEncoding != null) {
      loader.setFileEncoding(fileEncoding);
    }

    return loader.getProperties();
  }

  private void addPropertySources() {
    Assert.notNull(environment, "The environment must not be null.");
    Assert.notNull(singletonInstance, "The singletonInstance must not be null.");

    if (this.addToPropertySource) {
      Properties properties;
      if (this.singletonInstance instanceof Properties) {
        properties = (Properties) this.singletonInstance;
      } else if (this.singletonInstance instanceof Config) {
        properties = ((Config) this.singletonInstance).getProperties();
      } else {
        throw new IllegalArgumentException(
          "Object of class [" +
            singletonInstance.getClass().getName() + "] must be an instance of " +
            this.singletonInstance.getClass());
      }

      PropertiesPropertySource source = new PropertiesPropertySource(this.propertySourceName, properties);
      MutablePropertySources sources = ((ConfigurableEnvironment) this.environment).getPropertySources();
      sources.addLast(source);
    }
  }
}
