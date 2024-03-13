package org.caneroksuz.exception;

import lombok.Getter;

@Getter
public class CustomerManagerException extends RuntimeException{

    private final ErrorType errorType;

    public CustomerManagerException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
