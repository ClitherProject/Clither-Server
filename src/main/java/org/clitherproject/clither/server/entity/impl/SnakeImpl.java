package org.clitherproject.clither.server.entity.impl;

import java.util.ArrayList;
import java.util.List;

import org.clitherproject.clither.api.SnakeOwner;
import org.clitherproject.clither.api.entity.Entity;
import org.clitherproject.clither.api.entity.EntityType;
import org.clitherproject.clither.api.entity.Snake;
import org.clitherproject.clither.api.world.Position;
import org.clitherproject.clither.server.entity.EntityImpl;
import org.clitherproject.clither.server.net.PlayerConnection;
import org.clitherproject.clither.server.util.MathHelper;
import org.clitherproject.clither.server.world.PlayerImpl;
import org.clitherproject.clither.server.world.WorldImpl;

public class SnakeImpl extends EntityImpl implements Snake {

    private final SnakeOwner owner;
    private String name;

    public SnakeImpl(SnakeOwner owner, WorldImpl world, Position position) {
        super(EntityType.SNAKE, world, position);
        this.owner = owner;
        this.name = owner.getName();
    }

    @Override
    public boolean shouldUpdate() {
        // Cells should always update
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SnakeOwner getOwner() {
        return owner;
    }

    public double getSpeed() {
        return 30.0D * Math.pow(mass, -1.0D / 4.5D) * 50.0D / 40.0D;
    }

    @Override
    public void tick() {
        move();
        eat();
    }

    private void move() {
        if (!(owner instanceof PlayerImpl)) {
            return;
        }

        PlayerImpl player = (PlayerImpl) owner;

        PlayerConnection.MousePosition mouse = player.getConnection().getCellMousePosition(getID());
        if (mouse == null || !player.getConnection().isIndividualMovementEnabled()) {
            mouse = player.getConnection().getGlobalMousePosition();
            if (mouse == null) {
                return;
            }
        }

        // Get angle
        double deltaX = mouse.getX() - getX();
        double deltaY = mouse.getY() - getY();
        double angle = Math.atan2(deltaX, deltaY);

        if (Double.isNaN(angle)) {
            return;
        }

        // Distance between mouse pointer and cell
        double distance = position.distance(mouse.getX(), mouse.getY());
        double speed = Math.min(getSpeed(), distance);

        double x1 = getX() + (speed * Math.sin(angle));
        double y1 = getY() + (speed * Math.cos(angle));
        
        // TODO: Fire a move event here
        // Make sure we're not passing the world border
        if (x1 < world.getBorder().getLeft()) {
            x1 = world.getBorder().getLeft();
        }
        if (x1 > world.getBorder().getRight()) {
            x1 = world.getBorder().getRight();
        }
        if (y1 < world.getBorder().getTop()) {
            y1 = world.getBorder().getTop();
        }
        if (y1 > world.getBorder().getBottom()) {
            y1 = world.getBorder().getBottom();
        }

        setPosition(new Position(x1, y1));
    }

    private void eat() {
        List<EntityImpl> edibles = new ArrayList<>();
        int r = getPhysicalSize();

        double topY = getY() - r;
        double bottomY = getY() + r;
        double leftX = getX() - r;
        double rightX = getX() + r;

        for (Entity otherEntity : world.getEntities()) {
            EntityImpl other = (EntityImpl) otherEntity;
            if (other.equals(this)) {
                continue;
            }

            if (!other.collisionCheck(bottomY, topY, rightX, leftX)) {
                continue;
            }

            double multiplier = 1.25D;
            if (other instanceof FoodImpl) {
                edibles.add(other);
                continue;
            }

            // Is the other cell big enough to eat?
            if (other.getMass() * multiplier > mass) {
                continue;
            }

            // Eating range
            double dist = position.distance(other.getPosition());
            double eatingRange = getPhysicalSize() - (other.getPhysicalSize() * 0.4D);

            if (dist > eatingRange) {
                continue;
            }

            // Sweet, let's eat!
            edibles.add(other);
        }

        // Process the list of edibles
        for (EntityImpl entity : edibles) {
            this.addMass(entity.getMass());
            entity.kill(getID());
        }
    }

    @Override
    public void onRemove() {
        getOwner().removeSnake(this);
    }

    private boolean simpleCollide(Snake other, double collisionDist) {
        return MathHelper.fastAbs(getX() - other.getPosition().getX()) < (2 * collisionDist) && MathHelper.fastAbs(getY() - other.getPosition().getY()) < (2 * collisionDist);
    }
}
