package com.finalBanking.demo.Exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String acc) { super("Account not found: " + acc); }
}
