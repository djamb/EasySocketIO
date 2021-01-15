package com.aminano.socketservicelibrary;

import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;

public class SocketParameterLibrary {

  private String serverEndpoint;
  private String query;
  private boolean forceNew;
  private boolean reconnection;
  private int reconnectionAttempts;
  private int reconnectionDelay;
  private String[] transports;


  public SocketParameterLibrary(String serverEndpoint,String query){
    this.serverEndpoint=serverEndpoint;
    this.query=query;
    forceNew = true;
    reconnection = true;
    reconnectionAttempts = 2147483646;
    reconnectionDelay = 5000;
    transports = new String[] { WebSocket.NAME };
  }

  public String getServerEndpoint() {
    return serverEndpoint;
  }

  public void setServerEndpoint(String serverEndpoint) {
    this.serverEndpoint = serverEndpoint;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public boolean isForceNew() {
    return forceNew;
  }

  public void setForceNew(boolean forceNew) {
    this.forceNew = forceNew;
  }

  public boolean isReconnection() {
    return reconnection;
  }

  public void setReconnection(boolean reconnection) {
    this.reconnection = reconnection;
  }

  public int getReconnectionAttempts() {
    return reconnectionAttempts;
  }

  public void setReconnectionAttempts(int reconnectionAttempts) {
    this.reconnectionAttempts = reconnectionAttempts;
  }

  public int getReconnectionDelay() {
    return reconnectionDelay;
  }

  public void setReconnectionDelay(int reconnectionDelay) {
    this.reconnectionDelay = reconnectionDelay;
  }

  public String[] getTransports() {
    return transports;
  }

  public void setTransports(String[] transports) {
    this.transports = transports;
  }
}
