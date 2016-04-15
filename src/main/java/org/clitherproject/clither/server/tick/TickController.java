package org.clitherproject.clither.server;

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
