package org.clitherproject.clither.server.entity.impl;

import org.clitherproject.clither.api.entity.EntityType;
import org.clitherproject.clither.api.world.Position;
import org.clitherproject.clither.server.entity.EntityImpl;
import org.clitherproject.clither.server.world.WorldImpl;

public class MassImpl extends EntityImpl {

    public MassImpl(WorldImpl world, Position position) {
        super(EntityType.MASS, world, position);
    }

    @Override
    public boolean shouldUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tick() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }
}
