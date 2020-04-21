package org.sedaq.scp.service.exceptions;

/**
 * @author Pavel Seda
 */
public class SpecialCasesException extends RuntimeException {

    public SpecialCasesException() {
    }

    public SpecialCasesException(String message) {
        super(message);
    }

    public SpecialCasesException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpecialCasesException(Throwable cause) {
        super(cause);
    }

    public SpecialCasesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
