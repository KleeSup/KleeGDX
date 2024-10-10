package com.github.kleesup.kleegdx.core.exception;

/**
 * Thrown when an exception is declared as <i>invalid</i>.
 * Mostly thrown by helper methods in {@link com.github.kleesup.kleegdx.core.util.Verify}.
 */
public class InvalidCollectionException extends RuntimeException {
    public InvalidCollectionException() {
    }

    public InvalidCollectionException(String message) {
        super(message);
    }

    public InvalidCollectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCollectionException(Throwable cause) {
        super(cause);
    }

    public InvalidCollectionException(String message, Throwable cause,
                                      boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
