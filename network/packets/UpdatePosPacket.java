package network.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import network.Packet;

public class UpdatePosPacket extends Packet {
    private float x, y;

    public UpdatePosPacket(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public byte getId() {
        return 0x1;
    }

    @Override
    public void read(DataInput in) throws IOException {
        this.x = in.readFloat();
        this.y = in.readFloat();
    }

    @Override
    public void writeBody(DataOutput out) throws IOException {
        out.writeFloat(x);
        out.writeFloat(y);
    }
}
