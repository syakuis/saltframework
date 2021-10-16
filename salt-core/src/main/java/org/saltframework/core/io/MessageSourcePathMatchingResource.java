package org.saltframework.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 국제화 메세지에 사용될 하나 이상의 프로퍼티 파일을 찾아준다.
 * 일반적으로 메세지 프로퍼티는 Ant-style pattern 이나 wildcard 를 사용할 수 없어 동적으로 파일을 불러올 수 없다.
 * 이를 보완하기 위해 개발되었다. 메세지 프로퍼티를 찾을 대상 경로를 Ant-style pattern 으로 입력하여
 * {@link PathMatchingResourcePatternResolver} 를 이용하여 해당 파일이 실제로 존재하면
 * 파일의 절대경로를 모아서 반환한다.
 * 그외 자세한 설명은 {@link PathMatchingResourcePatternResolver} 참고한다.
 *
 * baseName 은 기본 메세지만 읽을 수 있게 설정하고 i18n 까지 읽어지지 않게 주의한다.
 * message_en_US.properties, message_ko_KR.properties 등 포함되면 안된다.
 *
 * <pre class="code">
 *   classpath:org/syaku/**&#47;message.properties   (yes)
 *   classpath:org/syaku/**&#47;*-message.properties (yes)
 *   classpath:org/syaku/**&#47;*.properties         (no)
 *   classpath:org/syaku/**&#47;message-*.properties (no)
 * </pre>
 *
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 16. 6. 7.
 *
 * @see PathMatchingResourcePatternResolver
 */
public class MessageSourcePathMatchingResource {
  private final Logger logger = LoggerFactory.getLogger(MessageSourcePathMatchingResource.class);

  private final PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

  /**
   * 하나의 메세지 프로퍼티 찾아 반환한다.
   *
   * @param baseName The String of baseName
   * @return The String of baseName (Absolute path)
   *
   * @throws IOException {@link PathMatchingResourcePatternResolver} Exception
   */
  public String getResource(String baseName) throws IOException {
    return getResourceBaseName(pathMatchingResourcePatternResolver.getResource(baseName));
  }

  /**
   * 하나이상의 메세지 프로퍼티를 찾아 반환한다.
   * @param baseNames The String array of baseName
   * @return The String array of baseName (Absolute path)
   * @throws IOException {@link PathMatchingResourcePatternResolver} Exception
   */
  public String[] getResources(String baseNames) throws IOException {
    return getResources(
        org.springframework.util.StringUtils.tokenizeToStringArray(baseNames, ","));
  }

  /**
   * 하나이상의 경로를 {@link PathMatchingResourcePatternResolver} 이용하여 파일을 찾아 반환한다.
   * @param baseNames The String array of baseName
   * @return The String array of baseName (Absolute path)
   * @throws IOException {@link PathMatchingResourcePatternResolver} Exception
   */
  public String[] getResources(String... baseNames) throws IOException {

    List<String> result = new ArrayList();

    for (String baseName : baseNames) {
      Resource[] resources = pathMatchingResourcePatternResolver.getResources(baseName);
      logger.debug("{} : Sets the baseName to load.", baseName);
      for (Resource resource : resources) {
        if (resource.exists()) {
          String resourceBaseName = getResourceBaseName(resource);
          if (resourceBaseName != null && result.indexOf(resourceBaseName) == -1) {
            result.add(resourceBaseName);
            logger.debug("{} : Added to MessageSource.", resourceBaseName);
          }
        }
      }
    }

    return result.toArray(new String[result.size()]);
  }

  /**
   * 메세지 프로퍼티에 맞는 경로로 수정하여 반환한다. 파일이 없으면 null 을 반환한다.
   * @param resource The {@link Resource} of resource
   * @return The String of baseName
   * @throws IOException {@link java.net.URI} Exception
   *
   * @see org.springframework.context.support.ReloadableResourceBundleMessageSource
   */
  private String getResourceBaseName(Resource resource) throws IOException {
    try {
      String uri = resource.getURI().toString();
      String extension = FilenameUtils.getExtension(uri);
      extension = extension != null ? "." + extension : extension;
      String basename = null;

      if (resource instanceof FileSystemResource) {
        basename = StringUtils.substringBefore(uri, extension);
      } else if (resource instanceof ClassPathResource) {
        basename = StringUtils.substringBefore(uri, extension);
      } else if (resource instanceof UrlResource) {
        String path = StringUtils.substringBetween(uri, ".jar!/", extension);
        if (path == null) {
          basename = StringUtils.substringBefore(uri, extension);
        } else {
          basename = "classpath:" + StringUtils.substringBetween(uri, ".jar!/", extension);
        }
      }

      if (basename != null) {
        return processBasename(basename);
      }

    } catch (FileNotFoundException e) {
      logger.error(e.getMessage());
    }

    return null;
  }

  private String processBasename(String basename) {
    String prefix = StringUtils.substringBeforeLast(basename, "/");
    String name = StringUtils.substringAfterLast(basename, "/");
    do {
      name = StringUtils.substringBeforeLast(name, "_");
    } while (name.contains("_"));
    return prefix + "/" + name;
  }
}
