package com.android.streye.annotation;

/**
 * Created by djamb on 8/04/19.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.android.streye.constant_share.MethodValue;
import com.android.streye.stringshadow.StringChachi;

public class CustomAnnotation extends Activity {
  public BaseApp2 application;



  @MethodValue(value = "pong")
  public void myMethod1(Object[] object) {
    Log.e("CustomAnnotation pong : ", "pong: " + object[0] + "    ");
  }

  @MethodValue(value = "ping")
  public void myMethod2(Object[] object) {
    Log.e("CustomAnnotation ping: ", "ping: ");
  }


  @MethodValue(value = "messages")
  public void myMethod7(Object[] object) {
    Log.e("recibo del server: ", ""+object[0]);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    application = (BaseApp2) getApplicationContext();
    application.runService();

    Log.e("aminano1111111111111", "" + application.sendData("connection", "SocketGeneralEvents"));

    final Handler handler2 = new Handler(Looper.getMainLooper());
    handler2.postDelayed(new Runnable() {
      @Override
      public void run() {
        Log.e("activity", ""+application.sendData("message", "caca"));
        //application.getMyService().closeSocket();
        //application.connect(new SocketParameterLibrary("https://socket-enterprise-dev.streye.com:443","device=glass/enterprise&user=" + "?@caca.com" + "&access_token=" + "señorcaca"));

      }
    }, 5000);


  }

  @Override
  protected void onResume() {
    super.onResume();
    StringChachi.bind2(this);
    //Intent intent = new Intent(this, CustomAnnotation2.class);
    //startActivity(intent);
  }

  @Override
  protected void onPause() {
    super.onPause();
    //StringChachi.unbind2(this);
  }
}