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
  public void reset() {
    super.reset();

    // Reset players to avoid resetting multiple times
    for (Player p : players.values()) {
      p.alive = true;
      p.graple.active = false;
    }
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
    boolean anyBlue = false;
    boolean anyRed = false;
    for (Player p : players.values()) {
      p.graple.update(dt, p.graple.active);
      if (p.team == Team.BLUE) {
        anyBlue = true;
        if (p.alive) {
         blueDead = false;
        }
      } else if (p.team == Team.RED) {
        anyRed = true;
        if (p.alive) {
          redDead = false;
        }
      }
    }

    if ((blueDead && anyBlue) || (redDead && anyRed)) {
      controller.nextRound();
    }

    if (deathBall.redDeath() || deathBall.blueDeath()) {
      controller.nextRound();
    }
  }
}
