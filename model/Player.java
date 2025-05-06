package model;

import java.awt.*;

public class Player extends Sprite {
  private Team team;

  public Player(float x, float y, Team team) {
    super(x, y, 0, 0, 1);
    this.team = team;
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
