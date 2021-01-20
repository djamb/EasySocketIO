package com.aminano.djamb.annotation;

import com.aminano.socketservicelibrary.BaseAppSocket;
import com.aminano.socketservicelibrary.SocketParameterLibrary;

public class BaseApp2 extends BaseAppSocket {

  @Override
  public SocketParameterLibrary setSocketConfiguration() {
    new SocketGeneralEvents();
    return new SocketParameterLibrary("http://192.168.1.108:3000", "");
  }
}


