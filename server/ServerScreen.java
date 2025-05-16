package server;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import struct.MyArrayList;

public class ServerScreen extends JPanel implements ActionListener {
  private static int WIDTH = 1000;
  private static int HEIGHT = 450;

  private ServerPacketManager pm;
  private int mouseX, mouseY; // temp demo

  private ScheduledExecutorService executor;

  private JTextArea textArea;

  public ServerScreen() {
    JFrame frame = new JFrame("Server Screen");
    frame.add(this);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    this.setLayout(null);
    textArea = new JTextArea();
    textArea.setBounds(10, 10, WIDTH - 20, HEIGHT - 20);
    textArea.setEditable(false);
    textArea.setFocusable(false);
    this.add(textArea);

    this.executor = Executors.newSingleThreadScheduledExecutor();
    this.executor.scheduleAtFixedRate(this::repaint, 0, 1000 / 20, TimeUnit.MILLISECONDS);
  }

  public void setPacketManager(ServerPacketManager pm) {
    this.pm = pm;
  }

  public void setMousePos(int mouseX, int mouseY) {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
  }

  public void setText(String text) {
    textArea.setText(text);
  }

  public void updateIPs(MyArrayList<String> ips) {
    StringBuilder sb = new StringBuilder();
    sb.append("Connected clients:\n");
    for (String ip : ips) {
      sb.append(" - ").append(ip).append("\n");
    }
    textArea.setText(sb.toString());
  }

  public Dimension getPreferredSize() {
    return new Dimension(WIDTH, HEIGHT);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.fillOval(mouseX - 3, mouseY - 3, 6, 6);
  }

  @Override
  public void actionPerformed(ActionEvent e) {}
}
