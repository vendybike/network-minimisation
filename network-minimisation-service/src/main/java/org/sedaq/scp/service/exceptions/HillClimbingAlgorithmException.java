package org.sedaq.scp.service.exceptions;

/**
 * @author Pavel Seda
 */
public class HillClimbingAlgorithmException extends RuntimeException {
    public HillClimbingAlgorithmException() {
    }

    public HillClimbingAlgorithmException(String message) {
        super(message);
    }

    public HillClimbingAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }

    public HillClimbingAlgorithmException(Throwable cause) {
        super(cause);
    }

    public HillClimbingAlgorithmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
