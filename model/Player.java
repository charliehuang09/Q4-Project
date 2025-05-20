package model;

import java.awt.*;
import struct.MyArrayList;

public class Player extends Sprite {
  static final float RADIUS = 30;
  private Team team;
  public CircleRigid body;
  public Graple graple;

  public Player(float x, float y, Team team, DeathBall deathBall) {
    this.team = team;
    this.body = new CircleRigid(x, y, RADIUS);
    this.graple = new Graple(this, deathBall);
  }

  public Player cloneWithOffset(float x_offset, float y_offset) {
    Player newPlayer = new Player(body.state.x, body.state.y, team, null);
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
    if (team == Team.RED) {
      g.setColor(Color.RED);
    } else {
      g.setColor(Color.BLUE);
    }
    g.fillOval(
        (int) (body.state.x - (RADIUS)),
        (int) (body.state.y - (RADIUS)),
        (int) RADIUS,
        (int) RADIUS);
    graple.draw(g);
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
  public void update(MyArrayList<Sprite> sprites, float dt) {
    boolean x_collides = false;
    boolean y_collides = false;
    try {
      for (Sprite sprite : sprites) {
        if (sprite == this) {
          continue;
        }
        if (Collision.isColliding(
            (Sprite) this.cloneWithOffset(body.state.x_vel * dt, 0), sprite)) {
          body.state.x_vel *= -1;
          body.state.x += body.state.x_vel * dt;
          x_collides = true;
        }
        if (Collision.isColliding(
            (Sprite) this.cloneWithOffset(0, body.state.y_vel * dt), sprite)) {
          body.state.y_vel *= -1;
          body.state.y += body.state.y_vel * dt;
          y_collides = true;
        }
        if (x_collides || y_collides) break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!x_collides) body.state.x += body.state.x_vel * dt;
    if (!y_collides) body.state.y += body.state.y_vel * dt;
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
