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
    if (id == this.id) {
      return;
    }

    if (!this.players.containsKey(id)) {
      System.out.println("[game] Player " + id + " not found.");
      return;
    }

    this.players.get(id).body.state.x = x;
    this.players.get(id).body.state.y = y;
  }

  public void updatePlayerTeam(int id, Team team) {
    if (!this.players.containsKey(id)) {
      System.out.println("[game] Player " + id + " not found.");
      return;
    }

    this.players.get(id).team = team;
  }

  public void initPlayer() {
    this.player = new Player(100, 10, "PlayerName", Team.BLUE, deathBall);
  }

  private void initGame() {
    this.sprites = new MyArrayList<Sprite>();

    this.deathBall = new DeathBall(300, 80);

    // Middle divider
    this.sprites.add(new Platform(500, 0, 1, 800, false));

    // Platform
    this.sprites.add(new Platform(500, 445, 1000, 100, true));
    this.sprites.add(new Platform(500, -100, 1000, 100, true));
    this.sprites.add(new Platform(1000, 0, 100, 600, true));
    this.sprites.add(new Platform(0, 0, 100, 600, true));
  }

  public void start() {
    executor.scheduleAtFixedRate(this::update, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }

  public void stop() {
    executor.shutdown();
  }

  public void update() {
    boolean[] keys = ClientController.keys;

    if (time == 0) {
      time = System.currentTimeMillis();
      return;
    }

    long ct = System.currentTimeMillis();
    float dt = (float) (ct - time) / 1000f;
    time = ct;

    player.update(sprites, dt, keys);
    deathBall.update(sprites, dt, keys);
    if (deathBall.redDeath()) {
      if (player.team == Team.RED) player.alive = false;
    }
    if (deathBall.blueDeath()) {
      if (player.team == Team.BLUE) player.alive = false;
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
