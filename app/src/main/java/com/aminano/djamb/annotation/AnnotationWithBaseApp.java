package com.aminano.djamb.annotation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.aminano.socketservicelibrary.BaseAppSocket;
import com.android.streye.constant_share.MethodValue;
import com.android.streye.stringshadow.StringChachi;

public class AnnotationWithBaseApp extends Activity {

  public static final String TAG = "AnnotationWithBaseApp";
  private BaseApp application;

  @MethodValue(value = "messages")
  public void myMethod7(Object[] object) {
    Log.i(TAG, "Recieve from server in event messages:    " + object[0]);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    application = (BaseApp) getApplicationContext();
    application.tryService(new BaseAppSocket.ServiceStatus() {
      @Override
      public void isRunning() {
        Log.i(TAG, "Send data to service?: " + application.sendData("message", "caca"));
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    StringChachi.bind2(this);
  }

}



