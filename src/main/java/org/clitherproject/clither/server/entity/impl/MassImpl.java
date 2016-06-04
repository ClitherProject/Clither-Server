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
