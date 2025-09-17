package com.finalBanking.demo.Exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomErrorException extends RuntimeException {


    private final HttpStatus status;
    private final String message;

    public CustomErrorException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }
}
