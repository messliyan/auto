package yuan.auto.plugin.mybatis;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

public class RenameJavaFilePlugin extends PluginAdapter {
  private String replaceString = "";
  private Pattern pattern = Pattern.compile("T");

  public RenameJavaFilePlugin() {
  }

  public boolean validate(List<String> warnings) {
    return true;
  }

  public void initialized(IntrospectedTable introspectedTable) {
    String oldType = introspectedTable.getBaseRecordType();
    Matcher matcher = this.pattern.matcher(oldType);
    oldType = matcher.replaceFirst(this.replaceString);
    introspectedTable.setBaseRecordType(oldType);
  }
}
