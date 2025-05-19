package model;

import java.awt.*;

import struct.MyArrayList;

public abstract class Sprite {
  public abstract boolean canMove();

  public abstract void draw(Graphics g);

  public abstract void applyForce(float x, float y, float mass);

  public abstract void applyForce(float x, float y);

  public abstract void update(MyArrayList<Sprite> sprites);

  public abstract boolean isColliding(Sprite sprite);

  public abstract void applyDrag(float x);
}
