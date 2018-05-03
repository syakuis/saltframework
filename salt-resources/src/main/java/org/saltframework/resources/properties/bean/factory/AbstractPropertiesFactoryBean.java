package org.saltframework.resources.properties.bean.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.saltframework.core.environment.Env;
import org.saltframework.resources.properties.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

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
public abstract class AbstractPropertiesFactoryBean implements FactoryBean<Properties>,
    EnvironmentAware, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(AbstractPropertiesFactoryBean.class);

  private Environment environment;
  private String fileEncoding;
  private String[] locations;
  private boolean singleton = true;
  private Properties singletonInstance;

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
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
   * fileEncoding > {@code environment.getProperty("file.encoding")}
   * > {@code Charset.defaultCharset().name()} > UTF-8
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
      result.add(location.replace("[profile]", profile));
    }

    return result.toArray(new String[result.size()]);
  }

    /**
   * Properties 생성한다.
   * @return Properties
   * @throws Exception PropertiesLoader
   * @see PropertiesLoader
   */
  protected abstract Properties createObject() throws Exception;

  @Override
  public Properties getObject() throws Exception {
    if (this.isSingleton()) {
      Assert.notNull(this.singletonInstance, "The instance has not been initialized. Run afterPropertiesSet().");
      return this.singletonInstance;
    } else {
     return createObject();
    }
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
    }
  }

  @Override
  public Class<Properties> getObjectType() {
    return Properties.class;
  }

  @Override
  public boolean isSingleton() {
    return this.singleton;
  }
}
