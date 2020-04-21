package org.sedaq.scp.service.exceptions;

/**
 * @author Pavel Seda
 */
public class TabuSearchAlgorithmException extends RuntimeException {

    public TabuSearchAlgorithmException() {
    }

    public TabuSearchAlgorithmException(String message) {
        super(message);
    }

    public TabuSearchAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }

    public TabuSearchAlgorithmException(Throwable cause) {
        super(cause);
    }

    public TabuSearchAlgorithmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
