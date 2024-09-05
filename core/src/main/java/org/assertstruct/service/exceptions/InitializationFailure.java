package org.assertstruct.service.exceptions;

/**
 * Error happened during assertStruct initialization
 */
public class InitializationFailure extends RuntimeException{

    public InitializationFailure(String message) {
        super(message);
    }

    public InitializationFailure(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializationFailure(Throwable cause) {
        super(cause);
    }

    public InitializationFailure(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
