package com.android.streye.constant_share;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Retention(RetentionPolicy.SOURCE)
@Target(value = FIELD)
public @interface ExtendsValue {
  Class<?> value();
}