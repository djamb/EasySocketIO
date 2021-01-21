package com.aminano.socketservicelibrary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import io.socket.client.Socket;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;

public class SocketService extends Service {
  private PowerManager.WakeLock mWakeLock;
  private Socket socket;
  private final static String channelId = "AminanoProperty";
  private IBinder mBinder = new MyBinder();
  private Object eventListener;

  @Override
  public void onCreate() {
    super.onCreate();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      startMyOwnForeground();
    } else {
      startForeground(210, new Notification());
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
    mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PartialWakeLockTag:");
    mWakeLock.acquire();
    return START_STICKY;
  }

  public void setListener(Object eventListener) {
    this.eventListener = eventListener;
    //((SocketOn) eventListener).socket(socket);
    bindSocketOn(this.eventListener, socket);
  }

  public boolean emitObject(String event, Object... sendData) {
    if (((BaseAppSocket) getApplicationContext()).getMServiceBound()) {
      if (eventListener != null) {
        //return ((SocketOn) eventListener).emit(event, sendData);
        bindSocketEmit(eventListener, event, sendData);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public void connectSocket(SocketParameterLibrary socketParameter) {
    SocketConfig socketConfig = new SocketConfig(socketParameter);
    socket = socketConfig.connect();
  }

  public void closeSocket() {
    //TODO ojito añadido el if (socket!=null)
    if (socket!=null)
    socket.disconnect();
    //socket=null;
  }

  private void startMyOwnForeground() {
    NotificationManager manager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationChannel channel =
        new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_NONE);
    manager.createNotificationChannel(channel);
    Notification notification = new NotificationCompat.Builder(this, channelId).build();
    startForeground(219, notification);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mWakeLock.release();
    if (socket != null) socket.disconnect();
  }

  public class MyBinder extends Binder {
    public SocketService getService() {
      return SocketService.this;
    }
  }

  public static void bindSocketOn(Object socketOn, Socket socket) {
    Method methodBindSocketOn = null;
    try {
      methodBindSocketOn = socketOn.getClass().getMethod("socket", socket.getClass());
      methodBindSocketOn.invoke(socketOn, socket);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      Log.e(TAG, "Error", e);
    }
  }

  public static void bindSocketEmit(Object socketOn, String event, Object... sendData) {
    Method methods = null;
    try {
      methods = socketOn.getClass().getMethod("emit", String.class, Object[].class);
      methods.invoke(socketOn, event, sendData);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      Log.e(TAG, "Error", e);
    }
  }

}
