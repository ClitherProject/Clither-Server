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

import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Logger;

public class Configuration {

    static Logger log = Logger.getGlobal();

    private Configuration() {}
    
    public static ClitherConfig load(String file) {
        try(FileReader reader = new FileReader(file)) {
            Properties properties = new Properties();
            properties.load(reader);
            ClitherConfig conf = new ClitherConfig();
            conf.server.port = Integer.parseInt(properties.getProperty("port"));
            int borderSize = Integer.parseInt(properties.getProperty("borderSize"));
            conf.world.border.right = borderSize;
            conf.world.border.bottom = borderSize;
            conf.server.ip = properties.getProperty("ip");
            conf.player.maxMass = Integer.parseInt(properties.getProperty("maxMass"));
            conf.player.startMass = Integer.parseInt(properties.getProperty("startMass"));
            conf.server.maxConnections = Integer.parseInt(properties.getProperty("maxPlayers"));
            conf.world.food.foodSize = Integer.parseInt(properties.getProperty("foodSize"));
            conf.server.name = properties.getProperty("name");
            return conf;
        } catch (Exception ex) {
            log.info("An internal error has occured whilist reading configuration file!");
            log.info(ex.getMessage());
            return new ClitherConfig();
        }
    }
    
}

