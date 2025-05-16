package render;

import java.awt.Graphics;
import model.Sprite;
import struct.MyArrayList;

public class Render {
  public static void draw(Graphics g, MyArrayList<Sprite> sprites) {
    for (Sprite sprite : sprites) {
      sprite.draw(g);
    }
  }
}
