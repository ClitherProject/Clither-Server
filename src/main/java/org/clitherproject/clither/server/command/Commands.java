package org.clitherproject.clither.server.command;

import java.util.Collection;
import java.util.logging.Logger;

import org.clitherproject.clither.server.ClitherServer;
import org.clitherproject.clither.server.world.PlayerImpl;

public class Commands {

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
            	Logger.getGlobal().info("IP: "+ob.getConnection().getRemoteAddress().toString().split(":")[0].split("/")[1]+" - Client ID: "+ob.getConnection().getRemoteAddress().toString().split(":")[1]+" - Name: "+ob.getName());
            }
            break;
        case "stop":
            ClitherServer.getInstance().shutdown();
            break;
		
        }
	}
}
