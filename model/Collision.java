package model;

public class Collision {

  public static boolean isColliding(CircleRigid circle1, CircleRigid circle2) {
    return ((circle1.state.x - circle2.state.x) * (circle1.state.x - circle2.state.x)
            + (circle1.state.y - circle2.state.y) * (circle1.state.y - circle2.state.y)
        < (circle1.radius + circle2.radius) * (circle1.radius * circle2.radius));
  }

  public static boolean isColliding(RectangleRigid rectangle1, RectangleRigid rectangle2) {
    if (rectangle1.rightX() < rectangle2.leftX()) return false;
    if (rectangle2.rightX() < rectangle1.leftX()) return false;

    if (rectangle1.downY() < rectangle2.upY()) return false;
    if (rectangle2.downY() < rectangle1.upY()) return false;

    return true;
  }

  public static boolean isColliding(CircleRigid circle, RectangleRigid rectangle) {
    if (circle.rightX() < rectangle.leftX()) return false;
    if (rectangle.rightX() < circle.leftX()) return false;

    if (circle.downY() < rectangle.upY()) return false;
    if (rectangle.downY() < circle.upY()) return false;

    return true;
  }

  public static boolean isColliding(RectangleRigid rectangle, CircleRigid circle) {
    return isColliding(circle, rectangle);
  }
}
