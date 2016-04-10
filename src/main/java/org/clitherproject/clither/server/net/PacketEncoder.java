package org.clitherproject.clither.server.net;

import org.clitherproject.clither.server.ClitherServer;
import org.clitherproject.clither.server.net.packet.Packet;
import org.clitherproject.clither.server.net.packet.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import java.nio.ByteOrder;
import java.util.List;

public class PacketEncoder extends MessageToMessageEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer().order(ByteOrder.LITTLE_ENDIAN);
        int packetId = PacketRegistry.CLIENTBOUND.getPacketId(packet.getClass());
        if (packetId == -1) {
            throw new IllegalArgumentException("Provided packet is not registered as a clientbound packet!");
        }

        buf.writeByte(packetId);
        packet.writeData(buf);
        out.add(new BinaryWebSocketFrame(buf));

        ClitherServer.log.finest("Sent packet ID " + packetId + " (" + packet.getClass().getSimpleName() + ") to " + ctx.channel().remoteAddress());
    }

}

