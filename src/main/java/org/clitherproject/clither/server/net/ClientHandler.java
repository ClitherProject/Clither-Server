package org.clitherproject.clither.server.net;

import org.clitherproject.clither.api.entity.Player;
import org.clitherproject.clither.api.event.player.PlayerDisconnectEvent;
import org.clitherproject.clither.server.ClitherServer;
import org.clitherproject.clither.server.world.PlayerImpl;
import org.clitherproject.clither.server.net.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends SimpleChannelInboundHandler<Packet> {

    private final ClitherServer server;
    private PlayerImpl player;
    
    static Logger log = Logger.getGlobal();

    public ClientHandler(ClitherServer server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.player = new PlayerImpl(ctx.channel());
        server.getPlayerList().addPlayer(player);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        PlayerDisconnectEvent event = new PlayerDisconnectEvent((Player) player);
        server.getPluginManager().callEvent(event);
        server.getPlayerList().removePlayer(player);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        player.getConnection().handle(packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //server.getLogger().log(Level.SEVERE, "Encountered exception in pipeline for client at " + ctx.channel().remoteAddress() + "; disconnecting client.", cause);
        //ctx.channel().close();
    }
}

