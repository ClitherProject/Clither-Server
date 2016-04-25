package org.clitherproject.clither.server.net.throwable;

public class UnknownPacketException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnknownPacketException() {
    }

    public UnknownPacketException(String message) {
        super(message);
    }

    public UnknownPacketException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownPacketException(Throwable cause) {
        super(cause);
    }

    public UnknownPacketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
