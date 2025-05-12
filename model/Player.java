package model;

import java.awt.*;

public class Player extends Sprite {
  static final float RADIUS = 30;
  private Team team;
  private CircleRigid body;

  public Player(float x, float y, Team team) {
    this.team = team;
    this.body = new CircleRigid(x, y, RADIUS);
  }

  @Override
  public boolean canMove() {
    return true;
  }

  @Override
  public void draw(Graphics g) {}

  @Override
  public void applyForce(float x, float y, float mass) {}

  @Override
  public boolean isColliding(Sprite sprite) {
    if (sprite instanceof Player) {
      Player player = (Player) sprite;
      return Collision.isColliding(this.body, player.body);
    }
    throw new RuntimeException("Unhandled collision type");
  }
}
