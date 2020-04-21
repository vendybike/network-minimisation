package org.sedaq.scp.service.exceptions;

/**
 * @author Pavel Seda
 */
public class ReadIOException extends RuntimeException {

    public ReadIOException() {
    }

    public ReadIOException(String message) {
        super(message);
    }

    public ReadIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadIOException(Throwable cause) {
        super(cause);
    }

    public ReadIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
