package com.android.streye.constant_share;

/**
 * Created by djamb on 9/04/19.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.ElementType.FIELD;

@Retention(RetentionPolicy.SOURCE)
@Target(value = FIELD)
public @interface StringValue {
  String value() default "";
}


