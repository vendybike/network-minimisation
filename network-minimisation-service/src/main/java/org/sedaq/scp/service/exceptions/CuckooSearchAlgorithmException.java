package org.sedaq.scp.service.exceptions;

/**
 * @author Pavel Seda
 */
public class CuckooSearchAlgorithmException extends RuntimeException {

    public CuckooSearchAlgorithmException() {
    }

    public CuckooSearchAlgorithmException(String message) {
        super(message);
    }

    public CuckooSearchAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }

    public CuckooSearchAlgorithmException(Throwable cause) {
        super(cause);
    }

    public CuckooSearchAlgorithmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
