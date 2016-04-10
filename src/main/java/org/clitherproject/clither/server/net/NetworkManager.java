package org.clitherproject.clither.server.net;

import java.io.IOException;

import org.clitherproject.clither.server.ClitherServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;

public class NetworkManager {

    private final ClitherServer server;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;

    public NetworkManager(ClitherServer server) {
        this.server = server;
    }

    public void start() throws IOException, InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler()).childHandler(new ClientInitializer(server));

        channel = b.bind(server.getConfig().server.port).sync().channel();

        ClitherServer.log.info("Server successfully started on port " + server.getConfig().server.port + ".");
    }

    public boolean shutdown() {
        if (channel == null) {
            return false;
        }

        try {
            channel.close().sync();
            return true;
        } catch (InterruptedException ex) {
            return false;
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static class ClientInitializer extends ChannelInitializer<SocketChannel> {

        private final ClitherServer server;

        public ClientInitializer(ClitherServer server) {
            this.server = server;
        }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            try {
                ch.config().setOption(ChannelOption.IP_TOS, 0x18);
            } catch (ChannelException ex) {
                // IP_TOS not supported by platform, ignore
            }
            ch.config().setAllocator(PooledByteBufAllocator.DEFAULT);

            ch.pipeline().addLast(new HttpServerCodec());
            ch.pipeline().addLast(new HttpObjectAggregator(65536));
            ch.pipeline().addLast(new WebSocketHandler());
            ch.pipeline().addLast(new PacketDecoder());
            ch.pipeline().addLast(new PacketEncoder());
            ch.pipeline().addLast(new ClientHandler(server));
        }
    }
}

