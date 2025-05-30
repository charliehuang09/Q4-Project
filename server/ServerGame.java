package server;

import game.Game;
import model.Player;
import model.Team;

public class ServerGame extends Game {
  private final ServerController controller;
  private long time = 0; // last update time

  public ServerGame(ServerController controller) {
    super();
    this.controller = controller;
  }

  public ServerController getController() {
    return this.controller;
  }

  @Override
  public void update() {
    if (time == 0) {
      time = System.currentTimeMillis();
      return;
    }

    long ct = System.currentTimeMillis();
    float dt = (float) (ct - time) / 1000f;
    time = ct;

    deathBall.update(sprites, dt, null);

    boolean blueDead = true;
    boolean redDead = true;
    for (Player p : players.values()) {
      if (!p.alive) {
        continue;
      }
      p.graple.update(dt, p.graple.active);
      if (p.team == Team.BLUE) {
        blueDead = false;
      } else if (p.team == Team.RED) {
        redDead = false;
      }
    }

    if (blueDead || redDead) {
      controller.nextRound();
    }

    if (deathBall.redDeath() || deathBall.blueDeath()) {
      controller.nextRound();
    }
  }
}
