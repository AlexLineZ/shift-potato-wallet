package ru.cft.template.exception;

public class BadTransactionException extends RuntimeException {

    public BadTransactionException() {
        super();
    }

    public BadTransactionException(String message) {
        super(message);
    }

    public BadTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadTransactionException(Throwable cause) {
        super(cause);
    }
}

