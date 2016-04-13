package org.clitherproject.clither.server.util;

public class MathHelper {

    /**
     * A fast absolute value function.
     * <p>
     *
     * @param input
     * @return
     */
    public static double fastAbs(double input) {
        return input < 0 ? -input : input;
    }
}
