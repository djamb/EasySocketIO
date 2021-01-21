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
import com.android.streye.constant_share.MethodValue;
import com.android.streye.stringshadow.StringChachi;
import io.socket.client.Socket;

public class BaseAppSocket extends Application implements SocketParameterInterface {
  //public abstract class BaseAppSocket extends Application implements SocketParameterInterface {

  private Intent intent;
  private boolean mServiceBound = false;
  private SocketService myService;
  private SocketParameterLibrary socketParameter;
  public static String TAG = "BaseAppSocket";

  public SocketService getMyService() {
    return myService;
  }





  public ServiceConnection getMyConnection = new ServiceConnection() {
    public void onServiceConnected(ComponentName className, IBinder binder) {
      SocketService.MyBinder myBinder = (SocketService.MyBinder) binder;
      myService = myBinder.getService();
      mServiceBound = true;
      Log.i(TAG, "onServiceConnected");
      setSocketParameter();
      if (serviceStatus != null) {
        serviceStatus.isRunning();
      }
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
      Log.i(TAG, "Service is not running");
      intent = new Intent(this, SocketService.class);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(intent);
      } else {
        startService(intent);
      }
      bindService(intent, getMyConnection, Context.BIND_AUTO_CREATE);
    } else {
      Log.i(TAG, "Service is running");
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

    this.socketParameter = socketParameter;
    myService.closeSocket();
    myService.connectSocket(this.socketParameter);
    StringChachi.bindApplication(getMyService());

  }

  public void disconnect() {
    myService.closeSocket();
  }

  public void setSocketParameter() {

    this.socketParameter = setSocketConfiguration();

    if (socketParameter != null) {
      myService.connectSocket(socketParameter);
      StringChachi.bindApplication(getMyService());
    }
  }

  public boolean isAutoRunServiceWhenSystemOn() {
    SharedPreferences prefs = getSharedPreferences("autoRunServiceWhenSystemOn", MODE_PRIVATE);
    return prefs.getBoolean("autoRunServiceWhenSystemOn", true);
  }

  public void setAutoRunServiceWhenSystemOn(boolean autoRunServiceWhenSystemOn) {
    SharedPreferences.Editor editor =
        getSharedPreferences("autoRunServiceWhenSystemOn", MODE_PRIVATE).edit();
    editor.putBoolean("autoRunServiceWhenSystemOn", autoRunServiceWhenSystemOn);
    editor.apply();
  }

  public SocketParameterLibrary setSocketConfiguration() {

    return null;
  }

  //public abstract SocketParameterLibrary setSocketConfiguration();

  public ServiceStatus serviceStatus;

  public void tryService(ServiceStatus serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public interface ServiceStatus {
    void isRunning();
  }
}
