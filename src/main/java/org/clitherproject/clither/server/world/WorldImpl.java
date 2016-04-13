package org.clitherproject.clither.server.world;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.clitherproject.clither.api.SnakeOwner;
import org.clitherproject.clither.api.entity.Entity;
import org.clitherproject.clither.api.entity.EntityType;
import org.clitherproject.clither.api.entity.Snake;
import org.clitherproject.clither.api.world.Position;
import org.clitherproject.clither.api.world.World;
import org.clitherproject.clither.server.ClitherServer;
import org.clitherproject.clither.server.config.ClitherConfig;
import org.clitherproject.clither.server.entity.EntityImpl;
import org.clitherproject.clither.server.entity.impl.FoodImpl;
import org.clitherproject.clither.server.entity.impl.MassImpl;
import org.clitherproject.clither.server.entity.impl.SnakeImpl;
import org.clitherproject.clither.server.tick.Tickable;

import com.google.common.collect.ImmutableList;

public class WorldImpl implements World {

    private final Random random = new Random(System.nanoTime());
    private final ClitherServer server;
    private final TIntObjectMap<EntityImpl> entities = new TIntObjectHashMap<>();
    private final int[] entityCounts = new int[EntityType.values().length];
    private int totalEntities = 0;
    private final Border border;
    private final View view;

    public WorldImpl(ClitherServer server) {
        this.server = server;
        this.border = new Border(server.getConfig());
        this.view = new View(server.getConfig());

        for (int i = 0; i < server.getConfig().world.food.startAmount; i++) {
            spawnEntity(EntityType.FOOD);
        }
    }

    @Override
    public EntityImpl spawnEntity(EntityType type) {
        return spawnEntity(type, getRandomPosition());
    }

    @Override
    public EntityImpl spawnEntity(EntityType type, Position position) {
        return spawnEntity(type, position, null);
    }

    public SnakeImpl spawnPlayerSnake(PlayerImpl player) {
        return spawnPlayerSnake(player, getRandomPosition());
    }

    public SnakeImpl spawnPlayerSnake(PlayerImpl player, Position position) {
        return (SnakeImpl) spawnEntity(EntityType.SNAKE, position);
    }

    private EntityImpl spawnEntity(EntityType type, Position position, SnakeOwner owner) {
        if (type == null || position == null) {
            return null;
        }

        EntityImpl entity;
        switch (type) {
            case SNAKE:
                if (owner == null) {
                    throw new IllegalArgumentException("Cell entities must have an owner");
                }

                entity = new SnakeImpl(owner, this, position);
                break;
            case FOOD:
                entity = new FoodImpl(this, position);
                break;
            case MASS:
                entity = new MassImpl(this, position);
                break;
            default:
                throw new IllegalArgumentException("Unsupported entity type: " + type);
        }

        entities.put(entity.getID(), entity);
        entityCounts[type.ordinal()]++;
        totalEntities++;
        return entity;
    }

    public void removeEntity(Entity entity) {
        removeEntity(entity.getID());
    }

    @Override
    public void removeEntity(int id) {
        if (!entities.containsKey(id)) {
            throw new IllegalArgumentException("Entity with the specified ID does not exist in the world!");
        }

        EntityImpl entity = entities.remove(id);
        entity.onRemove();
        entityCounts[entity.getType().ordinal()]--;
        totalEntities--;

        // TODO: Limit to viewbox?
        server.getPlayerList().getAllPlayers().stream().map(PlayerImpl::getTracker).forEach((t) -> t.remove(entity));
    }

    @Override
    public EntityImpl getEntity(int id) {
        return entities.get(id);
    }

    public List<EntityImpl> getRawEntities() {
        return ImmutableList.copyOf(entities.valueCollection());
    }

    @Override
    public Collection<Entity> getEntities() {
        return ImmutableList.copyOf(entities.valueCollection());
    }

    public int getSnakeCount() {
        return entityCounts[EntityType.SNAKE.ordinal()];
    }

    public int getFoodCount() {
        return entityCounts[EntityType.FOOD.ordinal()];
    }

    public int getMassCount() {
        return entityCounts[EntityType.MASS.ordinal()];
    }

    public Border getBorder() {
        return border;
    }

    public View getView() {
        return view;
    }

    @Override
    public ClitherServer getServer() {
        return server;
    }

    public Position getRandomPosition() {
        return new Position((random.nextDouble() * (Math.abs(border.left) + Math.abs(border.right))),
                (random.nextDouble() * (Math.abs(border.top) + Math.abs(border.bottom))));
    }

    private void spawnFood() {
        int spawnedFood = 0;
        while (getFoodCount() < server.getConfig().world.food.maxAmount && spawnedFood < server.getConfig().world.food.spawnPerInterval) {
            spawnEntity(EntityType.FOOD);
            spawnedFood++;
        }
    }

    public void tick(Consumer<Tickable> serverTick) {
        if (server.getTick() % server.getConfig().world.food.spawnInterval == 0) {
            spawnFood();
        }

        for (EntityImpl entity : entities.valueCollection()) {
            serverTick.accept(entity);
        }
    }

    public static class View {

        private final double baseX;
        private final double baseY;

        public View(ClitherConfig config) {
            this.baseX = config.world.view.baseX;
            this.baseY = config.world.view.baseY;
        }

        public View(double baseX, double baseY) {
            this.baseX = baseX;
            this.baseY = baseY;
        }

        public double getBaseX() {
            return baseX;
        }

        public double getBaseY() {
            return baseY;
        }
    }

    public static class Border {

        private final double left;
        private final double top;
        private final double right;
        private final double bottom;

        public Border(ClitherConfig config) {
            this.left = config.world.border.left;
            this.top = config.world.border.top;
            this.right = config.world.border.right;
            this.bottom = config.world.border.bottom;
        }

        public Border(double left, double top, double right, double bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public double getLeft() {
            return left;
        }

        public double getTop() {
            return top;
        }

        public double getRight() {
            return right;
        }

        public double getBottom() {
            return bottom;
        }
    }

}
