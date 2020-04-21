package org.sedaq.scp.service.exceptions;

/**
 * @author Pavel Seda
 */
public class SimulatedAnnealingAlgorithmException extends RuntimeException {

    public SimulatedAnnealingAlgorithmException() {
    }

    public SimulatedAnnealingAlgorithmException(String message) {
        super(message);
    }

    public SimulatedAnnealingAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimulatedAnnealingAlgorithmException(Throwable cause) {
        super(cause);
    }

    public SimulatedAnnealingAlgorithmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
