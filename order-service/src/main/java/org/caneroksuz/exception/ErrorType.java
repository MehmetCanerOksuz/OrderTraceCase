package org.caneroksuz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    ORDER_NOT_CREATED(4210, "Müşterinin Sipariş Oluşturma yetkisi bulunmamaktadır.", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(4211, "Böyle bir sipariş bulunamadı",HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(4212, "Böyle bir ürün bulunamadı",HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND_IN_ORDER(4213, "Siparişte bir ürün bulunamadı",HttpStatus.NOT_FOUND),


    ;

    private int code;
    private String message;
    HttpStatus httpStatus;
}
