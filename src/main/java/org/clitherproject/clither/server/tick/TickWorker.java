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
package org.clitherproject.clither.server.tick;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Porama2 on 15/4/2016.
 */
public class TickWorker extends Thread {
    public static final Logger log = Logger.getGlobal();
    List<Tickable> works = new ArrayList<>();

    public void add(Tickable work) {
        works.add(work);
    }

    public void Update() {
        this.start();
    }

    @Override
    public void run() {
        for (Tickable tickable : works) {
            try {
                tickable.tick();
            } catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        works.clear();
    }
}
