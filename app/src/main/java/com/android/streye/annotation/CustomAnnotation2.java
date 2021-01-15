package com.android.streye.annotation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.android.streye.constant_share.MethodValue;
import com.android.streye.stringshadow.StringChachi;

public class CustomAnnotation2 extends Activity {
  @MethodValue(value = "pong")
  public void myMethodCustomAnnotation2(Object[] x) {
    Log.e("CustomAnnotation2 aminano: ", "pong: "+x[0]);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StringChachi.bind2(this);
    //StringChachi.unbind2(this);
  }


}
