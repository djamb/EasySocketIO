[![](https://jitpack.io/v/djamb/EasySocketIO.svg)](https://jitpack.io/#djamb/EasySocketIO)
# EasySocketIO
EasySocketIo is a repository develop with annotation for a easy use of socket.io


# Gradle
Compile my wrapper:

```
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
  dependencies {
    implementation 'com.github.djamb.EasySocketIO:socketservicelibrary:1.0.6'
	}

```

# Use example:
This code is a basic example. I recommend you go to Activities in app module and see all examples.


# Instructions:

# Permission
```
  <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
  <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS"/>
  <uses-permission android:name="android.permission.WAKE_LOCK"/>
```

# Step 1

## Option 1
Baseapp extends BaseAppSocket and set your socket url, SocketGeneralEvents is not necessary, its a class for control general event in application enviorement.


**AndroidManifest.xml**
```
  <application
    android:usesCleartextTraffic="true"
    android:name=".BaseApp2"
  >

```

```
public class BaseApp extends BaseAppSocket {

  @Override
   public void onCreate() {
     super.onCreate();
     runService();
   }

  @Override
  public SocketParameterLibrary setSocketConfiguration() {
    return new SocketParameterLibrary("YOUR IP WITH PORT", "");
  }
}
```
## Option 2
You can connect and disconnect socket server in any class of your project

**AndroidManifest.xml**
```
<application
  android:name="com.aminano.socketservicelibrary.BaseAppSocket"
  android:usesCleartextTraffic="true"
>
```
**AnyClass**
```
  application.runService();

     application.tryService(new BaseAppSocket.ServiceStatus() {
       @Override
       public void isRunning() {
         application.connect(new SocketParameterLibrary("http://192.168.1.108:3000",""));
         Log.i(TAG, "Send data to service?: " + application.sendData("message", "caca"));
       }
     });
```

# Step 2
**AnnotationWithBaseApp**
Need it for run service, warning, only need to use it in 1 activity
```
    public BaseApp application;
    application = (BaseApp) getApplicationContext();
    application.tryService(new BaseAppSocket.ServiceStatus() {
       @Override
       public void isRunning() {
         Log.i(TAG, "Send data to service?: " + application.sendData("message", "caca"));
       }
    });
```

Send data to socket server. Return true if its send it
   ```
    //Send data to event name
    application.sendData("message", "caca")
```

Need it for subscribe to event
```
    // Suscribe event from view
    StringChachi.bind2(this);
```

Method to recieve events from server
```
    @MethodValue(value = "messages")
      public void myMethod7(Object[] object) {
        Log.e("recive from server in event messages: ", "" + object[0]);
      }
```

```
 if you dont want receive more event in this view, its neccesary when it your are going to destroy your view
     // Unsuscribe event from view
        //StringChachi.unbind2(this);
```


#Custom option
    You can autorun service and run socket when android is rebooted
    ```
    //application.setAutoRunServiceWhenSystemOn(true);
    ```
#PD. Gradle is buggy, not generated annotation files, use:
  ```
        classpath 'com.android.tools.build:gradle:3.5.0'
         ```