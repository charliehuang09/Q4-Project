package model;

import java.awt.*;
import java.util.ArrayList;

public abstract class Sprite {
  public abstract boolean canMove();

  public abstract void draw(Graphics g);

  public abstract void applyForce(float x, float y, float mass);

  public abstract void applyForce(float x, float y);

  public abstract void update(ArrayList<Sprite> sprites);

  public abstract boolean isColliding(Sprite sprite);

  public abstract void applyDrag(float x);
}
