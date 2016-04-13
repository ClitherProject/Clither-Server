package org.clitherproject.clither.server;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.clitherproject.clither.api.entity.Snake;
import org.clitherproject.clither.server.net.packet.Packet;
import org.clitherproject.clither.server.world.PlayerImpl;

import com.google.common.collect.ImmutableSet;

public class PlayerList {

    private final ClitherServer server;
    private final Set<PlayerImpl> players = new HashSet<>();
    
    static Logger log = Logger.getGlobal();

    public PlayerList(ClitherServer server) {
        this.server = server;
    }

    public Collection<PlayerImpl> getAllPlayers() {
        return players;
    }

    public void addPlayer(PlayerImpl player) {
        log.info(player.getAddress().toString().split(":")[0]+" ("+player.getClientID()+") has connected to the server!");
        players.add(player);
    }

    public void removePlayer(PlayerImpl player) {
        log.info(player.getAddress().toString().split(":")[0]+" ("+player.getClientID()+") has disconnected from the server!");
        players.remove(player);
        if (player != null) {
            for (Snake snake : player.getSnakes()) {
                server.getWorld().removeEntity(null);
            }
        }
    }

    public void sendToAll(Packet packet, PlayerImpl... except) {
        Set<PlayerImpl> excludes = ImmutableSet.copyOf(except);

        getAllPlayers().stream().filter((p) -> !excludes.contains(p)).forEach((p) -> p.getConnection().sendPacket(packet));
    }
}
