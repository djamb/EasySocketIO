package com.aminano.djamb.annotation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.android.streye.constant_share.MethodValue;
import com.android.streye.stringshadow.StringChachi;

public class CustomAnnotation2 extends Activity {

  public static final String TAG ="CustomAnnotation2";
  @MethodValue(value = "pong")
  public void myMethodCustomAnnotation2(Object[] x) {
    Log.i(TAG, "pong: "+x[0]);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StringChachi.bind2(this);
  }


}
