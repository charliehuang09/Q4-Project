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

  private ServerController controller;

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

  public void setController(ServerController controller) {
    this.controller = controller;
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
  }

  @Override
  public void actionPerformed(ActionEvent e) {
  }
}
