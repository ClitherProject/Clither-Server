package org.clitherproject.clither.server.tick;

import org.clitherproject.clither.server.net.packet.Packet;
import org.clitherproject.clither.server.world.PlayerImpl;

public class TickablePacketSend implements Tickable {
    private final PlayerImpl player;
    private final Packet packet;
    private boolean sent = false;

    public TickablePacketSend(PlayerImpl player, Packet packet) {
        this.player = player;
        this.packet = packet;
    }

    @Override
    public void tick() {
        if (!sent) {
            player.getConnection().sendPacket(packet);
            sent = true;
        }
    }
}
