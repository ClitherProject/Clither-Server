/**
 * This file is part of Clither.
 *
 * Clither is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Clither is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Clither.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    @SuppressWarnings("unused")
    public void removePlayer(PlayerImpl player) {
        if (player != null) {
            log.info(player.getAddress().toString().split(":")[0]+" ("+player.getClientID()+") has disconnected from the server!");
            players.remove(player);
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
