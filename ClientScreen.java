import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import model.Player;
import model.Sprite;
import model.Team;

public class ClientScreen extends JPanel implements ActionListener {
  ArrayList<Sprite> sprites;

  public ClientScreen() {

    this.sprites = new ArrayList<Sprite>();
    this.sprites.add(new Player(100, 100, Team.BLUE));
    Thread thread =
        new Thread(
            () -> {
              while (true) {
                this.update();
                try {
                  Thread.sleep(500);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            });
    thread.start();
  }

  private void update() {
    for (Sprite sprite : sprites) {
      sprite.applyForce(0, 0.1f);
    }
    for (Sprite sprite : sprites) {
      sprite.update();
    }
    repaint();
  }

  public Dimension getPreferredSize() {
    return new Dimension(1000, 450);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (int i = 0; i < sprites.size(); i++) {
      Render.draw(g, sprites);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {}
}
