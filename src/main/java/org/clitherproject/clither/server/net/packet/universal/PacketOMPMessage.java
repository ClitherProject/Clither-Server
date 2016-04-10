package org.clitherproject.clither.server.net.packet.universal;

import org.clitherproject.clither.server.net.packet.Packet;
import org.clitherproject.clither.server.net.throwable.MalformedPacketException;
import io.netty.buffer.ByteBuf;

public class PacketOMPMessage extends Packet {

    public String channel;
    public byte[] data;

    public PacketOMPMessage() {
    }

    public PacketOMPMessage(String channel) {
        this.channel = channel;
        this.data = new byte[0];
    }

    public PacketOMPMessage(String channel, byte[] data) {
        this.channel = channel;
        this.data = data;
    }

    @Override
    public void writeData(ByteBuf buf) {
        if (data.length > 32767) {
            throw new MalformedPacketException("Exceeded max OMP message size (" + data.length + " > 32767)");
        }

        writeUTF8(buf, channel);
        buf.writeBytes(data);
    }

    @Override
    public void readData(ByteBuf buf) {
        channel = readUTF8(buf);
        int size = buf.readableBytes();
        if (size > 32767) {
            throw new MalformedPacketException("Exceeded max OMP message size (" + size + " > 32767)");
        }

        data = new byte[size];
        buf.readBytes(data);
    }
}

