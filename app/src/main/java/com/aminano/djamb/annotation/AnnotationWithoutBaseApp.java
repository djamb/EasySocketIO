package com.aminano.djamb.annotation;

/**
 * Created by djamb on 8/04/19.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.aminano.socketservicelibrary.BaseAppSocket;
import com.aminano.socketservicelibrary.ManagerUtils;
import com.aminano.socketservicelibrary.SocketParameterLibrary;
import com.aminano.socketservicelibrary.SocketService;
import com.android.streye.constant_share.MethodValue;
import com.android.streye.stringshadow.StringChachi;

public class AnnotationWithoutBaseApp extends Activity {
  private BaseAppSocket application;
  Intent intent;
  public static final String TAG = "AnnotationWithoutBaseApp";

  @MethodValue(value = "messages")
  public void myMethod7(Object[] object) {
    Log.e(TAG, "Recieve from server in event messages:    " + object[0]);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    application = (BaseAppSocket) getApplicationContext();
    //You can autorun service and run socket when android is rebooted
    //application.setAutoRunServiceWhenSystemOn(true);
    //only need to start in 1 activity or applicaction that extends BaseAppSocket
    application.runService();

    application.tryService(new BaseAppSocket.ServiceStatus() {
      @Override
      public void isRunning() {
        //application.connect(new SocketParameterLibrary("http://192.168.1.108:3000",""));
        application.connect(new SocketParameterLibrary("http://192.168.43.134:3000", ""));
        Log.i(TAG, "Send data to service?: " + application.sendData("message", "hola clase"));
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    // Suscribe event from view
    StringChachi.bind2(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    // Unsuscribe event from view
    StringChachi.unbind2(this);
  }
}