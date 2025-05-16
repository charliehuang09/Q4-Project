package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.Point;

import network.packets.UpdatePosPacket;

public class GameScreen extends JPanel {
  private ClientScreen clientScreen;

  public GameScreen(ClientScreen clientScreen) {
    this.clientScreen = clientScreen;
    this.setPreferredSize(new Dimension(1000, 450));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Point p = this.getMousePosition();
    if (p != null) {
      g.fillOval(p.x - 3, p.y - 3, 6, 6);

      if (clientScreen.getPacketManager() != null) {
        try {
          clientScreen.getPacketManager().sendPacket(new UpdatePosPacket(p.x, p.y));
        } catch (Exception e) {
          System.out.println("[client:network] Failed to send packet, disconnecting: " + e.getMessage());
          clientScreen.getPacketManager().disconnect();
        }
      }
    }
  }
}
