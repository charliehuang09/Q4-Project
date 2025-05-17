import server.ServerController;
import server.ServerNetworkManager;
import server.ServerScreen;

public class Server {

  public static void main(String[] args) {
    ServerScreen screen = new ServerScreen();
    ServerNetworkManager networkManager = new ServerNetworkManager();
    new ServerController(networkManager, screen);
  }
}