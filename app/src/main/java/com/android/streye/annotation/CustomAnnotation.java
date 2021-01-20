package com.android.streye.annotation;

/**
 * Created by djamb on 8/04/19.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.aminano.socketservicelibrary.BaseAppSocket;
import com.aminano.socketservicelibrary.SocketParameterLibrary;
import com.android.streye.constant_share.MethodValue;
import com.android.streye.stringshadow.StringChachi;

public class CustomAnnotation extends Activity {
  public BaseApp2 application;

  @MethodValue(value = "messages")
  public void myMethod7(Object[] object) {
    Log.e("recive from server in event messages: ", "" + object[0]);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    application = (BaseApp2) getApplicationContext();
    //You can autorun service and run socket when android is rebooted
    //application.setAutoRunServiceWhenSystemOn(true);
    //only need to start in 1 activity or applicaction
    application.runService();


    //From test server, send data in message channel at 5 sec
    final Handler handler2 = new Handler(Looper.getMainLooper());
    handler2.postDelayed(new Runnable() {
      @Override
      public void run() {
        Log.e("Send data to socket", "" + application.sendData("message", "caca"));

        //You can discconect and reconnect
        //application.getMyService().closeSocket();
        //application.connect(new SocketParameterLibrary("http://192.168.1.108:3000",""));

      }
    }, 5000);
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
    //StringChachi.unbind2(this);
  }
}