package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.Packet;
import network.PacketManager;

public class ServerPacketManager extends PacketManager {
  public static final int MAX_CONNECTIONS = 4;

  private ServerSocket serverSocket;
  private ExecutorService executor;
  private ArrayList<IndividualPacketManager> connectionManagers;

  public ServerPacketManager() {
    executor = Executors.newFixedThreadPool(MAX_CONNECTIONS);
    connectionManagers = new ArrayList<>();
  }

  @Override
  public void onReceive(Packet packet) {
    System.out.println("[server:network] Received " + packet.getId());
  }

  @Override
  public void disconnect() {
    for (IndividualPacketManager ipm : connectionManagers) {
      ipm.disconnect();
    }
  }

  public void start(int port) throws IOException {
    executor.submit(() -> {
      try {
        serverSocket = new ServerSocket(port);
  
        while (true) {
          Socket newConnection = serverSocket.accept();
          IndividualPacketManager ipm = new IndividualPacketManager(newConnection, this);
          ipm.start();
          connectionManagers.add(ipm);
        }
      } catch (IOException e) {
        e.printStackTrace();
        disconnect();
      }
    });
  }
}