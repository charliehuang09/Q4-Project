package game;

import client.ClientController;
import java.awt.Graphics;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import model.DeathBall;
import model.Platform;
import model.Player;
import model.Sprite;
import model.Team;
import struct.MyArrayList;
import struct.MyHashMap;

public class Game {
  public int id = -1;

  public static final int FPS = 60; // frames per second
  public static final float DT_STEP = 1000f / FPS; // time step for drag calculation
  public static final float RESTITUTION = 0.8f; // bounce off platforms
  public static final float DRAG_MULTIPLIER = 0.995f;
  public static final float GROUND_FRICTION_MULTIPLIER = 0.99f; // friction when on ground
  public static final float GRAVITY = 200f; // gravity force
  public static final float MOVE_FORCE = 150f; // force applied when moving
  public static final float JUMP_FORCE = 5000f; // force applied when jumping

  public Player player;
  public DeathBall deathBall;
  public MyHashMap<Integer, Player> players = new MyHashMap<>();
  public MyArrayList<Sprite> sprites = new MyArrayList<>();

  private ScheduledExecutorService executor;
  private long time;

  public Game() {
    initGame();

    executor = Executors.newScheduledThreadPool(1);
  }

  public void setId(int id) {
    this.id = id;
  }

  public Player getPlayer() {
    return this.player;
  }

  public void addSprite(Sprite sprite) {
    this.sprites.add(sprite);
  }

  public void addPlayer(int id, Player player) {
    this.players.put(id, player);
  }

  public void removePlayer(int id) {
    if (!this.players.containsKey(id)) {
      System.out.println("[game] Player " + id + " not found.");
      return;
    }

    this.players.remove(id);
  }

  public void updatePlayerPosition(int id, float x, float y) {
    if (!this.players.containsKey(id)) {
      System.out.println("[game] Player " + id + " not found.");
      return;
    }

    this.players.get(id).body.state.x = x;
    this.players.get(id).body.state.y = y;
  }

  public void updatePlayerTethering(int id, boolean tethering) {
    if (!this.players.containsKey(id)) {
      System.out.println("[game] Player " + id + " not found.");
      return;
    }

    this.players.get(id).graple.active = tethering;
  }

  public void updatePlayerAlive(int id, boolean alive) {
    if (!this.players.containsKey(id)) {
      System.out.println("[game] Player " + id + " not found.");
      return;
    }

    this.players.get(id).alive = alive;
  }

  public void updatePlayerTeam(int id, Team team) {
    if (!this.players.containsKey(id)) {
      System.out.println("[game] Player " + id + " not found.");
      return;
    }

    this.players.get(id).team = team;
  }

  public void updateDeathBall(float x, float y, float xVel, float yVel) {
    this.deathBall.body.state.x = x;
    this.deathBall.body.state.y = y;
    this.deathBall.body.state.x_vel = xVel;
    this.deathBall.body.state.y_vel = yVel;
  }

  public void initPlayer() {
    this.player = new Player(0, 0, "PlayerName", Team.NONE, deathBall);
  }

  private void initGame() {
    this.sprites = new MyArrayList<Sprite>();

    this.deathBall = new DeathBall(500, 80);

    // Middle divider
    this.sprites.add(new Platform(500, 0, 1, 800, false));

    // Platform
    this.sprites.add(new Platform(500, 445, 1000, 100, true));
    this.sprites.add(new Platform(500, -100, 1000, 100, true));
    this.sprites.add(new Platform(1000, 0, 100, 600, true));
    this.sprites.add(new Platform(0, 0, 100, 600, true));
  }

  public void start() {
    // Put player in the right place based on their team
    if (player != null) {
      if (player.team == Team.BLUE) {
        player.body.state.x = 100;
        player.body.state.y = 400;
      } else if (player.team == Team.RED) {
        player.body.state.x = 900;
        player.body.state.y = 400;
      }
    }

    executor.scheduleAtFixedRate(this::update, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }

  public void stop() {
    executor.shutdown();
  }

  public void update() {
    if (time == 0) {
      time = System.currentTimeMillis();
      return;
    }
  
    long ct = System.currentTimeMillis();
    float dt = (float) (ct - time) / 1000f;
    time = ct;

    if (player != null) {
      // client-side game logic
      boolean[] keys = ClientController.keys;
  
      player.update(sprites, dt, keys);
      if (deathBall.redDeath()) {
        if (player.team == Team.RED) player.alive = false;
      }
      if (deathBall.blueDeath()) {
        if (player.team == Team.BLUE) player.alive = false;
      }
    } else {
      // server-side game logic
      deathBall.update(sprites, dt, null);
      for (Player p : players.values()) {
        if (!p.alive) continue; // Skip dead players
        p.graple.update(dt, p.graple.active);
      }
    }
  }

  public void draw(Graphics g) {
    for (Sprite sprite : sprites) {
      sprite.draw(g);
    }

    for (Player player : players.values()) {
      player.draw(g);
    }

    player.draw(g);
    deathBall.draw(g);
  }
}
