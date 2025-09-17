package com.finalBanking.demo.Exception;

import java.math.BigDecimal;

public class LimitExceededException extends RuntimeException{
    public LimitExceededException(BigDecimal amount, BigDecimal limit) {
        super("Transfer amount " + amount + " exceeds limit " + limit);
    }
}
