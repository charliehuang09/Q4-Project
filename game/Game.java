package game;

import java.awt.Graphics;
import model.DeathBall;
import model.Platform;
import model.Player;
import model.Sprite;
import model.Team;
import struct.MyArrayList;

public class Game {
  MyArrayList<Sprite> sprites;

  public Game() {
    initGame();
  }

  private void initGame() {
    this.sprites = new MyArrayList<Sprite>();
    this.sprites.add(new DeathBall(300, 80));

    // Player
    this.sprites.add(new Player(100, 80, Team.BLUE, (DeathBall) sprites.get(0)));

    // Platform
    this.sprites.add(new Platform(500, 445, 1000, 100));
  }

  public void update(float dt, boolean[] keys) {
    for (Sprite sprite : sprites) {
      sprite.applyForce(0, 0.2f);
      sprite.applyDrag(0.95f);
    }
    for (Sprite sprite : sprites) {
      sprite.update(sprites, dt, keys);
    }
  }

  public void draw(Graphics g) {
    for (Sprite sprite : sprites) {
      sprite.draw(g);
    }
  }
}
