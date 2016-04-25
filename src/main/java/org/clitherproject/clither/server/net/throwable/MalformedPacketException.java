package org.clitherproject.clither.server.net.throwable;

/**
 * Thrown when a packet is malformed.
 */
public class MalformedPacketException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MalformedPacketException() {
    }

    public MalformedPacketException(String message) {
        super(message);
    }

    public MalformedPacketException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedPacketException(Throwable cause) {
        super(cause);
    }

    public MalformedPacketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
