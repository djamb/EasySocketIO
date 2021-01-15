package com.aminano.socketservicelibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadCastInitSocket extends BroadcastReceiver {
  private static final String TAG = "BroadCastInitSocket";
  @Override
  public void onReceive(Context context, Intent intent) {
    if (((BaseAppSocket)context.getApplicationContext()).isAutoRunServiceWhenSystemOn()) {
      Log.e("aminano","BroadCastInitSocket autorun true");
      ((BaseAppSocket) context.getApplicationContext()).runService();
    }
  }
}
