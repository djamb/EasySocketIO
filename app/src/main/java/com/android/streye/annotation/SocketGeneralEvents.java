package com.android.streye.annotation;

import android.util.Log;
import com.android.streye.constant_share.MethodValue;
import com.android.streye.stringshadow.StringChachi;
import io.socket.client.Socket;

public class SocketGeneralEvents {

  public static String name = "SocketGeneralEvents";

  @MethodValue(value = Socket.EVENT_DISCONNECT)
  public void myMethod1(Object[] object) {
    Log.i(name + "  " + Socket.EVENT_DISCONNECT, "" + object[0]);
  }

  @MethodValue(value = Socket.EVENT_CONNECT_ERROR)
  public void myMethod2(Object[] object) {
    Log.i(name + "  " + Socket.EVENT_CONNECT_ERROR, "" + object[0]);
  }

  @MethodValue(value = Socket.EVENT_CONNECT)
  public void myMethod3(Object[] object) {
    Log.i(name + "  " + Socket.EVENT_CONNECT, "");
  }

  public SocketGeneralEvents() {
    StringChachi.bind2(this);
  }
}
