package org.saltframework.resources.i18n.bean.factory;

import java.io.IOException;
import java.util.Properties;

import org.saltframework.core.environment.Env;
import org.saltframework.core.io.MessageSourcePathMatchingResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;
import org.springframework.util.StringUtils;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2017. 9. 2.
 */
public class MessageSourceFactoryBean implements FactoryBean<MessageSource>,
    EnvironmentAware, InitializingBean {
  private final Logger logger = LoggerFactory.getLogger(MessageSourceFactoryBean.class);

  private final MessageSourcePathMatchingResource messageSourceMatchingPattern =
      new MessageSourcePathMatchingResource();
  private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

  private MessageSource instance;
  private Environment environment;
  private boolean singleton = true;

  private MessageSource parentMessageSource;
  private String[] baseNames;
  private int cacheSeconds = -1;
  private boolean concurrentRefresh = true;
  private boolean alwaysUseMessageFormat = false;
  private String defaultEncoding;
  private long cacheMillis = -1;
  private Properties fileEncodings;
  private Properties commonMessages;
  private boolean fallbackToSystemLocale = true;
  private boolean useCodeAsDefaultMessage = false;

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  public void setSingleton(boolean singleton) {
    this.singleton = singleton;
  }

  public void setParentMessageSource(MessageSource parentMessageSource) {
    this.parentMessageSource = parentMessageSource;
  }

  /**
   * 사용할 메세지 프로퍼티 경로를 입력한다. , 를 사용하여 여러개 입력할 수 있다.
   * @param baseName 문자열 경로
   */
  public void setBaseName(String baseName) {
    this.setBaseNames(StringUtils.tokenizeToStringArray(baseName, ","));
  }

  public void setBaseNames(String... baseNames) {
    this.baseNames = baseNames;
  }

  public void setAlwaysUseMessageFormat(boolean alwaysUseMessageFormat) {
    this.alwaysUseMessageFormat = alwaysUseMessageFormat;
  }

  /**
   * The setCacheSeconds method of {@link ReloadableResourceBundleMessageSource}
   * @param cacheSeconds int
   * @see ReloadableResourceBundleMessageSource
   */
  public void setCacheSeconds(int cacheSeconds) {
    this.cacheSeconds = cacheSeconds;
  }

  public void setConcurrentRefresh(boolean concurrentRefresh) {
    this.concurrentRefresh = concurrentRefresh;
  }

  public void setDefaultEncoding(String defaultEncoding) {
    this.defaultEncoding = defaultEncoding;
  }

  public void setCacheMillis(long cacheMillis) {
    this.cacheMillis = cacheMillis;
  }

  public void setFileEncodings(Properties fileEncodings) {
    this.fileEncodings = fileEncodings;
  }

  public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
    this.propertiesPersister = propertiesPersister;
  }

  public void setCommonMessages(Properties commonMessages) {
    this.commonMessages = commonMessages;
  }

  public void setFallbackToSystemLocale(boolean fallbackToSystemLocale) {
    this.fallbackToSystemLocale = fallbackToSystemLocale;
  }

  public void setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage) {
    this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
  }

  private MessageSource createObject() throws IOException {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setCacheSeconds(this.cacheSeconds);
    messageSource.setConcurrentRefresh(this.concurrentRefresh);
    messageSource.setAlwaysUseMessageFormat(this.alwaysUseMessageFormat);

    messageSource.setDefaultEncoding(Env.defaultFileEncoding(
        environment.getProperty(Env.FILE_ENCODING_NAMING), defaultEncoding));
    messageSource.setCacheMillis(this.cacheMillis);
    messageSource.setFileEncodings(this.fileEncodings);
    messageSource.setPropertiesPersister(this.propertiesPersister);
    messageSource.setCommonMessages(this.commonMessages);
    messageSource.setFallbackToSystemLocale(this.fallbackToSystemLocale);
    messageSource.setUseCodeAsDefaultMessage(this.useCodeAsDefaultMessage);

    if (parentMessageSource != null) {
      messageSource.setParentMessageSource(parentMessageSource);
    }

    String[] baseNames = messageSourceMatchingPattern.getResources(this.baseNames);

    if (logger.isDebugEnabled()) {
      for (String baseName : baseNames) {
        logger.debug("MessageSource : {}", baseName);
      }
    }

    if (baseNames == null) {
      return messageSource;
    }

    messageSource.setBasenames(baseNames);
    return messageSource;
  }


  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(environment, "The environment must not be null.");
    Assert.noNullElements(baseNames, "The baseNames array must contain non-null elements.");

    if (this.isSingleton()) {
      this.instance = this.createObject();
    }
  }

  @Override
  public MessageSource getObject() throws Exception {
    if (this.isSingleton()) {
      Assert.notNull(this.instance, "The environment must not be null. afterPropertiesSet()");
      return this.instance;
    }

    return this.createObject();
  }

  @Override
  public Class<MessageSource> getObjectType() {
    return MessageSource.class;
  }

  @Override
  public boolean isSingleton() {
    return this.singleton;
  }
}
