# EasySocketIO
EasySocketIo is a repository develop with annotation for a easy use of socket.io

#Use example:
This code is a basic example. I recommend you go to Activities in app module and see all examples.


For use it its need it:

# Step 1
Baseapp2 extends BaseAppSocket and set your socket url. Add android:name=".BaseApp2" to application in AndroidManifest.xml. SocketGeneralEvents is not necessary, its a class for control general event in application enviorement.

public class BaseApp2 extends BaseAppSocket {
  @Override
  public SocketParameterLibrary setSocketConfiguration() {
    new SocketGeneralEvents();
    return new SocketParameterLibrary("http://192.168.1.108:3000", "");
  }
}

# Step 2
CustomAnnotation 
