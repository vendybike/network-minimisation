package org.sedaq.scp.service.exceptions;

/**
 * @author Pavel Seda
 */
public class RepairOperatorException extends RuntimeException {
    public RepairOperatorException() {
    }

    public RepairOperatorException(String message) {
        super(message);
    }

    public RepairOperatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepairOperatorException(Throwable cause) {
        super(cause);
    }

    public RepairOperatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
