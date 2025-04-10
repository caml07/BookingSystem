package com.cabbooking.utils.exceptions;

public class BookingException extends Exception {
    private final int errorCode;  // Opcional, para gestionar tipos de errores específicos

    public BookingException(String message) {
        super(message);
        this.errorCode = 0;  // Valor por defecto
    }

    // Constructor adicional con código de error
    public BookingException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "BookingException{" +
                "message='" + getMessage() + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
