package com.aminano.socketservicelibrary;

import android.util.Log;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;

public class SocketConfig {
  private Socket socket;
  private SSLContext sc = null;
  private SocketParameterLibrary socketParameter;

  public SocketConfig(SocketParameterLibrary socketParameter) {
    this.socketParameter = socketParameter;
  }

  public Socket connect() {
    IO.Options options = new IO.Options();
    options.forceNew = socketParameter.isForceNew();
    options.reconnection = socketParameter.isReconnection();
    options.reconnectionAttempts = socketParameter.getReconnectionAttempts();
    options.reconnectionDelay = socketParameter.getReconnectionDelay();
    options.transports = socketParameter.getTransports();
    options.query = socketParameter.getQuery();

    try {
      sc = SSLContext.getInstance("SSL");
      sc.init(null, getTrustManager(), new java.security.SecureRandom());

      OkHttpClient okHttpClient = getOkHTTpClient(sc);
      IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
      IO.setDefaultOkHttpCallFactory(okHttpClient);
      options.callFactory = okHttpClient;
      options.webSocketFactory = okHttpClient;
      options.secure = true;
      socket = IO.socket(socketParameter.getServerEndpoint(), options);
      socket.connect();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return socket;
  }

  private TrustManager[] getTrustManager() {
    TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
          public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
          }

          @Override
          public void checkClientTrusted(X509Certificate[] arg0, String arg1)
              throws CertificateException {
            // Not implemented
          }

          @Override
          public void checkServerTrusted(X509Certificate[] arg0, String arg1)
              throws CertificateException {
            // Not implemented
          }
        }
    };
    return trustAllCerts;
  }

  private OkHttpClient getOkHTTpClient(SSLContext sc) {
    OkHttpClient okHttpClient =
        new OkHttpClient.Builder().hostnameVerifier(new RelaxedHostNameVerifier())
            .sslSocketFactory(sc.getSocketFactory(), new X509TrustManager() {
              @Override
              public void checkClientTrusted(X509Certificate[] chain, String authType)
                  throws CertificateException {

              }

              @Override
              public void checkServerTrusted(X509Certificate[] chain, String authType)
                  throws CertificateException {

              }

              @Override
              public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
              }
            })
            .build();
    return okHttpClient;
  }

  private static class RelaxedHostNameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }
}
