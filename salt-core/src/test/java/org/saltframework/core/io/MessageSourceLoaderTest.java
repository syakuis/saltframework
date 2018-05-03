package org.saltframework.core.io;

import java.io.IOException;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 8. 31.
 */
public class MessageSourceLoaderTest {
  private final Logger logger = LoggerFactory.getLogger(MessageSourceLoaderTest.class);

  private MessageSourcePathMatchingResource messageSourceMatchingPattern = new MessageSourcePathMatchingResource();

  private String basenamesPattern =
      "classpath*:org/hibernate/validator/message.properties," +
      "classpath*:message.properties," +
      "classpath*:org/saltframework/core/**/message.properties";

  @Before
  public void setup() {
    Locale.setDefault(Locale.US);
  }

  @Test
  public void loader() throws IOException {
    String[] resources = messageSourceMatchingPattern.getResources(basenamesPattern);
    logger.debug("{}", resources.length);
//    Arrays.stream(resources).forEach(resource -> logger.debug(resource));

    for (String resource : resources) {
      logger.debug(resource);
    }
  }

  @Test
  public void reloadableResourceBundleMessageSource() throws IOException {
    String[] resources = messageSourceMatchingPattern.getResources(basenamesPattern);
    ReloadableResourceBundleMessageSource messageSourceLoader = new ReloadableResourceBundleMessageSource();
    // 먼저 로드된 프로퍼티가 나중에 로드된 프로퍼티와 동일한 키인 경우 나중에 로드된 프로퍼티는 무시된다.
    messageSourceLoader.setBasenames(resources);

    MessageSourceAccessor messageSource = new MessageSourceAccessor(messageSourceLoader);

    String baseNames = basenamesPattern.replace("\\.properties", "");

    String[] resources2 = messageSourceMatchingPattern.getResources(baseNames);
    ReloadableResourceBundleMessageSource messageSourceLoader2 = new ReloadableResourceBundleMessageSource();
    // 먼저 로드된 프로퍼티가 나중에 로드된 프로퍼티와 동일한 키인 경우 나중에 로드된 프로퍼티는 무시된다.
    messageSourceLoader.setBasenames(resources2);

    MessageSourceAccessor messageSource2 = new MessageSourceAccessor(messageSourceLoader);

    Assert.assertEquals(messageSource.getMessage("hello"), messageSource2.getMessage("hello"));
    Assert.assertEquals(messageSource.getMessage("name"), messageSource2.getMessage("name"));

  }
}
