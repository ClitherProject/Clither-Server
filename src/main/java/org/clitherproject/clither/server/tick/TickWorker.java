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
