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
