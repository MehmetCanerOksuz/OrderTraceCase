package org.caneroksuz.exception;

import lombok.Getter;

@Getter
public class ProductManagerException extends RuntimeException{

    private final ErrorType errorType;

    public ProductManagerException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
