package com.aminano.socketservicelibrary;

import android.app.ActivityManager;
import android.content.Context;

public class ManagerUtils {

  public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
    ActivityManager manager = (ActivityManager) context.getApplicationContext()
        .getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
        Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }
}
