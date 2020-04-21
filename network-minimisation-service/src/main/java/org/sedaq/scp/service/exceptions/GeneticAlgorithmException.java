package org.sedaq.scp.service.exceptions;

/**
 * @author Pavel Seda
 */
public class GeneticAlgorithmException extends RuntimeException {

    public GeneticAlgorithmException() {
    }

    public GeneticAlgorithmException(String message) {
        super(message);
    }

    public GeneticAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneticAlgorithmException(Throwable cause) {
        super(cause);
    }

    public GeneticAlgorithmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
