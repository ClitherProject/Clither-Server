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
package org.clitherproject.clither.server;

import java.util.logging.Logger;

public class ClitherServer {
    private static ClitherServer instance;
    public static final Logger log = Logger.getGlobal();
    // private final String configurationFile = "server.properties";
    private final boolean debugMode = Boolean.getBoolean("debug");
    // private int tickThreads = Integer.getInteger("tickThreads", 1);
    private long tick = 0L;
    // private boolean running;
	
    public static void main(String[] args) throws Throwable {
        ClitherServer.instance = new ClitherServer();
    }
    
    public static ClitherServer getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return log;
    }

    public boolean isDebugging() {
        return debugMode;
    }

    /** public void loadConfig() {
        this.configuration = Configuration.load(configurationFile);
    } **/

    /** public void saveConfig() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("server.properties", "UTF-8");
        } catch (Exception ex) {
            log.severe("An internal error has occured whilist generating server.properties.");
            shutdown();
        }
        writer.println("name=Unknown Server");
        writer.println("ip=localhost");
        writer.println("port=443");
        writer.println("maxPlayers=20");
        writer.println("maxCells=20");
        writer.println("maxMass=85500");
        writer.println("minMassSplit=36");
        writer.println("minMassEject=24");
        writer.println("ejectedMassSize=16");
        writer.println("foodSize=1");
        writer.println("foodStartAmount=100");
        writer.println("virusStartAmount=10");
        writer.println("virusSize=50");
        writer.println("startMass=35");
        writer.println("recombineTime=5");
        writer.println("borderSize=6000");
        writer.close();
        loadConfig();
    } **/

    /** public OgarConfig getConfig() {
        return configuration;
    } **/

    public long getTick() {
        return tick;
    }

}
