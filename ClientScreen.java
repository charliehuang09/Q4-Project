import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ClientScreen extends JPanel implements ActionListener {
  Game game;

  public ClientScreen() {
    game = new Game();
    Thread thread =
        new Thread(
            () -> {
              while (true) {
                game.update();
                try {
                  Thread.sleep(50);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            });
    thread.start();
  }

  public Dimension getPreferredSize() {
    return new Dimension(1000, 450);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    game.draw(g);
  }

  @Override
  public void actionPerformed(ActionEvent e) {}
}
