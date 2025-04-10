package com.cabbooking.utils.exceptions;

public class UserException extends Exception {
    private final int errorCode;  // Opcional, para manejar tipos específicos de errores

    public UserException(String message) {
        super(message);
        this.errorCode = 0;  // Valor por defecto
    }

    // Constructor adicional con código de error
    public UserException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "UserException{" +
                "message='" + getMessage() + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
