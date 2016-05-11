package org.clitherproject.clither.server.net.packet.inbound;

import io.netty.buffer.ByteBuf;
import org.clitherproject.clither.server.net.packet.Packet;

public class PacketInNewSnake extends Packet {
    int U1 = 0; //UNKNOWN
    int U2 = 0; //UNKNOWN
    int packetType = -1;
    
	@Override
	public void writeData(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void readData(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

}
