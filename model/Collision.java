package model;

public class Collision {
  public static boolean isColliding(CircleRigid circle1, CircleRigid circle2) {
    float dx = circle1.state.x - circle2.state.x;
    float dy = circle1.state.y - circle2.state.y;
    float distanceSquared = (dx * dx) + (dy * dy);
    float radiiSum = circle1.radius + circle2.radius;
    return distanceSquared < (radiiSum * radiiSum);
  }

  public static boolean isColliding(RectangleRigid rectangle1, RectangleRigid rectangle2) {
    if (rectangle1.rightX() < rectangle2.leftX() || rectangle2.rightX() < rectangle1.leftX()) {
      return false;
    }

    if (rectangle1.downY() < rectangle2.upY() || rectangle2.downY() < rectangle1.upY()) {
      return false;
    }

    return true;
  }

  public static boolean isColliding(CircleRigid circle, RectangleRigid rectangle) {
    float closestX = Math.max(rectangle.leftX(), Math.min(circle.state.x, rectangle.rightX()));
    float closestY = Math.max(rectangle.upY(), Math.min(circle.state.y, rectangle.downY()));

    float dx = circle.state.x - closestX;
    float dy = circle.state.y - closestY;

    float distanceSquared = (dx * dx) + (dy * dy);
    return distanceSquared < (circle.radius * circle.radius);
  }

  public static boolean isColliding(RectangleRigid rectangle, CircleRigid circle) {
    return isColliding(circle, rectangle);
  }

  public static boolean isColliding(Sprite sprite1, Sprite sprite2) {
    if (sprite1 instanceof Player p1 && sprite2 instanceof Player p2) {
      return isColliding(p1.body, p2.body);
    }
    if (sprite1 instanceof Player p1 && sprite2 instanceof Platform p2) {
      return isColliding(p1.body, p2.body);
    }
    if (sprite1 instanceof Platform p1 && sprite2 instanceof Player p2) {
      return isColliding(p1.body, p2.body);
    }
    if (sprite1 instanceof Platform p1 && sprite2 instanceof Platform p2) {
      return isColliding(p1.body, p2.body);
    }
    if (sprite1 instanceof DeathBall d1 && sprite2 instanceof Platform p2) {
      return isColliding(d1.body, p2.body);
    }
    return false;
  }
}