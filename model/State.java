package model;

public class State {
  float x;
  float y;
  float x_vel;
  float y_vel;
  float mass;

  public State(float x, float y, float x_vel, float y_vel, float mass) {
    this.x = x;
    this.y = y;
    this.x_vel = x_vel;
    this.y_vel = y_vel;
    this.mass = mass;
  }
}
