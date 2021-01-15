package com.droiders.stringshadow;

import com.android.streye.constant_share.InterfaceValue;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


class AnnotatedInterface extends AnnotatedShadowElement {

  private static final String QUALIFIER_STRING = "java.lang.String";

  private String value;

  AnnotatedInterface(Element element) {
    super(element);
    value = element.getAnnotation(InterfaceValue.class).value();

  }

  @Override
  public boolean isTypeValid(Elements elements, Types types) {
    TypeMirror elementType = element.asType();
    TypeMirror string = elements.getTypeElement(QUALIFIER_STRING).asType();
    return types.isSameType(elementType, string);
  }

  @Override
  public String getShadowValue() {
    try {
      return  value ;

    } catch (Exception e) {
      return "";
    }
  }

  @Override
  TypeMirror getClassTypeMirror() {
    return null;
  }
}
