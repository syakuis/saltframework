package org.saltframework.resources.i18n;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saltframework.resources.i18n.bean.factory.MessageSourceFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 9. 3.
 */
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

    messageSourceAccessor = new MessageSourceAccessor(messageSource, Locale.KOREA);
    Assert.assertEquals(messageSourceAccessor.getMessage("name"), "최석균");
    Assert.assertEquals(messageSourceAccessor.getMessage("title"), "좋음");
    Assert.assertEquals(messageSourceAccessor.getMessage("locale"), "ko");
    Assert.assertEquals(messageSourceAccessor.getMessage("test"), "테스트");
  }

}

@Configuration
class MessageSourceConfiguration {

  @Bean
  public MessageSourceFactoryBean messageSource() {
    MessageSourceFactoryBean bean = new MessageSourceFactoryBean();
    bean.setBaseNames(
        "classpath:org/saltframework/resources/i18n/message.properties",
        "classpath:org/saltframework/resources/i18n/*/message.properties",
        "classpath:org/saltframework/resources/i18n/*/*.test.properties"
    );

    return bean;
  }
}
