package client.view;

import java.awt.*;
import javax.swing.*;

public class LobbyScreen extends JPanel {
  private ClientScreen clientScreen;
  private JLabel teamLabel;
  private JButton readyButton;
  private boolean isReady;

  public LobbyScreen(ClientScreen clientScreen) {
    this.clientScreen = clientScreen;
    this.setLayout(null);

    JLabel titleLabel = new JLabel("Lobby", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Bahnschrift", Font.BOLD, 24));
    titleLabel.setBounds(0, 20, 1000, 50);
    this.add(titleLabel);

    // Team Selection
    teamLabel = new JLabel("Team: None", SwingConstants.CENTER);
    teamLabel.setBounds(0, 80, 1000, 50);
    this.add(teamLabel);

    JButton blueTeamButton = new JButton("Join Blue Team");
    blueTeamButton.addActionListener(e -> joinTeam("Blue"));
    blueTeamButton.setBounds(200, 150, 200, 50);
    this.add(blueTeamButton);

    JButton redTeamButton = new JButton("Join Red Team");
    redTeamButton.addActionListener(e -> joinTeam("Red"));
    redTeamButton.setBounds(600, 150, 200, 50);
    this.add(redTeamButton);

    readyButton = new JButton("Ready");
    readyButton.addActionListener(e -> toggleReady());
    readyButton.setBounds(400, 250, 200, 50);
    this.add(readyButton);

    isReady = false;
  }

  private void joinTeam(String team) {
    teamLabel.setText("Team: " + team);
    clientScreen.getController().requestTeam(team.toUpperCase());
  }

  public void toggleReady() {
    isReady = !isReady;
    readyButton.setText(isReady ? "Unready" : "Ready");
    clientScreen.getController().readyUp(isReady);
  }
}
