package org.saltframework.core.environment;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.saltframework.core.util.String2;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 21.
 */
public class Env {
  public static final String FILE_ENCODING_NAMING = "file.encoding";
  public static final String FILE_ENCODING = StringUtils.defaultString(
      new String(Charset.defaultCharset().name()), "UTF-8");

  public static String defaultFileEncoding(String... encodings) {
    return StringUtils.defaultString(String2.defaultIfEmpty(encodings), FILE_ENCODING);
  }

  public static String[] getProfiles(Environment environment) {
    Assert.notNull(environment, "The environment argument must not be null.");

    String[] environmentProfiles = environment.getActiveProfiles();
    if (environmentProfiles != null && environmentProfiles.length == 0) {
      return environment.getDefaultProfiles();
    }

    return environmentProfiles;
  }
}
