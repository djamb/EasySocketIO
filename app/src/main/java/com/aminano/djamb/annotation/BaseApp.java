package com.aminano.djamb.annotation;

import com.aminano.socketservicelibrary.BaseAppSocket;
import com.aminano.socketservicelibrary.SocketParameterLibrary;


public class BaseApp extends BaseAppSocket {
  @Override
  public void onCreate() {
    super.onCreate();
    runService();
  }


  @Override
  public SocketParameterLibrary setSocketConfiguration() {
    //You can set general event here
    //new SocketGeneralEvents();
    return new SocketParameterLibrary("http://192.168.1.108:3000", "");
  }

}


