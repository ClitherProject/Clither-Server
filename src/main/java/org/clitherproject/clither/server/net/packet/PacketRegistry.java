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
package org.clitherproject.clither.server.net.packet;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.clitherproject.clither.server.net.packet.inbound.*;
import org.clitherproject.clither.server.net.packet.outbound.*;
import org.clitherproject.clither.server.net.packet.universal.*;

public class PacketRegistry {

    public static final ProtocolDirection CLIENTBOUND = new ProtocolDirection("CLIENTBOUND");
    public static final ProtocolDirection SERVERBOUND = new ProtocolDirection("SERVERBOUND");

    static {
        // Clientbound packets
    	// TODO: Register Clientbound packets..

        // Serverbound packets
    	// TODO: Register Serverbound packets..
    }

    // Static-use class
    private PacketRegistry() {
    }

    public static class ProtocolDirection {

        private final TIntObjectMap<Class<? extends Packet>> packetClasses = new TIntObjectHashMap<>(10, 0.5F);
        private final TObjectIntMap<Class<? extends Packet>> reverseMapping = new TObjectIntHashMap<>(10, 0.5F, -1);
        private final String name;

        private ProtocolDirection(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private void registerPacket(int packetId, Class<? extends Packet> clazz) {
            if (packetClasses.containsKey(packetId)) {
                throw new IllegalArgumentException("Packet with ID " + packetId + " is already registered for " + this + "!");
            }

            if (reverseMapping.containsKey(clazz)) {
                throw new IllegalArgumentException("Packet with class " + clazz + " is already registered for " + this + "!");
            }

            packetClasses.put(packetId, clazz);
            reverseMapping.put(clazz, packetId);
        }

        public int getPacketId(Class<? extends Packet> clazz) {
            return reverseMapping.get(clazz);
        }

        public Class<? extends Packet> getPacketClass(int packetId) {
            return packetClasses.get(packetId);
        }

        public Packet constructPacket(int packetId) {
            Class<? extends Packet> clazz = getPacketClass(packetId);
            if (clazz == null) {
                return null;
            }

            try {
                return clazz.newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public String toString() {
            return "ProtocolDirection{" + "name=" + name + '}';
        }
    }
}

