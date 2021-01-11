package yuan.auto.plugin.mybatis;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl.JdbcTypeInformation;

public class DateTypeResolver extends JavaTypeResolverDefaultImpl {
  private static final String PROP_USE_JSR_310 = "useJSR310";
  private static final String PROP_JAVA_TYPE_BY_COLUMN_NAME_PREFIX = "javaTypeByColumnName.";
  private Map<String, FullyQualifiedJavaType> javaTypeByColumnName = new HashMap();

  public DateTypeResolver() {
  }

  public void addConfigurationProperties(Properties properties) {
    super.addConfigurationProperties(properties);
    if ("true".equalsIgnoreCase(properties.getProperty("useJSR310"))) {
      this.typeMap.put(91, new JdbcTypeInformation("DATE", new FullyQualifiedJavaType(LocalDate.class.getName())));
      this.typeMap.put(92, new JdbcTypeInformation("TIME", new FullyQualifiedJavaType(LocalTime.class.getName())));
      this.typeMap.put(2013, new JdbcTypeInformation("TIME_WITH_TIMEZONE", new FullyQualifiedJavaType(LocalTime.class.getName())));
      this.typeMap.put(93, new JdbcTypeInformation("TIMESTAMP", new FullyQualifiedJavaType(LocalDateTime.class.getName())));
      this.typeMap.put(2014, new JdbcTypeInformation("TIMESTAMPTZ", new FullyQualifiedJavaType(LocalDateTime.class.getName())));
    }

    properties.stringPropertyNames().forEach((propName) -> {
      if (propName.startsWith("javaTypeByColumnName.")) {
        String columnName = propName.substring("javaTypeByColumnName.".length());
        String propValue = properties.getProperty(propName);
        this.javaTypeByColumnName.put(columnName, new FullyQualifiedJavaType(propValue));
      }

    });
  }
}
