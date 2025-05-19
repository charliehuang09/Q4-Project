import javax.swing.JFrame;

public class Test {
  public static void main(String[] args) {
    JFrame frame = new JFrame("Client");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new client.view.GameScreen(null));
    frame.pack();
    frame.setVisible(true);
  }
}