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
package org.clitherproject.clither.server.config;

public class ClitherConfig {

    public Server server = new Server();
    public World world = new World();
    public Player player = new Player();

    public static class Server {

        public int port = 443;
        public int maxConnections = 100;
        public String ip = "localhost";
        public String name = "Unknown Server";
        
    }

    public static class World {

        public View view = new View();
        public Border border = new Border();
        public Food food = new Food();

        public static class View {

            public double baseX = 1024;
            public double baseY = 592;
        }

        public static class Border {

            public double left = 0;
            public double right = 6000;
            public double top = 0;
            public double bottom = 6000;
        }

        public static class Food {

            public int spawnInterval = 20; // In ticks
            public int spawnPerInterval = 10; // How many food to spawn per interval
            public int startAmount = 100; // The amount of food to start the world with
            public int maxAmount = 500; // The maximum amount of food in the world at once
            public int foodSize = 1; // The size of food spawned on the map
            
        }
        
    }

    public static class Player {

        public int startMass = 35;
        public int maxMass = 255000;
        public int maxNickLength = 15;
    }
}

