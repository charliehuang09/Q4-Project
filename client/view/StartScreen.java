package client.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class StartScreen extends JPanel {
  private ClientScreen clientScreen;

  public StartScreen(ClientScreen clientScreen) {
    this.clientScreen = clientScreen;

    JButton joinButton = new JButton("Join");
    joinButton.addActionListener(this::attemptConnect);
    this.add(joinButton);
  }

  public void attemptConnect(ActionEvent e) {
    try {
      clientScreen.getPacketManager().connect("localhost", 31415);

      clientScreen.showScreen("gameScreen");
    } catch (IOException ex) {
      throw new RuntimeException("Can't connect to server");
    }
  }
}
