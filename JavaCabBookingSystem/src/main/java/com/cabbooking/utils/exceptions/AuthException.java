package com.cabbooking.utils.exceptions;

public class AuthException extends Exception {
    private final int errorCode;  // Opcional, si necesitas codificar tipos de errores

    public AuthException(String message) {
        super(message);
        this.errorCode = 0;  // Valor por defecto si no se necesita código de error
    }

    // Constructor adicional con código de error
    public AuthException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "AuthException{" +
                "message='" + getMessage() + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
