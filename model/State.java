package model;

public class State {
  public float x;
  public float y;
  public float x_vel;
  public float y_vel;
  public float mass;

  public State(float x, float y, float x_vel, float y_vel, float mass) {
    this.x = x;
    this.y = y;
    this.x_vel = x_vel;
    this.y_vel = y_vel;
    this.mass = mass;
  }
}
