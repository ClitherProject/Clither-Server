package org.clitherproject.clither.server.tick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Porama2 on 15/4/2016.
 */
public class TickWorker extends Thread {
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
                e.printStackTrace();
            }
        }
        works.clear();
    }
}
