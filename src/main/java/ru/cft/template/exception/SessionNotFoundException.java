package ru.cft.template.exception;


public class SessionNotFoundException extends RuntimeException{
    public SessionNotFoundException() {
        super();
    }

    public SessionNotFoundException(String message) {
        super(message);
    }

    public SessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionNotFoundException(Throwable cause) {
        super(cause);
    }
}
