package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import network.Packet;
import network.PacketManager;

public class ServerPacketManager extends PacketManager {

  private Selector selector;
  private ServerSocketChannel server;

  @Override
  public void onReceive(Packet packet) {
    System.out.println("[server:network] Received" + packet.getId());
  }

  @Override
  public void disconnect() {
    try {
      selector.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void start(int port) {
    try {
      selector = Selector.open();

      server = ServerSocketChannel.open();
      server.bind(new InetSocketAddress(port));
      server.configureBlocking(false);
      server.register(selector, SelectionKey.OP_ACCEPT);
    } catch (IOException e) {
      throw new RuntimeException("Failed to start server");
    }


    try {
      while (true) {
        selector.select();
  
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectedKeys.iterator();
  
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
  
          if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
          } else if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            Socket clientSocket = client.socket();
            receivePacket(new DataInputStream(clientSocket.getInputStream()));
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      disconnect();
    }
  }
}