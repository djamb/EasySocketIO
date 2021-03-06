package com.droiders.stringshadow;

import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

abstract class AnnotatedShadowElement {

  Element element;
  private Name qualifiedClassName;
  private Name simpleClassName;
  private Name elementName;
  private TypeMirror elementType;

  AnnotatedShadowElement(Element element) {
    this.element = element;
    elementName = element.getSimpleName();
    simpleClassName = element.getEnclosingElement().getSimpleName();
    TypeElement enclosingElement = ((TypeElement) element.getEnclosingElement());
    qualifiedClassName = enclosingElement.getQualifiedName();
    elementType = element.asType();
  }

  Name getQualifiedClassName() {
    return qualifiedClassName;
  }

  Name getSimpleClassName() {
    return simpleClassName;
  }

  Name getElementName() {
    return elementName;
  }

  TypeMirror getElementType() {
    return elementType;
  }

  Element getElement() {
    return element;
  }

  @Override
  public String toString() {
    return "Qualified class name : "
        + qualifiedClassName.toString()
        + "\n"
        + "Simple class name : "
        + simpleClassName.toString()
        + "\n"
        + "Element name : "
        + elementName.toString()
        + "\n"
        + "Element type : "
        + elementType.toString()
        + "\n";
  }

  abstract boolean isTypeValid(Elements elements, Types types);

  abstract String getShadowValue();

  abstract TypeMirror getClassTypeMirror() ;

}
