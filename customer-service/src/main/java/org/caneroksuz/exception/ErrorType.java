package org.caneroksuz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    EMAIL_ALREADY_EXIST(4210,"Böyle bir Müşteri bulunmaktadır",HttpStatus.BAD_REQUEST),
    CUSTOMER_NOT_CREATED(4211, "Müşteri olusturulamadı!!!", HttpStatus.BAD_REQUEST),
    CUSTOMER_NOT_FOUND(4212, "Böyle bir müşteri bulunamadı",HttpStatus.NOT_FOUND),


    ;

    private int code;
    private String message;
    HttpStatus httpStatus;
}
