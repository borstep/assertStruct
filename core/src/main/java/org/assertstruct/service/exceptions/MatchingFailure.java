package org.assertstruct.service.exceptions;

/**
 * Exception thrown when something unexpected happened during matching
 */
public class MatchingFailure extends RuntimeException{
    public MatchingFailure() {
    }

    public MatchingFailure(String message) {
        super(message);
    }

    public MatchingFailure(String message, Throwable cause) {
        super(message, cause);
    }

    public MatchingFailure(Throwable cause) {
        super(cause);
    }

    public MatchingFailure(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
