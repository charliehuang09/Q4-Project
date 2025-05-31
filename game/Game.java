package game;

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

public abstract class Game {
  public static final int HEIGHT = 600; // game height
  public static final int WIDTH = 1000; // game width

  public static final int FPS = 60; // frames per second
  public static final float DT_STEP = 1000f / FPS; // time step for drag calculation
  public static final float RESTITUTION = 0.8f; // bounce off platforms
  public static final float DRAG_MULTIPLIER = 0.995f;
  public static final float GROUND_FRICTION_MULTIPLIER = 0.99f; // friction when on ground
  public static final float GRAVITY = 200f; // gravity force
  public static final float MOVE_FORCE = 150f; // force applied when moving
  public static final float JUMP_FORCE = 5000f; // force applied when jumping

  public DeathBall deathBall;
  public MyHashMap<Integer, Player> players = new MyHashMap<>();
  public MyArrayList<Sprite> sprites = new MyArrayList<>();

  public int blueScore = 0;
  public int redScore = 0;

  private ScheduledExecutorService executor;

  public Game() {
    reset();
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

    this.players.get(id).setPosition(x, y);
  }

  public void updatePlayerTethering(int id, boolean tethering, float tetherLength) {
    if (!this.players.containsKey(id)) {
      System.out.println("[game] Player " + id + " not found.");
      return;
    }

    this.players.get(id).graple.active = tethering;
    this.players.get(id).graple.grapleLength = tetherLength;
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

  public void resetDeathBall() {
    if (deathBall == null) {
      deathBall = new DeathBall(500, 80);
    } else {
      deathBall.setPosition(500, 80);
      deathBall.body.state.x_vel = 0;
      deathBall.body.state.y_vel = 0;
    }
  }

  public void updateDeathBall(float x, float y, float xVel, float yVel) {
    this.deathBall.setPosition(x, y);
    this.deathBall.body.state.x_vel = xVel;
    this.deathBall.body.state.y_vel = yVel;
  }

  public void setScore(int blueScore, int redScore) {
    this.blueScore = blueScore;
    this.redScore = redScore;
  }

  public boolean isGameOver() {
    return blueScore >= 3 || redScore >= 3;
  }

  public void reset() {
    System.out.println("[game] Resetting game state...");
    this.sprites = new MyArrayList<Sprite>();

    resetDeathBall();

    // Middle divider
    this.sprites.add(new Platform(500, 0, 1, 800, false));

    // Platform
    this.sprites.add(new Platform(500, 445, 1000, 100, true));
    this.sprites.add(new Platform(500, -100, 1000, 100, true));
    this.sprites.add(new Platform(1000, 0, 100, 600, true));
    this.sprites.add(new Platform(0, 0, 100, 600, true));
  }

  public void start() {
    reset();

    blueScore = 0;
    redScore = 0;

    executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(this::update, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }

  public abstract void update();

  public void stop() {
    System.out.println("[game] Stopping game...");
    if (executor != null && !executor.isShutdown()) {
      executor.shutdownNow();
    }
  }
}
