package org.caneroksuz.exception;

import lombok.Getter;

@Getter
public class OrderManagerException extends RuntimeException{

    private final ErrorType errorType;

    public OrderManagerException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
