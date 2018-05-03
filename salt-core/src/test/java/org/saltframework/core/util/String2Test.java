package org.saltframework.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 3. 21.
 */
public class String2Test {

  @Test
  public void defaultString() {
    Assert.assertEquals(String2.defaultIfEmpty(""), null);
    Assert.assertEquals(String2.defaultIfEmpty("a"), "a");
    Assert.assertEquals(String2.defaultIfEmpty("a", "", null), "a");
    Assert.assertEquals(String2.defaultIfEmpty("a", "b", "c"), "c");
  }
}
