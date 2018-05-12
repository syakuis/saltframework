package org.saltframework.resources.properties;

import java.util.*;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Properties 값을 제공된 동작을 이용하여 원하는 형식과 원하는 결과로 얻을 수 있다.
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2017. 8. 26.
 * @see Properties
 */
public abstract class AbstractProperties {
  private final String delimiter = ",";

  private final Properties properties;

  public AbstractProperties(Properties properties) {
    Assert.notNull(properties, "The class must not be null");
    this.properties = properties;
  }

  public boolean isEmpty() {
    return properties.isEmpty();
  }

  public List<String> getKeys() {
    return getKeys(null);
  }

  public List<String> getKeys(String prefix) {
    Enumeration<Object> enumeration = this.properties.keys();
    List<String> result = new ArrayList<>();
    while(enumeration.hasMoreElements()) {
      String key = (String) enumeration.nextElement();
      if (prefix == null) {
        result.add(key);
      } else if (org.apache.commons.lang3.StringUtils.startsWith(key, prefix)) {
        result.add(key);
      }
    }
    return result;
  }

  public Properties getProperties() {
    return this.properties;
  }

  public Properties getProperties(String prefix) {
    return getProperties(prefix, false);
  }

  public Properties getProperties(String prefix, boolean prefixUse) {
    List<String> keys = getKeys(prefix);

    if (keys.size() == 0) {
      return null;
    }

    Properties properties = new Properties();

    for (String key : keys) {
      String name = prefixUse ? key : StringUtils.replace(key, prefix, "");
      properties.setProperty(
          name,
          this.properties.getProperty(key)
      );
    }

    return properties;
  }

  public String getString(String key) {
    return StringUtils.isEmpty(this.properties.getProperty(key)) ? null : this.properties.getProperty(key);
  }

  public String getString(String key, String defaultValue) {
    return getString(key) == null ? defaultValue : getString(key);
  }

  public Long getLonger(String key) {
    return getLonger(key, null);
  }

  public Long getLonger(String key, Long defaultValue) {
    Long longer = NumberUtils.createLong(this.properties.getProperty(key));

    if (longer == null) {
      return defaultValue;
    }

    return longer;
  }

  public Integer getInteger(String key) {
    return getInteger(key, null);
  }

  public Integer getInteger(String key, Integer defaultValue) {
    Integer integer = NumberUtils.createInteger(this.properties.getProperty(key));

    if (integer == null) {
      return defaultValue;
    }

    return integer;
  }

  public int getInt(String key) {
    return NumberUtils.toInt(this.properties.getProperty(key));
  }

  public int getInt(String key, int defaultValue) {
    return NumberUtils.toInt(this.properties.getProperty(key), defaultValue);
  }

  public long getLong(String key) {
    return NumberUtils.toLong(this.properties.getProperty(key));
  }

  public long getLong(String key, long defaultValue) {
    return NumberUtils.toLong(this.properties.getProperty(key), defaultValue);
  }

  public Boolean getBool(String key) {
    return getBool(key, null);
  }

  public Boolean getBool(String key, Boolean defaultValue) {
    String value = this.properties.getProperty(key);
    return value  == null && defaultValue != null ? defaultValue : Boolean.valueOf(value);
  }

  public boolean getBoolean(String key) {
    return BooleanUtils.toBoolean(this.properties.getProperty(key));
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    if (StringUtils.isEmpty(this.properties.getProperty(key))) {
      return defaultValue;
    }
    return BooleanUtils.toBoolean(this.properties.getProperty(key));
  }

  public <T> T get(String key) {
    return (T) this.properties.get(key);
  }

  public <T> T getArray(String key) {
    return this.getArray(key, delimiter, true);
  }

  public <T> T getArray(String key, String delimiter, boolean trim) {
    if (trim) {
      return (T) StringUtils.trimArrayElements(
          StringUtils.delimitedListToStringArray(this.getString(key), delimiter));
    }

    return (T) StringUtils.delimitedListToStringArray(this.getString(key), delimiter);
  }

  public String[] getStringArray(String key) {
    return this.getStringArray(key, delimiter, true);
  }

  public String[] getStringArray(String key, String delimiter, boolean trim) {
    return this.getArray(key, delimiter, trim);
  }

  public <T> List<T> getList(String key) {
    return this.getList(key, delimiter, true);
  }

  public <T> List<T> getList(String key, String delimiter, boolean trim) {
    T array[] = this.getArray(key, delimiter, trim);
    return Arrays.asList(array);
  }

  @Override
  public boolean equals(Object target) {
    if (this.properties == target) {
      return true;
    }

    if (!(target instanceof Properties)) {
      return false;
    }
    Properties object = (Properties) target;
    return Objects.equals(this.properties, object);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.properties);
  }

  @Override
  public String toString() {
    return this.properties.toString();
  }
}
