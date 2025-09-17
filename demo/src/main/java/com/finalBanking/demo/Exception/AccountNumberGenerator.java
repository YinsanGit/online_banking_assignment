package com.finalBanking.demo.Exception;

import java.util.UUID;

public class AccountNumberGenerator {
    public static String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}
