package org.clitherproject.clither.server.world;

import java.awt.Color;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import org.clitherproject.clither.api.Clither;
import org.clitherproject.clither.api.entity.Snake;
import org.clitherproject.clither.server.net.PlayerConnection;
import org.clitherproject.clither.server.net.packet.universal.PacketOMPMessage;

import com.google.common.collect.ImmutableSet;

import io.netty.channel.Channel;

public class PlayerImpl {
    private final PlayerConnection playerConnection;
    private final Set<Snake> snakes = new HashSet<>();
    private final PlayerTracker tracker;
    private String name;
    private boolean ompCapable;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock snakeRead = lock.readLock();
    static Logger log = Logger.getGlobal();
    private Color snakesColor;
    
    public PlayerImpl(Channel channel) {
        this.playerConnection = new PlayerConnection(this, channel);
        this.tracker = new PlayerTracker(this);
        log.info(getAddress().toString().split(":")[0]+" (" + getClientID() + ") has connected to the server!");
    }
    
    public SocketAddress getAddress() {
        return this.playerConnection.getRemoteAddress();
    }

    public PlayerConnection getConnection() {
        return this.playerConnection;
    }

    public void addSnake(Snake snake) {
        snakes.add(snake);
        tracker.updateView();
        tracker.updateNodes();
        // playerConnection.sendPacket(new PacketOutAddNode(cell.getID()));
    }

    public void removeSnake(Snake snake) {
        snakes.remove(snake);
        tracker.updateView();
        tracker.updateNodes();
    }

    public void removeSnake(int entityId) {
        Iterator<Snake> it = snakes.iterator();
        while (it.hasNext()) {
            if (it.next().getID() == entityId) {
                it.remove();
            }
        }
    }
    
    public int getSnakeIdAt(int index) {
        int i = 0;
        snakeRead.lock();
        try{
                Iterator<Snake> it = snakes.iterator();
                while (it.hasNext()) {
                        if(i == index)
                        {
                                i = it.next().getID();
                        break;
                        }
                        i++;
                    }
                
                        return i;
        }
                finally{
                        snakeRead.unlock();
                }
    }
    
    public double getTotalMass()
    {
        double totalMass = 0.0D;
        for (Snake snake : getSnakes()) {
            totalMass += snake.getMass();
        }
        return totalMass;
    }

    public Collection<Snake> getSnakes() {
        return ImmutableSet.copyOf(snakes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerTracker getTracker() {
        return tracker;
    }

    public boolean isPluginMessageCapable() {
        return ompCapable;
    }

    public void setOMPCapable(boolean ompCapable) {
        this.ompCapable = ompCapable;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.playerConnection);
        return hash;
    }
    
    public String getClientID(){
        return getAddress().toString().split(":")[1];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlayerImpl other = (PlayerImpl) obj;
        if (!Objects.equals(this.playerConnection, other.playerConnection)) {
            return false;
        }
        return true;
    }

    public boolean sendPluginMessage(String channel, byte[] data) {
        if (!isPluginMessageCapable()) {
            return false;
        }

        if (channel.toUpperCase().startsWith("OMP|") || channel.toUpperCase().startsWith("O2|")) {
            throw new IllegalArgumentException("Attempted to send a message on reserved channel \"" + channel + "\"!");
        }

        if (!Clither.getMessenger().isChannelRegistered(channel)) {
            throw new IllegalStateException("Attempted to send a message on channel \"" + channel + "\", but the channel was not registered!");
        }

        PacketOMPMessage packet = new PacketOMPMessage(channel, data);
        playerConnection.sendPacket(packet);
        return true;
    }
    
    public void setSnakesColor(Color color)
    {
    	snakesColor = color;
    }

    public Color getSnakesColor()
    {
    	return snakesColor;
    }
}
