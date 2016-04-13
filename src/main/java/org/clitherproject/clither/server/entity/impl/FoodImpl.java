package org.clitherproject.clither.server.entity.impl;

import org.clitherproject.clither.api.entity.EntityType;
import org.clitherproject.clither.api.entity.Food;
import org.clitherproject.clither.api.world.Position;
import org.clitherproject.clither.server.entity.EntityImpl;
import org.clitherproject.clither.server.world.WorldImpl;

public class FoodImpl extends EntityImpl implements Food {

    public FoodImpl(WorldImpl world, Position position) {
        super(EntityType.FOOD, world, position);
        this.mass = 1;
    }

    public boolean shouldUpdate() {
        return true;
    }

    public void tick() {
    }

}
