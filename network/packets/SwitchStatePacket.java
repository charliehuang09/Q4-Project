package network.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import network.Packet;

public class SwitchStatePacket extends Packet {
  public String newState;

  public SwitchStatePacket() {}

  public SwitchStatePacket(String newState) {
    this.newState = newState;
  }

  @Override
  public byte getId() {
    return 0x5;
  }

  @Override
  public void read(DataInput in) throws IOException {
    this.newState = in.readUTF();
  }

  @Override
  public void writeBody(DataOutput out) throws IOException {
    out.writeUTF(newState);
  }
}
