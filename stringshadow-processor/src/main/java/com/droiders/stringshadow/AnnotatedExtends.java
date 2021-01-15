package com.droiders.stringshadow;

import com.android.streye.constant_share.ExtendsValue;

import javax.lang.model.element.Element;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class AnnotatedExtends extends AnnotatedShadowElement {

  private TypeMirror types;

  AnnotatedExtends(Element element) {
    super(element);
    ExtendsValue a = element.getAnnotation(ExtendsValue.class);
    try {
      a.value();
    } catch (MirroredTypesException ex) {
      types = ex.getTypeMirrors().get(0);
    }
  }

  @Override
  boolean isTypeValid(Elements elements, Types types) {
    return true;
  }

  @Override
  String getShadowValue() {
    return null;
  }

  public TypeMirror getClassTypeMirror() {
    try {
      return types;
    } catch (Exception e) {
      return null;
    }
  }
}
