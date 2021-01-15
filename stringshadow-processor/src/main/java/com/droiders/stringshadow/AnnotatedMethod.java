package com.droiders.stringshadow;


import com.android.streye.constant_share.MethodValue;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


class AnnotatedMethod extends AnnotatedShadowElement {

  private static final String QUALIFIER_STRING = "java.lang.String";

  private String value;

  AnnotatedMethod(Element element) {
    super(element);
    value = element.getAnnotation(MethodValue.class).value();

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
      return "\"" + value + "\"";

    } catch (Exception e) {
      return "\"" + "\"";
    }
  }

  @Override
  TypeMirror getClassTypeMirror() {
    return null;
  }
}
