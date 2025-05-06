import javax.swing.JFrame;

import server.ServerScreen;

class Client {
  public static void main(String[] args) {
    ServerScreen screen = new ServerScreen();
    JFrame frame = new JFrame("Screen");

    frame.add(screen);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
