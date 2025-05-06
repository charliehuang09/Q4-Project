package model;

import java.awt.*;

abstract class Sprite {
  float x;
  float y;
  float vx;
  float vy;
  float mass;

  public Sprite(float x, float y, float vx, float vy, float mass) {}

  public abstract boolean canMove();

  public abstract void draw(Graphics g);

  public abstract void applyForce(float x, float y, float mass);

  public abstract boolean isColliding(Sprite sprite);

  public float getX() {
    return this.x;
  }

  public float getY() {
    return this.y;
  }

  public float getVx() {
    return this.vx;
  }

  public float getVy() {
    return this.vy;
  }

  public float getMass() {
    return this.mass;
  }
}
