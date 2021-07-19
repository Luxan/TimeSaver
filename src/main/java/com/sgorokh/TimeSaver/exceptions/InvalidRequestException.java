package com.sgorokh.TimeSaver.exceptions;

public class InvalidRequestException extends Exception {

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException() {
        super();
    }

}
