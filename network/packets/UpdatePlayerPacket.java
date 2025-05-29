package network.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import network.Packet;

public class UpdatePlayerPacket extends Packet {
  public int playerId;
  public float x, y;
  public boolean tethering;
  public boolean alive;

  // for registerPacket compatibility
  public UpdatePlayerPacket() {}

  public UpdatePlayerPacket(int playerId, float x, float y, boolean tethering, boolean alive) {
    this.playerId = playerId;
    this.x = x;
    this.y = y;
    this.tethering = tethering;
    this.alive = alive;
  }

  @Override
  public byte getId() {
    return 0x2;
  }

  @Override
  public void read(DataInput in) throws IOException {
    this.playerId = in.readInt();
    this.x = in.readFloat();
    this.y = in.readFloat();
    this.tethering = in.readBoolean();
    this.alive = in.readBoolean();
  }

  @Override
  public void writeBody(DataOutput out) throws IOException {
    out.writeInt(playerId);
    out.writeFloat(x);
    out.writeFloat(y);
    out.writeBoolean(tethering);
    out.writeBoolean(alive);
  }
}
