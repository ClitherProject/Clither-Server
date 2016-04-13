package org.clitherproject.clither.server.tick;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.clitherproject.clither.server.tick.Tickable;

/**
 * A TickWorker is a worker that processes {@link Tickable} objects.
 */
public class TickWorker implements Runnable {

    private static final AtomicInteger NEXT_WORKER_ID = new AtomicInteger(1);
    private final int workerId;
    private final Thread thread;
    private final Queue<Tickable> queue = new ConcurrentLinkedQueue<>();
    private boolean running = false;

    public TickWorker() {
        this.workerId = NEXT_WORKER_ID.getAndIncrement();
        this.thread = new Thread(this, "Tick Worker - #" + workerId);
    }

    public void start() {
        if (!running) {
            running = true;
            thread.start();
        }
    }

    public void shutdownGracefully() {
        if (running) {
            running = false;
        }
    }

    public void shutdown() {
        if (running) {
            running = false;
            thread.interrupt();
        }
    }

    public boolean waitForShutdown() {
        try {
            if (running) {
                shutdownGracefully();
            }

            thread.join();
            return true;
        } catch (InterruptedException ex) {
            return false;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public int getWorkerId() {
        return workerId;
    }

    public int getObjectsRemaining() {
        return queue.size();
    }

    public void tick(Tickable... tickables) {
        for (Tickable t : tickables) {
            queue.add(t);
        }
    }

    public void tick(Collection<Tickable> tickables) {
        queue.addAll(tickables);
    }

    public void waitForCompletion() {
        while (!queue.isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                // Whoops
            }
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                while (!queue.isEmpty()) {
                    queue.poll().tick();
                }

                Thread.sleep(1);
            }
        } catch (InterruptedException ex) {
            return;
        }
    }

}
