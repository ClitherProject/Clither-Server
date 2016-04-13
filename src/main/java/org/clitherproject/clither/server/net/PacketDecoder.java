package org.clitherproject.clither.server.net;

import org.clitherproject.clither.server.ClitherServer;
import org.clitherproject.clither.server.net.packet.Packet;
import org.clitherproject.clither.server.net.packet.PacketRegistry;
import org.clitherproject.clither.server.net.throwable.UnknownPacketException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.nio.ByteOrder;
import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<WebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        ByteBuf buf = frame.content().order(ByteOrder.LITTLE_ENDIAN);
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

        ClitherServer.log.finest("Received packet ID " + packetId + " (" + packet.getClass().getSimpleName() + ") from " + ctx.channel().remoteAddress());

        packet.readData(buf);
        out.add(packet);
    }

}

