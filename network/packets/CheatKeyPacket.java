package network.packets;

import java.io.DataInput;
import java.io.IOException;

import network.Packet;

public class CheatKeyPacket extends Packet {
    public CheatKeyPacket() {}

    @Override
    public byte getId() {
        return 0xd;
    }

    @Override
    public void read(DataInput in) throws IOException {}
}
