package org.sedaq.scp.service.exceptions;

/**
 * @author Pavel Seda
 */
public class CuckooSearchAlgorithm extends RuntimeException {

    public CuckooSearchAlgorithm() {
    }

    public CuckooSearchAlgorithm(String message) {
        super(message);
    }

    public CuckooSearchAlgorithm(String message, Throwable cause) {
        super(message, cause);
    }

    public CuckooSearchAlgorithm(Throwable cause) {
        super(cause);
    }

    public CuckooSearchAlgorithm(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
