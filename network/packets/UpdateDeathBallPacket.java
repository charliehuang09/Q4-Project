package network.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import network.Packet;

public class UpdateDeathBallPacket extends Packet {
  public float x;
  public float y;
  public float x_vel;
  public float y_vel;

  public UpdateDeathBallPacket() {
  }

  public UpdateDeathBallPacket(float x, float y, float x_vel, float y_vel) {
    this.x = x;
    this.y = y;
    this.x_vel = x_vel;
    this.y_vel = y_vel;
  }

  @Override
  public byte getId() {
    return 0xa;
  }

  @Override
  public void read(DataInput in) throws IOException {
    x = in.readFloat();
    y = in.readFloat();
    x_vel = in.readFloat();
    y_vel = in.readFloat();
  }

  @Override
  public void writeBody(DataOutput out) throws IOException {
    out.writeFloat(x);
    out.writeFloat(y);
    out.writeFloat(x_vel);
    out.writeFloat(y_vel);
  }
}
