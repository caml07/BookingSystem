package com.cabbooking.utils.exceptions;

public class DriverException extends Exception {
    private final int errorCode;  // Opcional, para gestionar tipos de errores específicos

    public DriverException(String message) {
        super(message);
        this.errorCode = 0;  // Valor por defecto
    }

    // Constructor adicional con código de error
    public DriverException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "DriverException{" +
                "message='" + getMessage() + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
