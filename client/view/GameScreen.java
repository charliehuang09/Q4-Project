package client.view;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class GameScreen extends JPanel implements KeyListener {
  private ClientScreen clientScreen;

  private boolean[] keys;
  private ScheduledExecutorService executor;

  public GameScreen(ClientScreen clientScreen) {
    this.clientScreen = clientScreen;

    this.setPreferredSize(new Dimension(1000, 450));
    addKeyListener(this);
    setFocusable(true);
    
    this.keys = new boolean[5];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = false;
    }
    
    setFocusable(true); // Allow the panel to receive key events
    addKeyListener(this); // Register KeyListener
    
    executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(this::repaint, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }

  @Override
  protected void paintComponent(Graphics g) {
    requestFocusInWindow();
    super.paintComponent(g);
    
    clientScreen.getController().keys = keys;
    clientScreen.getController().drawGame(g);
  }

  public void resetKeys() {
    for (int i = 0; i < keys.length; i++) {
      keys[i] = false;
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_W) {
      keys[0] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_A) {
      keys[1] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_S) {
      keys[2] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_D) {
      keys[3] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_Z) {
      keys[4] = true;
    }

    if (e.getKeyCode() == KeyEvent.VK_Y) {
      clientScreen.getController().onCheatKeyPressed();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_W) {
      keys[0] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_A) {
      keys[1] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_S) {
      keys[2] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_D) {
      keys[3] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_Z) {
      keys[4] = false;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {}
}
