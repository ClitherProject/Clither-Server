/**
 * This file is part of Clither.
 *
 * Cliter is free software: you can redistribute it and/or modify
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
 * along with Ogar.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.clitherproject.clither.server.gui;

import org.clitherproject.clither.server.ClitherServer;
import java.io.IOException;
import jline.TerminalFactory;
import jline.console.ConsoleReader;

public class ServerCLI implements Runnable {

    private final ClitherServer server;

    public ServerCLI(ClitherServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            ConsoleReader console = new ConsoleReader();
            console.setPrompt("> ");
            String line = null;
            while ((line = console.readLine()) != null) {
                server.handleCommand(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                TerminalFactory.get().restore();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
