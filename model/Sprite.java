package model;

import java.awt.*;

abstract class Sprite {
  public abstract boolean canMove();

  public abstract void draw(Graphics g);

  public abstract void applyForce(float x, float y, float mass);

  public abstract boolean isColliding(Sprite sprite);
}
