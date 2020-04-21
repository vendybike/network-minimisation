package org.sedaq.scp.service.exceptions;

/**
 * @author Pavel Seda
 */
public class MatricesUtilsException extends RuntimeException {

    public MatricesUtilsException() {
    }

    public MatricesUtilsException(String message) {
        super(message);
    }

    public MatricesUtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatricesUtilsException(Throwable cause) {
        super(cause);
    }

    public MatricesUtilsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
