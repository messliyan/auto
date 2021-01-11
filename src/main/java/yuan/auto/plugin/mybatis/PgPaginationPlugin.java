package yuan.auto.plugin.mybatis;

import java.util.List;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class PgPaginationPlugin extends PluginAdapter {
  public PgPaginationPlugin() {
  }

  public boolean validate(List<String> warnings) {
    return true;
  }

  public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    PrimitiveTypeWrapper integerWrapper = FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper();
    Field limit = new Field("limit", integerWrapper);
    limit.setVisibility(JavaVisibility.PRIVATE);
    topLevelClass.addField(limit);
    Method setLimit = new Method("setLimit");
    setLimit.setVisibility(JavaVisibility.PUBLIC);
    setLimit.addParameter(new Parameter(integerWrapper, "limit"));
    setLimit.addBodyLine("this.limit = limit;");
    topLevelClass.addMethod(setLimit);
    Method getLimit = new Method("getLimit");
    getLimit.setVisibility(JavaVisibility.PUBLIC);
    getLimit.setReturnType(integerWrapper);
    getLimit.addBodyLine("return limit;");
    topLevelClass.addMethod(getLimit);
    Field offset = new Field("offset", integerWrapper);
    offset.setVisibility(JavaVisibility.PRIVATE);
    topLevelClass.addField(offset);
    Method setOffset = new Method("setOffset");
    setOffset.setVisibility(JavaVisibility.PUBLIC);
    setOffset.addParameter(new Parameter(integerWrapper, "offset"));
    setOffset.addBodyLine("this.offset = offset;");
    topLevelClass.addMethod(setOffset);
    Method getOffset = new Method("getOffset");
    getOffset.setVisibility(JavaVisibility.PUBLIC);
    getOffset.setReturnType(integerWrapper);
    getOffset.addBodyLine("return offset;");
    topLevelClass.addMethod(getOffset);
    return true;
  }

  public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
    XmlElement ifLimitNotNullElement = new XmlElement("if");
    ifLimitNotNullElement.addAttribute(new Attribute("test", "limit != null"));
    ifLimitNotNullElement.addElement(new TextElement("limit ${limit}"));
    element.addElement(ifLimitNotNullElement);
    XmlElement ifOffsetNotNullElement = new XmlElement("if");
    ifOffsetNotNullElement.addAttribute(new Attribute("test", "offset != null"));
    ifOffsetNotNullElement.addElement(new TextElement("offset ${offset}"));
    element.addElement(ifOffsetNotNullElement);
    return true;
  }
}