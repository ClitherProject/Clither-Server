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
package org.clitherproject.clither.server.command;

import java.util.Collection;
import java.util.logging.Logger;

import org.clitherproject.clither.server.ClitherServer;
import org.clitherproject.clither.server.world.PlayerImpl;

@SuppressWarnings("unused")
public class Commands {

    private Commands() {}

	public static void onCommand(String s) {
        switch (s.toLowerCase().split(" ")[0]) {
        case "help":
            Logger.getGlobal().info("Command listing for Clither:");
            Logger.getGlobal().info("\thelp\t\t");
            Logger.getGlobal().info("\tstop\t\t");
            Logger.getGlobal().info("Command listing for Clither:");
            Logger.getGlobal().info("\tlist\t\t");
            break;
        case "list":
        	Logger.getGlobal().info("Currently connected players ("+ClitherServer.getInstance().getPlayerList().getAllPlayers().toArray().length+"):");
            Collection<PlayerImpl> players = ClitherServer.getInstance().getPlayerList().getAllPlayers();
            for (PlayerImpl ob : players){

                //TODO FIX THIS AFTER NETWORK WORK :P
                //Logger.getGlobal().info("IP: "+ob.getConnection().getRemoteAddress().toString().split(":")[0].split("/")[1]+" - Client ID: "+ob.getConnection().getRemoteAddress().toString().split(":")[1]+" - Name: "+ob.getName());
            }
            break;
        case "stop":
            ClitherServer.getInstance().shutdown();
            break;
        default:
            break;
        }
	}
}
