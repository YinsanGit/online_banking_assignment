package com.finalBanking.demo.Exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String acc, BigDecimal balance, BigDecimal requested) {
        super("Insufficient funds in " + acc + " (balance " + balance + ", requested " + requested + ")");
    }
}
