package org.clitherproject.clither.server.tick;

import java.util.function.Supplier;

public class TickableSupplier implements Tickable {
    private final Supplier supplier;
    private boolean ticked = false;

    public TickableSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public void tick() {
        if (!ticked) {
            supplier.get();
            ticked = true;
        }
    }
}

