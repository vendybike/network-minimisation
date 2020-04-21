package org.sedaq.scp.service.exceptions;

public class DifferentialEvolutionException extends RuntimeException {

    public DifferentialEvolutionException() {
    }

    public DifferentialEvolutionException(String message) {
        super(message);
    }

    public DifferentialEvolutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DifferentialEvolutionException(Throwable cause) {
        super(cause);
    }

    public DifferentialEvolutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
