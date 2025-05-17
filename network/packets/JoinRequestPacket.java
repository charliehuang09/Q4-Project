package network.packets;

import network.Packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class JoinRequestPacket extends Packet {
  // Note: Currently unused

  public String clientName;

  public JoinRequestPacket() {
    this.clientName = "";
  }

  public JoinRequestPacket(String clientName) {
    this.clientName = clientName;
  }

  @Override
  public byte getId() {
    return 0x1; // Unique ID for JoinRequestPacket
  }

  @Override
  public void read(DataInput input) throws IOException {
    clientName = input.readUTF();
  }

  @Override
  public void write(DataOutput output) throws IOException {
    output.writeUTF(clientName);
  }
}
