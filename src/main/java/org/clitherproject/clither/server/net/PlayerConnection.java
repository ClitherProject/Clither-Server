package org.clitherproject.clither.server.net;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import org.clitherproject.clither.server.net.packet.Packet;
import org.clitherproject.clither.server.net.packet.inbound.PacketInInitial;
import org.clitherproject.clither.server.net.packet.outbound.PacketOutSetUsername;
import org.clitherproject.clither.server.net.packet.outbound.PacketOutUpdateOwnSnake;
import org.clitherproject.clither.server.net.throwable.UnhandledPacketException;
import org.clitherproject.clither.server.world.PlayerImpl;

import com.google.common.base.Preconditions;

import io.netty.channel.Channel;

public class PlayerConnection {
    static Logger log = Logger.getGlobal();
    private final PlayerImpl player;
    private final Channel channel;
    private final Map<Integer, MousePosition> cellMousePositions = new HashMap<>();
    private boolean individualMovementEnabled = false;
    private MousePosition globalMousePosition;
    private ConnectionState state = ConnectionState.AUTHENTICATE;
    private int protocolVersion;
    private String authToken;

    public PlayerConnection(PlayerImpl player, Channel channel) {
        this.player = player;
        this.channel = channel;
    }

    public SocketAddress getRemoteAddress() {
        return channel.remoteAddress();
    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

    public void handle(Packet packet) {
        if (packet instanceof PacketOutSetUsername) {
            handle((PacketOutSetUsername) packet);
        } else if (packet instanceof PacketOutUpdateOwnSnake) {
            handle((PacketOutUpdateOwnSnake) packet);
        } else if (packet instanceof PacketInInitial) {
            handle((PacketInInitial) packet);
        } else {
            throw new UnhandledPacketException("Unhandled packet: " + packet);
        }
    }
    
    public void handle(PacketInInitial packet) {
        checkConnected();
        Preconditions.checkState(state == ConnectionState.AUTHENTICATE, "Not expecting AUTHENTICATE");
        state = ConnectionState.RESET;
        protocolVersion = packet.protocolVersion;
    }
    
    public void handle(PacketOutSetUsername packet) {
        checkConnected();
        state = ConnectionState.CONNECTED;
    }
    
    public void handle(PacketOutUpdateOwnSnake packet) {
        checkConnected();
    }
    
    public boolean isIndividualMovementEnabled() {
        return individualMovementEnabled;
    }

    public MousePosition getGlobalMousePosition() {
        return globalMousePosition;
    }

    public MousePosition getCellMousePosition(int id) {
        return cellMousePositions.get(id);
    }

    private void checkConnected() {
        Preconditions.checkState(state == ConnectionState.CONNECTED, "Connection is not in CONNECTED state!");
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.channel);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlayerConnection other = (PlayerConnection) obj;
        if (!Objects.equals(this.channel, other.channel)) {
            return false;
        }
        return true;
    }

    private static enum ConnectionState {

        AUTHENTICATE, RESET, TOKEN, CONNECTED;
    }

    public static class MousePosition {

        private final double x;
        private final double y;

        public MousePosition(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}
