package yuan.auto.plugin.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.VisitableElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class PgReturningIdInsertPlugin extends PluginAdapter {
  private Map<FullyQualifiedTable, List<XmlElement>> elementsToAdd = new HashMap();

  public PgReturningIdInsertPlugin() {
  }

  public boolean validate(List<String> warnings) {
    return true;
  }

  public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
    this.copyAndAddMethod(method, interfaze);
    return true;
  }

  public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
    this.copyAndAddMethod(method, interfaze);
    return true;
  }

  public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
    this.copyAndSaveElement(element, introspectedTable.getFullyQualifiedTable());
    return true;
  }

  public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
    this.copyAndSaveElement(element, introspectedTable.getFullyQualifiedTable());
    return true;
  }

  public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
    List<XmlElement> elements = (List)this.elementsToAdd.get(introspectedTable.getFullyQualifiedTable());
    if (elements != null) {
      Iterator var4 = elements.iterator();

      while(var4.hasNext()) {
        XmlElement element = (XmlElement)var4.next();
        document.getRootElement().addElement(element);
      }
    }

    return true;
  }

  private void copyAndAddMethod(Method method, Interface interfaze) {
    List<String> methodAnnotations = method.getAnnotations();

    for(int i = 0; i < methodAnnotations.size(); ++i) {
      String annotation = (String)methodAnnotations.get(i);
      if (annotation.contains("#{createdAt,jdbcType=TIMESTAMP}")) {
        annotation = annotation.replace("#{createdAt,jdbcType=TIMESTAMP}", "now()");
      }

      if (annotation.contains("#{updatedAt,jdbcType=TIMESTAMP}")) {
        annotation = annotation.replace("#{updatedAt,jdbcType=TIMESTAMP}", "now()");
      }

      methodAnnotations.set(i, annotation);
    }

    Method newMethod = new Method(method);
    newMethod.setName(method.getName() + "ReturningId");
    newMethod.setReturnType(new FullyQualifiedJavaType("long"));
    interfaze.addMethod(newMethod);
    List<String> annotations = newMethod.getAnnotations();
    if (annotations.size() > 3) {
      annotations.set(0, "@Select({");
      String third2Last = (String)annotations.get(annotations.size() - 3);
      third2Last = third2Last + ", ";
      annotations.set(annotations.size() - 3, third2Last);
      annotations.add(annotations.size() - 2, "    \" returning id\"");
    }

  }

  private XmlElement getTrimXmlElement(XmlElement element, String name, String attribute, String attributeValue) {
    List<VisitableElement> elements = element.getElements();
    Iterator var6 = elements.iterator();

    while(var6.hasNext()) {
      VisitableElement ele = (VisitableElement)var6.next();
      if (ele instanceof XmlElement) {
        XmlElement xml = (XmlElement)ele;
        if (Objects.equals(xml.getName(), name) && xml.getAttributes().stream().anyMatch((t) -> {
          return t.getName().equals(attribute) && t.getValue().equals(attributeValue);
        })) {
          return xml;
        }
      }
    }

    return null;
  }

  private void defaultUpdateAtAndCreateAt(XmlElement element) {
    XmlElement column = this.getTrimXmlElement(element, "trim", "prefix", "(");
    XmlElement created = this.getTrimXmlElement(column, "if", "test", "createdAt != null");
    XmlElement updated = this.getTrimXmlElement(column, "if", "test", "updatedAt != null");
    column.getElements().remove(created);
    column.getElements().remove(updated);
    TextElement createdAndUpdatedText = new TextElement("created_at,updated_at ");
    column.getElements().add(createdAndUpdatedText);
    XmlElement value = this.getTrimXmlElement(element, "trim", "prefix", "values (");
    XmlElement createdValue = this.getTrimXmlElement(value, "if", "test", "createdAt != null");
    XmlElement updatedValue = this.getTrimXmlElement(value, "if", "test", "updatedAt != null");
    value.getElements().remove(createdValue);
    value.getElements().remove(updatedValue);
    TextElement createdAndUpdatedTextValue = new TextElement("now(),now() ");
    value.getElements().add(createdAndUpdatedTextValue);
  }

  private void copyAndSaveElement(XmlElement element, FullyQualifiedTable fqt) {
    this.defaultUpdateAtAndCreateAt(element);
    XmlElement newElement = new XmlElement(element);
    String newId = null;
    Iterator iterator = newElement.getAttributes().iterator();

    while(true) {
      while(iterator.hasNext()) {
        Attribute attribute = (Attribute)iterator.next();
        if ("id".equals(attribute.getName())) {
          iterator.remove();
          newId = attribute.getValue() + "ReturningId";
        } else if ("keyColumn".equals(attribute.getName()) || "keyProperty".equals(attribute.getName()) || "useGeneratedKeys".equals(attribute.getName())) {
          iterator.remove();
        }
      }

      if (newId != null) {
        newElement.addAttribute(new Attribute("id", newId));
      }

      Attribute attribute = new Attribute("resultType", "long");
      newElement.addAttribute(attribute);
      newElement.setName("select");
      TextElement textElement = new TextElement("returning id");
      newElement.getElements().add(textElement);
      List<XmlElement> elements = (List)this.elementsToAdd.get(fqt);
      if (elements == null) {
        elements = new ArrayList();
        this.elementsToAdd.put(fqt, elements);
      }

      ((List)elements).add(newElement);
      return;
    }
  }
}
