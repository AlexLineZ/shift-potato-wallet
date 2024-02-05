package ru.cft.template.exception;

public class AccessRightsException extends RuntimeException{
    public AccessRightsException() {
        super();
    }

    public AccessRightsException(String message) {
        super(message);
    }

    public AccessRightsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessRightsException(Throwable cause) {
        super(cause);
    }
}
