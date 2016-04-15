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

    /*
    function InitialPacket() {
    this.unknown1 = 0;
    this.unknown2 = 0;
    this.packetType = consts.packetTypes.a;
    this.gameRadius =  216000; //21600; for test changed
    this.c = 411;
    this.sector_size = 480;
    this.sector_count_along_edge = 130;
    this.spangdv = 4.8 * 10;
    //nsp is propably the nood speed
    this.nsp1 = 4.25 * 100;
    this.nsp2 = 0.5 * 100;
    this.nsp3 = 12 * 100;
    this.mamu = 0.033 * 1E3;
    this.mamu2 = 0.028 * 1E3;
    this.cst = 0.43 * 1E3;
    this.protocol_version = 6;
}

InitialPacket.prototype.toBuffer = function() {
        var arr = new Uint8Array(26)
        var b = 0;
        b += msgUtil.writeInt8(b, arr, 0);
        b += msgUtil.writeInt8(b, arr, 0);
        b += msgUtil.writeInt8(b, arr, this.packetType);
        b += msgUtil.writeInt24(b, arr, this.gameRadius);
        b += msgUtil.writeInt16(b, arr, this.c);
        b += msgUtil.writeInt16(b, arr, this.sector_size);
        b += msgUtil.writeInt16(b, arr, this.sector_count_along_edge);
        b += msgUtil.writeInt8(b, arr, this.spangdv);
        b += msgUtil.writeInt16(b, arr, this.nsp1);
        b += msgUtil.writeInt16(b, arr, this.nsp2);
        b += msgUtil.writeInt16(b, arr, this.nsp3);
        b += msgUtil.writeInt16(b, arr, this.mamu);
        b += msgUtil.writeInt16(b, arr, this.mamu2);
        b += msgUtil.writeInt16(b, arr, this.cst);
        b += msgUtil.writeInt8(b, arr, this.protocol_version);
        return arr;
        }
     */
}
