package com.aminano.socketservicelibrary;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import com.android.streye.stringshadow.StringChachi;

public abstract class BaseAppSocket extends Application implements SocketParameterInterface {

  private Intent intent;
  private boolean mServiceBound = false;
  private SocketService myService;
  private SocketParameterLibrary socketParameter;


  @Override
  public void onCreate() {
    super.onCreate();
    //StringChachi.bind2(this);


  }

  public SocketService getMyService() {
    return myService;
  }

  public ServiceConnection getMyConnection = new ServiceConnection() {
    public void onServiceConnected(ComponentName className, IBinder binder) {
      SocketService.MyBinder myBinder = (SocketService.MyBinder) binder;
      myService = myBinder.getService();
      mServiceBound = true;
      Log.e("aminano baseappsocket","onServiceConnected");
      setSocketParameter();
    }

    public void onServiceDisconnected(ComponentName className) {
      mServiceBound = false;
    }
  };

  public boolean getMServiceBound() {
    return mServiceBound;
  }

  public void runService() {
    if (!ManagerUtils.isMyServiceRunning(SocketService.class, this)) {
      Log.e("aminano", "Service is not running");
      intent = new Intent(this, SocketService.class);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(intent);
      } else {
        startService(intent);
      }
      bindService(intent, getMyConnection, Context.BIND_AUTO_CREATE);
    } else {
      Log.e("aminano", "Service is running");
    }
  }

  public boolean sendData(String emitEvent, Object... emitObject) {
    if (getMyService() == null) {
      return false;
    } else {
      return getMyService().emitObject(emitEvent, emitObject);
    }
  }

  public void connect(SocketParameterLibrary socketParameter) {
    myService.closeSocket();
    this.socketParameter = socketParameter;
    myService.connectSocket(this.socketParameter);
    StringChachi.bindApplication(getMyService());
  }

  public void disconnect() {
    myService.closeSocket();
  }

  public void setSocketParameter() {
    this.socketParameter = setSocketConfiguration();
    myService.connectSocket(socketParameter);
    StringChachi.bindApplication(getMyService());
  }

  public boolean isAutoRunServiceWhenSystemOn() {
    SharedPreferences prefs = getSharedPreferences("autoRunServiceWhenSystemOn", MODE_PRIVATE);
    return prefs.getBoolean("autoRunServiceWhenSystemOn", true);
  }

  public void setAutoRunServiceWhenSystemOn(boolean autoRunServiceWhenSystemOn) {
    SharedPreferences.Editor editor = getSharedPreferences("autoRunServiceWhenSystemOn", MODE_PRIVATE).edit();
    editor.putBoolean("autoRunServiceWhenSystemOn", autoRunServiceWhenSystemOn);
    editor.apply();
  }

  public abstract SocketParameterLibrary setSocketConfiguration();
}
