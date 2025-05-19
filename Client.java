import client.ClientController;
import client.ClientPacketManager;
import client.view.ClientScreen;

public class Client {
  public static void main(String[] args) {
    ClientScreen screen = new ClientScreen();
    ClientPacketManager pm = new ClientPacketManager();
    new ClientController(pm, screen);
  }
}