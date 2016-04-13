package org.clitherproject.clither.server.net.packet.inbound;

import org.clitherproject.clither.server.net.packet.Packet;
import org.clitherproject.clither.server.net.throwable.WrongDirectionException;

import io.netty.buffer.ByteBuf;

public class PacketInInitial extends Packet {
    public int protocolVersion;

	@Override
	public void writeData(ByteBuf buf) {
        throw new WrongDirectionException();
		
	}

	@Override
	public void readData(ByteBuf buf) {
        this.protocolVersion = buf.readInt();
		
	}

}
