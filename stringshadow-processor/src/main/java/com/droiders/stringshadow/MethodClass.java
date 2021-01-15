package com.droiders.stringshadow;

import java.lang.reflect.Method;

public  class MethodClass {
  private Object bindClass;
  private java.lang.reflect.Method method;
  public MethodClass(Object bindClass,java.lang.reflect.Method method){
    this.bindClass=bindClass;
    this.method=method;
  }

  public Object getBindClass() {
    return bindClass;
  }

  public void setBindClass(Object bindClass) {
    this.bindClass = bindClass;
  }

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }
}
