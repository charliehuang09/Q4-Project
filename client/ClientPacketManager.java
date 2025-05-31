package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import network.Packet;
import network.PacketManager;
import network.packets.*;

public class ClientPacketManager extends PacketManager {
  protected Socket socket;
  protected DataInputStream in;
  protected DataOutputStream out;

  private ExecutorService receivingExecutor;
  private ScheduledExecutorService sendingExecutor;

  private ClientController controller;

  public ClientPacketManager() {
    registerPacket(SwitchStatePacket.class);
    registerPacket(SetTeamPacket.class);
    registerPacket(UpdatePlayerPacket.class);
    registerPacket(SetPlayerIdPacket.class);
    registerPacket(AddPlayerPacket.class);
    registerPacket(RemovePlayerPacket.class);
    registerPacket(UpdateDeathBallPacket.class);
    registerPacket(ResetPacket.class);
  }

  @Override
  public void onReceive(Packet packet) {
    ClientController.dprintln("[client:network] Received " + packet.getId());

    if (packet instanceof UpdatePlayerPacket upp) {
      controller.updatePlayer(upp.playerId, upp.x, upp.y, upp.tethering, upp.alive);
    } else if (packet instanceof SwitchStatePacket ssp) {
      controller.setCurrentState(ClientController.GameState.valueOf(ssp.newState));
    } else if (packet instanceof SetTeamPacket stp) {
      controller.selectTeam(stp.playerId, stp.team);
    } else if (packet instanceof SetPlayerIdPacket sip) {
      controller.setId(sip.playerId);
    } else if (packet instanceof AddPlayerPacket app) {
      controller.addPlayer(app.playerId, app.name, app.team);
    } else if (packet instanceof RemovePlayerPacket rpp) {
      controller.removePlayer(rpp.playerId);
    } else if (packet instanceof UpdateDeathBallPacket udp) {
      controller.updateDeathBall(udp.x, udp.y, udp.x_vel, udp.y_vel);
    } else if (packet instanceof ResetPacket) {
      controller.resetGame();
    } else {
      System.out.println("[client:controller] Unknown packet type: " + packet.getClass().getName());
    }
  }

  public void setController(ClientController controller) {
    this.controller = controller;
  }

  public void sendPacket(Packet packet) {
    try {
      super.sendPacket(packet, out);
    } catch (Exception e) {
      System.out.println("[client:network] Failed to send packet, disconnecting: " + e.getMessage());
      disconnect();
    }
  }

  public void connect(String host, int port) throws IOException {
    System.out.println("[client:network] Attempting to connect to " + host + ":" + port);
    socket = new Socket(host, port);

    in = new DataInputStream(socket.getInputStream());
    out = new DataOutputStream(socket.getOutputStream());

    startReceiving();
  }

  public void disconnect() {
    try {
      System.out.println("[client:network] Disconnecting...");
      controller.stopGame();
      controller.setCurrentState(ClientController.GameState.MENU);
      socket.close();
      receivingExecutor.shutdownNow();
      sendingExecutor.shutdownNow();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void startReceiving() {
    receivingExecutor = Executors.newFixedThreadPool(1);
    receivingExecutor.submit(() -> {
      try {
        while (true) {
          ClientController.dprintln("[client:network] Waiting for packet...");
          Packet packet = receivePacket(in);
          onReceive(packet);
        }
      } catch (IOException e) {
        System.out.println("[client:network] Connection closed by server: " + e.getMessage());
        disconnect();
      }
    });
  }

  public void startSending(ClientGame game) {
    sendingExecutor = Executors.newScheduledThreadPool(1);
    sendingExecutor.scheduleAtFixedRate(() -> {
      UpdatePlayerPacket upp = new UpdatePlayerPacket(controller.id, game.player.body.state.x, game.player.body.state.y,
          game.player.graple.active, game.player.alive);
      sendPacket(upp);
    }, 0, 1000 / 60, TimeUnit.MILLISECONDS);
  }

  public void stopSending() {
    if (sendingExecutor != null && !sendingExecutor.isShutdown()) {
      System.out.println("[client:network] Stopping sending executor...");
      sendingExecutor.shutdownNow();
    }
  }
}