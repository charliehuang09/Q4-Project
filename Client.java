import javax.swing.JFrame;

import client.ClientScreen;

class Client {
  public static void main(String[] args) {
    ClientScreen screen = new ClientScreen();
    JFrame frame = new JFrame("Screen");

    frame.add(screen);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
