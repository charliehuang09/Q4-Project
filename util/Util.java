package util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Util {
  public static synchronized void playSound(final String fileName) {
    new Thread(new Runnable() {
      public void run() {
        try {
          InputStream audioSrc = getClass().getResourceAsStream("/sound/" + fileName);
          InputStream bufferedIn = new BufferedInputStream(audioSrc);
          AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);

          Clip clip = AudioSystem.getClip();
          clip.open(audioIn);
          clip.start();

          clip.addLineListener(event -> {
            if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
              clip.close();
            }
          });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public static BufferedImage load(String filename) {
    try {
      return ImageIO.read(Util.class.getResourceAsStream("/img/" + filename));
    } catch (IOException e) {
      System.out.println(filename);
      e.printStackTrace();
      return null;
    }
  }

  public static float lerp(float a, float b, float t) {
    return a + (b - a) * t;
  }
}
