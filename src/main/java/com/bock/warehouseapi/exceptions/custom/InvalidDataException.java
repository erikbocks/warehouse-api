package com.bock.warehouseapi.exceptions.custom;

public class InvalidDataException  extends Exception{

    public InvalidDataException(){}

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Exception ex) {
        super(message, ex);
    }
}
