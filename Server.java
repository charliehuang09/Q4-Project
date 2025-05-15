import java.io.IOException;

import server.ServerPacketManager;
import server.ServerScreen;

public class Server {

  public static void main(String[] args) {
    ServerScreen screen = new ServerScreen();
    ServerPacketManager pm = new ServerPacketManager();
    screen.setPacketManager(pm);
    pm.setScreen(screen);

    try {
      pm.start(31415);
    } catch (IOException e) {
      throw new RuntimeException("Failed to start server");
    }
  }
}