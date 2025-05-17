import java.io.IOException;

import server.ServerNetworkManager;
import server.ServerScreen;

public class Server {

  public static void main(String[] args) {
    ServerScreen screen = new ServerScreen();
    ServerNetworkManager pm = new ServerNetworkManager();
    screen.setPacketManager(pm);
    pm.setScreen(screen);

    try {
      pm.start(31415);
    } catch (IOException e) {
      throw new RuntimeException("Failed to start server");
    }
  }
}