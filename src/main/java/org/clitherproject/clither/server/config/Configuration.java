package org.clitherproject.clither.server.config;

import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Logger;

public class Configuration {

    static Logger log = Logger.getGlobal();
    
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
            log.info(ex.getMessage().toString());
            return new ClitherConfig();
        }
    }
    
}

