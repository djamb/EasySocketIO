[![](https://jitpack.io/v/djamb/EasySocketIO.svg)](https://jitpack.io/#djamb/EasySocketIO)
# EasySocketIO
EasySocketIo is a repository develop with annotation for a easy use of socket.io


#Gradle
Compile my wrapper:

```
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
  dependencies {
    implementation 'com.github.djamb.EasySocketIO:socketservicelibrary:1.0.4'
	}

```

# Use example:
This code is a basic example. I recommend you go to Activities in app module and see all examples.


# For use it its need it:

# Step 1

## Option 1
Baseapp2 extends BaseAppSocket and set your socket url. Add android:name=".BaseApp2" to application in . SocketGeneralEvents is not necessary, its a class for control general event in application enviorement.


**AndroidManifest.xml**
```
<application
  android:name=".BaseApp2"
>

```

```
public class BaseApp2 extends BaseAppSocket {
  @Override
  public SocketParameterLibrary setSocketConfiguration() {
    new SocketGeneralEvents();
    return new SocketParameterLibrary("YOUR IP WITH PORT", "");
  }
}
```
## Option 2 (**Under construction**)
You can connect and disconnect socket server in any class of your project

**AndroidManifest.xml**
```
<application
  android:name="com.aminano.socketservicelibrary.BaseAppSocket"
>
```
**AnyClass**
```
  //application.getMyService().closeSocket();
  application.connect(new SocketParameterLibrary("ip","query"));
```

# Step 2
**CustomAnnotation**
Need it for run service only need to use it in 1 activity
```
    public BaseApp2 application;
    application = (BaseApp2) getApplicationContext();
    //You can autorun service and run socket when android is rebooted
    //application.setAutoRunServiceWhenSystemOn(true);
    //only need to start in 1 activity or applicaction
    application.runService();
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

