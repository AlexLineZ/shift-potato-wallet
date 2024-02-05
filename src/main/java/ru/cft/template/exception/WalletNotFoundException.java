package ru.cft.template.exception;

public class WalletNotFoundException extends RuntimeException{
    public WalletNotFoundException() {
        super();
    }

    public WalletNotFoundException(String message) {
        super(message);
    }

    public WalletNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletNotFoundException(Throwable cause) {
        super(cause);
    }
}
