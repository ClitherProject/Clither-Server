package org.clitherproject.clither.server.net.packet;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import org.clitherproject.clither.server.net.packet.inbound.*;
import org.clitherproject.clither.server.net.packet.outbound.*;
// import org.clitherproject.clither.server.net.packet.universal.*;

public class PacketRegistry {

    public static final ProtocolDirection CLIENTBOUND = new ProtocolDirection("CLIENTBOUND");
    public static final ProtocolDirection SERVERBOUND = new ProtocolDirection("SERVERBOUND");

    static {
        // Clientbound packets
        CLIENTBOUND.registerPacket(1, PacketOutSetUsername.class);

        // Serverbound packets
        SERVERBOUND.registerPacket(1, PacketInG.class);
        SERVERBOUND.registerPacket(2, PacketInNewSnake.class);
        SERVERBOUND.registerPacket(3, PacketInPong.class);
        SERVERBOUND.registerPacket(4, PacketInSpawnFood.class);
        SERVERBOUND.registerPacket(5, PacketInGlobalHighscore.class);
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
                throw new IllegalArgumentException("Packet" + " is already registered for " + this + "!");
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

