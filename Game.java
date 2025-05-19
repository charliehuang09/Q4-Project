import java.awt.Graphics;
import java.util.ArrayList;
import model.Platform;
import model.Player;
import model.Sprite;
import model.Team;

public class Game {
  ArrayList<Sprite> sprites;

  public Game() {
    initGame();
  }

  private void initGame() {
    this.sprites = new ArrayList<Sprite>();
    this.sprites.add(new Player(100, 100, Team.BLUE));
    this.sprites.add(new Platform(100, 200, 1000, 10));
  }

  public void update() {
    for (Sprite sprite : sprites) {
      sprite.applyForce(0, 0.1f);
      sprite.applyDrag(0.95f);
    }
    for (Sprite sprite : sprites) {
      sprite.update(sprites);
    }
  }

  public void draw(Graphics g) {
    for (Sprite sprite : sprites) {
      sprite.draw(g);
    }
  }
}
