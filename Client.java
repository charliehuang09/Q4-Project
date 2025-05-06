import client.Screen;

class Client {
  public static void main(String[] args) {
    Screen screen = new Screen();
    JFrame frame = new JFrame("Screen");

    frame.add(screen);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
