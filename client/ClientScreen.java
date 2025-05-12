package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ClientScreen extends JPanel implements ActionListener {
  public ClientScreen() {
    JFrame frame = new JFrame("Client Screen");
    frame.add(this);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  public Dimension getPreferredSize() {
    return new Dimension(1000, 450);
  }

  @Override
  public void paintComponent(Graphics g) {}

  @Override
  public void actionPerformed(ActionEvent e) {}
}
