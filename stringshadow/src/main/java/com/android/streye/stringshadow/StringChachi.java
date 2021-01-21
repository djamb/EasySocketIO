package com.android.streye.stringshadow;

import android.app.Service;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by djamb on 1/01/20.
 */

public class StringChachi {

  private static final String TAG = "StringChachi";
  public static Object prueba;
  public static Object socketOn;
  public static boolean firstTime = true;

  public static String getPackage(String qualifier) {
    return qualifier.substring(0, qualifier.lastIndexOf('.'));
  }

  public static void bind2(Object activity) {
    Class bindingClass = null;
    Method methods = null;
    try {
      bindingClass = Class.forName(getPackage(activity.getClass().getName()) + ".SocketOnSelector");
      Class object = getSuperClass(activity.getClass());
      if (firstTime) {
        Constructor constructor = bindingClass.getConstructor();
        prueba = constructor.newInstance();
        Class bindingClass2 =
            Class.forName(getPackage(activity.getClass().getName()) + ".SocketOn");
        Constructor constructor2 = bindingClass2.getConstructor(prueba.getClass());
        socketOn = constructor2.newInstance(prueba);
        firstTime = false;
      }
      methods = prueba.getClass().getMethod("bindViewer", object);
      methods.invoke(prueba, activity);
    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      Log.e(TAG, "Error", e);
    }
  }

  public static void unbind2(Object activity) {
    Class object = getSuperClass(activity.getClass());
    Method methods = null;
    try {
      methods = prueba.getClass().getMethod("unBind", object);
      methods.invoke(prueba, activity);
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public static void bindApplication(Service service) {
    Method methods = null;
    Class object = getSuperClass(service.getClass());
    try {
      methods = service.getClass().getMethod("setListener", object);
      methods.invoke(service, socketOn);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      Log.e(TAG, "Error", e);
    }
    //try {
    //bindingClass = Class.forName(getPackage(application.getClass().getName()) + ".SuperService");
    //Constructor constructor = bindingClass.getConstructor();
    //serviceGetter = constructor.newInstance();
    //} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
    //  Log.i(TAG, "Error", e);
    //}
  }

  public static Class getSuperClass(Class c) {
    Class object = null;
    while (c != null) {
      object = c;
      c = c.getSuperclass();
    }
    return object;
  }
}
