package org.clitherproject.clither.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.nio.ByteOrder;
import java.util.List;

import org.clitherproject.clither.server.ClitherServer;
import org.clitherproject.clither.server.net.packet.Packet;
import org.clitherproject.clither.server.net.packet.PacketRegistry;

public class PacketDecoder extends MessageToMessageDecoder<WebSocketFrame> {

	@SuppressWarnings("deprecation")
	@Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        ByteBuf buf = frame.content().order(ByteOrder.BIG_ENDIAN);
        if (buf.capacity() < 1) {
            // Discard empty messages
            return;
        }

        buf.resetReaderIndex();
        int packetId = buf.readUnsignedByte();
        Packet packet = PacketRegistry.SERVERBOUND.constructPacket(packetId);

        if (packet == null) {
            //throw new UnknownPacketException("Unknown packet ID: " + packetId);
            return;
        }

        ClitherServer.log.finest("Received packet " + " (" + packet.getClass().getSimpleName() + ") from " + ctx.channel().remoteAddress());

        packet.readData(buf);
        out.add(packet);
    }

}
