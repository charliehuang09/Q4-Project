package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.Packet;
import network.PacketManager;

public class ClientPacketManager extends PacketManager {

  protected Socket connection;
  protected DataInputStream in;
  protected DataOutputStream out;

  private ExecutorService executor;

  public ClientPacketManager() {
    executor = Executors.newSingleThreadExecutor();
    executor.submit(this::start);
  }

  @Override
  public void onReceive(Packet packet) {
    System.out.println("[client:network] Received " + packet.getId());
  }

  public void connect(String host, int port) throws IOException {
    connection = new Socket(host, port);

    in = new DataInputStream(connection.getInputStream());
    out = new DataOutputStream(connection.getOutputStream());
  }

  public void disconnect() {
    try {
      connection.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void start() {
    try {
      while (true) {
        Packet packet = receivePacket(in);
        onReceive(packet);
      }
    } catch (IOException e) {
      e.printStackTrace(); // for temporary debug
      disconnect();
    }
  }
}