package org.saltframework.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 21.
 */
public class String2 {
  public static String defaultIfEmpty(String... strings) {
    String result = null;

    for (String string : strings) {
      result =  StringUtils.defaultIfEmpty(string, result);
    }

    return result;
  }
}
