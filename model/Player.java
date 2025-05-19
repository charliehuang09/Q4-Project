package model;

import java.awt.*;
import java.util.ArrayList;

public class Player extends Sprite {
  static final float RADIUS = 30;
  private Team team;
  public CircleRigid body;

  public Player(float x, float y, Team team) {
    this.team = team;
    this.body = new CircleRigid(x, y, RADIUS);
  }

  public Player cloneWithOffset(float x_offset, float y_offset) {
    Player newPlayer = new Player(body.state.x, body.state.y, team);
    newPlayer.body.state.x_vel = body.state.x_vel + x_offset;
    newPlayer.body.state.y_vel = body.state.y_vel + y_offset;
    return newPlayer;
  }

  @Override
  public boolean canMove() {
    return true;
  }

  @Override
  public void draw(Graphics g) {
    g.drawOval(
        (int) (body.state.x - (RADIUS)),
        (int) (body.state.y - (RADIUS)),
        (int) RADIUS,
        (int) RADIUS);
  }

  @Override
  public void applyForce(float x, float y, float mass) {
    body.state.x_vel += x;
    body.state.y_vel += y;
  }

  @Override
  public void applyForce(float x, float y) {
    applyForce(x, y, body.state.mass);
  }

  @Override
  public void update(ArrayList<Sprite> sprites) {
    boolean x_collides = false;
    boolean y_collides = false;
    try {
      for (Sprite sprite : sprites) {
        if (sprite == this) {
          continue;
        }
        if (Collision.isColliding((Sprite) this.cloneWithOffset(body.state.x_vel, 0), sprite)) {
          body.state.x_vel *= -1;
          body.state.x += body.state.x_vel;
          x_collides = true;
        }
        if (Collision.isColliding((Sprite) this.cloneWithOffset(0, body.state.y_vel), sprite)) {
          body.state.y_vel *= -1;
          body.state.y += body.state.y_vel;
          y_collides = true;
        }
        if (x_collides || y_collides) break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!x_collides) body.state.x += body.state.x_vel;
    if (!y_collides) body.state.y += body.state.y_vel;
  }

  @Override
  public boolean isColliding(Sprite sprite) {
    if (sprite instanceof Player) {
      Player player = (Player) sprite;
      return Collision.isColliding(this.body, player.body);
    }
    throw new RuntimeException("Unhandled collision type");
  }

  @Override
  public void applyDrag(float multiplier) {
    this.body.state.x_vel *= multiplier;
    this.body.state.y_vel *= multiplier;
  }
}
