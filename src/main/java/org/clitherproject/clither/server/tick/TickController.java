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

/**
 * Created by Porama2 on 15/4/2016.
 */
public class TickController {
    List<Tickable> works = new ArrayList<>();
    TickWorker[] workers = null;

    public TickController(int threads) {
        workers = new TickWorker[threads];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new TickWorker();
        }
    }

    public void Update() {
        int cworkers = 0;
        for (Tickable work : works) {
            cworkers++;
            if (cworkers > (workers.length - 1)) cworkers = 0;
            workers[cworkers].add(work);
        }
    }
}
