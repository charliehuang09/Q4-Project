package model;

import java.awt.*;

public class Player extends Sprite {
  static final float RADIUS = 30;
  private Team team;
  private CircleRigid body;

  public Player(float x, float y, Team team) {
    super(x, y, 0, 0, 1);
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
    return false;
  }
}
